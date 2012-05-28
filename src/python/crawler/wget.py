#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import subprocess 
import time
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
        self.__process = None
        self.__pid = None


    def start(self):
        """
        starts the wget crawl process
        :returns: wget process exit code
        """
        self.__process = subprocess.Popen(
                                    ['wget', '-e robots=off', '-rH', '-l 1',
                                    '--directory-prefix=', self.__tmp_folder,
                                    self.__url], shell=False)

        self.__pid = self.__process.pid
        print("wget process with pid {0} started.".format(self.__pid))


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
            print("no process running.")


    def __del__(self):
        """
        Exits wget wrapper and returns
        wget returncode
        """
        retc = self.__process.returncode
        print("wget process with pid {0} stopped.".format(self.__pid))
        print('wget module exit.')
        return retc
        

if __name__ == '__main__':
    a = Wget('www.heise.de','.')
    a.start()
    time.sleep(1)
    a.stop()
    #XXX process cannot be killed twice yet, does it matter?
