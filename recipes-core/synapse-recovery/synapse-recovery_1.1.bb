SUMMARY = "Synapse Recovery Scripts and Files"
DESCRIPTION = "Scripts to recover from a Synapse Recovery image"
SECTION = "base"
PR = "r5"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://synapse-recovery.sh \
           file://synapse-recovery.init"
S = "${WORKDIR}"

inherit allarch update-rc.d

INITSCRIPT_NAME = "synapse-recovery"
INITSCRIPT_PARAMS = "start 99 S ."

do_configure() {
	:
}

do_compile() {
	:
}

do_install () {
	# install synapse-recovery.sh into /usr/bin
	install -d ${D}${sbindir}
	install -m 0755 ${S}/synapse-recovery.sh ${D}${sbindir}/synapse-recovery

	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${S}/synapse-recovery.init \
		${D}${sysconfdir}/init.d/synapse-recovery
}

FILES_${PN} = "${sbindir}/synapse-recovery \
	           ${sysconfdir}/init.d/synapse-recovery"
