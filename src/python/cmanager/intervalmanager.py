#!/usr/bin/env python
# encoding: utf-8

import cmanager.crawlmanager as c
import util.files as utl
import util.times 
import time 
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
        self.__end_time = None

    def start(self, delay_in_sec=0):
        """
        starts the intervalmanager, which starts
        the crawlmanager procedure with a given 
        delay
        :returns: @todo

        """

        #fetch interval from config
        self.__interval = float(config.get('crawler.interval'))*60
        
        #a bit ugly, fix it
        #only to display some information
        cur_time = time.strftime("%a, %d %b %Y %H:%M:%S",
                                 time.gmtime(util.times.get_localtime_sec()))
        next_time = time.strftime("%a, %d %b %Y %H:%M:%S",
                                 time.gmtime(util.times.get_localtime_sec() + delay_in_sec))

        #delay before next crawl 
        if delay_in_sec != 0:
            print("""\nCurrent time: {0},\nnext crawl will start in {1} seconds @ {2}."""
                  .format(cur_time, delay_in_sec, next_time))
            time.sleep(float(delay_in_sec))

        self.__cmanager = c.CrawlerManager(utl.unique_items_from_file('url.txt'))
        self.__start_time = util.times.get_localtime_sec()
        self.__cmanager.register_done(self.end_time)
        self.__cmanager.start()
         

    def end_time(self):
        """
        registers end time of last crawl and calculates
        delay and starts next run
        :returns: @todo

        """
        self.__end_time = util.times.get_localtime_sec()
        next_crawl_time = self.__start_time + self.__interval
        
        while next_crawl_time < self.__end_time:
            next_crawl_time = next_crawl_time + self.__interval
        
        delay = next_crawl_time - self.__end_time
        #delay = next_crawl_time + self.__interval * (int((self.__end_time - next_crawl_time)  / self.__interval) + 1) - self.__end_time
        self.start(delay)
   

    def __del__ (self):
        """@todo: Docstring for __del__ 
        :returns: @todo
        """
        print('interval manager exit.')

    
