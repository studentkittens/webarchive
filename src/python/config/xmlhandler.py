#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

from xml.etree.ElementTree import ElementTree

import config.options as options
import logging

tree = ElementTree()

configPath = '../../../conf/webarchive.conf.xml'


def parse():
    try:
        tree.parse(configPath)
    except KeyError:
        logging.exception('Config File not found')
    
def set_element(tagname, str):
    tagname = changeinputstring(tagname)
    tree.find(tagname).text = str
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
        if value[index] == "." :
            value = value[0:index] + '/' +  value[index+1:end]
        index=index + 1
    return value

'''
    if value == ( 'crawler/interval' or 'server/notify/interval'):
        try:
            return changeoutputstring(tree.findtext(value))
        except KeyError:
            return ''
    else:

    try:
        return tree.findtext(value)
    except KeyError:
        return ''

def changeoutputstring(value):
    index = 0
    start = 0
    time = 0
    while index < len(value):
        if value[index] == ":" :
            time = time*60 + int(value[start:index])
            start = index + 1
        if index == len(value)-1:
            time = time*60 + int(value[start:index+1])
        index = index + 1
    time = str(time)
    return time
'''
