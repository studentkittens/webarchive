#!/usr/bin/env python
# encoding: utf-8
# crawlmanager module
#
import time
import multiprocessing.pool as mpool
import util.files as utl
import crawler.crawljob as job


class CrawlerManager(object):
    """
    Simple crawljob manager
    """
    MAX_INSTANCES = 2
    CRAWL_DEPTH = 2
    CRAWL_INTERVAL = 36000

    def __init__(self,urls):
        if len(urls) > 0:
            self.__urls = list(urls)
            self.__pool = mpool.ThreadPool(MAX_INSTANCES)
           


    def start(self):
        """
        start threadpool with max number of
        instances
        :returns: @todo

        """
        for item in enumerate(self.__urls):
            self.__pool.map_async(crawljob, item)
        self.__pool.close()
        self.__pool.join()


    def shutdown(self, hard=False):
        """@todo: Docstring for shutdown

        :hard: @todo
        :returns: @todo

        """
        pass



if __name__ == '__main__':
    cm = CrawlerManager(utl.unique_items_from_file('url.txt'))
