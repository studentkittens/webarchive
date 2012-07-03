#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

from xml.etree.ElementTree import ElementTree

tree = ElementTree()

gConfigPath ='webarchive.conf.xml'

#   Load xml config file by the given path
def load(configPath):
    global gConfigPath
    gConfigPath = configPath
    try:
        tree.parse(configPath)
        return True
    except IOError:
        return False

#   Set url to value.
#   If url is found, it is set to value and writes file and return is true.
#   If not, return is False
def set_element(url, value):
    tagname = url_to_xpath(url)
    try:
        tree.find(tagname).text = value
    except AttributeError:
        return False
    else:
        write_file()
        return True

#   Writes the actual Element Tree to xml file.
def write_file():
    tree.write(gConfigPath)

#   Returns value for url
#   If not found, it returns ''
def get_element(url):
    xpath = url_to_xpath(url)
    try:
        value = tree.findtext(xpath)
        if value == None:
            return False
        return value
    except AttributeError:
        return False

#   Converts the given url string
def url_to_xpath(url):
    return url.strip().replace('.', '/')

