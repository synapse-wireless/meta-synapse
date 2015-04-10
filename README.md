## Getting the Code
-----
The following instructions are based on those at:
https://github.com/linux4sam/meta-atmel

```bash
export E10_SRC=~/work/e10-2.x

git clone git://git.yoctoproject.org/poky ${E10_SRC}
git clone git://git.openembedded.org/meta-openembedded ${E10_SRC}/meta-openembedded
git clone git://github.com/meta-qt5/meta-qt5.git ${E10_SRC}/meta-qt5
git clone git://github.com/linux4sam/meta-atmel ${E10_SRC}/meta-atmel
git clone git@git.synapse-wireless.com:octo/meta-synapse.git ${E10_SRC}/meta-synapse
cd ${E10_SRC} && git checkout -b my_branch origin/dizzy-12.0.1
cd ${E10_SRC}/meta-openembedded && git checkout -b my_branch origin/dizzy
cd ${E10_SRC}/meta-qt5 && git checkout -b my_branch origin/master
cd ${E10_SRC}/meta-atmel && git checkout -b my_branch origin/dizzy
```

## Creating your build directory
-----

```bash
cd ${E10_SRC}
export TEMPLATECONF=meta-synapse-conf
source oe-init-build-env build-atmel
```

If there are any documented changes to the `local.conf` then those will need to be
get set in your `build-atmel/conf` directory as well.

## Enabling bitbake in your terminal
-----

```bash
cd ${E10_SRC}
source oe-init-build-env build-atmel
```

You will do the above step anytime you open a new terminal to interact
with Yocto. It is not a destructive command. It can be run over and over.

## Steps to generate the Rescue Image Recipe
-----

```bash
bitbake synapse-image-e10-rescue
```

To recompile you would first have to clean the image:
```bash
bitbake -c cleansstate synapse-image-e10-rescue
```

Before you can use the first to command to build it again. *Note:* That is is
cleansstate with 2 s characters. These steps can be combined into:
```bash
bitbake -c cleansstate synapse-image-e10-rescue; bitbake synapse-image-e10-rescue
```

## Interesting Recipes
-----

* virtual/kernel - the Linux kernel package
* synapse-image-e10-rescue - the E10 rescue image (kernel + initramfs)
* synapse-image-e10-ota - the E10 OS image for the OTA procedure
* synapse-recovery - script that runs in the rescue image to reload the
* package-index - regenerates the metadata for opkg for any built packages
kernel and the rootfs

## Extra Packages We Build
-----

* iproute2 - provides `ip`
* net-tools - provides `ifconfig`
* iputils - provides `ping`, `hostname`, and `arp`
* nano - cause vi can be scary
* openvpn
* python-pip
* python-pytz
* python-tornado

## Yocto Dependencies
-----

* chrpath
* gawk
* wget
* git-core
* diffstat
* unzip
* texinfo
* build-essentials


## Booting via tftp
-----

```bash
setenv ipaddr <E10 ip addr>
setenv serverip <tftp server ip addr>
tftpboot 0x21000000 uImage-at91sam9x5ek.bin
```
