# See poky/meta-skeleton/recipes-kernel/linux/linux-yocto-custom.bb for
# documentation

LINUX_VERSION ?= "3.10"
LINUX_VERSION_EXTENSION ?= "-custom"

KCONFIG_MODE="--alldefconfig"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

PR = "r1"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV = "35158dd80a94df2b71484b9ffa6e642378209156"
SRCREV_sama5d4-xplained = "46f4253693b0ee8d25214e7ca0dde52e788ffe95"

KBRANCH = "linux-3.10-at91"
SRC_URI = "git://github.com/linux4sam/linux-at91.git;protocol=git;branch=${KBRANCH};nocheckout=1"
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

SRC_URI += "file://0001-Add-support-for-Connect-E10-to-Kconfig.patch"
SRC_URI += "file://0002-Add-board-connect-e10-file.patch"
SRC_URI += "file://0003-Add-board-connect-e10-to-Makefile.patch"
SRC_URI += "file://0004-Add-board-connect-e10-to-mach-types.patch"
SRC_URI += "file://tun_kmodule.cfg"

# We use the AT91SAM9G20 so add that to the device trees built in
KERNEL_DEVICETREE += "${S}/arch/arm/boot/dts/at91sam9g20ek.dts"

