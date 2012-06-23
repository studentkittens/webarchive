#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

from xml.etree.ElementTree import ElementTree

import logging

tree = ElementTree()

configPath = '../../../conf/webarchive.conf.xml'


def parse():
    try:
        tree.parse(configPath)
    except KeyError:
        logging.exception('Config File not found')


def set_element(tagname, value):
    tagname = changeinputstring(tagname)
    tree.find(tagname).text = value
    write_file()


def write_file():
    try:
        tree.write(configPath)
    except:
        logging.exception('Writing file failed')


def get_element(value):
    value = changeinputstring(value)
    try:
        parse()
        return tree.findtext(value)
    except:
        logging.info('Tagname not found')
        return ''


def changeinputstring(value):
    index = 0
    end = len(value)
    while index < end:
        if value[index] == ".":
            value = value[0:index] + '/' + value[index + 1:end]
        index += 1
    return value
