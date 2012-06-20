#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import archive.config.reader as reader
import archive.config.writer as writer


def get(value):
    return reader.get(value)


def get_default(value):
    return reader.get_default(value)


def get_with_default(value):
    return reader.get_with_default(value)


def set(value, str):
    return writer.set(value, str)


def set_default(value):
    return writer.set_default(value)
