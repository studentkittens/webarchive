#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import archive.config.xmlhandler as xmlhandler
import archive.config.options as options


def get_default(url):
    """
    Try find default value for url.
    If url is found, return value.
    Else it returns ''
    """
    try:
        value = options.default_options[url]
        return value
    except KeyError:
        return False


def get(url):
    """
    Try find value for url.
    If url is found, return value.
    If url isn't found, try to get default value
    """
    try:
        ret = xmlhandler.get_element(url)
        if ret == False:
            return get_default(url)
        else:
            return ret
    except KeyError:
        return False
