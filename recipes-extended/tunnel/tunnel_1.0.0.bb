DESCRIPTION = "Reverse SSH tunnel template"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/license.txt;md5=7c7bf297f1e53906530baf3f40893003"
SECTION="base"

# Set what files will be copied into this package:
SRC_URI = "file://tunnel.sh \
file://X90tunnel"

do_configure() {
}

do_install() {
    install -d ${D}/etc/init.d/
    install -m 0744 ${WORKDIR}/X90tunnel ${D}/etc/init.d/
    install -m 0744 ${WORKDIR}/tunnel.sh ${D}/etc/init.d/
    pushd ${D}/etc/init.d/
    ln -s ./tunnel ../rc5.d/X90tunnel
    popd
}
