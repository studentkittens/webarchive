#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import subprocess
import logging

class Rsync(object):
    """
    """
    def __init__(self, src_path, dest_path):
        """
        A simple rsync wrapper that ,,mirrors''
        the content of the source to destination path
        
        :src_path: source path
        :dest_path: destination path
    
        """
        self.__src = src_path
        self.__dest = dest_path
        self.__process = None
        self.__pid = None
       
    def start_sync(self):
        """
        Starts the synchronization process,
        this function will block until finished
        
        """
        cmd = 'rsync -avcP {src} {dest}'.format(src=self.__src, dest=self.__dest)
        self.__process = subprocess.Popen(cmd, shell=True,
                                               stdout = subprocess.PIPE,
                                               stderr = subprocess.PIPE)
        
        self.__pid = self.__process.pid

        logging.info('rsync process with pid {0} started'.format(self.__pid))
        logging.info('Syncing from "" to ""'.format(self.__src, self.__dest))
        
        out,err = self.__process.communicate()
        self.__pid = self.__process.poll()

        if self.__pid == 0:
            logging.info("rsync process with pid {0} finished.".format(self.__pid))
        else:
            logging.warning('rsync failed with exit-code {0}: {1}'.format(self.__pid, err))

    
if __name__ == '__main__':
    r = Rsync('/home/christoph/music/*','/home/christoph/temp/')
    print(r.start_sync())

