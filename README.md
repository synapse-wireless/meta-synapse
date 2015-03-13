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

## Steps to generate the Stage 1 Kernel/Initramfs
-----
1. cd ~/poky/
2. source oe-init-build-env build-atmel
3. cp ../meta-synapse/build-dir/conf/* ./conf/
4. bitbake core-image-minimal