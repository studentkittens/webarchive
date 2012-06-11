#!/usr/bin/env python
# encoding: utf-8

"""
Recover DB from XML
"""

__author__ = 'Christopher Pahl'


import glob
import os
import fnmatch
import crawler.git as git
import util.paths as paths
import logging
import threading

from crawler.xmlreader import XMLReader
from multiprocessing.pool import ThreadPool


class XMLDBRecover(object):
    """Docstring for XMLDBRecover """

    def __init__(self):
        """@todo: to be defined """
        self.__metalist = []
        self.__shutdown = False

    def __iterate_filetree(self, wrapper):
        for root, dirnames, filenames in os.walk(wrapper.domain):
            if self.__shutdown:
                raise KeyboardInterrupt

            xmlfiles = fnmatch.filter(filenames, '*.xml')
            if len(xmlfiles) > 0:
                for xml in xmlfiles:
                    xml_path = os.path.join(root, xml)
                    try:
                        reader = XMLReader(xml_path)
                        self.__metalist.append(reader.parse())
                    except:
                        print('Parsing of XML failed on file:', xml_path)
                        raise

    def __iterate_commits(self, wrapper):
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
        for branch in wrapper.list_branches():
            logging.info('Processing branch ' + branch + ' (' + wrapper.domain + ')')
            wrapper.checkout(branch)
            self.__iterate_commits(wrapper)

    def recover_domain(self, domain):
        try:
            wrapper = git.Git(domain)
            self.__iterate_branches(wrapper)
        except KeyboardInterrupt:
            logging.warn(threading.current_thread().name + ': Interrupted while traversing Archive')
        finally:
            wrapper.checkout('master')

    def sanitize_domain(self, domain):
        wrapper = git.Git(domain)
        wrapper.checkout('master')

    def load(self):
        try:
            self.__init__()
            domain_patt = os.path.join(paths.get_content_root(), '*')
            domain_list = glob.glob(domain_patt)
            threadPool = ThreadPool(8)
            threadPool.map(self.recover_domain, domain_list)
            threadPool.close()
            threadPool.join()
        except KeyboardInterrupt:
            print('Got interrupted')
        finally:
            self.__shutdown = True
            threadPool.close()
            threadPool.join()
            logging.info('Making sure everything is clean on master..')
            for domain in domain_list:
                self.sanitize_domain(domain)

        return self.__metalist

    def save(self, metalist):
        pass


def main():
    rec = XMLDBRecover()
    rec.load()

if __name__ == '__main__':
    main()
