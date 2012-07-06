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

def file(path):
    '''
    Set Path for Logfile.
    '''
    global logfile
    logfile = path

def severity(value):
    '''
    Set Loglevel.
    Possible level's:
    debug, info, warning, error and critical.
    '''
    global loglevel
    loglevel = value

def debug(message):
    log_print(' Debug: ', message, 1)

def info(message):
    log_print(' Info: ', message, 2)

def warning(message):
    log_print(' Warning: ', message, 3)

def error(message):
    log_print(' Error: ', message, 4)

def critical(message):
    log_print(' Critical: ', message, 5)

def log_print(art, message, value):
    '''
    Prints Log message to shell, if message level is highter than loglevel.
    Calls method to write log message to Log file.
    '''
    if (value >= loglevel):
        print(time() + art + message)
    log_write(art, message)

def log_write(value, message):
    '''
    Write Log message to log file
    '''
    log = open ('log.txt', 'a+')
    log.write(time() + value + message + '\n')
    log.close()

def log_read():
    '''
    Prints all Log Messages to shell
    '''
    log = open ('log.txt', 'r')
    print (log.read())
    log.close()

def time():
    '''
    Get Timestamp for Log Messages
    '''
    return strftime("%d.%m.%Y %H:%M:%S :")
