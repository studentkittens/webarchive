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
from crawler.xmlreader import XMLReader
from pprint import pprint


class XMLDBRecover(object):
    """Docstring for XMLDBRecover """

    def __init__(self):
        """@todo: to be defined """
        self.__metalist = []

    def __iterate_filetree(self, wrapper):
        for root, dirnames, filenames in os.walk(wrapper.domain):
            xmlfiles = fnmatch.filter(filenames, '*.xml')
            if len(xmlfiles) > 0:
                print('     -', xmlfiles)
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
        print('   *', commit_list)
        if len(commit_list) == 1:
            # Only one commit there, no checkout needed
            # (assuming it's the most recent)
            self.__iterate_filetree(wrapper)
        else:
            for commit in commit_list:
                self.__iterate_filetree(wrapper)

    def __iterate_branches(self, wrapper):
        for branch in wrapper.list_branches():
            print('=>', branch, '(', wrapper.domain, ')')
            wrapper.checkout(branch)
            self.__iterate_commits(wrapper)

    def load(self):
        domain_patt = os.path.join(paths.get_content_root(), '*')
        for domain in glob.glob(domain_patt):
            wrapper = git.Git(domain)
            self.__iterate_branches(wrapper)
            wrapper.checkout('master')

        pprint(self.__metalist)
        return self.__metalist

    def save(self, metalist):
        pass


def main():
    rec = XMLDBRecover()
    rec.load()

if __name__ == '__main__':
    main()
