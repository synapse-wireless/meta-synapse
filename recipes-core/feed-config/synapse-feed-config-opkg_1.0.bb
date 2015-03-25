SUMMARY = "Synapse opkg repo config"
DESCRIPTION = "Configs to setup Synapse opkg repo"
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://synapse-feeds.conf"
S = "${WORKDIR}"

do_install () {
    # install feed-synapse.conf into /etc/opkg/
	install -D -m 0644 synapse-feeds.conf \
        ${D}${sysconfdir}/opkg/synapse-feeds.conf
}

FILES_${PN} = "${sysconfdir}/opkg/synapse-feeds.conf"
# Add new Destination files here to be included in the package

# Prevents do_package failures with:
# debugsources.list: No such file or directory:
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
