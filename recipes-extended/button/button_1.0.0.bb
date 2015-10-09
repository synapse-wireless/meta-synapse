DESCRIPTION = "LED control helper scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/license.txt;md5=7c7bf297f1e53906530baf3f40893003"
SECTION="base"

# Set what files will be copied into this package:
SRC_URI = "file://button \
file://button_gpio \
file://license.txt"

do_configure() {
}

do_install() {
    install -d ${D}/usr/bin/
    install -d ${D}/etc/init.d/
    install -d ${D}/etc/rc5.d/

    install -m 0755 ${WORKDIR}/button ${D}/usr/bin/
    install -m 0755 ${WORKDIR}/button_gpio ${D}/etc/init.d/

    cd ${D}/usr/bin/
    ln -s button gpio9260
    cd -

    cd ${D}/etc/rc5.d/
    ln -s ../init.d/button_gpio ./S15button_gpio
    cd -
}
