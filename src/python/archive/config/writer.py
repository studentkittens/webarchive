#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import archive.config.reader as reader
import archive.config.xmlhandler as xmlhandler

#   Set url to value
def set_value(url, value):
    xmlhandler.set_element(url, value)

#   Set url back to default
def set_default(value):
    default = reader.get_default(value)
    if default == '':
        return ''
    else:
        set_value(value, default)
