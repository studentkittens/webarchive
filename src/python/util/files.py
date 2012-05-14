#!/usr/bin/env python
# encoding: utf-8
# filereader module
#
import os
import fcntl
from termcolor import cprint

def unique_items_from_file(path):
    urls = set()
    try:
        with open(path,'r') as urlfile:
            for line in urlfile:
                if line[0] != '#':
                    urls.add(line.rstrip())
    except IOError as oemar:
        error = ('!!!!!',type(oemar),str(oemar))
        cprint(error,'red')
    return urls

def mkdir_noerror(path):
    """@todo: Docstring for mkdir_noerror

    :path: @todo
    :returns: @todo

    """
    try:
        os.mkdir(path)
    except OSError as oserr:
        print(type(oserr), str(oserr))
    
#http://amix.dk/blog/post/19531     
    """
def lockFile(lockfile):
    try:
        fp = open(lockfile, 'w')
        fcntl.flock(fp, fcntl.LOCK_EX)
    except:
        return False
    return True
"""


if __name__ == '__main__':
    print(unique_items_from_file('../url.txt'))
