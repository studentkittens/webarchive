#!/usr/bin/env python
# encoding: utf-8

"""
DB Recover submodule
"""

__author__ = 'Christopher Pahl'

import os
import logging

from archive.dbrecover.pickle_recover import PickleDBRecover
from archive.dbrecover.xml_recover import XMLDBRecover
from archive.crawler.dbgen import DBGenerator
from archive.util.paths import get_dbpath
from archive.dbrecover.repair import repair


def rebuild():
    """
    Rebuilds the db either by using PickleDBRecover or XMLDBRecover
    """
    strategies = [PickleDBRecover(), XMLDBRecover()]
    for strategy in strategies:
        logging.info('(!!) Probing strategy: ' + strategy.description)
        metalist = strategy.load()
        if metalist:
            while True:
                try:
                    logging.info('-> Success! Generating Database.')
                    d = DBGenerator(metalist)
                    d.batch()
                    d.close()
                    logging.info('Making sure everything is clean on master..')
                    repair()
                    break
                except KeyboardInterrupt:
                    print('Got Interrupt. Please wait till DB Query is done.')
            break


def remove():
    """
    Removes db
    """
    dbpath = get_dbpath()
    os.remove(dbpath)
