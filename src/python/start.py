#!/usr/bin/env python
# encoding: utf-8


import init.init as init
import util.files as utl
import cmanager.crawlmanager as c
import logging

logging.basicConfig(filename='/tmp/archive/archive.log',level=logging.DEBUG)
init.init_archive('/tmp')
cm = c.CrawlerManager(utl.unique_items_from_file('url.txt')) 
cm.start()
