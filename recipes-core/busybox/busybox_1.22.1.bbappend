FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
            file://lsof.cfg \
            file://netstat.cfg \
           "
