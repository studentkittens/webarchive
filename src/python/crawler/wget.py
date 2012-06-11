#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import subprocess
import os
import time
import shlex
import config.reader as config
import util.files as files
import logging


class Wget(object):
    """
    A simple wget wrapper class.
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

        urlset = files.unique_items_from_file(config.get('crawler.urllistpath'))
        self.__exclude_urls = ', '.join(list(urlset.difference({self.__url})))

    def start(self):
        """
        starts the wget crawl process
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
        wget_path = os.path.join(config.get('general.root'),
                                           'wget_' + self.__url + '.log')
        wget_log = open(wget_path, 'w')

        self.__process = subprocess.Popen(cmd, shell=False,
                                          bufsize=-1,
                                          stdout=wget_log,
                                          stderr=wget_log)

        self.__pid = self.__process.pid
        logging.info("[WGET] with pid {0} started.".format(self.__pid))

    def poll(self):
        if self.__process is not None:
            return self.__process.poll()
        else:
            return True

    def stop(self):
        """
        kills a still running wget process.
        """
        if self.__process != None:
            try:
                logging.info('[WGET] Stopping process with pid {0}'.format(self.__pid))

                self.__process.terminate()
            finally:
                self.__process = None
        else:
            logging.warn("no process running.")


###########################################################################
#                                unittest                                 #
###########################################################################

if __name__ == '__main__':
    a = Wget('www.heise.de', '.')
    a.start()
    time.sleep(1)
    a.stop()
