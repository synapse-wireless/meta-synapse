#!/bin/sh
#
# This script is a template for how a reverse
# ssh tunnel to a remote server could be established.

export PASSWORD="xxxxxxxx"

log=/tmp/tunnel.log

echo 'LOG:' $log
while true; do
  echo `date` '- Opening ssh tunnel' | tee -a $log
  /usr/bin/ssh -R 8061:localhost:22 username@ipaddress -y -N -K 60
  echo `date` '- return value = ' $? | tee -a $log
  echo 'reopening tunnel in 5 seconds'
  sleep 5
done
