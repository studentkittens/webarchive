#!/usr/bin/env python
# encoding: utf-8

from termcolor import cprint

def unique_items_from_file(path):
    urls = set()
    try:
        with open(path,'r') as urlfile:
            for line in urlfile:
                if line[0] != '#':
                    urls.add(line.rstrip())
    except IOError as oemar:
        error = (type(oemar),str(oemar))
        cprint(error,'red')
    return urls

if __name__ == '__main__':
    print(unique_items_from_file('../url.txt'))

