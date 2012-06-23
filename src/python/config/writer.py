#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import reader
import xmlhandler
import logging

def set_value(tagname, str):
    xmlhandler.set_element(tagname, str)
    
def set_configPath(value):
    xmlhandler.configPath = value

def set_default(value):
    default = reader.get_default(value)
    if default == '':
        return ''
    else:
        set_value(value, default)
    
'''    
def set(value, st):
    if (value == ('crawler.depth' or 'crawler.maxInst' or 'server.port' or 
        'server.notify.interval' or 'javadapter.port')):
        try:
            var1 = int(st)
            options.actual_options[value]= var1
            return value + ' is set to ' + str(var1)
        except ValueError:
            return 'NAN'
        
    if (value == 'crawler.interval'):
        try:
            var2 = float(st)
            options.actual_options[value]= var2
            return value + ' is set to ' + str(var2)
        except ValueError:
            return 'NAN'
        
    else:
        try:
            options.actual_options[value] = st
            return value + ' is set to ' + st
        except KeyError:
            return value + ' is not a Item'

def set_default(value):
    try:
        options.actual_options[value] = reader.get_default(value)
        return value + ' set to ' + options.actual_options[value]
    except KeyError:
        return value + ' is not a Item'
'''
