# See poky/meta-skeleton/recipes-kernel/linux/linux-yocto-custom.bb for
# documentation

LINUX_VERSION ?= "3.10"
LINUX_VERSION_EXTENSION ?= "-custom"

KCONFIG_MODE="--alldefconfig"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

PR = "r3"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV = "d3038f20f43a21f221c6bde62ab0e312749603a8"

KBRANCH = "linux-3.10-at91-e10"
SRC_URI = "git://github.com/synapse-wireless/linux-at91.git;protocol=git;branch=${KBRANCH};nocheckout=1"
SRC_URI += "file://defconfig"

python __anonymous () {
  if d.getVar('UBOOT_FIT_IMAGE', True) == 'xyes':
    d.appendVar('DEPENDS', ' u-boot-mkimage-native dtc-native')
}

do_deploy_append() {
	if [ "${UBOOT_FIT_IMAGE}" = "xyes" ]; then
		DTB_PATH="${B}/arch/${ARCH}/boot/dts/"
		if [ ! -e "${DTB_PATH}" ]; then
			DTB_PATH="${B}/arch/${ARCH}/boot/"
		fi

		cp ${S}/arch/${ARCH}/boot/dts/${MACHINE}*.its ${DTB_PATH}
		cd ${DTB_PATH}
		mkimage -f ${MACHINE}.its ${MACHINE}.itb
		install -m 0644 ${MACHINE}.itb ${DEPLOYDIR}/${MACHINE}.itb
		cd -
	fi
}

KERNEL_MODULE_AUTOLOAD += "atmel_usba_udc g_serial"

COMPATIBLE_MACHINE = "(sama5d4ek|sama5d4-xplained|sama5d3xek|sama5d3-xplained|at91sam9x5ek|at91sam9rlek|at91sam9m10g45ek)"

KERNEL_IMAGETYPE = "uImage"

#FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

PREMIRRORS = ""
MIRRORS = ""

SRC_URI += "file://tun_kmodule.cfg"

# We use the AT91SAM9G20 so add that to the device trees built in
KERNEL_DEVICETREE += "${S}/arch/arm/boot/dts/at91sam9g20ek.dts"

pkg_postinst_kernel-image(){
#!/bin/sh -e
# Commands to carry out
KERNEL_MTD=/dev/mtd3
KERNEL_IMG=uImage-${KERNEL_VERSION}
TESTPASSES=5

die() {
    logger -p daemon.emerg -t kernel-image-install -s $*
    exit 1
      }

# We only want this to run on the actual device
[ "x${D}" != "x" ] || exit 1

# Test NAND for bad blocks. Mark them bad if found. Run ${TESTPASSES} tests.
nandtest -p ${TESTPASSES} -m ${KERNEL_MTD}

# Erase flash which is required for the next steps
flash_erase ${KERNEL_MTD} 0 0 || die "Failed to erase ${KERNEL_MTD}"

# Program in kernel
nandwrite -p ${KERNEL_MTD} /boot/${KERNEL_IMG} \
    || die "Failed to write kernel to NAND"

echo "The new kernel will be loaded at the next reboot."
}

