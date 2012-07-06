#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import archive.config.reader as reader
import archive.config.xmlhandler as xmlhandler


def set_value(url, value):
    """
    Sets given value
    """
    return xmlhandler.set_element(url, value)


def set_default(url):
    """
    Sets given url to default value
    """
    default = reader.get_default(url)
    if default == '':
        return ''
    else:
        set_value(url, default)
