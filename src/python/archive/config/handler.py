#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import archive.config.reader as reader
import archive.config.writer as writer
import archive.config.xmlhandler as xmlhandler


# Convert string to int,
# if it's a numerical string
def _as_int(url):
    if type(url) is str and value.isnumeric():
        return int(value)
    else:
        return value

#   Returns actual value for url.
#   If not found, it returns default value.
def get(url):
    return _as_int(reader.get(url))

#   Returns default value for url.
def get_default(url):
    return _as_int(reader.get_default(url))

#   Sets value of url to str.
def set(url, str):
    return writer.set_value(url, str)

#   Sets value of url back to default value.
def set_default(url):
    return writer.set_default(url)

#   Load xml file with given path
def load(path):
    return xmlhandler.load(path)

