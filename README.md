## Steps to setup
-----
Follow the instructions below, taken from https://github.com/linux4sam/meta-atmel

1.  Clone the Yocto/Poky git repository - "git clone git://git.yoctoproject.org/poky ~/poky"
2.  Checkout the "dizzy" branch - "cd ~/poky; git checkout dizzy-12.0.1 -b my_branch"
3.  Clone the meta-openembedded git repository - "git clone git://git.openembedded.org/meta-openembedded ~/poky/meta-openembedded"
4.  Checkout the proper meta-openembedded branch - "cd ~/poky/meta-openembedded; git checkout origin/dizzy -b my_branch"
5.  Clone the meta-qt5 git repository - "git clone git://github.com/meta-qt5/meta-qt5.git ~/poky/meta-qt5"
6.  Checkout the proper meta-qt5 branch - "cd ~/poky/meta-qt5; git checkout origin/master -b my_branch"
5.  Clone the linux4sam/meta-atmel layer - "git clone git://github.com/linux4sam/meta-atmel ~/poky/meta-atmel"
6.  Checkout the proper meta-atmel layer - "cd ~/poky/meta-atmel; git checkout origin/dizzy -b my_branch"
7.  Clone the meta-synapse layer - "git clone git@git.synapse-wireless.com:octo/meta-synapse.git ~/poky/meta-synapse"

## Setup Steps Bash-ified

```bash
PROJ_DIR=~/work/e10-2.x
git clone git://git.yoctoproject.org/poky ${PROJ_DIR}
git clone git://git.openembedded.org/meta-openembedded ${PROJ_DIR}/meta-openembedded
git clone git://github.com/meta-qt5/meta-qt5.git ${PROJ_DIR}/meta-qt5
git clone git://github.com/linux4sam/meta-atmel ${PROJ_DIR}/meta-atmel
git clone git@git.synapse-wireless.com:octo/meta-synapse.git ${PROJ_DIR}/meta-synapse
cd ${PROJ_DIR} && git checkout -b my_branch origin/dizzy-12.0.1
cd ${PROJ_DIR}/meta-openembedded && git checkout -b my_branch origin/dizzy
cd ${PROJ_DIR}/meta-qt5 && git checkout -b my_branch origin/master
cd ${PROJ_DIR}/meta-atmel && git checkout -b my_branch origin/dizzy
```

## Steps to generate the Stage 1 Kernel/Initramfs
-----
1. cd ~/poky/
2. source oe-init-build-env build-atmel
3. cp ../meta-synapse/build-dir/conf/* ./conf/
4. bitbake core-image-minimal

## Dependencies

* chrpath

## Bitbake dance
-----
To update the stage1-additions package and have it reflected in the rootfs image

From inside the build-atmel directory, after you have sourced the oe-init-build-env file...
```bash
bitbake -c cleansstate stage1-additions; bitbake stage1-additions
bitbake -c cleansstate core-image-minimal; bitbake core-image-minimal
```

To build the kernel the first time, comment out these lines

#CONFIG_INITRAMFS_SOURCE="/home/cbagwell/poky/build-atmel/tmp/deploy/images/at91sam9x5ek/core-image-minimal-at91sam9x5ek.cpio.gz"
#CONFIG_INITRAMFS_ROOT_UID=0
#CONFIG_INITRAMFS_ROOT_GID=0

```bash
bitbake -c cleansstate virtual/kernel; bitbake virtual/kernel
```

To update the Stage1-Kernel and initramfs
Add back in the lines you just uncommeted, but have it pointing to your initramfs location.
then re-run the command above.


## Booting via tftp

```
setenv ipaddr <E10 ip addr>
setenv serverip <tftp server ip addr>
tftpboot 0x21000000 uImage-at91sam9x5ek.bin
```