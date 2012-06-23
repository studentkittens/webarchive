#!/usr/bin/env python
# encoding: utf-8

"""
Recover DB using pickle

I.e. ,,pickling'' the metalist and ,,unpickling''
it again to run the DBGenerator on it
"""

import os
import pickle
import glob
import uuid

import archive.config.handler as config
import archive.util.times as times

__author__ = 'Christopher Pahl'


class PickleDBRecover(object):
    """
    Recovers database from previously generated pickle files
    """
    def __init__(self):
        self.__pickle_path = os.path.join(config.get('general.root'), 'pickle_cache')

    @property
    def description(self):
        """
        :returns: description
        """
        return 'Unpickling cached metalists in pickle_cache/'

    def load(self):
        """
        Loads pickle files and regenerates metalist

        :returns: metalist object
        """
        metalist = []
        metalist_files = glob.glob(os.path.join(self.__pickle_path, '*.pickle'))
        for metalist_dump in metalist_files:
            with open(metalist_dump, 'rb') as file_desc:
                metalist += pickle.load(file_desc)
        return metalist

    def save(self, metalist):
        """
        Dumps given metalist as pickle file
        """
        uid = str(uuid.uuid4())
        metalist_path = os.path.join(self.__pickle_path, times.get_sys_time() + '_' + uid + '.pickle')
        with open(metalist_path, 'wb') as file_desc:
            pickle.dump(metalist, file_desc)
