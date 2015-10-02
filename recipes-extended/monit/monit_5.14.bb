# Based off of a recipe found here:  
# https://github.com/openembedded/openembedded/blob/master/recipes/monit/monit_4.10.1.bb

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=ea116a7defaf0e93b3bb73b2a34a3f51"

DEPENDS = "openssl"
PR = "r0"

SRC_URI = "http://mmonit.com/monit/dist/monit-${PV}.tar.gz\ 
           file://monitrc\
           file://monit.init"

SRC_URI[md5sum] = "1b3ae1eb08a0914402a8764e5689c1c5"
SRC_URI[sha256sum] = "d0424c3ee8ed43d670ba039184a972ac9f3ad6f45b0806ec17c23820996256c6"

INITSCRIPT_NAME = "monit"
INITSCRIPT_PARAMS = "defaults 99"

inherit autotools update-rc.d

# Remove PAM and largefile support to reduce size and library dependencies.
# Enable an optimized compile (reduces size by ~ 2x)
EXTRA_OECONF = "--with-ssl-lib-dir=${STAGING_LIBDIR} --with-ssl-incl-dir=${STAGING_INCDIR}\
                --without-pam --without-largefiles --enable-optimized\
                libmonit_cv_setjmp_available=yes libmonit_cv_vsnprintf_c99_conformant=yes"

do_install_append() {
    install -d ${D}$sysconfdir/init.d/
    install -m 755 ${WORKDIR}/monit.init ${D}${sysconfdir}/init.d/monit
    install -m 600 ${WORKDIR}/monitrc ${D}${sysconfdir}/monitrc
}

# Don't mess with this file during an upgrade
CONFFILES_${PN} += "${sysconfdir}/monitrc"

# Monit doesn't support splitting the build and source dirs.
B = "${S}"
