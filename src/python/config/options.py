#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Florian Bauer'


default_options = {
        'general.root': '/tmp/archive/',
        'general.filterpath': 'filter/',
        'crawler.depth': 1,
        'crawler.interval': 0.5,
        'crawler.maxInst': 10,
        'crawler.userAgent': 'Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3) Gecko/20040924 Epiphany/1.4.4 (Ubuntu)',
        #'webarchive/0.1 +https://github.com/studentkittens/webarchive',
        'crawler.tempRoot': 'tmp/',
        'crawler.ignoreRobots': 'true',
        'crawler.urllistpath': 'url.txt',
        'crawler.customWgetParms': '',
        'db.path': 'metadata.db',
        'db.sqlSource': '../sql/',
        'xml.schemaPath': 'xml/',
        'xml.configPath': '..\\..\\..\\conf\\webarchive.conf.xml',
        'server.port': 42420,
        'server.notify.interval': 15,
        'javadapter.port': 42421
        }


    
