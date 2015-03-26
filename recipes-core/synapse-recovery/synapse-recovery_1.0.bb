SUMMARY = "Synapse Recovery Scripts and Files"
DESCRIPTION = "Files and scripts necessary to recover from a Synapse Recovery IMG"
SECTION = "base"
PR = "r2"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://run-recovery.sh"
S = "${WORKDIR}"

do_install () {
	# install run-recovery.sh into /usr/bin
	install -d ${D}${sbindir}
	install -m 0755 run-recovery.sh ${D}${sbindir}/run-recovery
}

FILES_${PN} = "${sbindir}/run-recovery"
# Add new Destination files here to be included in the package

# Prevents do_package failures with:
# debugsources.list: No such file or directory:
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
