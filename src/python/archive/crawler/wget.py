#!/usr/bin/env python
# encoding: utf-8

"""
Wget Wrapper submodule used by a crawljob
"""

__author__ = 'Christoph Piechula'

import subprocess
import os
import time
import shlex
import logging

import archive.config.reader as config
import archive.util.files as files
import archive.util.paths as paths


class Wget(object):
    """
    A wget wrapper submodule
    """

    def __init__(self, url, tmp_folder):
        """
        :url: url that will be fetched
        :tmp_folder: folder to save website content
        """
        self.__url = url.strip()
        self.__tmp_folder = tmp_folder
        self.__depth = config.get('crawler.depth')
        self.__robots = "off" if config.get('crawler.ignoreRobots') == "true" else "on"
        self.__user_agent = config.get('crawler.userAgent')
        self.__custom_cmd = config.get('crawler.customWgetParms')
        self.__base = 'wget "{user_agent}" -e robots={rob} -r -l {depth} \
                      --exclude-domains "{ex_domains}" {custom_parms} -P {folder} {url}'
        self.__process = None
        self.__pid = None
        self.__wget_logfile = None

        urlset = files.unique_items_from_file()
        self.__exclude_urls = ', '.join(list(urlset.difference({self.__url})))

    def start(self):
        """
        Starts the wget crawl process
        :returns: wget process exit code
        """

        cmd = self.__base.format(user_agent=self.__user_agent,
                                 rob=self.__robots,
                                 depth=self.__depth,
                                 ex_domains=self.__exclude_urls,
                                 custom_parms=self.__custom_cmd,
                                 folder=self.__tmp_folder,
                                 url=self.__url)

        cmd = shlex.split(cmd)
        wget_path = os.path.join(paths.get_log_dir(), 'wget_' + self.__url + '.log')
        self.__wget_logfile = open(wget_path, 'w')

        self.__process = subprocess.Popen(cmd, shell=False,
                                          bufsize=-1,
                                          stdout=self.__wget_logfile,
                                          stderr=self.__wget_logfile)

        self.__pid = self.__process.pid
        logging.info("[WGET] with pid {0} started.".format(self.__pid))

    def poll(self):
        """
        Polls if process is still running
        :returns: boolean flag if still running or not
        """
        if self.__process is not None:
            return self.__process.poll()
        else:
            return True

    def stop(self):
        """
        Kills a still running wget process
        """
        if self.__process != None:
            try:
                logging.info('[WGET] Stopping process with pid {0}'.format(self.__pid))

                self.__process.terminate()
            finally:
                self.__process = None
                self.__wget_logfile.close()
        else:
            logging.warn("no process running.")

    def __del__(self):
        if self.__wget_logfile is not None:
            self.__wget_logfile.close()
