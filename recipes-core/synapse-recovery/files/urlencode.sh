#!/bin/sh

# A very primative URL string encoder that works within the limited
# environment provided by the rescue image.



# sed cmds to urlencode
SEDCMDS='s:%:%25:g; s: :%20:g; s:<:%3C:g; s:>:%3E:g; s:#:%23:g; s:{:%7B:g; s:}:%7D:g; s:|:%7C:g; s:\\:%5C:g; s:\^:%5E:g; s:~:%7E:g; s:\[:%5B:g; s:\]:%5D:g; s:`:%60:g; s:;:%3B:g; s:/:%2F:g; s:?:%3F:g; s^:^%3A^g; s:@:%40:g; s:=:%3D:g; s:&:%26:g; s:\$:%24:g; s:\!:%21:g; s:\*:%2A:g; s:,:%2C:g; s:(:%28:g; s:):%29:g;'

urlencode_sed() {
        result=`echo "$*" | sed -e "$SEDCMDS"`
        echo $result
}
[ $# -ge 1 ] && input="$*" || read input

urlencode_sed "$input"