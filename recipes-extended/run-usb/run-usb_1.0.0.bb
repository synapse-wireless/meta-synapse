# Edit this section as necessary...
DESCRIPTION = "Some custom init script..."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/license.txt;md5=7c7bf297f1e53906530baf3f40893003"
SECTION="base"

# Set what files will be copied into this package:
SRC_URI = "file://RunUsb.py \
file://license.txt"

do_configure() {
}

do_install() {
    install -d ${D}/etc/init.d/
    install -d ${D}/etc/rc5.d/
    install -m 0744 ${WORKDIR}/RunUsb.py ${D}/etc/init.d/
    cd ${D}/etc/rc5.d/
    ln -s ../init.d/RunUsb.py ./X77RunUsb.py
    cd -
}
