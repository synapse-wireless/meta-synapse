#!/bin/sh

RECOVERY_TAR=
RECOVERY_MTD=/dev/mtd6
ROOTFS_MTD=/dev/mtd5
ROOTFS_UBI=core-image-stage2-at91sam9x5ek.ubifs
KERNEL_MTD=/dev/mtd3
KERNEL_IMG=uImage-at91sam9x5ek.bin
UBOOT_BOOTCMD="nboot 0x21000000 0 c0000"
UBOOT_BOOTARGS="console=ttyS0,115200 mtdparts=atmel_nand:128K(bootstrap)ro,384K(uboot)ro,256K(environment),3328K(uImage)ro,9216K(recovery)ro,211M(rootfs),-(other) ubi.mtd=rootfs root=ubi0:rootfs rw rootfstype=ubifs"

die() {
    logger -p daemon.emerg -t synapse-recovery -s $*
    exit 1
}

while getopts m:t: name; do
	case ${name} in
		m)
			RECOVERY_MTD="${OPTARG}" ;;
		t)
			RECOVERY_TAR="${OPTARG}" ;;
		?)
			printf "Usage: %s: [-m /dev/mtdX] [-t recovery.tar.xz]" $0
			exit 2
			;;
	esac
done

REBOOT=no

# Verify recovery image

# Extract recovery image
if [ ! -z ${RECOVERY_TAR} ]; then
	tar Jxf ${RECOVERY_TAR} -C /run/ \
		|| die "Failed to extract kernel & rootfs from ${RECOVERY_TAR}"
else
	nanddump ${RECOVERY_MTD} | tar Jxf - -C /run/ \
		|| die "Failed to extract kernel & rootfs from NAND"
fi
cd /run/ || die "Failed to cd to /run/"
md5sum -c md5sums || die "Failed to validate md5sums"

# if the recovery image contained a rootfs, load it
if [ -e /run/${ROOTFS_URI} ]; then
    # Test NAND for bad blocks. Mark them bad if found. Run 5 tests.
    nandtest -p 5 -m ${ROOTFS_MTD}

    # Don't actually erase because that clears the erase counters
    # which is necessary to do proper wear leveling
    #flash_erase ${ROOTFS_MTD} 0 0 || die "Failed to erase ${ROOTFS_MTD}"

    # Create rootfs
    ubiformat ${ROOTFS_MTD} -y || die "Failed to ubiformat ${ROOTFS_MTD}"
    ubiattach -p ${ROOTFS_MTD} || die "Failed to ubiattach ${ROOTFS_MTD}"
    ubimkvol /dev/ubi0 -N rootfs -m || die "Failed to ubimkvol rootfs"
    ubiupdatevol /dev/ubi0_0 /run/${ROOTFS_UBI} \
        || die "Failed to write rootfs UBI to NAND"

    REBOOT=yes
fi

# if the recovery image contained a rootfs, load it
if [ -e /run/${KERNEL_IMG} ]; then
    # Test NAND for bad blocks. Mark them bad if found. Run 5 tests.
    nandtest -p 5 -m ${KERNEL_MTD}

    # Erase flash which is required for the next steps
    flash_erase ${KERNEL_MTD} 0 0 || die "Failed to erase ${KERNEL_MTD}"

    # Program in kernel
    nandwrite -p ${KERNEL_MTD} /run/${KERNEL_IMG} \
        || die "Failed to write kernel to NAND"

    REBOOT=yes
fi

# Set u-boot params to boot
fw_setenv bootargs ${UBOOT_BOOTARGS} || die "Failed to set u-boot bootargs"
fw_setenv bootcmd ${UBOOT_BOOTCMD} || die "Failed to set u-boot bootcmd"

# Reboot
if [ "x${REBOOT}" == "xyes" ]; then
    reboot
fi
