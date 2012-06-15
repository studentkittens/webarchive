#!/usr/bin/env python
# encoding: utf-8

"""
Intervalmanager module to control the crawlmanager behaviour
"""
__author__ = 'Christoph Piechula'

import time
import signal
import logging
import threading
import cmd
import cmanager.crawlmanager as c
import util.files as utl
import util.times
import config.reader as config


class IntervalManager(object):
    """
    IntervalManager, manages crawling intervals including start, stop functionality
    """
    def __init__(self):
        self.__interval = None
        self.__cmanager = None
        self.__start_time = None
        self.__kill_mtx = threading.Lock()
        self.__status_lock = threading.Lock()
        self.__set_status('ready')

        signal.signal(signal.SIGINT, self.__ctrlc_handler)

    def __set_status(self, state):
        """
        Set system status threadsafely

        :state: The new state to set
        """
        self.__status_lock.acquire()
        self.__system_status = state
        self.__status_lock.release()

    @property
    def status(self):
        """
        :returns: current system status
        """
        self.__status_lock.acquire()
        state = self.__system_status
        self.__status_lock.release()
        return state

    def __ctrlc_handler(self, signum, frame):
        """
        Called on Ctrl-C
        """
        print('Interrupt: ', signum)
        self.kill()

    def start(self, delay_in_sec=0):
        """
        Starts the intervalmanager, which starts
        the crawlmanager procedure with a given delay

        :delay_in_sec: delay time in seconds
        """
        # fetch interval from config in seconds
        self.__interval = float(config.get('crawler.interval')) * 60

        # delay before next crawl
        if delay_in_sec != 0:
            logging.info("""
                  Next crawl will start in {0} seconds."""
                  .format(delay_in_sec))
            time.sleep(delay_in_sec)

        if self.status != 'stop':
            self.__cmanager = c.CrawlerManager(
                    utl.unique_items_from_file(
                    config.get('crawler.urllistpath')))
            self.__start_time = util.times.get_localtime_sec()
            self.__cmanager.register_done(self.crawling_done_callback)
            self.__set_status('active')
            self.__cmanager.start()
        else:
            self.__set_status('ready')

    def crawling_done_callback(self):
        """
        Registers end time of last crawl and calculates
        delay and starts next run

        """
        if self.status != 'stop':
            self.__set_status('ready')
            current_time = util.times.get_localtime_sec()
            next_crawl_time = self.__start_time + self.__interval

            while next_crawl_time < current_time:
                next_crawl_time = next_crawl_time + self.__interval

            delay = next_crawl_time - current_time
            self.start(delay)
        else:
            self.__set_status('ready')

    def stop(self):
        """
        Stopps the interval manager
        """
        if self.status != 'stop':
            print("Intervalmanager stopped, Crawljobs may be still running.")

        self.__set_status('stop')

    def kill(self):
        """
        Kills the system hard, like ctrl + c
        """
        print("Killing and cleaning crawljobs...")
        self.__kill_mtx.acquire()
        self.__cmanager.shutdown()
        self.stop()
        self.__kill_mtx.release()


class CrawlerShell(cmd.Cmd):
    """
    Interactive command shell to start, stop, kill and quit crawling procedure
    """
    intro = 'Crawler Shell: Type help or ? to list commands \
             \nUse Ctrl-P and Ctrl-N to repeat the last commands'
    prompt = '>>> '

    # Internal:

    def __init__(self, imanager, condvar, autostart=False):
        super(CrawlerShell, self).__init__()
        self.__imanager = imanager
        self.__cv = condvar
        self.__autostart = autostart
        self.__activeflag = False
        self.__quitflag = False

    def set_quitflag(self, state):
        """
        Setting 'quit' flag
        """
        self.__quitflag = state

    def set_activeflag(self, state):
        """
        Setting 'active' flag
        """
        self.__activeflag = state

    def activeflag(self):
        """
        Getter for 'active' flag
        """
        return self.__activeflag

    def quitflag(self):
        """
        Getter for 'quit' flag
        """
        return self.__quitflag

    # Called before start:

    def preloop(self):
        # Well, this is silly.
        time.sleep(0.1)

        if self.__autostart:
            self.do_start(None)

    # Commands:

    def do_start(self, arg):
        """
        Invokes start command
        """
        'Starts crawljobs if stopped previously.'
        if self.__activeflag == False:
            self.__activeflag = True
            self.__cv.acquire()
            self.__cv.notify()
            self.__cv.release()
        return False

    def do_status(self, arg):
        """
        Invokes status command
        """
        'Status of crawler an intervalmanager.'
        print(self.__imanager.status)
        return False

    def do_stop(self, arg):
        """
        Invokes stop command
        """
        'Stopps self.__imanager.'
        self.__imanager.stop()
        return False

    def do_quit(self, arg):
        """
        Invokes quit command
        """
        'Quits Intervalmanager, Crawljobs will still run until finished.'
        self.__cv.acquire()
        self.__quitflag = True
        self.__cv.notify()
        self.__cv.release()
        return True

    def do_EOF(self, arg):
        """
        Invokes quit on EOF
        """
        return self.do_quit(arg)

###########################################################################
#                                unittest                                 #
###########################################################################
