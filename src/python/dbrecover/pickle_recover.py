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

import config.reader as config
import util.times as times

__author__ = 'Christopher Pahl'


class PickleDBRecover(object):
    """Docstring for DBPickleRecover """
    def __init__(self):
        self.__pickle_path = os.path.join(config.get('general.root'), 'pickle_cache')

    @property
    def description(self):
        return 'Unpickling cached metalists in pickle_cache/'

    def load(self):
        metalist = []
        metalist_files = glob.glob(os.path.join(self.__pickle_path, '*.pickle'))
        for metalist_dump in metalist_files:
            with open(metalist_dump, 'rb') as fd:
                metalist += pickle.load(fd)

        return metalist

    def save(self, metalist):
        uid = str(uuid.uuid4())
        metalist_path = os.path.join(self.__pickle_path, times.get_sys_time() + '_' + uid + '.pickle')
        with open(metalist_path, 'wb') as fd:
            pickle.dump(metalist, fd)
