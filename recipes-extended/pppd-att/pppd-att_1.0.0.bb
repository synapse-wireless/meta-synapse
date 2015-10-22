DESCRIPTION = "PPPD Scripts for dialing AT&T over a cellular connection."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/license.txt;md5=84ac358e39d1066105ec3ceb37c0cb44"
SECTION="base"

RRECOMMENDS_${PN} = "kernel-module-ppp-async kernel-module-ppp-generic kernel-module-sierra kernel-module-usbserial ppp"

# Set what files will be copied into this package:
SRC_URI = "file://att \
file://att_chat \
file://license.txt"

do_configure() {
}

do_install() {
    install -d ${D}/etc/ppp/peers/
    install -m 0755 ${WORKDIR}/att ${D}/etc/ppp/peers
    install -m 0755 ${WORKDIR}/att_chat ${D}/etc/ppp/peers/
}

pkg_postinst_${PN} () {
#!/bin/ash 

if [ ! -c /dev/ppp ] ; then
    mknod /dev/ppp c 108 0
    chown root:dialout /dev/ppp
    chmod 664 /dev/ppp
fi

}
