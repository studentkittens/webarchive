#!/usr/bin/env python
# encoding: utf-8

import time
import multiprocessing.pool as mpool
import util.files as uff
import cjob.crawljob as job

MAX_INSTANCES = 2
CRAWL_DEPTH = 2
CRAWL_INTERVAL = 36000

def crawljob(ident,url):
    try:
        print("ich bin crawler " + str(ident) + " und crawle "+ url)
        j = job.CrawlJob(ident,url)
    except Exception as e:
        print(e)
    return ident

class CrawlerManager(object):

    def __init__(self,urls):
        if len(urls) > 0:
            self.__urls = list(urls)
            self.__pool = mpool.ThreadPool(MAX_INSTANCES)
            results = [self.__pool.apply_async(crawljob, i)
                for i in enumerate(self.__urls)]

            print(results)
            self.__pool.close()
            self.__pool.join()

if __name__ == '__main__':
    cm = CrawlerManager(uff.unique_items_from_file('url.txt'))
