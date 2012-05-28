#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christopher Pahl'

import glob
import copy
import os.path
import traceback

class FilterSystem(object):
    def __init__(self, plugin_path = None):
        self.__source_list = []
        self.load(plugin_path)

    def load(self, plugin_path = None):
        """
        Load a list of *.py files from a directory

        The .py files are read in and stored in-memory
        
        :param plugin_path: A path to a directory with .py files 
                            or None to read the value from the cfg
        :returns: None
        """
        # If not path is given,
        # we'll try to read it from the cfg
        if plugin_path is None:
            # TODO: load from cfg
            actual_path = '/tmp/filter'
        else:
            actual_path = plugin_path
        
        # Built a list of (path_to_filter, filter_source)
        for source in sorted(glob.glob(os.path.join(actual_path,'*.py'))):
            with open(source, 'r') as handle:
                self.__source_list.append(
                    (source, handle.read() + '\n'))

    def clear(self):
        """
        Clear loaded filters
        
        :returns: None
        """
        # Clear list by unreferencing old items
        self.__source_list = []

    def check(self, meta_dict):
        """
        Perform the actual filtering

        Iterate over all loaded filters, and exec() them

        :param meta_dict: the dictionary that will be passed to the filter
                          as a 'filter_input' global var
        :returns: True if file should be kept, or False if it should be deleted
        """
        decision = True

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
                exec(source[1], input_dict)
            except: 
                # an error inside the filter happened,
                # this is probably one of the few reasons where
                # a catchall is fine. :)
                print('Got Exception while executing the filter from:')
                print('>>>', source[0], '<<<')
                traceback.print_exc()

            try: 
                if input_dict['filter_result'] == False:
                    decision = False
                    break
            except KeyError:
                print('You deleted filter_result, filthy Bastard!')

        return decision


def main():
    """
    Tests
    """
    import os
    os.system("""
        mkdir -p /tmp/py_filter
        echo 'print("a.py:", filter_input)\nfilter_result = True\ndel filter_result'  > /tmp/py_filter/a.py
        echo 'print("b.py:", filter_input)\nfilter_result = False' > /tmp/py_filter/b.py
        """)

    f = FilterSystem('/tmp/py_filter')
    print('=> ', f.check({'input': 42}))
    f.clear()
    print('=> ', f.check({'input': 21}))

if __name__ == '__main__':
    main()
