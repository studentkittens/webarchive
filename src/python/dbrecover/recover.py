#!/usr/bin/env python
# encoding: utf-8

"""
Recover.
"""

__author__ = 'Christopher Pahl'


import os

from dbrecover.pickle_recover import PickleDBRecover
from dbrecover.xml_recover import XMLDBRecover
from crawler.dbgen import DBGenerator
from util.paths import get_dbpath


def rebuild():
    strategies = [PickleDBRecover(), XMLDBRecover()]
    for strategy in strategies:
        metalist = strategy.load()
        if metalist is not None:
            d = DBGenerator(metalist)
            d.batch()
            d.close()
            break


def remove():
    dbpath = get_dbpath()
    os.remove(dbpath)


def main():
    rebuild()

if __name__ == '__main__':
    main()
