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
                line = line.strip()
                if len(line) > 0 and line[0] != '#':
                    urls.add(line)
    except IOError:
        logging.exception("cannot read url list from disk.")
    return urls


###########################################################################
#                                unittest                                 #
###########################################################################
if __name__ == '__main__':
    class TestUtilFiles(unittest.TestCase):
        def setUp(self):
            # generate testfile with 7 urls of which 3 are equal
            testinput = "www.heise.de\n \
                         www.heise.de\n \
                         www.golem.de   \n \
                         # a comment \n \
                         www.linux.org\n \
                         \n\
                         www.heise.de\n \
                         www.phoronix.com\n \
                         # ignore this\n \
                         www.golem.de\n"

            # feed test file with input
            with open('testfile_files.txt', 'w') as test_file:
                test_file.write(testinput)

        def test_num_of_elements(self):
            # get url list from file
            urls = unique_items_from_file('testfile_files.txt')
            # only 4 urls should be unique
            self.assertTrue(len(urls) == 4)
            self.assertEqual({
                'www.linux.org',
                'www.heise.de',
                'www.golem.de',
                'www.phoronix.com'}, urls)

        def tearDown(self):
            # clean test data
            os.remove('testfile_files.txt')

    # starts unittest
    unittest.main()
