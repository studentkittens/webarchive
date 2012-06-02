#!/usr/bin/env python
# encoding: utf-8
# crawlmanager module
#
import multiprocessing.pool as mpool
import util.files as utl
import config.reader as config
import crawler.crawljob as job
import threading

running = []
running_mtx = threading.Lock()

def crawljob(ident,url):
    try:
        print('Job #{cid} ({curl}) started.'.format(cid = ident, curl = url))
        j = job.CrawlJob(ident, url)
        running_mtx.acquire()
        running.append(j)
        running_mtx.release()
        j.run()
    except Exception as err:
        print(err)
    return ident


class CrawlerManager(object):
    """
    Simple crawljob manager
    """
    def __init__(self,urls):
        if len(urls) > 0:
            self.__done_callback = None
            self.__urls = list(urls)
            self.__pool = mpool.ThreadPool(config.get('crawler.maxInst'))

    def start(self):
        """
        start threadpool with max number of
        instances
        :returns: @todo

        """
        results = [self.__pool.apply_async(crawljob, i)
                for i in enumerate(self.__urls)]

        # Now wait till all jobs finished 
        self.__pool.close()
        self.__pool.join()

        self.__done_callback()
        self.__done_callback = None

    def register_done(self, func):
        self.__done_callback = func

    def shutdown(self):
        for job in running:
            job.shutdown()

        self.__pool.close()
        self.__pool.join()

if __name__ == '__main__':
    cm = CrawlerManager(utl.unique_items_from_file('url.txt'))
