#!/usr/bin/env python
# encoding: utf-8

"""
Recover. Srsly.
"""

__author__ = 'Christopher Pahl'


import os
import logging

from dbrecover.pickle_recover import PickleDBRecover
from dbrecover.xml_recover import XMLDBRecover
from crawler.dbgen import DBGenerator
from util.paths import get_dbpath
from dbrecover.repair import repair


def rebuild():
    strategies = [PickleDBRecover(), XMLDBRecover()]
    for strategy in strategies:
        logging.info('(!!) Probing strategy: ' + strategy.description)
        metalist = strategy.load()
        if metalist:
            while True:
                try:
                    d = DBGenerator(metalist)
                    d.batch()
                    d.close()
                    logging.info('Making sure everything is clean on master..')
                    repair()
                    break
                except KeyboardInterrupt:
                    print('Got Interrupt. Please wait till DB Query is done.')


def remove():
    dbpath = get_dbpath()
    os.remove(dbpath)


def main():
    rebuild()

if __name__ == '__main__':
    main()
