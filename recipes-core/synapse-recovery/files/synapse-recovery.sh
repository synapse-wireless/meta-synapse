#!/bin/sh

TESTPASSES=5
RECOVERY_TAR=
RECOVERY_MTD=/dev/mtd6

ROOTFS_MTD=/dev/mtd5
ROOTFS_UBI=rootfs-at91sam9x5ek.ubifs
ROOTFS_NANDTEST_DATA="/tmp/nandtest.rootfs.log"

KERNEL_MTD=/dev/mtd3
KERNEL_IMG=uImage-at91sam9x5ek.bin
KERNEL_NANDTEST_DATA="/tmp/nandtest.kernel.log"

UBOOT_MTD=/dev/mtd1
UBOOT_IMG=u-boot-at91sam9x5ek.bin
UBOOT_NANDTEST_DATA="/tmp/nandtest.uboot.log"

UBOOT_BOOTCMD="nboot 0x21000000 0 c0000"
UBOOT_BOOTARGS="console=ttyS0,115200 mtdparts=atmel_nand:128K(bootstrap)ro,384K(uboot)ro,256K(environment),3328K(uImage),9216K(rescue)ro,211M(rootfs),-(recovery) ubi.mtd=rootfs root=ubi0:rootfs rw rootfstype=ubifs"

# For snap-lighting
BOOTSRVURL="http://boot.snap-lighting.com"
MACADDR=`cat /sys/class/net/eth0/address | cut -d : -f 4,5,6 | cut -c 1-2,4-5,7-8`
LOGPATH="log/$MACADDR"
CLEANEDLOG="/tmp/clean.log"

die() {
    logger -p daemon.emerg -t synapse-recovery -s $*
    exit 1
}

