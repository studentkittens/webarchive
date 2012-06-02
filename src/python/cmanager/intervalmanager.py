#!/usr/bin/env python
# encoding: utf-8

import time 
import logging

import cmanager.crawlmanager as c
import util.files as utl
import util.times 
import config.reader as config

class IntervalManager(object):
    """
    Simple IntervalManager that manages interval of crawl util.times
    """

    def __init__(self):
        """@todo: to be defined """
        self.__interval = None 
        self.__cmanager = None
        self.__start_time = None
        self.__crawling_done_callback = None

    def format_time(self, time_in_secs):
        return time.strftime("%a, %d %b %Y %H:%M:%S", time_in_secs)

    def start(self, delay_in_sec=0):
        """
        starts the intervalmanager, which starts
        the crawlmanager procedure with a given 
        delay
        """
        #fetch interval from config
        self.__interval = float(config.get('crawler.interval'))*60
        
        #only to display some information
        cur_time = self.format_time(time.gmtime(util.times.get_localtime_sec()))
        next_time = self.format_time(time.gmtime(util.times.get_localtime_sec() + delay_in_sec))

        #delay before next crawl 
        if delay_in_sec != 0:
            print('\nCurrent time: {0},\nnext crawl will start in {1} seconds @ {2}.'
                  .format(cur_time, delay_in_sec, next_time))
            time.sleep(delay_in_sec)

        self.__cmanager = c.CrawlerManager(utl.unique_items_from_file('url.txt'))
        self.__start_time = util.times.get_localtime_sec()
        self.__cmanager.register_done(self.crawling_done_callback)

        try:
            self.__cmanager.start()
        except KeyboardInterrupt:
            logging.warn('Got Ctrl-C => Will shutdown() now')
            self.__cmanager.shutdown()

    def crawling_done_callback(self):
        """
        registers end time of last crawl and calculates
        delay and starts next run
        :returns: @todo

        """
        self.__crawling_done_callback = util.times.get_localtime_sec()
        next_crawl_time = self.__start_time + self.__interval
        
        while next_crawl_time < self.__crawling_done_callback:
            next_crawl_time = next_crawl_time + self.__interval
        
        delay = next_crawl_time - self.__crawling_done_callback
        self.start(delay)
