#!/usr/bin/env python
# encoding: utf-8

"""
Cleaner submodule to clean  and restructure downloaded content by crawljob
"""

__author__ = 'Christoph Piechula'

import subprocess
import os
import sys
import shutil
import logging

import util.times as times
import crawler.filter as filter
from crawler.metadata import MetaData


class Cleaner:
    """
    Cleaner submodule, walk dir tree, cleans and restructures
    tmp_crawler_folder hierarchy and calls filter subsystem
    """
    def __init__(self, path):
        self.__mdlist = []
        self.__path = path
        self.__commitTime = times.get_sys_time()
        self.__filtersys = filter.FilterSystem()

    def __restructure_file(self, tmp_crawler_folder, file_name):
        """
        Restructures tmp_crawler_folder, collects meta_object list
        for interesting files

        :tmp_crawler_folder: tmp_crawler_folder path of content file
        :file_name: name to content file itself
        """
        src_file = os.path.join(tmp_crawler_folder, file_name)
        dest_file = os.path.join(tmp_crawler_folder, '____data')

        try:
            os.rename(src_file, dest_file)
            os.mkdir(src_file)
            # moving an renaming to 'data'
            final_data_path = os.path.join(src_file, 'data')
            shutil.move(dest_file, final_data_path)
        except OSError as err:
            logging.exception('error during cleaning procedure')
        else:
            meta_obj = MetaData.build_metadata_from_file(self.__path,
                                                         final_data_path,
                                                         self.__commitTime)
            keep = self.__filtersys.check(meta_obj)
            if keep:
                self.__mdlist.append(meta_obj)
            else:
                os.remove(os.path.join(src_file, 'data'))
                os.removedirs(src_file)
        finally:
            # if something goes wrong
            if os.path.exists(dest_file):
                os.remove(dest_file)

    @property
    def meta_list(self):
        """
        :returns: metalist
        """
        return self.__mdlist

    def restructure(self):
        """
        Walks through crawler temp folder hierarchy calling internal
        restructure method to 'normalize' file hierarchy
        """
        logging.debug("starting restructure.")
        try:
            tree = os.walk(os.path.abspath(self.__path))
            for leaf in tree:
                for data_file in leaf[2]:
                    self.__restructure_file(leaf[0], data_file)
        except OSError:
            logging.exception("cannot walk through dir structure.")

    def clean_empty(self):
        """
        Removes empty files and folders
        """
        logging.debug("removing empty files and folders.")
        subprocess.call(['find', self.__path, '-empty', '-delete'])

###########################################################################
#                                 content                                 #
###########################################################################
if __name__ == '__main__':
    TESTDATA_PATH = 'testdata/cleaner/'

    class TestMime(unittest.TestCase):
        def setUp(self):
            shutil.copytree(os.path.join(TESTDATA, 'before'),
                            os.path.join(TESTDATA_PATH, 'after'))

    unittest.main()

# if __name__ == '__main__':
#     c = Cleaner(sys.argv[1])
#     c.restructure()
#     c.clean_empty()
#     print(c.print_list())
