#!/usr/bin/env python
# encoding: utf-8

"""
Path helper module
"""

__author__ = 'Christopher Pahl'

import os
import unittest
import archive.config.reader as config


def get_dbpath():
    """
    :returns: db path
    """
    return os.path.join(config.get('general.root'), config.get('db.path'))


def get_sqlpath():
    """
    :returns: sql statements path
    """
    return os.path.join(config.get('general.root'), config.get('db.sqlSource'))


def get_content_root():
    """
    :return: '{archive_root}/content/' path
    """
    return os.path.join(config.get('general.root'), 'content/')


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
    return os.path.join(config.get('general.root'), 'logs/')


def get_temp_root():
    """
    :returns: temp dir path
    """
    return os.path.join(config.get('general.root'), config.get('crawler.tempRoot'))


def get_urllist_path():
    """
    :returns: return path to url.txt
    """
    return os.path.join(config.get('general.root'), config.get('crawler.urllistpath'))

###########################################################################
#                                unittest                                 #
###########################################################################

# some predefined values to test with
ROOT = config.get('general.root')
SQL_PATH = 'sql/'
DB_PATH = 'metadata.db'
CRAWLER_TMP_ROOT = 'tmp/'

CONTENT_DIR = 'content/'
LOG_DIR = 'logs/'
TEMP_DIR = 'tmp/'
DOMAIN = 'www.domain_name.org'
URL = 'url.txt'

if __name__ == '__main__':
    class TestPaths(unittest.TestCase):

        def test_get_db_path(self):
            self.assertEqual(get_dbpath(), os.path.join(ROOT, DB_PATH))

        def test_get_sql_path(self):
            self.assertEqual(get_sqlpath(), os.path.join(ROOT, SQL_PATH))

        def test_get_content_root(self):
            self.assertEqual(get_content_root(), os.path.join(ROOT, CONTENT_DIR))

        def test_get_domain_path(self):
            self.assertEqual(get_domain_path(DOMAIN),
                os.path.join(ROOT, CONTENT_DIR, DOMAIN))

        def test_get_archive_root(self):
            self.assertEqual(get_archive_root(), os.path.join(ROOT))

        def test_get_log_dir(self):
            self.assertEqual(get_log_dir(),
                os.path.join(ROOT, LOG_DIR))

        def test_get_temp_dir(self):
            self.assertEqual(get_temp_root(),
                os.path.join(ROOT, TEMP_DIR))

        def test_get_urllistpath(self):
            self.assertEqual(get_urllist_path(),
                os.path.join(ROOT, URL))

    unittest.main()
