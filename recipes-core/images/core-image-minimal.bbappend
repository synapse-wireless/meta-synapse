MKUBIFS_ARGS = " -e 0x1f800 -c 2048 -m 0x800  -x lzo"

IMAGE_FSTYPES += "${INITRAMFS_FSTYPES}"
IMAGE_INSTALL += "u-boot-fw-utils opkg htop mtd-utils mtd-utils-jffs2 mtd-utils-ubifs stage1-additions"



