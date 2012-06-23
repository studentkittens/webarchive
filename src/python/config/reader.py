#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import logging
import xmlhandler
import options


def get_default(value):
    try:
        return options.default_options[value]
    except KeyError:
        logging.info('No default found')
        return ''


def get(value):
    try:
        ret = xmlhandler.get_element(value)
        if ret == '' :
            return get_default(value)
        else:
            return ret
    except KeyError:
        return ''


def get_with_default(value):
    try:
        return 'Actual: ' + str(get(value)) + '   Default: ' + str(get_default(value))
    except KeyError:
        return 'Wrong Item Name'
