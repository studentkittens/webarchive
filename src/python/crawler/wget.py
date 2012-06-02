#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import subprocess 
import time
import shlex
import config.reader as config
from termcolor import cprint, colored 

class Wget(object):
    """
    Simple wget wrapper class
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
        cprint("[WGET PROCESS] with pid {0} started.".format(self.__pid), "green")

    def wait(self):
        """@todo: Docstring for wait
        :returns: @todo

        """
        
        self.__process.wait()

    def stop(self):
        """
        kills a still running wget process.
        """
        if self.__process != None:
            try:
                self.__process.terminate()
                
            finally:
                out, err = self.__process.communicate()
                print(out, err)
        else:
            #TODO, Logger?
            cprint("no process running.","red")


    def __del__(self):
        """
        Exits wget wrapper and returns
        wget returncode
        """
        retc = self.__process.returncode
        cprint("[WGET PROCESS] with pid {0} stopped, returncode was {1}."
               .format(self.__pid, retc), "blue")
        

if __name__ == '__main__':
    a = Wget('www.heise.de','.')
    a.start()
    time.sleep(1)
    a.stop()
    #XXX process cannot be killed twice yet, does it matter?
