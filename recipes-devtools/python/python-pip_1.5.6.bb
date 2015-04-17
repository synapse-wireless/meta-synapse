# Using version meta-openembedded fido
# However this can't be dropped since we are tweaking the depends
# It would need to be changed into a bbappend
# The original package depends on everything include IDLE which we
# don't need
# http://cgit.openembedded.org/meta-openembedded/plain/meta-python/recipes-devtools/python/python-pip_1.5.6.bb
SUMMARY = "PIP is a tool for installing and managing Python packages"
LICENSE = "MIT & LGPL-2.1"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=45665b53032c02b35e29ddab8e61fa91"

SRC_URI = "https://pypi.python.org/packages/source/p/pip/pip-${PV}.tar.gz"

SRC_URI[md5sum] = "01026f87978932060cc86c1dc527903e"
SRC_URI[sha256sum] = "b1a4ae66baf21b7eb05a5e4f37c50c2706fa28ea1f8780ce8efe14dcd9f1726c"

S = "${WORKDIR}/pip-${PV}"

inherit setuptools

# Since PIP is like CPAN for PERL we need to drag in all python modules to ensure everything works
RDEPENDS_${PN} = "\
	python-2to3 \
	python-bsddb \
	python-codecs \
	python-compile \
	python-compiler \
	python-compression \
	python-crypt \
	python-ctypes \
	python-curses \
	python-datetime \
	python-db \
	python-difflib \
	python-distutils \
	python-elementtree \
	python-email \
	python-fcntl \
	python-gdbm \
	python-html \
	python-io \
	python-json \
	python-lang \
	python-logging \
	python-mailbox \
	python-math \
	python-mime \
	python-misc \
	python-mmap \
	python-multiprocessing \
	python-netclient \
	python-netserver \
	python-numbers \
	python-pickle \
	python-pkgutil \
	python-pprint \
	python-profile \
	python-pydoc \
	python-re \
	python-readline \
	python-resource \
	python-robotparser \
	python-shell \
	python-smtpd \
	python-sqlite3 \
	python-stringold \
	python-subprocess \
	python-syslog \
	python-terminal \
	python-textutils \
	python-threading \
	python-unittest \
	python-unixadmin \
	python-xml \
	python-xmlrpc \
	python-zlib \
	python-distribute"
