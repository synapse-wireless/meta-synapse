# Bump due to changes this brings in
PR = "r2"

# This allows us to override the value for the MACHINE that we are
# currently using to specify a specific config we want to use
KMACHINE := "connect-e10"

# Use uImage for U-Boot
KERNEL_IMAGETYPE = "uImage"

FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI += "file://0001-Add-support-for-Connect-E10-to-Kconfig.patch"
SRC_URI += "file://0002-Add-board-connect-e10-file.patch"
SRC_URI += "file://0003-Add-board-connect-e10-to-Makefile.patch"
SRC_URI += "file://0004-Add-board-connect-e10-to-mach-types.patch"

# Helper script to load our kernel into flash when the package is installed
# on the device to make upgrades seamless for the user.
pkg_postinst_kernel-image(){
#!/bin/sh -e
# Commands to carry out
KERNEL_MTD=/dev/mtd3
KERNEL_IMG=uImage-${KERNEL_VERSION}
TESTPASSES=5

die() {
    logger -p daemon.emerg -t kernel-image-install -s $*
    exit 1
      }

# We only want this to run on the actual device
[ "x${D}" != "x" ] || exit 1

# Test NAND for bad blocks. Mark them bad if found. Run ${TESTPASSES} tests.
nandtest -p ${TESTPASSES} -m ${KERNEL_MTD}

# Erase flash which is required for the next steps
flash_erase ${KERNEL_MTD} 0 0 || die "Failed to erase ${KERNEL_MTD}"

# Program in kernel
nandwrite -p ${KERNEL_MTD} /boot/${KERNEL_IMG} \
    || die "Failed to write kernel to NAND"

echo "The new kernel will be loaded at the next reboot."
}

