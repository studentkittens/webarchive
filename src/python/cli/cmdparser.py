#!/usr/bin/env python
# encoding: utf-8


"""Archive interface.

Usage:
  archive.py [--loglevel=<severity>] init [<path>]
  archive.py [--loglevel=<severity>] crawler [--start]
  archive.py [--loglevel=<severity>] javadapter [--start]
  archive.py [--loglevel=<severity>] db (--rebuild|--remove)
  archive.py [--loglevel=<severity>] repair
  archive.py config (--get=<confurl>|--set=<confurl><arg>)
  archive.py -h | --help
  archive.py --version

General Options:
  -h --help                Show this screen.
  --version                Show version.
  --loglevel=<loglevel>    Set the loglevel to any of debug, info, warning, error, critical.

Service Options:
  --start                  Start service automatically and enter shell.

DB Options:
  --rebuild                Rebuild Databse completely from XML Data.
  --remove                 Remove the Database completely.

Config Options:
  --set=<confurl><value>   Set a Value in the config permanently.
  --get=<confurl>          Acquire a Value in the config by it's url.

"""

__author__ = 'Christopher Pahl, Christoph Piechula'

import threading
import logging
import sys
import os

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
import util.paths as paths


LOCKFILE = 'global'


class Cli(object):
    """
    Archive commandline intepreter
    """
    def __init__(self):
        """
        Collected arguments
        """
        self.__filelock = lock.FileLock(LOCKFILE, folder=config.get('general.root'), timeout=0.1)
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

        try:
            logging.basicConfig(level=severity,
                                filename=os.path.join(paths.get_log_dir(), 'archive.log'),
                                format='%(asctime)s - %(levelname)s - %(message)s')
        except IOError as err:
            print('Cannot open log - file structure probably does not exist yet:', err)
        else:
            print('Logging will be printed to logfile only.')

        # iterating through arguments
        for module, handler in submodules.items():
            if self.__arguments[module]:
                try:
                    handler()
                except lock.FileLockException:
                    print("archive is currently locked with global.lock.")
                    sys.exit(0)

    def handle_init(self):
        """
        Initializes archive paths
        """
        try:
            path = self.__arguments['<path>']
            init_archive(path)
        except KeyError:
            init_archive()

    def cmd_loop(self, shell,  i, cv):
        """
        The cmdloop runs in a seperate thread.
        """
        shell.cmdloop()
        i.stop()

    def handle_crawler(self):
        """
        Starts and controls crawler commandline
        """
        self.__filelock.acquire()
        cv = threading.Condition()
        im = imgur.IntervalManager()

        do_autostart = False
        if self.__arguments['--start']:
            print('Note: Will start automatically')
            do_autostart = True

        shell = imgur.CrawlerShell(imanager=im, condvar=cv, autostart=do_autostart)
        cmd_thread = threading.Thread(target=self.cmd_loop, args=(shell, im, cv))
        cmd_thread.start()

        cv.acquire()
        while shell.quitflag() is False:
            cv.wait()
            if im.status == 'ready' and shell.quitflag() is False:
                logging.info('=========== START ==============')
                im.start()
                shell.set_activeflag(False)
        cv.release()
        cmd_thread.join()

    def handle_javadapter(self):
        """
        Starts javadapter commandline
        """
        server = None
        if self.__arguments['--start']:
            server = javadapter.start('localhost')

        javadapter.ServerShell(server_instance=server).cmdloop()

        if self.__arguments['--start']:
            server.shutdown()

    def handle_db(self):
        'Handle "db" submodule'
        if self.__arguments['--rebuild']:
            self.__filelock.acquire()
            rebuild()
        elif self.__arguments['--remove']:
            try:
                self.__filelock.acquire()
                remove()
                print('Done.')
            except OSError as err:
                print('Unable to delete database:', err)

    def handle_config(self):
        """
        Invokes Config Handler operations
        """
        if self.__arguments['--get']:
            print(config.get(self.__arguments['<confurl>']))
        elif self.__arguments['--set']:
            pass
            # TODO: Wait for config implementation.

    def handle_repair(self):
        """
        Invokes archive rapair tool
        """
        self.__filelock.acquire()
        repair()

if __name__ == '__main__':
    c = Cli()
