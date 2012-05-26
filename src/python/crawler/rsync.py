#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import subprocess

class Rsync(object):
    """
    simple rsync wrapper that ,,mirrors''
    source to destination
    """
    def __init__(self, src_path, dest_path):
        """
        expects source and destination path for 
        synchronisation
        """
        self.__src = src_path
        self.__dest = dest_path
        self.__process = None
        self.__pid = None
       
    def start_sync(self):
        """
        starts the synchronization process,
        this function will block until finished
        
        :returns: returns commandline exit code
        """

        self.__process = subprocess.Popen(["rsync -avc " + self.__src + " " + self.__dest],
                                    shell=True)
        
        self.__pid = self.__process.pid
        print("rsync process with pid {0} started.".format(self.__pid))


    def __del__(self):
        """
        Exits rsync wrapper and returns
        rsync returncode
        """
        retc = self.__process.returncode
        print("rsync process with pid {0} stopped.".format(self.__pid))
        print('rsync module exit.')
        return retc

if __name__ == '__main__':
    r = Rsync('/home/christoph/music/*','/home/christoph/temp/')
    print(r.start_sync())

