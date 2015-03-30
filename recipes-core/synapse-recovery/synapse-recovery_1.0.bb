SUMMARY = "Synapse Recovery Scripts and Files"
DESCRIPTION = "Files and scripts necessary to recover from a Synapse Recovery IMG"
SECTION = "base"
PR = "r3"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://run-recovery.sh \
           file://run-recovery.init"
S = "${WORKDIR}"

inherit allarch update-rc.d

INITSCRIPT_NAME = "run-recovery"
INITSCRIPT_PARAMS = "start 99 S ."

do_configure() {
	:
}

do_compile() {
	:
}

do_install () {
	# install run-recovery.sh into /usr/bin
	install -d ${D}${sbindir}
	install -m 0755 ${S}/run-recovery.sh ${D}${sbindir}/run-recovery

	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${S}/run-recovery.init \
		${D}${sysconfdir}/init.d/run-recovery
}

FILES_${PN} = "${sbindir}/run-recovery \
	           ${sysconfdir}/init.d/run-recovery"
