#!/usr/bin/python

# WARNING:  This file may be overwritten by the boot server.

import sys
import os
import os.path
import traceback
import uuid

boot_server = ''
update_script_name = ''

SILENT = ' 2> /dev/null'
PREFIX = 'RunNet: '

def is_button_pressed():
    if sys.platform.startswith('win'):
        return False
    rv = os.system('/usr/bin/button 1> /dev/null')
    return (rv == 0)

def set_red_led(value):
    os.system("redled " + str(value))

def get_mac_addr():
    mac = '%x' % (uuid.getnode())
    return mac[-6:]


def is_update_enabled():
    if sys.platform.startswith('win'):
        return None
    return not os.path.exists('/etc/no_update')


def run_script(filepath):
    if os.path.exists(filepath) and os.path.isfile(filepath):
        print PREFIX + 'Running Script'
        try:
            rv = execfile(filepath, globals())
            if rv:
                print 'errno = ', rv
        except:
            traceback.print_exc()
        print PREFIX + 'End of script'


def runNet():
    set_red_led(1)
    if is_button_pressed() or is_update_enabled():
        print PREFIX + 'Retrieving script'
        mac = get_mac_addr()
        try:
            rv = os.chdir('/tmp')
            # wget options:  Timeout=5sec, tries=2  (otherwise we might block here forever)
            rv = os.system('wget -q -T 5 -t 2 http://%s/scripts/%s/%s' % (boot_server, mac, update_script_name) )
            if rv == 0:
                run_script('/tmp/' + update_script_name)
                # If you want to keep your script, your script should back
                # itself up. If the file already exists, the next time a
                #  d/l attempt is made, the new file will be renamed and
                # thus the wrong script will be run.  Fair warning.
                os.system('rm -f /tmp/' + update_script_name)
            else:
                print PREFIX + 'Update not found or server not running'
        except:
            traceback.print_exc()
    else:
        print PREFIX + 'Skipping update'
    print PREFIX + 'done'
    set_red_led(0)

if __name__ == '__main__':
    runNet()

