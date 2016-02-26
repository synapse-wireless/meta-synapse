#!/bin/sh
#
# This script will negate the active low on reset line GPIO43. This GPIO pin is sourced 
# from the Atmel AT91SAM9260 cpu pin P5 labled in the E10 PB11. This trace is labeled "RF1_RST" on the schematic
# and the destination is the Synapse Snap module reset line pin 2.
# 

# turn off the reset which is active low. Assert it high.
echo 1 > /sys/class/gpio/gpio43/value

