#!/usr/bin/env python
# encoding: utf-8

"""
Recover.
"""

__author__ = 'Christopher Pahl'

from pickle_recover import PickleDBRecover
from xml_recover import XMLDBRecover
from crawler.dbgen import DBGenerator


def recover():
    strategies = [PickleDBRecover(), XMLDBRecover()]
    for strategy in strategies:
        metalist = strategy.load()
        if metalist is not None:
            d = DBGenerator(metalist)
            d.batch()
            d.close()


def main():
    recover()

if __name__ == '__main__':
    main()
