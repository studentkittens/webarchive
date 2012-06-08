#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import time 
import logging
import cmd

import cmanager.crawlmanager as c
import util.files as utl
import util.times 
import config.reader as config

class IntervalManager(object):
    """
    IntervalManager, manages crawling intervals
    """

    def __init__(self):
        self.__interval = None 
        self.__cmanager = None
        self.__start_time = None
        self.__keep_running = True
        self.__crawling_done_callback = None

    def format_time(self, time_in_secs):
        """
        :time_in_secs: time in seconds since the epoch
        :returns: Beautifully formatted time string
        """
        return time.strftime("%a, %d %b %Y %H:%M:%S", time_in_secs)

    def start(self, delay_in_sec=0):
        """
        Starts the intervalmanager, which starts
        the crawlmanager procedure with a given delay
        
        :delay_in_sec: delay time in seconds
        """
        #fetch interval from config
        self.__interval = float(config.get('crawler.interval'))*60
        

        #delay before next crawl 
        if delay_in_sec != 0:
            #only to display some information
            cur_time = self.format_time(time.localtime(util.times.get_localtime_sec()))
            next_time = self.format_time(time.localtime(util.times.get_localtime_sec() + delay_in_sec))
            logging.info("""
                  Current time: {0},
                  next crawl will start in {1} seconds @ {2}."""
                  .format(cur_time, delay_in_sec, next_time))
            time.sleep(delay_in_sec)

        self.__cmanager = c.CrawlerManager(utl.unique_items_from_file(config.get('crawler.urllistpath')))
        self.__start_time = util.times.get_localtime_sec()
        self.__cmanager.register_done(self.crawling_done_callback)

        try:
            if self.__keep_running:
                self.__cmanager.start()
        except KeyboardInterrupt:
            logging.warn('Got Ctrl-C => Will shutdown() now')
            self.__cmanager.shutdown()

    def crawling_done_callback(self):
        """
        Registers end time of last crawl and calculates
        delay and starts next run

        """
        self.__crawling_done_callback = util.times.get_localtime_sec()
        next_crawl_time = self.__start_time + self.__interval
        
        while next_crawl_time < self.__crawling_done_callback:
            next_crawl_time = next_crawl_time + self.__interval
        
        delay = next_crawl_time - self.__crawling_done_callback
        self.start(delay)

    def stop(self):
        self.__keep_running = False
        print("Intervalmanager stopped, crawljobs may be still running.")


    def kill(self):
        self.__cmanager.test()
        print("Killing an cleaning crawljobs...")



class CrawlerShell(cmd.Cmd):
    intro = 'Crawler Shell: Type help or ? to list commands\nUse Ctrl-P and Ctrl-N to repeat the last commands'
    prompt = '>>> '

    def do_kill(self, arg):
        'Kills crawljobs immediatelly + cleanup'
        IntervalManager.kill(IntervalManager)
        return False 

    def do_start(self, arg):
        'Starts crawljobs if stopped previously.'
        IntervalManager.start(IntervalManager)
        return False
    
    def do_stop(self, arg):
        'Stopps Intervalmanager.'
        IntervalManager.stop(IntervalManager)
        return False

    def do_quit(self, arg):
        'Quits Intervalmanager, Crawljobs will still run until finished.'
        return True

    def do_EOF(self, arg):
        return True

###########################################################################
#                                unittest                                 #
###########################################################################

