SUMMARY = "The rescue kernel + initramfs for the Connect E10"

IMAGE_INSTALL = "packagegroup-core-boot ${ROOTFS_PKGMANAGE_BOOTSTRAP} ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE = "8192"

# Causes Yocto to create basic /dev nodes necessary to boot
USE_DEVFS = "0"

# only build the filesystem necessary
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"

IMAGE_INSTALL += "u-boot-fw-utils gnupg mtd-utils mtd-utils-ubifs"
IMAGE_INSTALL += "synapse-recovery"

# allows root to login with no password
IMAGE_FEATURES_append = "ssh-server-dropbear"

# Don't need syslog filling up our RAM (since the rootfs is a RAM disk)
BAD_RECOMMENDATIONS += "busybox-syslog"

# The following sets the hostname of the rescue image to 'rescue'
set_hostname_to_rescue() {
	echo 'rescue' > ${IMAGE_ROOTFS}/etc/hostname
}

# Allow the user to login as root with no password
unzap_empty_root_password() {
	if [ -e ${IMAGE_ROOTFS}/etc/shadow ]; then
		sed -i -e 's%^root:\*:%root::%' ${IMAGE_ROOTFS}/etc/shadow
	elif [ -e ${IMAGE_ROOTFS}/etc/passwd ]; then
		sed -i -e 's%^root:\*:%root::%' ${IMAGE_ROOTFS}/etc/passwd
	fi
}

ROOTFS_POSTPROCESS_COMMAND_append = "\
	set_hostname_to_rescue ; \
	unzap_empty_root_password ; \
"
