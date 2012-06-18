#!/usr/bin/env python
# encoding: utf-8

"""
Mime extractor helper module
"""

__author__ = 'Christoph Piechula'

import magic
import unittest
import subprocess
import os


def get_mime(filepath):
    """
    Determinates the mime type of a given file

    :filepath: path to file
    :returns: mime type as string

    """
    result = "application/octet-stream"
    with magic.Magic(flags=magic.MAGIC_MIME_TYPE) as m:
        result = m.id_filename(filepath)
    return result

###########################################################################
#                                unittest                                 #
###########################################################################

TESTDATA_PATH = '/home/christoph/devf/webarchive/src/python/testdata/html/'

def file_helper(path_to_file):
    mime = subprocess.check_output(["file", "-b", "--mime-type", path_to_file])
    mime = str(mime, "UTF-8").strip()
    return mime

if __name__ == '__main__':
    class TestMime(unittest.TestCase):
        def test_get_mime(self):
            file_ = os.path.join(TESTDATA_PATH, 'a.html')
            self.assertEqual(file_helper(file_), get_mime(file_))
            self.assertEqual('text/html', get_mime(file_))

            file_ = os.path.join(TESTDATA_PATH, 'b.html')
            self.assertEqual(file_helper(file_), get_mime(file_))
            self.assertEqual('text/html', get_mime(file_))

            file_ = os.path.join(TESTDATA_PATH, 'c.html')
            self.assertEqual(file_helper(file_), get_mime(file_))
            self.assertEqual('text/plain', get_mime(file_))

            file_ = os.path.join(TESTDATA_PATH, 'd.html')
            self.assertEqual(file_helper(file_), get_mime(file_))
            self.assertEqual('application/octet-stream', get_mime(file_))

    unittest.main()
