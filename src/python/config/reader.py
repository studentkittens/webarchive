#!/usr/bin/env python
# encoding: utf-8


default_options = {
        'general.root': '/tmp/archive/',
        'general.filterpath': 'filter/',
        'crawler.depth': 1,
        'crawler.interval': 0.5,
        'crawler.maxInst': 10,
        'crawler.userAgent': 'webarchive/0.1 +https://github.com/studentkittens/webarchive',
        'crawler.tempRoot': 'tmp/',
        'crawler.ignoreRobots': 'true',
        'crawler.urllistpath': 'url.txt',
        'crawler.customWgetParms': '',
        'db.path': 'metadata.db',
        'db.sqlSource': 'sql/',
        'xml.schemaPath': 'xml/',
        'server.port': 42420,
        'server.notify.interval': 15,
        'javadapter.port': 42421
        }


def get_default(value):
    try:
        return default_options[value]
    except KeyError:
        return ''


def get(value):
    try:
        return default_options[value]
    except KeyError:
        return ''
