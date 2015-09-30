DESCRIPTION = "LED control helper scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/license.txt;md5=7c7bf297f1e53906530baf3f40893003"
SECTION="base"

# Set what files will be copied into this package:
SRC_URI = "file://redled \
file://greenled"

do_configure() {
}

do_install() {
    install -d ${D}/usr/bin/
    install -m 0755 ${WORKDIR}/redled ${D}/usr/bin/
    install -m 0755 ${WORKDIR}/greenled ${D}/usr/bin/
}
