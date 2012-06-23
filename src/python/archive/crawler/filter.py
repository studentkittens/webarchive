#!/usr/bin/env python
# encoding: utf-8

"""
Filter plugin system submodule
"""

__author__ = 'Christopher Pahl'

import glob
import copy
import os.path
import traceback
import logging

import archive.config.handler as config


class FilterSystem(object):
    """
    Filtersystem submodule invoked by a crawljob
    """
    def __init__(self, plugin_path=None):
        self.__source_list = []
        self.load(plugin_path)

    def load(self, plugin_path=None):
        """
        Load a list of \*.py files from a directory

        The .py files are read in and stored in-memory

        :plugin_path: A path to a directory with .py files
                      or None to read the value from the cfg
        """
        # If no path is given,
        # we'll try to read it from the cfg
        if plugin_path is None:
            actual_path = os.path.join(config.get('general.root'), config.get('general.filterpath'))
        else:
            actual_path = plugin_path

        # Built a list of (path_to_filter, filter_source)
        for source in sorted(glob.glob(os.path.join(actual_path, '*.py'))):
            with open(source, 'r') as handle:
                self.__source_list.append(
                    (source, handle.read() + '\n'))

    def clear(self):
        """
        Clears loaded filters

        """
        # Clear list by unreferencing old items
        self.__source_list = []

    def check(self, meta_dict):
        """
        Perform the actual filtering

        Iterate over all loaded filters, and exec() them

        :meta_dict: the dictionary that will be passed to the filter
                    as a 'filter_input' global var
        :returns: True if file should be kept, or False if it should be deleted
        """
        decision = True

        if len(self.__source_list) > 0:
            # All global variables seen inside the filter
            # are defined as key: value pairs,
            # for now we only have two of them
            input_dict = {
                    'filter_input':  copy.deepcopy(meta_dict),
                    'filter_result': True
            }

            # Try to execute the source
            # and stop if any filter says no
            for source in self.__source_list:
                try:
                    print('Exec:', source)
                    exec(source[1], input_dict)
                except:
                    # an error inside the filter happened,
                    # this is probably one of the few reasons where
                    # a catchall is fine. :)
                    logging.warn('Got Exception while executing the filter from:')
                    logging.warn(source[0])
                    traceback.print_exc()

                try:
                    if input_dict['filter_result'] == False:
                        decision = False
                        break
                except KeyError:
                    logging.warn('You deleted filter_result, filthy Bastard!')

        return decision

###########################################################################
#                                unittest                                 #
###########################################################################


if __name__ == '__main__':
    import os
    import shutil
    import unittest

    def sanitize_source(source):
        return '\n'.join([line.strip() for line in source.splitlines()])

    FILTER_TESTDIR = 'pyfilter_tests'
    FILTER_SOURCES = {
            'a.py': """
                    filter_result = True
                    del filter_result
                    """,
            'b.py': """
                    if filter_input['test_input'] == 42: filter_result = False
                    """
    }

    class TestFilter(unittest.TestCase):
        def setUp(self):
            # Write sources to disk
            os.mkdir(FILTER_TESTDIR)
            for name, source in FILTER_SOURCES.items():
                with open(os.path.join(FILTER_TESTDIR, name), 'w') as test_source:
                    test_source.write(sanitize_source(source))

        def do_filtering_tests(self, fisys):
            self.assertTrue(fisys.check({'test_input': 21}))
            self.assertFalse(fisys.check({'test_input': 42}))

        def test_filter(self):
            fisys = FilterSystem(FILTER_TESTDIR)
            self.do_filtering_tests(fisys)
            fisys.clear()
            fisys.load(plugin_path=FILTER_TESTDIR)
            self.do_filtering_tests(fisys)
            fisys.clear()
            self.assertTrue(fisys.check({'test_input': 42}))

        def tearDown(self):
            shutil.rmtree(FILTER_TESTDIR)

    unittest.main()
