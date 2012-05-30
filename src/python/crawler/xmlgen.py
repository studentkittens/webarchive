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
    xmlns:xsi='{xsi}'
    xmlns:wa='{wa}'
    xsi:schemaLocation='{schema}'>
    <wa:meta
        wa:url="{meta[url]}"
        wa:mimeType="{meta[mimeType]}"
        wa:path="{meta[abspath]}"
        wa:createTime="{meta[createTime]}"
        wa:title="{meta[title]}"
    >
        <wa:commitTag
            wa:commitTime="{meta[commitTime]}"
            wa:domain="{meta[domain]}"
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
    #TODO Schema only
    XSI = 'http://www.w3.org/2001/XMLSchema-instance'
    WA = 'http://www.hof-university.de/webarchive'
    SCHEMA = 'http://www.hof-university.de/webarchive file:/home/ccwelich/git/webarchive/xml/file.xsd'
    
    def __init__(self, meta_obj_list=None, xsi=XSI, wa=WA, schema=SCHEMA):
        self.__meta_list = meta_obj_list
        self.__xsi = xsi
        self.__wa = wa
        self.__schema = schema
        self.__xml_list = [] 
        self.__gen_xml()

         

    def __gen_xml(self):
        """
        generates xml list from metadata list
        """
        for item in self.__meta_list:
            xml_node = (XML_TEMPLATE.format(xsi = self.__xsi,
                                            wa = self.__wa,
                                            schema = self.__schema,
                                            meta = item), item['abspath'][:-4])
            print(item['abspath'])
            self.__xml_list.append(xml_node)
        
    
    def dump_all(self):
        """
        dumps all previously generated metadata to disk
        """
        print("before dumpe")
        if len(self.__xml_list) > 0 and self.__meta_list is not None:
            print("dumpe xml files")
            for item in self.__xml_list:
                with open(os.path.join(item[1],'data.xml'),'w') as f:
                    f.write(str(item[0]))
        else:
            print("nothing to dump.")

    
        
if __name__ == '__main__':
    testdata = [{
            'url' : "www.heise.de/index.html",
            'mimeType' : "text/html",
            'abspath' : "www.heise.de/index.html",
            'createTime' : "2012-05-15T17:28:42",
            'title' : "heise online",
            'commitTime' : "2012-05-15T17:30:00",
            'domain' : "www.heise.de"
    }]

    x = XmlGenerator(testdata) 
    x.dump_all()










