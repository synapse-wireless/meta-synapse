FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

PREMIRRORS = ""
MIRRORS = ""

SRC_URI += "file://connect_e10_defconfig"

do_configure_prepend() {
	cp ${WORKDIR}/connect_e10_defconfig ${WORKDIR}/defconfig
}

