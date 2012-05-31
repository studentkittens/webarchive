#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import os
import traceback
import config.reader as config
import crawler.wget as wget
import util.paths as paths
import util.files as ufile
import crawler.cleaner as cleaner 
import crawler.xmlgen as xmlgen
import crawler.rsync as rsync
import shutil
import util.filelock as lock
import crawler.git as git

class CrawlJob():
    def __init__(self, ident, url):
        self.__path = os.path.join(config.get('crawler.tempRoot'), url)
        print("*******************************************************************", self.__path)
        self.__metalist = None
        self.__url = url
        self.run()

    def run(self):
        """@todo: Docstring for run
        :returns: @todo

        """
        ufile.mkdir_noerror(self.__path)
        try:
            self.start_crawl() 
            self.start_clean()
            self.start_xml_gen()
            self.start_sync()
        except:
            traceback.print_exc()
        finally:
            shutil.rmtree(self.__path, ignore_errors=True)
    
    def start_crawl(self):
        """@todo: Docstring for start_crawl
        :returns: @todo

        """
        wget_proc = wget.Wget(self.__url,os.path.abspath(self.__path))
        wget_proc.start()
        wget_proc.wait()
    
    def start_clean(self):
        """@todo: Docstring for start_clean
        :returns: @todo

        """
        cleaner_proc = cleaner.Cleaner(self.__path)
        cleaner_proc.clean_empty()
        cleaner_proc.restructure()
        cleaner_proc.clean_empty()
        self.__metalist = cleaner_proc.meta_list

    def start_xml_gen(self):
        """@todo: Docstring for start_xml_gen
        :returns: @todo

        """
        xml_proc = xmlgen.XmlGenerator(self.__metalist)
        xml_proc.dump_all()
    
    def start_sync(self):
        """@todo: Docstring for start_sync
        :returns: @todo

        """
        content_path = os.path.join(config.get('general.root'),'content') 
        itemlist = os.listdir(self.__path)

        for domain in itemlist:
            fsmutex = lock.FileLock(domain, folder = content_path)
            fsmutex.acquire()
            git_proc = git.Git(domain)
            git_proc.init()
            git_proc.checkout('empty')
            git_proc.branch(self.__metalist[0]['commitTime'])
            
            rsync.Rsync(os.path.join(self.__path,domain), content_path).start_sync()

            git_proc.commit('Site {domain_name} was crawled.'.format(domain_name = domain))
            git_proc.recreate_master()
            fsmutex.release()

    


if __name__ == '__main__':
    c = CrawlJob(3,'www.nullcat.de')

