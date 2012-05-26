#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import xml.dom.minidom as node
import xml.dom.minidom
import os

template = ("""<?xml version="1.0" encoding="UTF-8"?>
<wa:file xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:wa='http://www.hof-university.de/webarchive'
    xsi:schemaLocation='http://www.hof-university.de/webarchive file:/home/ccwelich/git/webarchive/xml/file.xsd'>
    <wa:meta wa:s="xxxasdfasdfasdfasdfs">
        <wa:commitTag/>
    </wa:meta>
    <wa:data>
    </wa:data>
</wa:file>""")


class XmlGenerator:
    def __init__(self, meta_obj=None):
        self.__xmlfile = self.__gen_xml()


    def write(self,path='.'):
        with open(os.path.join('','data.xml'),'w') as f:
           f.write(str(self.__xmlfile))
    
    def __gen_xml(self):
        doc = xml.dom.minidom.parseString(template)
        node = doc.getElementsByTagName("wa:meta")[0]
        node.setAttribute("wa:katzenbaunm","miku")
        node.setAttribute("wa:kka","asd")
        node.setAttribute("wa:s","xxx")
        node.setAttribute("wa:xxx","miku")
        return doc.toprettyxml(newl='')

if __name__ == '__main__':
    x = XmlGenerator() 
    x.write()
