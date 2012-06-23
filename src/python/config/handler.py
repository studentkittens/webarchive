#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import config.reader as reader
import config.writer as writer
import config.xmlhandler as xmlhandler

def get(value):
    return reader.get(value)

def get_default(value):
    return reader.get_default(value)

def get_with_default(value):
    return reader.get_with_default(value)

def get_path():
    return xmlhandler.configPath

def set(tagname, str):
    return writer.set_value(tagname, str)
   
def set_default(value):
    return writer.set_default(value)

def set_configPath(value):
    return writer.set_configPath(value)
