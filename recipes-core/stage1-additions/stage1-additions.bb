SUMMARY = "Rootfs additions for the Stage 1 Rootfs"
DESCRIPTION = "Rootfs additions for the Stage 1 Rootfs"
SECTION = "examples"
PR = "r1"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"


PACKAGES = "${PN}"

#SRC_URI = "file://fw_env.config"
# Add new SRC_URI += lines here


S = "${WORKDIR}"

do_install () {
	:
#	mkdir -p ${D}${sysconfdir}
# Add new mkdirs for your destination path here.  Look at meta/conf/bitbake.conf for built-in variables

#	install -D fw_env.config ${D}${sysconfdir}/
# Add new installation lines here

}

#FILES_${PN} = "${sysconfdir}/fw_env.config"
# Add new Destination files here to be included in the package

# Prevents do_package failures with:
# debugsources.list: No such file or directory:
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
