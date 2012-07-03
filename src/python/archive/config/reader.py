#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import archive.config.xmlhandler as xmlhandler
import archive.config.options as options

#   Try find default value for url.
#   If url is found, return value.
#   Else it returns ''
def get_default(url):
    try:
        value = options.default_options[url]
        return value
    except KeyError:
        return False

#   Try find value for url.
#   If url is found, return value.
#   If url isn't found, try to get default value
def get(url):
    try:
        ret = xmlhandler.get_element(url)
        if ret == False:
            return get_default(url)
        else:
            return ret
    except KeyError:
        return False

