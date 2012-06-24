#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

from xml.etree.ElementTree import ElementTree

tree = ElementTree()

gConfigPath = 'webarchive.conf.xml'


def load(configPath):
    global gConfigPath
    gConfigPath = configPath
    tree.parse(configPath)


def set_element(tagname, value):
    tagname = url_to_xpath(tagname)
    try:
        tree.find(tagname).text = value
    except AttributeError:
        return False
    else:
        write_file()
        return True


def write_file():
    tree.write(gConfigPath)


def get_element(value):
    xpath = url_to_xpath(value)
    try:
        return tree.findtext(xpath)
    except:
        return ''


def url_to_xpath(url):
    return url.strip().replace('.', '/')
