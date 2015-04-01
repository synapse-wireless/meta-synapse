DESCRIPTION = "Tornado is an open source version of the scalable, non-blocking web server and tools that power FriendFeed."
HOMEPAGE = "http://tornadoweb.org"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SRCNAME = "tornado"

SRC_URI = "https://pypi.python.org/packages/source/t/${SRCNAME}/${SRCNAME}-${PV}.tar.gz"
SRC_URI[md5sum] = "985c0e704b765c33a6193d49d1935588"
SRC_URI[sha256sum] = "900c5124ebdb6598ca8e8a0c5888f41a5f14117952d5515258e3d20222b21bfa"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit setuptools

RDEPENDS_${PN} = "\
	python-core \
"

