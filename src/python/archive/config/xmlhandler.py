#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

from xml.etree.ElementTree import ElementTree


tree = ElementTree()
gConfigPath ='webarchive.conf.xml'


def load(configPath):
    """
    Load xml config file by the given path
    """
    global gConfigPath
    gConfigPath = configPath
    try:
        tree.parse(configPath)
        return True
    except IOError:
        return False


def set_element(url, value):
    """
    Set url to value.
    If url is found, it is set to value and writes file and return is true.
    If not, return is False
    """
    tagname = url_to_xpath(url)
    try:
        tree.find(tagname).text = value
    except AttributeError:
        return False
    else:
        write_file()
        return True


def write_file():
    """
    Writes the actual Element Tree to xml file.
    """
    tree.write(gConfigPath)


def get_element(url):
    """
    Returns value for url
    If not found, it returns ''
    """
    xpath = url_to_xpath(url)
    try:
        value = tree.findtext(xpath)
        if value == None:
            return False
        return value
    except AttributeError:
        return False


def url_to_xpath(url):
    """
    Converts the given url string
    """
    return url.strip().replace('.', '/')
