#!/usr/bin/env python
# encoding: utf-8

"""
DBGenerator is capable of generating an sqlite database
from a list of metadictionaries.
"""

__author__ = 'Christopher Pahl'

import logging
import traceback
import sqlite3
import glob
import os

import util.filelock as lock
from util.paths import get_dbpath
from util.paths import get_archive_root
from util.paths import get_sqlpath


class DBGenerator(object):
    """
    DBGenerator module
    """
    def __init__(self, meta_list=None):
        """
        Take a metalist and load sql templates from disk.
        Start the actual generating by calling batch()

        :meta_list: A list of MetaData Dictionaries
        """
        self.__connection = sqlite3.connect(get_dbpath())
        self.__cursor = self.__connection.cursor()
        self.__statements = self.load_statements()
        self.__cursor.executescript(self.__statements['create'])
        self.__metalist = meta_list

        # We have to maintain a seperate lock for the db,
        # the db itself is locked from itself, but many separate
        # statements are not
        self.__db_lock = lock.FileLock(file_name='db.lock',
                folder=get_archive_root(),
                timeout=100)

    def load_statements(self):
        """
        (Re-)Load Sql Files from Disk

        This is already called in init

        :returns: a dictionary with statements, indexed by name (e.g. 'create')
        """
        self.__statements = []
        statements = dict()
        for source in glob.glob(os.path.join(get_sqlpath(), '*.sql')):
            with open(source, 'r') as fd:
                source_base = os.path.basename(source)
                if source_base.endswith('.sql'):
                    source_base = source_base[:-4]

                statements[source_base] = fd.read()
        return statements

    def execute_statement(self, source_name, arglist=None):
        """
        Exececute a previously loaded statement by name

        :source_name: Sourcename to execute (e.g. 'create')
        :arglist: You may pass an additional list of variable elements
        """
        source = self.__statements[source_name]
        if arglist is not None:
            self.__cursor.executemany(source, arglist)
        else:
            self.__cursor.executemany(source)

    def batch(self):
        """
        Start db creating procedure

        :returns: a truthy value on success
        """
        try:
            self.__db_lock.acquire()
            self.insert_mime_domain()
            self.insert_mdata_ctag()
            self.insert_history()
            self.__db_lock.release()
        except lock.FileLockException:
            logging.critical('File-lock timed out; no db update was done')
        except:
            logging.critical('Unexcpected error while generating DB')
            logging.critical(traceback.format_exc())
            return False
        else:
            return True

    def insert_mime_domain(self):
        """
        Fill mimeType and domain table
        """
        mimes = []
        domains = []

        for item in self.__metalist:
            mimes.append((item['mimeType'],))
            domains.append((item['domain'],))

        self.execute_statement('insert_mimetype', mimes)
        self.execute_statement('insert_domain', domains)

        self.__mimedict = self.select('mimeType', 'mimeID', 'mimeName')
        self.__domaindict = self.select('domain', 'domainID', 'domainName')

    def insert_mdata_ctag(self):
        """
        Fill metadata and committag table
        """
        mdata = []
        ctags = set()

        for item in self.__metalist:
            mdata.append((
                    item['url'],
                    self.__mimedict[item['mimeType']],
                    item['path']))

            ctags.add((
                    item['commitTime'],
                    self.__domaindict[item['domain']]
                ))

        self.execute_statement('insert_metadata', mdata)
        self.execute_statement('insert_committag', list(ctags))

        self.__mdidlist = self.select('metaData', 'metaId', 'url')
        self.__ctaglist = self.select('commitTag', 'commitId', 'commitTime', 'domainId')

    def insert_history(self):
        """
        Fill history table
        """
        history = []

        for item in self.__metalist:
            ctag_tuple_key = (item['commitTime'], self.__domaindict[item['domain']])

            try:
                metadata_id = self.__mdidlist[item['url']]
                commitag_id = self.__ctaglist[ctag_tuple_key]

                history.append((
                    metadata_id,
                    commitag_id,
                    item['createTime'],
                    item['title']
                    ))
            except KeyError:
                logging.warning('Cannot find URL ' + item['url'] + ' in DB.')

        self.execute_statement('insert_history', history)

    def select(self, table, *columns):
        """
        Internal helper for collecting data

        :table: Table on which a SELECT shall be performed
        :columns: a list of columns to select
        :returns: A dictionary of column[0]: column[1:]
        """
        row_dict = dict()

        sql_statement = ''.join(['SELECT ', ', '.join(columns), ' FROM ', table, ';'])
        self.__cursor.execute(sql_statement)

        rows = self.__cursor.fetchall()
        for row in rows:
            if len(row) == 2:
                row_dict[row[1]] = row[0]
            else:
                row_dict[row[1:]] = row[0]

        return row_dict

    def close(self):
        """
        Close connection and commit.
        """
        self.__connection.commit()
        self.__cursor.close()

if __name__ == '__main__':
    d = DBGenerator([
        {
            'mimeType'   : 'application/png',
            'domain'     : 'www.heise.de',
            'url'        : 'www.heise.de/news',
            'path'       : '.',
            'commitTime' : '23324534634634',
            'createTime' : '32535245634634',
            'title'      : 'news for professionals'
            },
        {
            'mimeType'   : 'application/jpeg',
            'domain'     : 'www.golem.de',
            'url'        : 'www.golem.de/news',
            'path'       : '..',
            'commitTime' : '23324534634634',
            'createTime' : '32535245634634',
            'title'      : 'news for retards'
            }
        ])
    d.batch()
    d.close()
