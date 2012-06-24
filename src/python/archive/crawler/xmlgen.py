#!/usr/bin/env python
# encoding: utf-8

"""
Xmlgen submodule that contains the metadata xml template
"""

__author__ = 'Christoph Piechula'

import os
import logging
from xml.sax.saxutils import quoteattr


XML_TEMPLATE = """<?xml version="1.0" encoding="UTF-8"?>
<wa:file
    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
    xmlns:wa='http://www.hof-university.de/webarchive'
    xsi:schemaLocation='http://www.hof-university.de/webarchive {schema_path}'>
    <wa:meta
        wa:url={meta[url]}
        wa:mimeType={meta[mimeType]}
        wa:path={meta[path]}
        wa:createTime={meta[createTime]}
        wa:title={meta[title]}
    >
        <wa:commitTag
            wa:commitTime={meta[commitTime]}
            wa:domain={meta[domain]}
        />
    </wa:meta>
    <wa:data>

    </wa:data>
</wa:file>

"""


class XmlGenerator(object):
    """
    XmlGenerator, generates xml file from meta nodelist

    """
    SCHEMA = 'file:xml/file.xsd'

    def __init__(self, meta_obj_list=None, schema_path=SCHEMA):
        """
        Generates xml meta files and dumps them to disk

        :meta_obj_list: list with meta objects to dump
        :schema_path: path of schema file
        """
        self.__meta_list = meta_obj_list
        self.__schema_path = schema_path
        self.__xml_list = []
        self.__gen_xml()

    def __gen_xml(self):
        """
        Generates xml list from metadata list
        """
        for item in self.__meta_list:
            metadict = dict()
            for key, value in item.items():
                metadict[key] = quoteattr(value)

            xml_node = (XML_TEMPLATE.format(schema_path=self.__schema_path,
                                            meta=metadict),
                                            item['path'][:-4])

            self.__xml_list.append(xml_node)

    def dump_all(self):
        """
        Dumps all previously generated metadata to disk
        """
        if len(self.__xml_list) > 0 and self.__meta_list is not None:
            for item in self.__xml_list:
                with open(os.path.join(item[1], 'data.xml'), 'w') as f:
                    f.write(str(item[0]))
        else:
            logging.warn("nothing to dump - Go eat something.")

###########################################################################
#                                unittest                                 #
###########################################################################

if __name__ == '__main__':
    import unittest
    from archive.crawler.xmlreader import XmlReader

    class TestXMLGen(unittest.TestCase):
        def setUp(self):
            self.__input_dict = {
                'url': "www.heise.de/index.html",
                'mimeType': "text/html",
                'path': "test",
                'createTime': "2012-05-15T17:28:42",
                'title': "heise \"online\" &&&&! ",
                'commitTime': "2012-05-15T17:30:00",
                'domain': "www.heise.de"
            }

        def test_xmlgen_and_read(self):
            XmlGenerator([self.__input_dict]).dump_all()
            trans_dict = XmlReader('data.xml').parse()
            self.assertEqual(trans_dict, self.__input_dict)

        def tearDown(self):
            os.remove('data.xml')

    unittest.main()
