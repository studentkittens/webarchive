#!/usr/bin/env python
# encoding: utf-8

"""
XMLDBRecover submodule
"""

__author__ = 'Christopher Pahl'


import glob
import os
import fnmatch
import crawler.git as git
import util.paths as paths
import logging
import threading

from crawler.xmlreader import XmlReader
from multiprocessing.pool import ThreadPool


class XMLDBRecover(object):
    """
    XMLDBRecover submodule class
    """

    def __init__(self):
        """
        Inits metalist and shutdown flag on create
        """
        self.__metalist = []
        self.__shutdown = False

    @property
    def description(self):
        """
        :returns: module description
        """
        return 'Collecting Metadata through XML Files'

    def __iterate_filetree(self, wrapper):
        """
        Walks through file tree and generates
        metadata list from xml files by invoking XmlReader()
        """
        for root, dirnames, filenames in os.walk(wrapper.domain):
            if self.__shutdown:
                raise KeyboardInterrupt

            xmlfiles = fnmatch.filter(filenames, '*.xml')
            if len(xmlfiles) > 0:
                for xml in xmlfiles:
                    xml_path = os.path.join(root, xml)
                    try:
                        reader = XmlReader(xml_path)
                        self.__metalist.append(reader.parse())
                    except:
                        print('Parsing of XML failed on file:', xml_path)
                        raise

    def __iterate_commits(self, wrapper):
        """
        Iterates git commits
        """
        # Last commit is not used, since it's the
        # ,,Initialized'' commit from the empty base branch
        commit_list = wrapper.list_commits()[:-1]
        if len(commit_list) == 1:
            # Only one commit there, no checkout needed
            # (assuming it's the most recent)
            self.__iterate_filetree(wrapper)
        else:
            for commit in commit_list:
                self.__iterate_filetree(wrapper)

    def __iterate_branches(self, wrapper):
        """
        Iterates git branches
        """
        for branch in wrapper.list_branches():
            logging.info('Processing branch ' + branch + ' (' + wrapper.domain + ')')
            wrapper.checkout(branch)
            self.__iterate_commits(wrapper)

    def recover_domain(self, domain):
        """
        Iterates through given domain trying to recover metadata
        """
        try:
            wrapper = git.Git(domain)
            self.__iterate_branches(wrapper)
        except KeyboardInterrupt:
            logging.warn(threading.current_thread().name + ': Interrupted while traversing Archive')
        finally:
            wrapper.checkout('master')

    def load(self):
        """
        Invokes threaded xml recovery
        """
        try:
            self.__init__()
            domain_patt = os.path.join(paths.get_content_root(), '*')
            domain_list = glob.glob(domain_patt)
            threadPool = ThreadPool(16)
            threadPool.map(self.recover_domain, domain_list)
            threadPool.close()
            threadPool.join()
        except KeyboardInterrupt:
            print('Got interrupted')
        finally:
            self.__shutdown = True
            threadPool.close()
            threadPool.join()

        return self.__metalist

    def save(self, metalist):
        pass
