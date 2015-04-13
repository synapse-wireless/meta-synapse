#!/bin/sh

TESTPASSES=5
RECOVERY_TAR=
RECOVERY_MTD=/dev/mtd6
ROOTFS_MTD=/dev/mtd5
ROOTFS_UBI=rootfs-at91sam9x5ek.ubifs
KERNEL_MTD=/dev/mtd3
KERNEL_IMG=uImage-at91sam9x5ek.bin
UBOOT_MTD=/dev/mtd1
UBOOT_IMG=u-boot-at91sam9x5ek.bin
UBOOT_BOOTCMD="nboot 0x21000000 0 c0000"
UBOOT_BOOTARGS="console=ttyS0,115200 mtdparts=atmel_nand:128K(bootstrap)ro,384K(uboot)ro,256K(environment),3328K(uImage)ro,9216K(rescue)ro,211M(rootfs),-(recovery) ubi.mtd=rootfs root=ubi0:rootfs rw rootfstype=ubifs"

die() {
    logger -p daemon.emerg -t synapse-recovery -s $*
    exit 1
}

while getopts m:t:p: name; do
	case ${name} in
		m)
			RECOVERY_MTD="${OPTARG}" ;;
		t)
			RECOVERY_TAR="${OPTARG}" ;;
		p)
			TESTPASSES=${OPTARG} ;;
		?)
			printf "Usage: %s: [-m /dev/mtdX] [-t recovery.tar.xz] [-p NUM]" $0
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
	# This can fail because we don't know how big the file is and as a result
	# we will get random bytes of the NAND at the end which will be run
	# through xz -d first which can cause tar to lose its mind.
	nanddump ${RECOVERY_MTD} 2>/dev/null | tar Jvxf - -C /run/ 2>/dev/null
	[ -e /run/md5sums ] || die "Failed to extract rootfs & kernel from NAND \
		or no recovery data present"
fi
cd /run/ || die "Failed to cd to /run/"
md5sum -c md5sums || die "Failed to validate md5sums"

# if the recovery image contained a rootfs, load it
if [ -e /run/${ROOTFS_URI} ]; then
    # Test NAND for bad blocks. Mark them bad if found. Run ${TESTPASSES} tests.
    nandtest -p ${TESTPASSES} -m ${ROOTFS_MTD}

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
    # Test NAND for bad blocks. Mark them bad if found. Run ${TESTPASSES} tests.
    nandtest -p ${TESTPASSES} -m ${KERNEL_MTD}

    # Erase flash which is required for the next steps
    flash_erase ${KERNEL_MTD} 0 0 || die "Failed to erase ${KERNEL_MTD}"

    # Program in kernel
    nandwrite -p ${KERNEL_MTD} /run/${KERNEL_IMG} \
        || die "Failed to write kernel to NAND"

    REBOOT=yes
fi

# if the recovery image contained U-Boot, load it
if [ -e /run/${UBOOT_IMG} ]; then
    # Test NAND for bad blocks. Mark them bad if found. Run ${TESTPASSES} tests.
    nandtest -p ${TESTPASSES} -m ${UBOOT_MTD}

    # Erase flash which is required for the next steps
    flash_erase ${UBOOT_MTD} 0 0 || die "Failed to erase ${UBOOT_MTD}"

    # Program in kernel
    nandwrite -p ${UBOOT_MTD} /run/${UBOOT_IMG} \
        || die "Failed to write U-Boot to NAND"

    REBOOT=yes
fi

# Set u-boot params to boot
fw_setenv bootargs ${UBOOT_BOOTARGS} || die "Failed to set u-boot bootargs"
fw_setenv bootcmd ${UBOOT_BOOTCMD} || die "Failed to set u-boot bootcmd"

# Reboot
if [ "x${REBOOT}" == "xyes" ]; then
	# if we used the RECOVERY_MTD then we need to clear it out
	# so that the next time we reboot we don't flash ourselves again
	if [ -z ${RECOVERY_TAR} ]; then
		flash_erase ${RECOVERY_MTD} 0 0
	fi

    reboot
fi
