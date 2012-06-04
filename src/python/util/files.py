#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christopher Pahl'

import os
import logging

def unique_items_from_file(path):
    """
    Creates a unique set with urls from a given file
    commented urls '#' are ignored

    :path: path to url list file
    """
    urls = set()
    try:
        with open(path,'r') as urlfile:
            for line in urlfile:
                if line[0] != '#':
                    urls.add(line.strip())
    except IOError:
        logging.exception("cannot read url list from disk.")
    return urls



###########################################################################
#                                unittest                                 #
###########################################################################

if __name__ == '__main__':
    print(unique_items_from_file('../url<F3>.txt'))
