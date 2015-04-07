SUMMARY = "opkg repo config to point to Synapse server"
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

FEED_URI = "http://update.synapse-wireless.com/e10"

do_compile () {
	mkdir -p ${S}/${sysconfdir}/opkg
	# I had ${PACKAGE_EXTRA_ARCHS} here before but it generated
	# a lot of stuff we don't use
	for feed in all armv5te ${MACHINE_ARCH}; do
		echo "src/gz ${feed} ${FEED_URI}/${feed}" \
			>> ${S}/${sysconfdir}/opkg/synapse-feed.conf
	done
}

do_install() {
    # install synapse-feed.conf into /etc/opkg/
	install -d ${D}${sysconfdir}/opkg
	install -m 0644 ${S}/${sysconfdir}/opkg/* ${D}${sysconfdir}/opkg/
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES_${PN} = "${sysconfdir}/opkg/synapse-feed.conf"
