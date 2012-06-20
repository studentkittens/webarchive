#!/usr/bin/env python
# encoding: utf-8

"""
XmlReader submodule
"""

__author__ = 'Christopher Pahl'

from xml.etree.ElementTree import ElementTree


def actual_name(nodetag):
    return nodetag[nodetag.rfind('}') + 1:]


def makecleandict(dirtydict):
    cleandict = dict()
    for key, value in dirtydict.items():
        cleandict[actual_name(key)] = value
    return cleandict


class XmlReader(object):
    """Read a certain XML File and return a metadict"""

    def __init__(self, xmlpath):
        """
        Convert any valid data.xml to a metadict

        :xmlpath: path to data.xml
        """
        self.__xmlpath = xmlpath
        self.__tree = ElementTree()
        self.__tree.parse(xmlpath)

    def parse(self):
        """
        Do actual parsing

        :returns: a new metadict
        """
        metadict, tagdict = dict(), dict()
        for node in self.__tree.iter():
            tag = actual_name(node.tag)

            if tag == 'meta':
                metadict = makecleandict(node.attrib)

            if tag == 'commitTag':
                tagdict = makecleandict(node.attrib)

        return dict(metadict, **tagdict)
