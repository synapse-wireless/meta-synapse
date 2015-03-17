SUMMARY = "The stage 2 rootfs for the Connect E10"

IMAGE_INSTALL = "packagegroup-core-boot ${ROOTFS_PKGMANAGE_BOOTSTRAP} ${CORE_IMAGE_EXTRA_INSTALL}"
IMAGE_INSTALL += "mtd-utils mtd-utils-ubifs python"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE ?= "8192"

MKUBIFS_ARGS = " -e 0x1f800 -c 2048 -m 0x800  -x lzo"

IMAGE_FSTYPES += "${INITRAMFS_FSTYPES}"

IMAGE_INSTALL += "u-boot-fw-utils gnupg mtd-utils mtd-utils-ubifs"
