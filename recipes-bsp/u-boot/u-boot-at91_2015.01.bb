require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://README;beginline=1;endline=22;md5=2687c5ebfd9cb284491c3204b726ea29"

SRCREV = "${AUTOREV}"

PV = "v2015.01-at91"

COMPATIBLE_MACHINE = '(sama5d3xek|sama5d3-xplained|at91sam9x5ek|at91sam9rlek|at91sam9m10g45ek|sama5d4ek|sama5d4-xplained)'

SRC_URI = "git://github.com/cardoe/u-boot-at91.git;branch=u-boot-2015.01-at91-e10"

S = "${WORKDIR}/git"

do_compile () {
    if [ "${@base_contains('DISTRO_FEATURES', 'ld-is-gold', 'ld-is-gold', '', d)}" = "ld-is-gold" ] ; then
        sed -i 's/$(CROSS_COMPILE)ld$/$(CROSS_COMPILE)ld.bfd/g' config.mk
    fi

    unset LDFLAGS
    unset CFLAGS
    unset CPPFLAGS

    cd ${S}; git status; cd -

    oe_runmake connect-e10_defconfig
    oe_runmake ${UBOOT_MAKE_TARGET}
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

