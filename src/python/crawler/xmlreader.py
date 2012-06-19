#!/usr/bin/env python
# encoding: utf-8

"""
XMLReader submodule
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


class XMLReader(object):
    """Docstring for XMLReader """

    def __init__(self, xmlpath):
        self.__xmlpath = xmlpath
        self.__tree = ElementTree()
        self.__tree.parse(xmlpath)

    def parse(self):
        metadict, tagdict = dict(), dict()
        for node in self.__tree.iter():
            tag = actual_name(node.tag)

            if tag == 'meta':
                metadict = makecleandict(node.attrib)

            if tag == 'commitTag':
                tagdict = makecleandict(node.attrib)

        return dict(metadict, **tagdict)


if __name__ == '__main__':
    # tests for this are in xmlgen.py
    from pprint import pprint
    pprint(XMLReader('data.xml').parse())
