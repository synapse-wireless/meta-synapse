SUMMARY = "The stage 2 rootfs for the Connect E10"

IMAGE_INSTALL = "packagegroup-core-boot ${ROOTFS_PKGMANAGE_BOOTSTRAP}"
IMAGE_INSTALL += "${CORE_IMAGE_EXTRA_INSTALL}"
IMAGE_INSTALL += "mtd-utils mtd-utils-ubifs python openssh ntp"
IMAGE_INSTALL += "u-boot-fw-utils synapse-feed-config-opkg sudo"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE ?= "8192"

MKUBIFS_ARGS = " -e 0x1f800 -c 2048 -m 0x800  -x lzo"

IMAGE_FSTYPES += "${INITRAMFS_FSTYPES}"

IMAGE_FEATURES += "package-management"

# create 'snap' user with password 'synapse'
inherit extrausers
EXTRA_USERS_PARAMS = "\
   useradd -P synapse -s /bin/sh -e 0 -G sudo,dialout snap; \
"

# The following forces the 'snap' user to reset their password on first login
force_passwd_reset() {
	sed -e 's%^snap:\([^:]*\):[0-9]*:%snap:\1:0:%' \
		-i ${IMAGE_ROOTFS}/etc/shadow
}

ROOTFS_POSTPROCESS_COMMAND_append = " force_passwd_reset; "
