#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import time
import signal
import logging
import threading
import cmd
import ctypes
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
        self.__crawler_running = False
        self.__kill_mtx = threading.Lock()
        self.__crawling_done_callback = None

        # Fun.
        self.__statussen = {
                (False, False): 'quit',
                (True, False): 'nocrawl',
                (False, True): 'stop',
                (True, True): 'active'
        }

        signal.signal(signal.SIGINT, self.__ctrlc_handler)

    def __ctrlc_handler(self, signum, frame):
        print('Interrupt: ', signum)
        self.kill()

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
        self.__interval = float(config.get('crawler.interval')) * 60

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

        if self.__keep_running:
            self.__crawler_running = True
            self.__cmanager.start()

    def crawling_done_callback(self):
        """
        Registers end time of last crawl and calculates
        delay and starts next run

        """
        self.__crawler_running = False
        self.__crawling_done_callback = util.times.get_localtime_sec()
        next_crawl_time = self.__start_time + self.__interval

        while next_crawl_time < self.__crawling_done_callback:
            next_crawl_time = next_crawl_time + self.__interval

        delay = next_crawl_time - self.__crawling_done_callback
        if self.__keep_running:
            self.start(delay)

    @property
    def status(self):
        return self.__statussen[(self.__keep_running, self.__crawler_running)]

    def stop(self):
        print("Intervalmanager stopped, crawljobs may be still running.")
        self.__keep_running = False

    def kill(self):
        self.__kill_mtx.acquire()
        print("Killing and cleaning crawljobs...")
        self.__cmanager.shutdown()
        self.__crawler_running = False
        self.__keep_running = False
        self.__kill_mtx.release()


class CrawlerShell(cmd.Cmd):
    intro = 'Crawler Shell: Type help or ? to list commands\nUse Ctrl-P and Ctrl-N to repeat the last commands'
    prompt = '>>> '

    def set_imanager(self, imanager, cv):
        self.__imanager = imanager
        self.__cv = cv

    def do_start(self, arg):
        'Starts crawljobs if stopped previously.'
        self.__cv.acquire()
        self.__cv.notify()
        self.__cv.release()
        return False

    def do_status(self, arg):
        'Status of crawler an intervalmanager.'
        print(self.__imanager.status)
        return False

    def do_stop(self, arg):
        'Stopps self.__imanager.'
        self.__imanager.stop()
        return False

    def do_quit(self, arg):
        'Quits Intervalmanager, Crawljobs will still run until finished.'
        return True

    def do_EOF(self, arg):
        return self.do_quit(arg)

###########################################################################
#                                unittest                                 #
###########################################################################
