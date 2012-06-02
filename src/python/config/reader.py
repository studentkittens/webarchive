#!/usr/bin/env python
# encoding: utf-8
# config module
#
# Hardcoded fake values 
#
options = {
        'crawler.tempRoot': '/tmp/archive/tmp/',
        'general.root': '/tmp/archive/',
        'general.filterpath':'/tmp/archive/filter/',
        'crawler.maxInst' : 10,
        'crawler.interval' : 0.5,
        'crawler.depth' : 1,
        'crawler.IgnoreRobots' : 'true'
        }

def get(value):
    try:
        return options[value]
    except KeyError:
        return ''
