#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'

import archive.config.reader as reader
import archive.config.writer as writer
import archive.config.xmlhandler as xmlhandler
import unittest

def _as_int(value):
    '''
    Converts value to int if string is numeric
    '''
    if type(value) is str and value.isnumeric():
        return int(value)
    else:
        return value

def get(url):
    '''
    Returns actual value for url.
    If not found, it returns default value.
    '''
    return _as_int(reader.get(url))

def get_default(url):
    '''
    Returns defaulft value for url
    '''
    return _as_int(reader.get_default(url))

def set(url, str):
    '''
    Set value of url to given string
    '''
    return writer.set_value(url, str)

def set_default(url):
    return writer.set_default(url)

def load(path):
    '''
    Load xml file with given path
    '''
    return xmlhandler.load(path)

class TestHandler(unittest.TestCase):
    def setUp(self):
        self.assertTrue(load('webarchive.conf.xml'))
        
    def test_load(self):
        self.assertTrue(load(xmlhandler.gConfigPath))
        self.assertFalse(load('notfound.xml'))

    def test_get(self):
        self.assertEqual(get('general.root'), '/tmp/archive/')
        self.assertFalse(get('not.found'))
        self.assertEqual(get('test.main'), -1)

    def test_set(self):
        self.assertFalse(set('not.found', 'value'))
        self.assertTrue(set('general.root', '/tmp'))

    def test_getDefault(self):
        self.assertEqual(get_default('general.root'), '/tmp/archive/')
        self.assertFalse(get_default('not.found'))
    

if __name__ == '__main__':
    unittest.main()
   
