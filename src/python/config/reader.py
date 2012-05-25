#!/usr/bin/env python
# encoding: utf-8
# config module
#
# Hardcoded fake values 
def get(value):
    if value == 'crawl.tmplocation':
        return '/tmp/archive_sync/'
    elif:
        value == 'crawl.archivepath':
            return '/tmp/archive/'
    else:
        return ''
