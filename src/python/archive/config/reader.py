#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'


import archive.config.options as options


def get_default(value):
    try:
        return options.default_options[value]
    except KeyError:
        return ''


def get(value):
    try:
        return options.actual_options[value]
    except KeyError:
        return ''


def get_with_default(value):
    try:
        return 'Actual: ' + str(options.actual_options[value]) + '   Default: ' + str(options.default_options[value])
    except KeyError:
        return 'Wrong Item Name'
