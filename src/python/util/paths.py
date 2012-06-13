#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christopher Pahl'

import os
import unittest
import config.reader as config


def get_dbpath():
    return os.path.join(config.get('general.root'), config.get('db.path'))


def get_content_root():
    """
    Return {archive_root}/content/
    """
    return os.path.join(config.get('general.root'), 'content')


def get_domain_path(domain):
    """
    Creates domain path from archive path an given domain

    :domain: domain to which you want to get a path
    :returns: domain path as string
    """
    return os.path.join(get_content_root(), domain)


def get_archive_root():
    return config.get('general.root')


def get_log_dir():
    return os.path.join(config.get('general.root'), 'logs')


def get_temp_root():
    return os.path.join(config.get('general.root'), config.get('crawler.tempRoot'))

###########################################################################
#                                unittest                                 #
###########################################################################

if __name__ == '__main__':
    class TestPaths(unittest.TestCase):
        def test_domain_path(self):
            self.assertEqual(get_domain_path('hello'),
                os.path.join(config.get('general.root'), 'content', 'hello'))

        def test_content_root(self):
            self.assertEqual(get_content_root(),
                os.path.join(config.get('general.root'), 'content'))

    unittest.main()
