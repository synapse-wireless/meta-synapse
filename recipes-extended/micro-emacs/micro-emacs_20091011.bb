# Based on http://cgit.openembedded.org/openembedded/plain/recipes/micro-emacs
# Updated License stuff so it would actually build...

DESCRIPTION = "Mini-version of emacs, from http://www.jasspa.com"
SECTION = "console/utils"
PRIORITY = "optional"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://license.txt;md5=cf397fd8ee1f0168c3d80422aa9d8323"
DEPENDS = "ncurses"
PR = "r1"

S = "${WORKDIR}/me091011"

SRC_URI = "http://www.jasspa.com/release_20090909/jasspa-mesrc-${PV}.tar.gz;name=archive \
           file://zaurus_make.patch"

do_compile () {
        oe_runmake -C src -f zaurus.gmk mec
}

do_install() {
        install -d ${D}${bindir}
        install -d ${D}${datadir}/jasspa/macros
        install -m 0755 src/mec ${D}${bindir}/mec
        install -m 0644 ${WORKDIR}/*.* ${D}${datadir}/jasspa/macros/
}

PACKAGES += " ${PN}-macros"
FILES_${PN}-macros = "${datadir}/jasspa"


SRC_URI[archive.md5sum] = "a4db0f1b9bf6fd9699d13366e15305b9"
SRC_URI[archive.sha256sum] = "44109a9118da34a1d026450ec85a3f85d983c4015c97db2b9abbad69e8d90889"

