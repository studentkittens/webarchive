#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import subprocess
import os, sys,shutil
import logging

import util.times as times
import crawler.filter as filter
from crawler.metadata import MetaData

class Cleaner:
    """
    Cleaner, walk dir tree, cleans and restructures
    tmp_crawler_folder hierarchy, calls filter subsystem
    """
    def __init__(self,path):
        self.__mdlist = []
        self.__path = path
        self.__commitTime = times.get_sys_time()
        self.__filtersys = filter.FilterSystem()

    def __restructure_file(self,tmp_crawler_folder,file_name):
        """
        restructures tmp_crawler_folder node, collects meta_object list
        for interesting files
        :tmp_crawler_folder: tmp_crawler_folder path of content file 
        :file_name: name to content file itself 
        """
        src_file = os.path.join(tmp_crawler_folder, file_name)
        dest_file = os.path.join(tmp_crawler_folder, '____data')
        
        try:
            os.rename(src_file,dest_file)
            os.mkdir(src_file)
            # moving an renaming to 'data'
            final_data_path= os.path.join(src_file, 'data')
            shutil.move(dest_file,final_data_path)
        
        except OSError as err:
            pass # TODO: Log Error
        else:
            meta_obj = MetaData.build_metadata_from_file(self.__path,
                                                        final_data_path,
                                                        self.__commitTime)
            keep = self.__filtersys.check(meta_obj)
            if keep:
                self.__mdlist.append(meta_obj)
            else:
                os.remove(os.path.join(src_file,'data'))
                os.removedirs(src_file)
    @property
    def meta_list(self):
        """@todo: Docstring for meta_list
        :returns: @todo

        """
        return self.__mdlist

    def restructure(self):
        """
        walks through tmp_crawler_folder hierarchy calling internal
        restructure method.
        """
        logging.debug("starting restructure.")
        tree = os.walk(os.path.abspath(self.__path))
        for leaf in tree:
            for data_file in leaf[2]:
                self.__restructure_file(leaf[0],data_file)

    
    def clean_empty(self):
        # TODO: (Hopefully) Replace by pure python code
        logging.debug("removing empty files and folders.")
        subprocess.call(['find',self.__path,'-empty','-delete'])


if __name__ == '__main__':
    c = Cleaner(sys.argv[1])
    c.restructure()
    c.clean_empty()
    print(c.print_list())
