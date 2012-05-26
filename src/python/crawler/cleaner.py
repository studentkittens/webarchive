#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import subprocess
import os, sys,shutil
import util.files as ufile
from metadata import MetaData

class Cleaner:
    def __init__(self,path):
        self.__mdlist = []
        self.__path = path

    def clean_empty(self):
        # TODO: (Hopefully) Replace by pure python code
        subprocess.call(['find',self.__path,'-empty','-delete'])

    def __restructure_file(self,folder,file_path):
        src_file = '/'.join([folder,file_path])
        dest_file = '/'.join([folder,'__data'])
        
        try:
            os.rename(src_file,dest_file)
            os.mkdir(src_file)
            shutil.move(dest_file,src_file)
        except OSError as e:
            pass # TODO: Log Error
        else:
            self.__mdlist.append(MetaData.build_metadata_from_file(self.__path,dest_file))

    def restructure(self):
        tree = os.walk(os.path.abspath(self.__path))
        for leaf in tree:
            for data_file in leaf[2]:
                self.__restructure_file(leaf[0],data_file)

    def print_list(self):
        return self.__mdlist

if __name__ == '__main__':
    c = Cleaner(sys.argv[1])
    c.restructure()
    c.clean_empty()
    print(c.print_list())
