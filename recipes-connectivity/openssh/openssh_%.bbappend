do_install_append() {
	# Disable DNS lookup on connections
	sed -i -e 's:#UseDNS yes:UseDNS no:' \
		${WORKDIR}/sshd_config \
		${D}${sysconfdir}/ssh/sshd_config

	# Disable root logins with a password
	sed -i -e 's:#PermitRootLogin yes:PermitRootLogin without-password:' \
		${WORKDIR}/sshd_config \
		${D}${sysconfdir}/ssh/sshd_config
}
