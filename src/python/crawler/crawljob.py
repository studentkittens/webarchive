#!/usr/bin/env python
# encoding: utf-8
# crawljob  module
#
import os
import config.reader as kampfer
import cjob.wget as wget
import util.files as ufile

class CrawlJob():
    def __init__(self,ident,url):
        self.__path = kampfer.get('crawl.tmplocation') + str(ident) + '_' + url
        print(self.__path)
        ufile.mkdir_noerror(self.__path)
        os.chdir(self.__path)
        wget.Wget(url,self.__path)

if __name__ == '__main__':
    c = CrawlJob(3,'www.nullcat.de')

