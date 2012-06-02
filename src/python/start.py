#!/usr/bin/env python
# encoding: utf-8

import init.init as init
import logging
import cmanager.intervalmanager as interval

logging.basicConfig(level=logging.WARNING)

init.init_archive('/tmp')

i = interval.IntervalManager()
i.start()
