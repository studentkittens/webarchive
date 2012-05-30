#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import os
import traceback
import config.reader as config
import crawler.wget as wget
import util.files as ufile
import crawler.cleaner as cleaner 
import crawler.xmlgen as xmlgen
import crawler.rsync as rsync
class CrawlJob():
    def __init__(self, ident, url):
        self.__path = os.path.join(config.get('crawler.tempRoot'), url)
        
        print(self.__path, url, ident)
        try:
            ufile.mkdir_noerror(self.__path)
            wget_proc = wget.Wget(url,os.path.abspath(self.__path))
            wget_proc.start()
            wget_proc.wait()
            cleaner_proc = cleaner.Cleaner(self.__path)
            cleaner_proc.clean_empty()
            cleaner_proc.restructure()
            cleaner_proc.clean_empty()
        
            metalist = cleaner_proc.meta_list
            print(metalist)
            xml_proc = xmlgen.XmlGenerator(metalist)
            xml_proc.dump_all()
            itemlist = os.listdir(self.__path)
            for item in itemlist:
                print("wwwwwwwooooooooooooooooooooo " +os.path.join(self.__path,item))
                rsync.Rsync(os.path.join(self.__path,item), '/tmp/archive/content/').start_sync()
        except:
            traceback.print_exc()
if __name__ == '__main__':
    c = CrawlJob(3,'www.nullcat.de')

