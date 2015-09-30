# Edit this section as necessary...
DESCRIPTION = "Some custom init script..."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/license.txt;md5=7c7bf297f1e53906530baf3f40893003"
SECTION="base"

# Set what files will be copied into this package:
SRC_URI = "file://RunUsb.py"

do_configure() {
}

do_install() {
    install -d ${D}/etc/init.d/
    install -m 0744 ${WORKDIR}/RunUsb.py ${D}/etc/init.d/
    pushd ${D}/etc/init.d/
    ln -s ./RunUsb.py ../rc5.d/X77RunUsb.py
    popd
}
