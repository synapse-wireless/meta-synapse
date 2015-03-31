SUMMARY = "The rootfs for the Connect E10 tailored for Over-the-Air updates"

inherit synapse-image-e10-base

# set 'snap' user with password 'synapse'
inherit extrausers
EXTRA_USERS_PARAMS_append = "\
   usermod -P synapse snap; \
"

# The following forces the 'snap' user to reset their password on first login
force_passwd_reset() {
	sed -e 's%^snap:\([^:]*\):[0-9]*:%snap:\1:0:%' \
		-i ${IMAGE_ROOTFS}/etc/shadow
}

ROOTFS_POSTPROCESS_COMMAND_append = " force_passwd_reset; "
