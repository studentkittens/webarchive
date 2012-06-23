#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import archive.config.xmlhandler as xmlhandler
import archive.config.options as options


def get_default(value):
    try:
        return options.default_options[value]
    except KeyError:
        return ''


def get(value):
    try:
        ret = xmlhandler.get_element(value)
        if ret == '':
            return get_default(value)
        else:
            return ret
    except KeyError:
        return ''
