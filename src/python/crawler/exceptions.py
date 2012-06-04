#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christopher Pahl'

class ShutdownException(Exception):
    """
    Is thrown on shutdown on the crawljob
    """
    pass
