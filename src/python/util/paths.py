#!/usr/bin/env python
# encoding: utf-8

"""
Path helper module
"""

__author__ = 'Christopher Pahl'

import os
import unittest
import config.reader as config


def get_dbpath():
    """
    :returns: db path
    """
    return os.path.join(config.get('general.root'), config.get('db.path'))


def get_content_root():
    """
    :return: '{archive_root}/content/' path
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
    """
    :returns: archive root path
    """
    return config.get('general.root')


def get_log_dir():
    """
    :returns: log dir path
    """
    return os.path.join(config.get('general.root'), 'logs')


def get_temp_root():
    """
    :returns: temp dir path
    """
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
