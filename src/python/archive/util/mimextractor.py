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
    try:
        with magic.Magic(flags=magic.MAGIC_MIME_TYPE) as m:
            result = m.id_filename(filepath)
    except:
        # something may go wrong
        pass
    return result

###########################################################################
#                                unittest                                 #
###########################################################################

if __name__ == '__main__':
    TESTDATA_PATH = 'testdata/html/'

    def file_helper(path_to_file):
        mime = subprocess.check_output(["file", "-b", "--mime-type", path_to_file])
        mime = str(mime, "UTF-8").strip()
        return mime

    # check if `file` returns the same MIME as we do
    class TestMime(unittest.TestCase):
        def test_get_mime(self):
            expectations = {
                    'a.html': 'text/html',
                    'b.html': 'text/html',
                    'c.html': 'text/plain',
                    'd.html': 'application/octet-stream'
                    }

            for key, value in expectations.items():
                path = os.path.join(TESTDATA_PATH, key)
                self.assertEqual(file_helper(path), get_mime(path))
                self.assertEqual(value, get_mime(path))

    unittest.main()
