#!/usr/bin/env python
# encoding: utf-8


"""Archive interface.

Usage:
  archive.py [--loglevel=<severity>] init [<path>]
  archive.py [--loglevel=<severity>] crawler (--start|--stop)
  archive.py [--loglevel=<severity>] javadapter (--start|--stop)
  archive.py [--loglevel=<severity>] db (--rebuild|--remove)
  archive.py [--loglevel=<severity>] repair
  archive.py config (--get=<confurl>|--set=<confurl><arg>)
  archive.py -h | --help
  archive.py --version

Options:
  -h --help                Show this screen.
  --version                Show version.
  --loglevel=<loglevel>    Set the loglevel to any of debug, info, warning, error, critical.
  --start                  Starting a service.
  --stop                   Stopping a service.
  --rebuild                Rebuild Databse completely from XML Data.
  --remove                 Remove the Database completely.
  --set=<confurl><value>   Set a Value in the config permanently.
  --get=<confurl>          Acquire a Value in the config by it's url.

"""

__author__ = 'Christopher Pahl, Christoph Piechula'

import threading
import logging
import sys

# External dep.
# pip install docopt
from docopt import docopt

from init.init import init_archive
from dbrecover.recover import rebuild, remove
from dbrecover.repair import repair

import cmanager.intervalmanager as imgur
import javadapter.server as javadapter
import config.reader as config
import util.filelock as lock


LOCKFILE = 'global'


class Cli(object):
    """
    Archive commandline intepreter
    """

    def __init__(self):
        """
        Collected arguments
        """
        self.__filelock = lock.FileLock(LOCKFILE, folder=config.get('general.root'), timeout=1)
        self.__arguments = docopt(__doc__, version='Archive 1.0')
        submodules = {
                'init': self.handle_init,
                'crawler': self.handle_crawler,
                'javadapter': self.handle_javadapter,
                'db': self.handle_db,
                'config': self.handle_config,
                'repair': self.handle_repair
                }

        try:
            loglevel = self.__arguments['<severity>'].upper()
            severity = getattr(logging, loglevel)
        except KeyError:
            severity = logging.INFO
        except AttributeError:
            print('Error: \"loglevel\" is not a valid severity level')
            print(__doc__)
            sys.exit(-1)

        logging.basicConfig(level=severity,
                            format='%(asctime)s - %(levelname)s - %(message)s')

        #iterating through arguments
        for module, handler in submodules.items():
            if self.__arguments[module]:
                try:
                    handler()
                except lock.FileLockException:
                    print("archive is currently locked with global.lock.")
                    sys.exit(0)

    def not_implemented(self):
        raise NotImplementedError('It\'s not implemented, Sam.')

    def handle_init(self):
        try:
            path = self.__arguments['<path>']
            init_archive(path)
        except KeyError:
            init_archive()

    def cmd_loop(self,  i, cv):
        shell = imgur.CrawlerShell()
        shell.set_imanager(i, cv)
        print('')
        shell.cmdloop()
        i.stop()

    def handle_crawler(self):
        if self.__arguments['--start']:
            self.__filelock.acquire()
            cv = threading.Condition()
            i = imgur.IntervalManager()
            cmd_thread = threading.Thread(target=self.cmd_loop, args=(i, cv))
            cmd_thread.start()

            while i.status != 'quit':
                cv.acquire()
                cv.wait()
                if i.status == 'nocrawl':
                    print('=========== START ==============')
                    i.start()
                cv.release()
            cmd_thread.join()

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
            self.__filelock.acquire()
            rebuild()
        elif self.__arguments['--remove']:
            try:
                self.__filelock.acquire()
                remove()
                print('Done.')
            except OSError as err:
                print('Unable to delete databse:', err)

    def handle_config(self):
        if self.__arguments['--get']:
            print(config.get(self.__arguments['<confurl>']))
        elif self.__arguments['--set']:
            self.not_implemented()  # TODO: Wait for config implementation.

    def handle_repair(self):
        try:
            self.__filelock.acquire()
        except lock.FileLockException:
            print("archive is currently locked with global.lock.")
            sys.exit(0)
        repair()

if __name__ == '__main__':
    c = Cli()
