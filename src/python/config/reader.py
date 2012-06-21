#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'


import xmlreader
import options


def get_default(value):
    try:
        return options.default_options[value]
    except KeyError:
        return 'Wrong Item Name'


def get(value):
    try:
        return xmlreader.get_elemnt(value)
    except KeyError:
        return 'Wrong Item Name'


def get_with_default(value):
    try:
        return 'Actual: ' + get(value) + '   Default: ' + get_default(value)
    except KeyError:
        return 'Wrong Item Name'
