#!/usr/bin/env python
# encoding: utf-8

def unique_items_from_file(path):
    urls = set()
    try:
        with open(path,'r') as urlfile:
            for line in urlfile:
                urls.add(line.rstrip())
    except:
        print('test')
        #print(type(imar),str(imar))
    return urls

if __name__ == '__main__':
    print(unique_items_from_file('url.txt_'))

