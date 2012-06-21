#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'


import xmlreader
import options


def get_default(value):
    try:
        return options.default_options[value]
    except KeyError:
        return ''


def get(value):
    try:
        if xmlreader.get_element(value) == '' :
            return get_default()
        else:
            return xmlreader.get_element(value)
    except KeyError:
        return ''


def get_with_default(value):
    try:
        return 'Actual: ' + str(get(value)) + '   Default: ' + str(get_default(value))
    except KeyError:
        return 'Wrong Item Name'
