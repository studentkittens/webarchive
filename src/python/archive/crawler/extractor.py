#!/usr/bin/env python
# encoding: utf-8

"""
Title extractor module which makes possible to extract title names
from various content files. Currently only text/html files are supported.
"""

__author__ = 'Christoph Piechula'

from bs4 import BeautifulSoup
import logging
import unittest
import os


def extract_html(file_path):
    """
    Title extractor for text/html files.

    :file_path: path to html file
    :returns: extracted title or empty string if no title is found

    """
    doc = None

    with open(file_path, 'r') as html_file:
        doc = html_file.read()
    soup = BeautifulSoup(doc)
    return soup.title.string

# extractor list, please add new extractor 'plugins' to this list
EXTRACTORS = {
        "text/html": extract_html
}


def get_title(file_path, mime):
    """
    Extractor 'interface' to delegate file by
    mime type

    :file_path: path to content file
    :mime: mime type of that file to determinate extractor
    :returns: extracted title as string if extraction is successful
              else an empty string will be returned

    """
    title = ""
    try:
        title = EXTRACTORS[mime](file_path)
    except IOError:
        logging.exception('cannot read file.')
    finally:
        return str(title)

###########################################################################
#                                unittest                                 #
###########################################################################
TESTDATA_PATH = 'testdata/html/'

if __name__ == '__main__':
    class TestTitle(unittest.TestCase):
        def test_get_mime(self):

            expectations = {'a.html': {'title': 'Golem.de: IT-News für Profis', 'mime': 'text/html'},
                            'b.html': {'title': "heise online | IT-News, c't, iX, Technology Review, Telepolis", 'mime': 'text/html'},
                            'c.html': {'title': '', 'mime': 'text/plain'},
                            'd.html': {'title': '', 'mime': 'application/octet-stream'}}

            for key, value in expectations.items():
                path = os.path.join(TESTDATA_PATH, key)
                self.assertEqual(value['title'], get_title(path, value['mime']))

    unittest.main()
