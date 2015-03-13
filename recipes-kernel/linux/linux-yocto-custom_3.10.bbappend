KERNEL_IMAGETYPE = "uImage"

FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

PREMIRRORS = ""
MIRRORS = ""

SRC_URI += "file://connect_e10_defconfig"
SRC_URI += "file://0001-Add-support-for-Connect-E10-to-Kconfig.patch"
SRC_URI += "file://0002-Add-board-connect-e10-file.patch"
SRC_URI += "file://0003-Add-board-connect-e10-to-Makefile.patch"
SRC_URI += "file://0004-Add-board-connect-e10-to-mach-types.patch"

do_configure_prepend() {
	cp ${WORKDIR}/connect_e10_defconfig ${WORKDIR}/defconfig
	rm -f ${B}/.config
	rm -f ${B}/.config.old
}

