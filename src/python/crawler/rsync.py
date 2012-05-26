#!/usr/bin/env python
# encoding: utf-8

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
       
    def start_sync(self):
        """
        starts the synchronization process,
        this function will block until finished
        
        :returns: returns commandline exit code
        """

        self.__process = subprocess.Popen(["rsync -avc " + self.__src + " " + self.__dest],
                                    shell=True)
        self.__process.communicate()
        return self.__process.returncode


    def __del__(self):
        """
        rsync module exit 
        """
        print("rsync module exit.")


if __name__ == '__main__':
    r = Rsync('/home/christoph/xsd/*','/home/christoph/temp/')
    print(r.start_sync())
