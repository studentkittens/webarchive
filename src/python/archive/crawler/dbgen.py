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
import os

import archive.util.filelock as lock
import archive.crawler.statements as sqlsource
from archive.util.paths import get_dbpath
from archive.util.paths import get_archive_root


class DBGenerator(object):
    """
    DBGenerator module
    """
    def __init__(self, meta_list=None, abspath=None):
        """
        Take a metalist and load sql templates from disk.
        Start the actual generating by calling batch()

        :meta_list: A list of MetaData Dictionaries
        """
        if abspath is None:
            self.__connection = sqlite3.connect(get_dbpath())
        else:
            self.__connection = sqlite3.connect(abspath)

        self.__cursor = self.__connection.cursor()
        self.__cursor.executescript(sqlsource.statements['create'])
        self.__metalist = meta_list

        # We have to maintain a seperate lock for the db,
        # the db itself is locked from itself, but many separate
        # statements are not
        self.__db_lock = lock.FileLock(file_name='db.lock',
                folder=get_archive_root(),
                timeout=100)

    def execute_statement(self, source_name, arglist=None):
        """
        Exececute a previously loaded statement by name

        :source_name: Sourcename to execute (e.g. 'create')
        :arglist: You may pass an additional list of variable elements
        """
        source = sqlsource.statements[source_name]
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
    import unittest

    REAL_DB = 'metadata.db'
    SAMPLE_DB = 'dummy.db'
    SAMPLE_DATA = [{
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
            'createTime' : '32535245634637',
            'title'      : 'news for retards'
            }]

    class TestDBGen(unittest.TestCase):
        def create_sample_db(self):
            conn = sqlite3.connect(SAMPLE_DB)
            crsr = conn.cursor()

            with open('../sql/create.sql', 'r') as create_stmt:
                crsr.executescript(create_stmt.read())

            def write_dict(metadict, rowid):
                insert_script = """
                        BEGIN IMMEDIATE;
                        INSERT INTO mimeType VALUES({0}, '{meta[mimeType]}');
                        INSERT INTO domain VALUES({1}, '{meta[domain]}');
                        INSERT INTO commitTag VALUES({2}, '{meta[commitTime]}', {1});
                        INSERT INTO metaData VALUES({3}, '{meta[url]}', {0}, '{meta[path]}');
                        INSERT INTO history VALUES({3}, {2}, '{meta[createTime]}', '{meta[title]}');
                        COMMIT;
                """.format(rowid, rowid + 1, rowid + 2, rowid + 3, meta=metadict)
                crsr.executescript(insert_script)

            rowid = 0
            write_dict(SAMPLE_DATA[0], rowid)
            rowid += 5
            write_dict(SAMPLE_DATA[1], rowid)
            conn.commit()

            return crsr

        def test_equality(self):
            def compare_rows(table, column, dmmy, real):
                sql = 'SELECT ' + column + ' FROM ' + table
                dmmy_rows = set(dmmy.execute(sql))
                real_rows = set(real.execute(sql))
                self.assertEqual(dmmy_rows, real_rows)

            d = DBGenerator(SAMPLE_DATA, abspath=REAL_DB)
            d.batch()
            d.close()
            dmmy_crsr = self.create_sample_db()
            real_crsr = sqlite3.connect(REAL_DB)

            compare_rows('mimeType', 'mimeName', dmmy_crsr, real_crsr)
            compare_rows('domain', 'domainName', dmmy_crsr, real_crsr)
            compare_rows('commitTag', 'commitTime', dmmy_crsr, real_crsr)
            compare_rows('metaData', 'url', dmmy_crsr, real_crsr)
            compare_rows('metaData', 'path', dmmy_crsr, real_crsr)
            compare_rows('history', 'createTime', dmmy_crsr, real_crsr)
            compare_rows('history', 'title', dmmy_crsr, real_crsr)

        def tearDown(self):
            os.remove(SAMPLE_DB)
            os.remove(REAL_DB)

    unittest.main()
