#!/usr/bin/env python
# encoding: utf-8

"""
Crawl Manager module to manage running crawljob instances
"""

__author__ = 'Christoph Piechula'

import multiprocessing.pool as mpool
import archive.config.handler as config
import archive.crawler.crawljob as job

import threading
import logging


has_been_stopped = False
running = []
running_mtx = threading.Lock()


def crawljob(ident, url):
    """
    Crawljob callback method runs an own thread
    starts actual crawljob

    :ident: identifier given by threadpool
    :url: url to crawl
    """
    print('CRAWL')
    if has_been_stopped:
        return -1

    try:
        logging.info('Job #{cid} ({curl}) started.'.format(cid=ident, curl=url))
        j = job.CrawlJob(ident, url)
        running_mtx.acquire()
        running.append(j)
        running_mtx.release()
        j.run()
    except Exception:
        logging.exception("error during starting crawljob pool")
    return ident


class CrawlerManager(object):
    """
    Crawljob manager, threadpool which launches a pool of
    threads
    """
    def __init__(self, urls):
        """
        :urls: set of urls that will be crawled
        """
        self.__die = False
        if len(urls) > 0:
            self.__done_callback = None
            self.__urls = list(urls)
            self.__pool = mpool.ThreadPool(config.get('crawler.maxInst'))

    def start(self):
        """
        Starts threadpool with max number of instances
        """
        results = [self.__pool.apply_async(crawljob, url)
                for url in enumerate(self.__urls)]

        # Now wait till all jobs finished
        self.__pool.close()
        self.__pool.join()

        if self.__done_callback is not None:
            self.__done_callback()

        self.__done_callback = None

    def register_done(self, func):
        """
        Register method for callback function which is
        triggered after work is done
        """
        self.__done_callback = func

    def shutdown(self):
        """
        Shuts down all currently running crawljobs
        and joins/closes the pool
        """
        # This is necessary to stop jobs, that
        # was started because others were interrupted by shutdown()
        global has_been_stopped
        has_been_stopped = True
        for job in running:
            job.shutdown()

        self.__pool.close()
        self.__pool.join()

        # It's okay to start threads now
        has_been_stopped = False
