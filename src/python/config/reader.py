#!/usr/bin/env python
# encoding: utf-8

import options

def get_default(value):
    try:
        return options.default_options[value]
    except KeyError:
        return 'Wrong Item Name'

def get(value):
    try:
        return options.actual_options[value]
    except KeyError:
        return 'Wrong Item Name'

def get_with_default(value):
    try:
        return 'Actual: ' + str(options.actual_options[value]) + '   Default: ' + str(options.default_options[value])
    except KeyError:
        return 'Wrong Item Name'
