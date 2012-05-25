#!/usr/bin/env python
# encoding: utf-8

import subprocess

class Rsync(object):
    """
    simple rsync wrapper that ,,mirrors''
    source to destination
    """
    def __init__(self, src_path, dest_path):
        self.__src = src_path
        self.__dest = dest_path
       
    def start_sync(self):
        """
        starts the synchronization process,
        this function will block until finished
        
        :returns: returns commandline exit code
        """
        retv = subprocess.call(['rsync','-a','-c',
                                self.__src, self.__dest])
        return retv

if __name__ == '__main__':
    r = Rsync('/home/christoph/xsd/','/home/christoph/temp/')
    print(r.start_sync())
