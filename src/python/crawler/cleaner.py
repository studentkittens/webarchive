#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import subprocess
import os, sys,shutil
import util.files as ufile
from metadata import MetaData

class Cleaner:
    """
    Cleaner, walk dir tree, cleans and restructures
    folder hierarchy, calls filter subsystem
    """
    def __init__(self,path):
        self.__mdlist = []
        self.__path = path

    def __restructure_file(self,folder,file_path):
        """
        restructures folder node, collects meta_object list
        for interesting files
        :folder: folder path of content file 
        :file_path: path to content file itself 
        """
        src_file = os.path.join(folder, file_path)
        dest_file = os.path.join(folder, '____data')
        
        try:
            os.rename(src_file,dest_file)
            os.mkdir(src_file)
            # moving an renaming to 'data'
            shutil.move(dest_file,os.path.join(src_file, 'data'))
        except OSError as e:
            pass # TODO: Log Error
        else:
            meta_obj = MetaData.build_metadata_from_file(self.__path,dest_file)
            keep = filter.filter(meta_obj)
            if keep:
                self.__mdlist.append(meta_obj)
            else:
                os.remove(os.path.join(src_file,'data')
                os.removedirs(src_file)


    def restructure(self):
        """
        walks through folder hierarchy calling internal
        restructure method.
        """
        tree = os.walk(os.path.abspath(self.__path))
        for leaf in tree:
            for data_file in leaf[2]:
                self.__restructure_file(leaf[0],data_file)

    
    def clean_empty(self):
        # TODO: (Hopefully) Replace by pure python code
        subprocess.call(['find',self.__path,'-empty','-delete'])


    def print_list(self):
        return self.__mdlist

if __name__ == '__main__':
    c = Cleaner(sys.argv[1])
    c.restructure()
    c.clean_empty()
    print(c.print_list())
