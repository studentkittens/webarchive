#!/usr/bin/env python
# encoding: utf-8

import sqlite3
import glob
import os 
class DBGenerator(object):

    def __init__(self, meta_list = None):
        self.__connection = sqlite3.connect('metadata.db')
        self.__cursor = self.__connection.cursor()
        self.__statements = self.load_statements()
        self.__cursor.executescript(self.__statements['create'])
        self.__metalist = meta_list

    def load_statements(self):
        statements = dict()
        for source in glob.glob('../sql/*.sql'):
            with open(source,'r') as f:
                source_base = os.path.basename(source)
                statements[source_base[:-4]] = f.read()
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
            mimes.append( (item['mimeType'],) )
            domains.append( (item['domain'],) )

        self.execute_statement('insert_mimetype', mimes)
        self.execute_statement('insert_domain', domains)
        
        self.__mimedict = self.select('mimeType', 'mimeID', 'mimeName')
        self.__domaindict = self.select('domain', 'domainID', 'domainName')

        print(self.__mimedict)
        print(self.__domaindict)

    def insert_mdata_ctag(self):
        mdata = []
        ctags = []

        for item in self.__metalist:
            mdata.append((
                    item['url'],
                    self.__mimedict[item['mimeType']],
                    item['abspath']))

            ctags.append((
                    item['commitTime'],
                    self.__domaindict[item['domain']]
                ))

        print(mdata)
        self.execute_statement('insert_metadata', mdata) 
        self.execute_statement('insert_committag', ctags) 

        self.__mdidlist = self.select('metaData', 'metaId', 'url', 'path')
        self.__ctaglist = self.select('commitTag', 'domainId', 'commitId')

        print(self.__mdidlist)
        print(self.__ctaglist)

    def insert_history(self):
        history = []

        for item in self.__metalist:
            history.append((
                self.__mdidlist[item['url']],
                self.__ctaglist[self.__domaindict[item['domain']]],
                item['createTime'],
                item['title']
                ))

        print('/' * 60)
        print(history)

        self.execute_statement('insert_history', history) 


    def select(self, table, *columns):
        row_dict = dict()
        sql_statement = 'SELECT '
        for col in columns:
            sql_statement += col + ','

        sql_statement = sql_statement[:-1] + ' FROM ' + table + ';'
        self.__cursor.execute(sql_statement)

        rows = self.__cursor.fetchall()
        for row in rows:
            row_dict[row[1]] = row[0]


        print(len(rows), '==', len(row_dict))
        return row_dict


    def close(self):
        self.__connection.commit()
        self.__cursor.close()

    def __exit__(self):
        self.close()
            

if __name__ == '__main__':
    d = DBGenerator([
        {
            'mimeType'   : 'application/png',
            'domain'     : 'www.heise.de',
            'url'        : 'www.heise.de/news',
            'abspath'    : '.',
            'commitTime' : '23324534634634',
            'createTime' : '32535245634634',
            'title'      : 'news for professionals'
            },
        {
            'mimeType'   : 'application/jpeg',
            'domain'     : 'www.golem.de',
            'url'        : 'www.golem.de/news',
            'abspath'    : '..',
            'commitTime' : '23324534634634',
            'createTime' : '32535245634634',
            'title'      : 'news for retards'
            }
        ])
    d.batch()
    d.close()


