#!/usr/bin/env python
# encoding: utf-8

import sqlite3
import glob
import os 
class DBGenerator(object):
    """Docstring for DBGenerator """

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

    def insert_mime_domain(self):
        mimes = set()
        domains = set()

        for item in self.__metalist:
            mimes.add(item['mimeType'])
            domains.add(item['domain'])

        for mime in mimes:
            self.__cursor.execute(self.__statements['insert_mimetype'],(mime,))

        for domain in domains:
            self.__cursor.execute(self.__statements['insert_domain'],(domain,))
        
        self.__connection.commit()

        print(self.select('domain', '*'))

    def select(self, table, column):
        self.__cursor.execute('SELECT {col} FROM {tab};'.format(col = column, tab = table))
        return self.__cursor.fetchall()

    def close(self):
        self.__connection.close()


if __name__ == '__main__':
    d = DBGenerator([
                {'mimeType': 'png', 'domain': 'www.heise.de'},
                {'mimeType': 'jpeg','domain': 'www.golem.de'}
            ])
    d.insert_mime_domain()
    d.close()


