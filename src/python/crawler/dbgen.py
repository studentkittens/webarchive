#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christopher Pahl'

import sqlite3
import glob
import os
import config.reader as config
from dbrecover.pickle_recover import PickleDBRecover


class DBGenerator(object):

    def __init__(self, meta_list=None):
        self.__connection = sqlite3.connect('metadata.db')
        self.__cursor = self.__connection.cursor()
        self.__statements = self.load_statements()
        self.__cursor.executescript(self.__statements['create'])
        self.__metalist = meta_list

    def load_statements(self):
        """
        (Re-)Load Sql Files from Disk
        """
        self.__statements = []
        statements = dict()
        for source in glob.glob(os.path.join(config.get('db.sqlSource'), '*.sql')):
            with open(source, 'r') as fd:
                source_base = os.path.basename(source)
                if source_base.endswith('.sql'):
                    source_base = source_base[:-4]

                statements[source_base] = fd.read()
        return statements

    def execute_statement(self, source_name, arglist=None):
        source = self.__statements[source_name]
        if arglist is not None:
            self.__cursor.executemany(source, arglist)
        else:
            self.__cursor.executemany(source)

    def batch(self):
        self.insert_mime_domain()
        self.insert_mdata_ctag()
        self.insert_history()

    def insert_mime_domain(self):
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
        mdata = []
        ctags = []

        for item in self.__metalist:
            mdata.append((
                    item['url'],
                    self.__mimedict[item['mimeType']],
                    item['path']))

            ctags.append((
                    item['commitTime'],
                    self.__domaindict[item['domain']]
                ))

        self.execute_statement('insert_metadata', mdata)
        self.execute_statement('insert_committag', ctags)

        self.__mdidlist = self.select('metaData', 'metaId', 'url')
        self.__ctaglist = self.select('commitTag', 'commitId', 'commitTime', 'domainId')

    def insert_history(self):
        history = []

        for item in self.__metalist:
            ctag_tuple_key = (item['commitTime'], self.__domaindict[item['domain']])
            history.append((
                self.__mdidlist[item['url']],
                self.__ctaglist[ctag_tuple_key],
                item['createTime'],
                item['title']
                ))

        self.execute_statement('insert_history', history)

    def select(self, table, *columns):
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
        rec = PickleDBRecover()
        rec.save(self.__metalist)
        self.__connection.commit()
        self.__cursor.close()

if __name__ == '__main__':
    d = DBGenerator([
        {
            'mimeType'   : 'application/png',
            'domain'     : 'www.heise.de',
            'url'        : 'www.heise.de/news',
            'path'    : '.',
            'commitTime' : '23324534634634',
            'createTime' : '32535245634634',
            'title'      : 'news for professionals'
            },
        {
            'mimeType'   : 'application/jpeg',
            'domain'     : 'www.golem.de',
            'url'        : 'www.golem.de/news',
            'path'    : '..',
            'commitTime' : '23324534634634',
            'createTime' : '32535245634634',
            'title'      : 'news for retards'
            }
        ])
    d.batch()
    d.close()
