#!/usr/bin/env python
# encoding: utf-8

__author__ 'Christopher Pahl, Christoph Piechula'

"""Archive interface.

Usage:
  archive.py init [<path>]
  archive.py crawler (--start|--stop)
  archive.py javadapter (--start|--stop)
  archive.py db 
  archive.py config
  archive.py -h | --help
  archive.py --version

Options:
  -h --help     Show this screen.
  --version     Show version.
  --start       Starting a service.
  --stop        Stopping a service.

"""
import threading
import logging

from docopt import docopt
from init.init import init_archive

import cmanager.intervalmanager as imgur
import javadapter.server as javadapter

class Cli(object):
    """
    Archive commandline intepreter
    """

    def __init__(self):
        """
        Collected arguments
        """
        logging.basicConfig(level=logging.INFO)

        self.__arguments = docopt(__doc__, version='Archive 1.0')
        submodules = {
                'init': self.handle_init,
                'crawler': self.handle_crawler,
                'javadapter': self.handle_javadapter,
                'db': self.handle_db,
                'config': self.handle_config 
                }

        #iterating through arguments
        for module, handler in submodules.items():
            if self.__arguments[module]:
                handler()

    def not_implemented(self):
        print("No Sam, it won't work - do it yourself.'")
        print('No habla Espanol. Taco.')

    def handle_init(self):
        try:
            path = self.__arguments['<path>']
            init_archive(path)
        except KeyError:
            init_archive()
        
    def server_start(self, interval_manager):
        print('[INTERVAL] Started')
        interval_manager.start()


    def cmd_loop(self, IntervalManager,  i):
        imgur.CrawlerShell().cmdloop() 
        i.shutdown()

    def handle_crawler(self):
        if self.__arguments['--start']:
            i = imgur.IntervalManager()
            threading.Thread(target = self.cmd_loop, args = (self,i,)).start()
            print("\n")
            i.start()
            print('Goodbye')
        elif self.__arguments['--stop']:
            self.not_implemented()


    def handle_javadapter(self):
        if self.__arguments['--start']:
            server = javadapter.start('localhost')
            javadapter.ServerShell().cmdloop()
            server.shutdown()
        elif self.__arguments['--stop']:
            self.not_implemented()

    def handle_db(self):
        pass

    def handle_config(self):
        pass


if __name__ == '__main__':
    c = Cli()
