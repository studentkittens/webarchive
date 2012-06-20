#!/usr/bin/env python
# encoding: utf-8

"""
Module to repair archive by removing invalid file locks
"""

import os
import glob
import logging
import subprocess

import util.paths as paths
import crawler.git as git


__author__ = 'Christoph Piechula, Christopher Pahl'


def clear_locks():
    """
    Searches for *.lock and tries to remove found filesystem locks
    """
    cmd = 'find {path} -iname "*.lock"'.format(path=paths.get_content_root())
    try:
        locklist = str(subprocess.check_output(cmd, shell=True), 'UTF-8').splitlines()

        if len(locklist) > 0:
            logging.info('Wil remove following files:')
            for lockfile in locklist:
                logging.info('rm -f ' + lockfile)
                os.remove(lockfile)
    except subprocess.CalledProcessError:
        logging.exception('Cannot clear locks')


def sanitize_domain(domain):
    """
    Sanitizes domain by checking out master
    """
    wrapper = git.Git(domain)
    wrapper.checkout('master')


def repair():
    """
    Walks through domain hierarchy invoking repair() and clear_locks()
    """
    try:
        # Make sure all repos are on the most recent one
        # Additional errorchecking might take place here
        domain_patt = os.path.join(paths.get_content_root(), '*')
        for domain in glob.glob(domain_patt):
            logging.info('- Sanitizing: ' + domain)
            sanitize_domain(domain)

        # Remove all .lock files that are left by (very) rough shutdowns
        clear_locks()
    except OSError:
        logging.exception('Unable to repair archive')
