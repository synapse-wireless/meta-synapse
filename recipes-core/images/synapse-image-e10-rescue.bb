SUMMARY = "The rescue kernel + initramfs for the Connect E10"

IMAGE_INSTALL = "packagegroup-core-boot ${ROOTFS_PKGMANAGE_BOOTSTRAP} ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE ?= "8192"

# only build the filesystem necessary
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"

IMAGE_INSTALL += "u-boot-fw-utils gnupg mtd-utils mtd-utils-ubifs"
IMAGE_INSTALL += "synapse-recovery"

# allows root to login with no password
IMAGE_FEATURES_append = "debug-tweaks ssh-server-dropbear"

# The following sets the hostname of the rescue image to 'rescue'
set_hostname_to_rescue() {
	echo 'rescue' > ${IMAGE_ROOTFS}/etc/hostname
}

ROOTFS_POSTPROCESS_COMMAND_append = " set_hostname_to_rescue; "
