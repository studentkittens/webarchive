#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import os

"""
ugly xml template.
"""

XML_TEMPLATE = """<?xml version="1.0" encoding="UTF-8"?>
<!--
To change this template, choose Tools | Templates
and open the template in the editor.
-->


<wa:file
    xmlns:xsi='{0}'
    xmlns:wa='{1}'
    xsi:schemaLocation='{2}'>
    <wa:meta
        wa:url="{3[url]}"
        wa:mimeType="{3[mimeType]}"
        wa:path="{3[path]}"
        wa:createTime="{3[createTime]}"
        wa:title="{3[title]}"
    >
        <wa:commitTag
            wa:commitTime="{3[commitTime]}"
            wa:domain="{3[domain]}"
        />
    </wa:meta>
    <wa:data>

    </wa:data>
</wa:file>

"""

class XmlGenerator:
    """
    XmlGenerator, generates xml file from meta nodelist
    some ugly needed? code
    """
    XSI = 'http://www.w3.org/2001/XMLSchema-instance'
    WA = 'http://www.hof-university.de/webarchive'
    SCHEMA = 'http://www.hof-university.de/webarchive file:/home/ccwelich/git/webarchive/xml/file.xsd'
    
    def __init__(self, meta_obj=None, xsi=XSI, wa=WA, schema=SCHEMA):
        self.__meta = meta_obj
        self.__xsi = xsi
        self.__wa = wa
        self.__schema = schema
        self.__xmlfile = self.__gen_xml()
         

    def write(self,path='.'):
        with open(os.path.join('','dataaaa.xml'),'w') as f:
           f.write(str(self.__xmlfile))
    
    def __gen_xml(self):
        for item in self.__meta:
            print(XML_TEMPLATE.format(self.__xsi, self.__wa, self.__schema, item))
        
        
if __name__ == '__main__':
    testdata = [{
            'url' : "www.heise.de/index.html",
            'mimeType' : "text/html",
            'path' : "www.heise.de/index.html",
            'createTime' : "2012-05-15T17:28:42",
            'title' : "heise online",
            'commitTime' : "2012-05-15T17:30:00",
            'domain' : "www.heise.de"
    }]

    x = XmlGenerator(testdata) 











