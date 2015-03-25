do_install_append() {
    sed -e 's/# %sudo/%sudo/' -i ${D}${sysconfdir}/sudoers
}
