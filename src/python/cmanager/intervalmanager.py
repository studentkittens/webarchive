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
        self.__system_status = 'ready'
        self.__kill_mtx = threading.Lock()

        # Fun.
        self.__status = {

                (False, False): 'stop',
                (True, False): 'nocrawl',
                (False, True): 'stop',
                (True, True): 'active'
        }

        signal.signal(signal.SIGINT, self.__ctrlc_handler)

    def __ctrlc_handler(self, signum, frame):
        print('Interrupt: ', signum)
        self.kill()

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
            logging.info("""
                  Next crawl will start in {0} seconds."""
                  .format(delay_in_sec))
            time.sleep(delay_in_sec)

        if self.__system_status != 'stop':
            self.__cmanager = c.CrawlerManager(utl.unique_items_from_file(config.get('crawler.urllistpath')))
            self.__start_time = util.times.get_localtime_sec()
            self.__cmanager.register_done(self.crawling_done_callback)
            self.__system_status = 'active'
            self.__cmanager.start()
        else:
            self.__system_status = 'ready'

    def crawling_done_callback(self):
        """
        Registers end time of last crawl and calculates
        delay and starts next run

        """
        if self.__system_status != 'stop':
            self.__system_status = 'ready'
            current_time = util.times.get_localtime_sec()
            next_crawl_time = self.__start_time + self.__interval

            while next_crawl_time < current_time:
                next_crawl_time = next_crawl_time + self.__interval

            delay = next_crawl_time - current_time
            self.start(delay)
        else:
            self.__system_status = 'ready'

    @property
    def status(self):
        return self.__system_status

    def stop(self):
        print("Intervalmanager stopped, crawljobs may be still running.")
        self.__system_status = 'stop'

    def kill(self):
        self.__kill_mtx.acquire()
        print("Killing and cleaning crawljobs...")
        self.__cmanager.shutdown()
        self.stop()
        self.__kill_mtx.release()


class CrawlerShell(cmd.Cmd):
    intro = 'Crawler Shell: Type help or ? to list commands\nUse Ctrl-P and Ctrl-N to repeat the last commands'
    prompt = '>>> '

    def set_imanager(self, imanager):
        self.__imanager = imanager

    def set_condvar(self, cv):
        self.__cv = cv

    def set_quitflag(self, state):
        self.__quitflag = state

    def set_activeflag(self, state):
        self.__activeflag = state

    def activeflag(self):
        return self.__activeflag

    def quitflag(self):
        return self.__quitflag

    def do_start(self, arg):
        'Starts crawljobs if stopped previously.'
        if self.__activeflag == False:
            self.__activeflag = True
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
        self.__cv.acquire()
        self.__quitflag = True
        self.__cv.notify()
        self.__cv.release()
        return True

    def do_EOF(self, arg):
        return self.do_quit(arg)

###########################################################################
#                                unittest                                 #
###########################################################################
