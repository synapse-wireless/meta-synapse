DESCRIPTION = "Ultra fast JSON encoder and decoder for Python"
HOMEPAGE = "http://github.com/esnme/ultrajson"
SECTION = "devel/python"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/../LICENSE.txt;md5=cbb7d7d409df43b6a129e9c06d33f931"
SRCNAME = "ujson"

SRC_URI = "\
	https://pypi.python.org/packages/source/u/${SRCNAME}/${SRCNAME}-${PV}.zip;name=archive \
	https://raw.githubusercontent.com/esnme/ultrajson/master/LICENSE.txt;name=lic \
"
SRC_URI[archive.md5sum] = "8148a2493fff78940feab1e11dc0a893"
SRC_URI[archive.sha256sum] = "68cf825f227c82e1ac61e423cfcad923ff734c27b5bdd7174495d162c42c602b"
SRC_URI[lic.md5sum] = "cbb7d7d409df43b6a129e9c06d33f931"
SRC_URI[lic.sha256sum] = "3d0bf08e9f269f093baea3c6ab1a44b05d531197493942414114e969cd67b594"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-numbers"

