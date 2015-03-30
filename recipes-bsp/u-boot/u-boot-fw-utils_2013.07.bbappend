FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI += "file://fw_env.config.e10"

do_configure_append() {
	rm -f ${S}/tools/env/fw_env.config
	cp ${WORKDIR}/fw_env.config.e10 ${S}/tools/env/fw_env.config
}

do_install_append() {
	rm ${D}${base_sbindir}/fw_printenv
	cd ${D}${base_sbindir}
	ln -s fw_setenv fw_printenv
}
