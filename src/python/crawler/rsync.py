#!/usr/bin/env python
# encoding: utf-8

"""
Rsync submodule
"""

__author__ = 'Christoph Piechula'

import subprocess
import logging


def rsync(src_path, dest_path):
    """
    Starts the synchronization process,
    this function will block until finished

    :src_path: the directory/file to sync
    :dest_path: the directory to sync files to
    """
    cmd = ['rsync', '-acP', src_path, dest_path]
    process = subprocess.Popen(cmd, stdout=subprocess.PIPE,
                                    stderr=subprocess.PIPE)

    logging.info('Rsyncing: ' + '  '.join(cmd))
    out, err = process.communicate()
    if process.poll() is not 0:
        logging.warning('rsync failed with exit-code: {1}'.format(err))


###########################################################################
#                                Unittests                                #
###########################################################################


if __name__ == '__main__':
    import unittest
    import os
    import shutil

    # Awaited:
    # src/
    #   test_file
    #
    # dest/
    #   src/
    #     test_file

    RSYNC_DST = 'rsync_test_dst'
    RSYNC_SRC = 'rsync_test_src'
    TEST_FILE = 'test_file'

    # This test doesn't do much, except testing for the very basic
    # functionality - what else should one do here?
    class TestRsync(unittest.TestCase):
        def setUp(self):
            os.mkdir(RSYNC_SRC)
            self.__test_file = os.path.join(RSYNC_SRC, TEST_FILE)
            with open(self.__test_file, 'w') as test_file:
                test_file.write('To be or not to be - Do bee doo be doo')

        def test_sync(self):
            self.assertTrue(os.path.exists(self.__test_file))
            print('Syncing', RSYNC_SRC, '=>', RSYNC_DST)
            rsync(RSYNC_SRC, RSYNC_DST)
            self.assertTrue(os.path.exists(self.__test_file))
            self.assertTrue(os.path.exists(os.path.join(RSYNC_DST, RSYNC_SRC, TEST_FILE)))

        def tearDown(self):
            shutil.rmtree(RSYNC_SRC, ignore_errors=True)
            shutil.rmtree(RSYNC_DST, ignore_errors=True)

    unittest.main()
