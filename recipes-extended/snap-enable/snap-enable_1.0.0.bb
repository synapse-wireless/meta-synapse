DESCRIPTION = "Enable Snap module"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/license.txt;md5=7c7bf297f1e53906530baf3f40893003"
SECTION="base"

# Set what files will be copied into this package:
SRC_URI = "file://snap-enable.sh \
file://license.txt"

do_configure() {
}

do_install() {
    install -d ${D}/etc/init.d/
    install -d ${D}/etc/rcS.d/
    install -m 0755 ${WORKDIR}/snap-enable.sh ${D}/etc/init.d/

    cd ${D}/etc/rcS.d/
    ln -s ../init.d/snap-enable ./S95snap-enable
    cd -
}
