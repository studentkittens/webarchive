#!/usr/bin/env python
# encoding: utf-8


import init.init as init
import util.files as utl
import cmanager.crawlmanager as c
import cmanager.intervalmanager as interval
import time

init.init_archive('/tmp')

i = interval.IntervalManager()
i.start()
#cm = c.CrawlerManager(utl.unique_items_from_file('url.txt')) 
#cm.start()
