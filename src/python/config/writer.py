#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import options
import reader

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
