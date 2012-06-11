#!/usr/bin/env python
# encoding: utf-8


"""Archive interface.

Usage:
  archive.py init [<path>]
  archive.py crawler (--start|--stop)
  archive.py javadapter (--start|--stop)
  archive.py db (--rebuild|--remove)
  archive.py config (--get|--set) <confurl> [<arg>]
  archive.py -h | --help
  archive.py --version

Options:
  -h --help     Show this screen.
  --version     Show version.
  --start       Starting a service.
  --stop        Stopping a service.
  --rebuild     Rebuild Databse completely from XML Data.
  --remove      Remove the Database completely.
  --set         Set a Value in the config permanently.
  --get         Acquire a Value in the config by it's url.

"""

__author__ = 'Christopher Pahl, Christoph Piechula'

import threading
import logging

# External dep.
# pip install docopt
from docopt import docopt

from init.init import init_archive
from dbrecover.recover import rebuild, remove

import cmanager.intervalmanager as imgur
import javadapter.server as javadapter
import config.reader as config


class Cli(object):
    """
    Archive commandline intepreter
    """

    def __init__(self):
        """
        Collected arguments
        """
        logging.basicConfig(level=logging.INFO,
                            format='%(asctime)s - %(name)s - %(message)s')

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
        raise NotImplementedError('It\'s not implemented, Sam.')

    def handle_init(self):
        try:
            path = self.__arguments['<path>']
            init_archive(path)
        except KeyError:
            init_archive()

    def cmd_loop(self,  i):
        shell = imgur.CrawlerShell()
        shell.set_imanager(i)
        print('')
        shell.cmdloop()
        i.stop()

    def handle_crawler(self):
        if self.__arguments['--start']:
            i = imgur.IntervalManager()
            threading.Thread(target=self.cmd_loop, args=(i, )).start()
            i.start()
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
        if self.__arguments['--rebuild']:
            rebuild()
        elif self.__arguments['--remove']:
            try:
                remove()
                print('Done.')
            except OSError as err:
                print('Unable to delete databse:', err)

    def handle_config(self):
        if self.__arguments['--get']:
            print(config.get(self.__arguments['<confurl>']))
        elif self.__arguments['--set']:
            self.not_implemented()  # TODO: Wait for config implementation.


if __name__ == '__main__':
    c = Cli()
