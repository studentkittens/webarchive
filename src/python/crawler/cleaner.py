#!/usr/bin/env python
# encoding: utf-8

"""
Cleaner submodule to clean  and restructure downloaded content by crawljob
"""

__author__ = 'Christoph Piechula'

import subprocess
import os
import shutil
import logging
import unittest
import itertools

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
        except OSError:
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

    def path_helper(folder):
        return os.path.join(TESTDATA_PATH, folder)

    def content_helper(folder):
        content = [item for item in os.walk(folder)]
        return list(itertools.starmap(lambda root, dirs, files: [dirs, files], content))

    class TestCleaner(unittest.TestCase):
        def setUp(self):
            # setting path vars
            self.__raw_data = path_helper('raw_data')
            self.__restruct_test_data = path_helper('restruct')
            self.__clean_test_data = path_helper('clean')
            self.__restruct_should_be = path_helper('what_raw_data_should_look_like_after_restruct')
            self.__clean_should_be = path_helper('what_raw_data_should_look_like_after_clean_empty')
            # copying raw 'downlaoded' data for restruct testing
            shutil.copytree(self.__raw_data, self.__restruct_test_data)
            # copying 'raw restructured' data for clean empty testing
            shutil.copytree(self.__clean_should_be, self.__clean_test_data)
            self.__clean_test_dataer = Cleaner(self.__restruct_test_data)

        def compare_filetree(self, should_be, test_data):
            should_be = content_helper(should_be)
            really_is = content_helper(test_data)
            self.assertEqual(should_be, really_is)

        def test_restructure(self):
            self.__clean_test_dataer.restructure()
            self.compare_filetree(self.__restruct_should_be, self.__restruct_test_data)

        def test_clean_empty(self):
            self.__clean_test_dataer.clean_empty()
            self.compare_filetree(self.__clean_should_be, self.__clean_test_data)

        def tearDown(self):
            shutil.rmtree(self.__restruct_test_data, ignore_errors=True)
            shutil.rmtree(self.__clean_test_data, ignore_errors=True)

    unittest.main()
