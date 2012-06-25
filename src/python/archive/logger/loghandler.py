#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

from time import *
import archive.config.handler as config

debug = 1
info = 2
warning = 3
error = 4
critical = 5

loglevel = 3
logfile = 'log.txt'

#   Set Path for Log File
def file(path):
    global logfile
    logfile = path

#    Set loglebel
def severity(value):
    global loglevel
    loglevel = value

#   Log Debug message
def debug(message):
    log_print(' Debug: ', message, 1)

#   Log Info message
def info(message):
    log_print(' Info: ', message, 2)

#   Log Warning message
def warning(message):
    log_print(' Warning: ', message, 3)

#   Log Error message
def error(message):
    log_print(' Error: ', message, 4)

#   Log Critical message
def critical(message):
    log_print(' Critical: ', message, 5)

#   Print Log message to shell
#   Calls method to write log message to Log file
def log_print(art, message, value):
    if (value >= loglevel):
        print(time() + art + message)
    log_write(art, message)

#   Write Log message to Log file
def log_write(value, message):
    log = open ('log.txt', 'a+')
    log.write(time() + value + message + '\n')
    log.close()

#   Print all Log messages to shell
def log_read():
    log = open ('log.txt', 'r')
    print (log.read())
    log.close()

#   Returns Timestamp for Log messages
def time():
    return strftime("%d.%m.%Y %H:%M:%S :")
