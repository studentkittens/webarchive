#!/usr/bin/env python
# encoding: utf-8

"""
Recover DB using pickle

I.e. ,,pickling'' the metalist and ,,unpickling''
it again to run the DBGenerator on it
"""

import os
import pickle
import config.reader as config

__author__ = 'Christopher Pahl'


class PickleDBRecover(object):
    """Docstring for DBPickleRecover """
    def __init__(self):
        self.__pickle_path = os.path.join(config.get('general.root'), 'db.pickle')

    def load(self):
        with open(self.__pickle_path, 'rb') as fd:
            return pickle.load(fd)

    def save(self, metalist):
        with open(self.__pickle_path, 'wb') as fd:
            pickle.dump(metalist, fd)
