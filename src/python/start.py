#!/usr/bin/env python
# encoding: utf-8


import init.init as init
import logging
import cmanager.intervalmanager as interval

logging.basicConfig(filename='/tmp/archive/archive.log',level=logging.DEBUG)

init.init_archive('/tmp')

i = interval.IntervalManager()
i.start()
