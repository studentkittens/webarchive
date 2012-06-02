#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import os
import logging
import traceback
import time
import shutil

import config.reader as config
import crawler.wget as wget
import util.paths as paths
import util.files as ufile
import crawler.cleaner as cleaner 
import crawler.xmlgen as xmlgen
import crawler.rsync as rsync
import util.filelock as lock
import crawler.git as git
import crawler.exceptions

class CrawlJob(object):
    def __init__(self, ident, url):
        self.__path = os.path.join(config.get('crawler.tempRoot'), url)
        self.__metalist = None
        self.__url = url
        self.__ident = ident
        self.__shutdown = False

    def shutdown(self):
        self.__shutdown = True

    def run(self):
        ufile.mkdir_noerror(self.__path)
        try:
            print('--> Crawling')
            self.start_crawl() 
            print('--> Cleaning')
            self.start_clean()
            print('--> Gen XML')
            self.start_xml_gen()
            print('--> Rsyncing')
            self.start_sync()
            print('--> Done')
        except crawler.exceptions.ShutdownException:
            print('Job #{cid} ({curl}) stopped.'.format(cid = self.__ident, curl = self.__url))
        except:
            traceback.print_exc()
        finally:
            shutil.rmtree(self.__path, ignore_errors=True)
    
    def start_crawl(self):
        wget_proc = wget.Wget(self.__url,os.path.abspath(self.__path))
        wget_proc.start()

        # Check in a polling loop for the termination
        # of wget - Or shutdown ealry if desired
        while wget_proc.poll() is None:
            time.sleep(0.1)

            # shutdown() was called
            if self.__shutdown is True:
                logging.info('Stopping wget')
                wget_proc.stop()
                raise crawler.exceptions.ShutdownException() 
    
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
            domain_path  = paths.get_domain_path(domain)
            fsmutex = lock.FileLock(domain, folder = content_path)
            fsmutex.acquire()
            
            try:
                logging.debug('Creating directory:', domain_path)
                os.mkdir(domain_path)
            except OSError:
                # This is expected
                pass

            git_proc = git.Git(domain)
            git_proc.init()
            git_proc.checkout('empty')
            git_proc.branch(self.__metalist[0]['commitTime'])
            
            rsync.Rsync(os.path.join(self.__path, domain), content_path).start_sync()

            git_proc.commit('Site {domain_name} was crawled.'.format(domain_name = domain))
            git_proc.recreate_master()
            fsmutex.release()

if __name__ == '__main__':
    c = CrawlJob(3,'www.nullcat.de')

