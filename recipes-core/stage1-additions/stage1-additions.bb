SUMMARY = "Rootfs additions for the Stage 1 Rootfs"
DESCRIPTION = "Rootfs additions for the Stage 1 Rootfs"
SECTION = "examples"
PR = "r1"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI = "file://test1.txt \
           file://test2.txt"

S = "${WORKDIR}"

do_install () {
	mkdir -p ${D}${localstatedir}
	install -D test1.txt ${D}${localstatedir}/
	install -D test2.txt ${D}${localstatedir}/

}

FILES_${PN} = "${localstatedir}/test1.txt ${localstatedir}/test2.txt"

# Prevents do_package failures with:
# debugsources.list: No such file or directory:
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
