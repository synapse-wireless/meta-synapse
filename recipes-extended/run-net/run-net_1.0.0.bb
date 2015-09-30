# Edit this section as necessary...
DESCRIPTION = "Net Syncronization Script"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/license.txt;md5=7c7bf297f1e53906530baf3f40893003"
SECTION="base"

# Set what files will be copied into this package:
SRC_URI = "file://RunNet.py"

do_configure() {
}

do_install() {
    install -d ${D}/etc/init.d/
    install -m 0744 ${WORKDIR}/RunNet.py ${D}/etc/init.d/
    pushd ${D}/etc/init.d/
    ln -s ./RunNet.py ../rc5.d/X88RunNet.py
    popd
}