log() {
        msg="$*"
        quotedMsg=`/usr/sbin/urlencode.sh $msg`
        data1="$BOOTSRVURL/$LOGPATH --post-data message=$quotedMsg"
        #echo "Data = [$data1]"
        result="`wget -q -O - $data1 `"
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
			printf "Usage: %s: [-m /dev/mtdX] [-t recovery.tgz] [-p NUM]" $0
			exit 2
			;;
	esac
done

REBOOT=no
log "SnapLighting Upgrade 1.x to 2.x - Entering Stage 2"
# Verify recovery image

# Extract recovery image
if [ ! -z ${RECOVERY_TAR} ]; then
	tar zxf ${RECOVERY_TAR} -C /run/ \
		|| die "Failed to extract kernel & rootfs from ${RECOVERY_TAR}"
else
            # This can fail because we don't know how big the file is and as a result
	# we will get random bytes of the NAND at the end which will be run
	# through xz -d first which can cause tar to lose its mind.
	nanddump ${RECOVERY_MTD} 2>/dev/null | tar zvxf - -C /run/ 2>/dev/null
	[ -e /run/md5sums ] || die "Failed to extract rootfs & kernel from NAND \
		or no recovery data present"
fi
log "  SLU: Recovery image extracted"

cd /run/ || die "Failed to cd to /run/"
md5sum -c md5sums || die "Failed to validate md5sums"

# if the recovery image contained a rootfs, load it
if [ -e /run/${ROOTFS_URI} ]; then
    log "  SLU: nandtest of rootfs partition starting"
    # Test NAND for bad blocks. Mark them bad if found. Run ${TESTPASSES} tests.
    #nandtest -p ${TESTPASSES} -m ${ROOTFS_MTD}
    touch "$ROOTFS_NANDTEST_DATA"
    nandtest -p ${TESTPASSES} -m ${ROOTFS_MTD} 2>&1 | tee -a  ${ROOTFS_NANDTEST_DATA}

    # Log the results
    if [ -f "$ROOTFS_NANDTEST_DATA" ]; then
        
                # Clean up the output
                 # Clean up the output
                `cat $ROOTFS_NANDTEST_DATA | tr '\r' '\n' | grep -v 'checking\|reading\|writing\|erasing' | sed '/^\s*$/d' > $CLEANEDLOG`
                
                # Read in the file
                logdata=`cat $CLEANEDLOG`
                log "  SLU: nandtest of $ROOTFS_MTD (rootfs):\n$logdata"
    else
        echo "No Recovery Nandtest Data File Found: $ROOTFS_NANDTEST_DATA"
    fi

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
    # nandtest -p ${TESTPASSES} -m ${KERNEL_MTD}
     log "  SLU: nandtest of kernel partition starting"

    touch "$KERNEL_NANDTEST_DATA"
    nandtest -p ${TESTPASSES} -m ${KERNEL_MTD} 2>&1 | tee -a ${KERNEL_NANDTEST_DATA}

    # Log the results
    if [ -f "$KERNEL_NANDTEST_DATA" ]; then
        
                # Clean up the output
                 # Clean up the output
                `cat $KERNEL_NANDTEST_DATA | tr '\r' '\n' | grep -v 'checking\|reading\|writing\|erasing' | sed '/^\s*$/d' > $CLEANEDLOG`
                
                # Read in the file
                logdata=`cat $CLEANEDLOG`
                log "  SLU: nandtest of $KERNEL_MTD (kernel):\n$logdata"
    else
        echo "No Kernel Nandtest Data File Found: $KERNEL_NANDTEST_DATA"
    fi

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
    # nandtest -p ${TESTPASSES} -m ${UBOOT_MTD}
    touch "$UBOOT_NANDTEST_DATA"
    log "  SLU: nandtest of uboot partition starting"

    nandtest -p ${TESTPASSES} -m ${UBOOT_MTD} 2>&1 | tee -a  ${UBOOT_NANDTEST_DATA}

    # Log the results
    if [ -f "$UBOOT_NANDTEST_DATA" ]; then
        
                # Clean up the output
                 # Clean up the output
                `cat $UBOOT_NANDTEST_DATA | tr '\r' '\n' | grep -v 'checking\|reading\|writing\|erasing' | sed '/^\s*$/d' > $CLEANEDLOG`
                
                # Read in the file
                logdata=`cat $CLEANEDLOG`
                log "  SLU: nandtest of $UBOOT_MTD (uboot):\n$logdata"
    else
        echo "No Kernel Nandtest Data File Found: $KERNEL_NANDTEST_DATA"
    fi

    # Erase flash which is required for the next steps
    flash_erase ${UBOOT_MTD} 0 0 || die "Failed to erase ${UBOOT_MTD}"

    # Program in kernel
    nandwrite -p ${UBOOT_MTD} /run/${UBOOT_IMG} \
        || die "Failed to write U-Boot to NAND"

    REBOOT=yes
fi
# NOTE: All calls to fw_setenv are done TWICE.
#            This is because the UBOOT NAND Enviornment Store has two banks
#            Doing each call twice ensures both banks contain the same information
# Set u-boot params to boot
fw_setenv bootargs ${UBOOT_BOOTARGS} || die "Failed to set u-boot bootargs"
fw_setenv bootargs ${UBOOT_BOOTARGS} || die "Failed to set u-boot bootargs"

fw_setenv bootcmd ${UBOOT_BOOTCMD} || die "Failed to set u-boot bootcmd"
fw_setenv bootcmd ${UBOOT_BOOTCMD} || die "Failed to set u-boot bootcmd"

fw_printenv

log "SnapLighting Upgrade 1.x to 2.x - Leaving Stage 2, Rebooting Now."
# Reboot
if [ "x${REBOOT}" == "xyes" ]; then
	# if we used the RECOVERY_MTD then we need to clear it out
	# so that the next time we reboot we don't flash ourselves again
	if [ -z ${RECOVERY_TAR} ]; then
		flash_erase ${RECOVERY_MTD} 0 0
	fi

    reboot
fi
