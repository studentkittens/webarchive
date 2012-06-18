#!/usr/bin/env python
# encoding: utf-8

"""
Helper module for url.txt
"""
__author__ = 'Christopher Pahl'

import unittest
import logging
import os


def unique_items_from_file(path):
    """
    Creates a unique set with urls from a given file
    commented urls '#' are ignored

    :path: path to url list file
    """
    urls = set()
    try:
        with open(path, 'r') as urlfile:
            for line in urlfile:
                if line[0] != '#':
                    urls.add(line.strip())
    except IOError:
        logging.exception("cannot read url list from disk.")
    return urls


###########################################################################
#                                unittest                                 #
###########################################################################

class TestUtilFiles(unittest.TestCase):
    def setUp(self):
        # generate testfile with 12 urls of which 8 are equal
        with open('testfile_files.txt','w') as test_file:
            for x in range(1, 10):
                test_file.write('www.heise.de\n')
                test_file.write('www.golem.de\n')
            test_file.write('www.phoronix.de\n')
            test_file.write('www.linux.org\n')

    def test_num_of_elements(self):
        # get url list from file
        urls = unique_items_from_file('testfile_files.txt')
        # only 4 urls should be unique
        self.assertTrue(len(urls) == 4)

    def tearDown(self):
        # clean test data
        os.remove('testfile_files.txt')


if __name__ == '__main__':
    unittest.main()
