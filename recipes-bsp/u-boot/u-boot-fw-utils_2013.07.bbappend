FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI += "file://fw_env.config.e10"

do_configure_append() {
	rm -f ${S}/tools/env/fw_env.config
	cp ${WORKDIR}/fw_env.config.e10 ${S}/tools/env/fw_env.config
}
