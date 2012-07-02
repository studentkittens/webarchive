#!/usr/bin/env python
# encoding: utf-8


"""Archive interface.

Usage:
  archive.py [--loglevel=<severity>] init [<path>]
  archive.py [--config=<path>] [--loglevel=<severity>] crawler [--start]
  archive.py [--config=<path>] [--loglevel=<severity>] javadapter [--start]
  archive.py [--config=<path>] [--loglevel=<severity>] db (--rebuild|--remove)
  archive.py [--config=<path>] [--loglevel=<severity>] repair
  archive.py [--config=<path>] config (--get=<confurl>|--set=<confurl> <value>|--default=<confurl>)
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
  --config=<path>
  --set=<confurl> <value>  Set a Value in the config permanently.
  --get=<confurl>          Acquire a Value in the config by it's url.
  --default=<confurl>      Acquire the Default-Value of this url.
"""

__author__ = 'Christopher Pahl, Christoph Piechula'

import threading
import logging
import socket
import sys
import os

# External dep.
# pip install docopt
from docopt import docopt

from archive.init.init import init_archive
from archive.dbrecover.recover import rebuild, remove
from archive.dbrecover.repair import repair

import archive.cmanager.intervalmanager as imgur
import archive.javadapter.server as javadapter
import archive.config.handler as config
import archive.util.filelock as lock
import archive.util.paths as paths


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
        self._args = docopt(__doc__, version='Archive 1.0')
        submodules = {
                'init': self.handle_init,
                'crawler': self.handle_crawler,
                'javadapter': self.handle_javadapter,
                'db': self.handle_db,
                'config': self.handle_config,
                'repair': self.handle_repair
                }

        try:
            loglevel = self._args['<severity>'].upper()
            severity = getattr(logging, loglevel)
        except KeyError:
            severity = logging.INFO
        except AttributeError:
            print('Error: \"loglevel\" is not valid severity level')
            print(__doc__)
            sys.exit(-1)

        try:
            logging.basicConfig(level=severity,
                    filename=os.path.join(paths.get_log_dir(), 'archive.log'),
                                format='%(asctime)s - %(levelname)s - %(message)s')
            pass
        except IOError as err:
            # Disable warning for initialization
            if self._args['init'] is False:
                print('Cannot open log - file structure probably does not exist yet:', err)

        # Set up config to another file if desired
        try:
            if self._args['init'] is False and self._args['--config']:
                config.load(os.path.abspath(self._args['--config']))
            elif self._args['init'] is False:
                config.load('webarchive.conf.xml')
        except IOError as err:
            print('FATAL: Unable to locate config:', err)
            sys.exit(-4)

        # iterating through arguments
        for module, handler in submodules.items():
            if self._args[module]:
                try:
                    handler()
                except lock.FileLockException:
                    print("archive is currently locked with global.lock.")
                    sys.exit(0)

    def handle_init(self):
        """
        Initializes archive paths
        """
        path = self._args['<path>']
        if path is not None:
            init_archive(path)
        else:
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
        if self._args['--start']:
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
        if self._args['--start']:
            try:
                server = javadapter.start('localhost')
            except socket.error as err:
                print('Cannot start javadapter:', err)
                sys.exit(-2)

        try:
            javadapter.ServerShell(server_instance=server).cmdloop()
        except KeyboardInterrupt:
            print('Interrupted.')
        finally:
            if server is not None:
                server.shutdown()

    def handle_db(self):
        'Handle "db" submodule'
        if self._args['--rebuild']:
            self.__filelock.acquire()
            rebuild()
        elif self._args['--remove']:
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
        if self._args['--get']:
            print(config.get(self._args['--get']))
        elif self._args['--set']:
            config.set(self._args['--set'], self._args['<value>'])
        elif self._args['--default']:
            print(config.get_default(self._args['--default']))
        elif self._args['--config']:
            config.load(self._args['--config'])

    def handle_log(self):
        if self._args['--severity']:
            log.severity(self._args['--severity'])


    def handle_repair(self):
        """
        Invokes archive rapair tool
        """
        self.__filelock.acquire()
        repair()

if __name__ == '__main__':
    c = Cli()
