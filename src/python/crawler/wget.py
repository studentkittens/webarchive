#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import subprocess 
import time
import shlex
import config.reader as config
import shutil
from termcolor import cprint, colored 

class Wget(object):
    """
    A simple wget wrapper class.
    """

    def __init__(self, url, tmp_folder):
        """
        :url: url that will be fetched
        :tmp_folder: folder to save website content
        """
        self.__url = url
        self.__tmp_folder = tmp_folder
        self.__depth = config.get('crawler.depth')
        self.__robots = "off"
        self.__base = 'wget -e robots={rob} -rH -l {depth} -P {folder} {url}'
        self.__process = None
        self.__pid = None

    def start(self):
        """
        starts the wget crawl process
        :returns: wget process exit code
        """
        cmd = self.__base.format(rob=self.__robots, depth=self.__depth,
                                 folder=self.__tmp_folder, url=self.__url) 
        cmd = shlex.split(cmd)
        self.__process = subprocess.Popen(cmd, shell=False,
                                               stdout = subprocess.PIPE,
                                               stderr = subprocess.PIPE)

        self.__pid = self.__process.pid
        #TODO, Logger? 
        cprint("[WGET PROCESS] with pid {0} started."
                .format(self.__pid), "green")

    def poll(self):
        if self.__process is not None:
            return self.__process.poll()
        else:
            return True

    def stop(self):
        """
        kills a still running wget process.
        """
        if self.__process != None:
            try:
                cprint('[WGET] Stopping process with pid {0}'
                        .format(self.__pid),"red")

                self.__process.terminate()
            finally:
                self.__process = None
        else:
            #TODO, Logger?
            cprint("no process running.","red")


#------------------------------------------------------------------------------
if __name__ == '__main__':
    a = Wget('www.heise.de','.')
    a.start()
    time.sleep(1)
    a.stop()
