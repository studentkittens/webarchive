#!/usr/bin/env python
# encoding: utf-8
# crawlmanager module
#
import time
import multiprocessing.pool as mpool
import util.files as utl
import config.reader as config
import crawler.crawljob as job
import threading

running = []
running_mtx = threading.Lock()

def crawljob(ident,url):
    try:
        print("ich bin crawler " + str(ident) + " und crawle "+ url)
        j = job.CrawlJob(ident, url)
        running_mtx.acquire()
        running.append(j)
        running_mtx.release()
    except Exception as e:
        print(e)
    return ident


class CrawlerManager(object):
    """
    Simple crawljob manager
    """
    #TODO interval manager
    def __init__(self,urls):
        if len(urls) > 0:
            self.__done = False
            self.__urls = list(urls)
            print(config.get('crawler.maxInst'))
            self.__pool = mpool.ThreadPool(config.get('crawler.maxInst'))


    def start(self):
        """
        start threadpool with max number of
        instances
        :returns: @todo

        """
#        for item in enumerate(self.__urls):
 #           print(item)
  #          self.__pool.map_async(crawljob, item)
        results = [self.__pool.apply_async(crawljob, i)
                for i in enumerate(self.__urls)]
        self.__pool.close()
        self.__pool.join()
        self.__done = True

    @property
    def done(self):
        return self.__done

    def shutdown(self, hard=False):
        """@todo: Docstring for shutdown

        :hard: @todo
        :returns: @todo

        """
        pass



if __name__ == '__main__':
    cm = CrawlerManager(utl.unique_items_from_file('url.txt'))
