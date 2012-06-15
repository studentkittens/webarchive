#!/usr/bin/env python
# encoding: utf-8

"""
A very useful error handling module to make error handling much more easy.
"""

__author__ = 'Christopher Pahl'


class ShutdownException(Exception):
    """
    Is thrown on shutdown on the crawljob
    """
    pass
