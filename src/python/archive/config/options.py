#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'


default_options = {
        'general.root': '/tmp/archive/',
        'general.filterpath': 'filter/',
        'crawler.depth': 1,
        'crawler.interval': 1,
        'crawler.maxInst': 10,
        'crawler.userAgent': 'webarchive/0.1 +https://github.com/studentkittens/webarchive',
        'crawler.tempRoot': 'tmp/',
        'crawler.ignoreRobots': 'true',
        'crawler.urllistpath': 'url.txt',
        'crawler.customWgetParms': '',
        'db.path': 'metadata.db',
        'db.sqlSource': 'sql/',
        'server.port': 42420,
        'server.notify.interval': 15,
        'javadapter.port': 42421
        }
