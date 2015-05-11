SUMMARY = "Synapse udev rules"
DESCRIPTION = "udev rules to help support Synapse hardware"
SECTION = "base"
PR = "r0"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://98-synapse-hw.rules \
           file://98-synapse-snap.rules"
S = "${WORKDIR}"

RDEPENDS="udev"

do_configure() {
	:
}

do_compile() {
	:
}

do_install () {
	# install udev rules to /lib/udev/rules.d/
	install -d ${D}${base_libdir}/udev/rules.d/
	install -m 0644 ${S}/98-synapse-hw.rules ${D}${base_libdir}/udev/rules.d/
	install -m 0644 ${S}/98-synapse-snap.rules ${D}${base_libdir}/udev/rules.d/
}

FILES_${PN} = "${base_libdir}/udev/rules.d/"
