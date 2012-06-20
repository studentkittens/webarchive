#!/usr/bin/env python
# encoding: utf-8

"""
Crawljob module, encapsulated "crawling thread" which is managed by
the Crawl Manager
"""

__author__ = 'Christoph Piechula, Christopher Pahl'

import os
import logging
import traceback
import time
import shutil

import config.reader as config
import crawler.wget as wget
import util.paths as paths
import crawler.cleaner as cleaner
import crawler.xmlgen as xmlgen
import util.filelock as lock
import crawler.git as git
import crawler.exceptions
import crawler.dbgen

from dbrecover.pickle_recover import PickleDBRecover
from crawler.rsync import rsync


class CrawlJob(object):
    """
    A CrawlJob Process that starts with crawling
    and ends with db commit.

    """
    def __init__(self, ident, url):
        self.__path = os.path.join(paths.get_temp_root(), url)
        self.__metalist = None
        self.__url = url
        self.__ident = ident
        self.__shutdown = False

    def shutdown(self):
        """
        Shutdown flag for 'hard'-way shutdown
        """
        self.__shutdown = True

    def run(self):
        """
        Starts the Crawljob procedure
        """
        try:
            os.mkdir(self.__path)
        except OSError:
            pass

        try:
            logging.info('--> Crawling')
            self.start_crawl()
            logging.info('--> Cleaning')
            self.start_clean()
            logging.info('--> Gen XML')
            self.start_xml_gen()
            logging.info('--> Rsyncing')
            self.start_sync()
            logging.info('--> Gen DB')
            self.start_dbgen()
            logging.info('--> Caching metalist')
            rec = PickleDBRecover()
            rec.save(self.__metalist)
            logging.info('--> Done')
        except crawler.exceptions.ShutdownException:
            logging.info('Job #{cid} ({curl}) stopped.'
                  .format(cid=self.__ident, curl=self.__url))
        except:
            traceback.print_exc()
        finally:
            shutil.rmtree(self.__path, ignore_errors=True)

    def start_crawl(self):
        """
        Starts the wget module to download specific website
        """
        wget_proc = wget.Wget(self.__url, os.path.abspath(self.__path))
        wget_proc.start()

        # Check in a polling loop for the termination
        # of wget - Or shutdown ealry if desired
        while wget_proc.poll() is None:
            time.sleep(0.1)

            # shutdown() was called
            if self.__shutdown is True:
                logging.info('Stopping wget')
                wget_proc.stop()
                raise crawler.exceptions.ShutdownException()

    def start_clean(self):
        """
        Starts the cleaning procedure which kills empty files, dirs
        and restructures the complete dir hierarchy downloaded by a crawljob
        """
        cleaner_proc = cleaner.Cleaner(self.__path)
        cleaner_proc.clean_empty()
        cleaner_proc.restructure()
        cleaner_proc.clean_empty()
        self.__metalist = cleaner_proc.meta_list

    def start_xml_gen(self):
        """
        Dumps 'in-memory' metadata list to xml files on disk
        """
        xml_proc = xmlgen.XmlGenerator(self.__metalist)
        xml_proc.dump_all()

    def start_sync(self):
        """
        Starts rsync procedure (rsync submodule) to mirror temporaray source
        to destination -> content archive path
        """
        content_path = paths.get_content_root()
        itemlist = os.listdir(self.__path)

        for domain in itemlist:
            domain_path = paths.get_domain_path(domain)
            fsmutex = lock.FileLock(domain, folder=content_path)
            fsmutex.acquire()

            try:
                logging.debug('Creating directory: ' + domain_path)
                os.mkdir(domain_path)
            except OSError:
                # This is expected
                # (I swear)
                pass

            git_proc = git.Git(domain)
            git_proc.init()
            git_proc.checkout('empty')
            git_proc.branch(self.__metalist[0]['commitTime'])

            rsync(os.path.join(self.__path, domain), content_path)

            git_proc.commit('Site {domain_name} was crawled.'
                            .format(domain_name=domain))
            git_proc.recreate_master()
            fsmutex.release()

    def start_dbgen(self):
        """
        Invokes DB generator submodule to create db statements from
        metalist and 'commiting' to db
        """
        db = crawler.dbgen.DBGenerator(self.__metalist)
        db.batch()
        db.close()

###########################################################################
#                                unittest                                 #
###########################################################################

if __name__ == '__main__':
    c = CrawlJob(3, 'www.nullcat.de')
