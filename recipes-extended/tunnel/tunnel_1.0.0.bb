DESCRIPTION = "Reverse SSH tunnel template"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/license.txt;md5=7c7bf297f1e53906530baf3f40893003"
SECTION="base"

# Set what files will be copied into this package:
SRC_URI = "file://tunnel.sh \
file://X90tunnel \
file://license.txt"

do_configure() {
}

do_install() {
    install -d ${D}/etc/init.d/
    install -d ${D}/etc/rc5.d/
    install -m 0744 ${WORKDIR}/tunnel.sh ${D}/etc/init.d/
    cd ${D}/etc/rc5.d/
    ln -s ../init.d/tunnel ../X90tunnel
    cd -
}
