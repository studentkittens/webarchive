#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import archive.config.reader as reader
import archive.config.xmlhandler as xmlhandler


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
