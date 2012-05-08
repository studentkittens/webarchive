#!/usr/bin/env python
# encoding: utf-8

# Hardcoded fake values 
def get(value):
    if value == 'crawl.tmplocation':
        return '/tmp/archive_sync/'
    else:
        return ''
