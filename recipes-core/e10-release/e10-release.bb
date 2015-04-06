SUMMARY = "Synapse E10 Release"
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

S = "${WORKDIR}"

# Use the distro info for our versioning so we don't have the number
# in two places.
PV = "${DISTRO_VERSION}"

inherit allarch

do_install () {
	# Generate the file
	echo "${PV}" > ${S}/E10_version

	# Install it to /etc/E10_version
	install -d ${D}${sysconfdir}
	install -m 0755 ${S}/E10_version ${D}${sysconfdir}/E10_version

	# Create
	ln -s E10_version ${D}/${sysconfdir}/E10Version
}

FILES_${PN} = "\
	${sysconfdir}/E10_version \
	${sysconfdir}/E10Version \
"
