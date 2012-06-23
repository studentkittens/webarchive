#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import archive.config.reader as reader
import archive.config.writer as writer
import archive.config.xmlhandler as xmlhandler


# Convert string to int,
# if it's a numerical string
def _as_int(value):
    if type(value) is str and value.isnumeric():
        return int(value)
    else:
        return value


def get(value):
    return _as_int(reader.get(value))


def get_default(value):
    return _as_int(reader.get_default(value))


def set(tagname, str):
    return writer.set_value(tagname, str)


def set_default(value):
    return writer.set_default(value)


def load(path):
    return xmlhandler.load(path)
