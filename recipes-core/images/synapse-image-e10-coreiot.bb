SUMMARY = "The default rootfs for the Connect E10"

include synapse-image-e10-base.bb

IMAGE_INSTALL += "python-pip python-tornado iproute2 net-tools iputils"
IMAGE_INSTALL += "nano"
IMAGE_INSTALL += "button leds tunnel run-usb run-net"
