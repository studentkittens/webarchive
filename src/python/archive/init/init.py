#!/usr/bin/env python
# encoding: utf-8

"""
Init module to initialize archive parms on first start
"""

import os

import archive.init.default_cfg as default_cfg
import archive.config.handler as config
import archive.init.xsdtemplate as xsd

__author__ = 'Christopher Pahl'

DEFAULT_URLS = """
www.hack.org
www.blendpolis.de
www.golem.de
www.heise.de
www.stackoverflow.com
"""



def init_archive(init_path=os.getcwd()):
    """
    Gets and sets parms at on the first start of archive
    """
    base_path = os.path.abspath(os.path.join(init_path, 'archive'))
    if not os.path.exists(base_path):
        try:
            # Create top direcoty
            os.mkdir(base_path)
            ACTUAL_CONFIG = default_cfg.CONFIG_TEMPLATE.format(
                        archive_path=base_path,
                        filter_path=config.get_default('general.filterpath'),
                        depth=config.get_default('crawler.depth'),
                        interval_in_min=config.get_default('crawler.interval'),
                        max_inst=config.get_default('crawler.maxInst'),
                        user_agent=config.get_default('crawler.userAgent'),
                        temp_dir=config.get_default('crawler.tempRoot'),
                        robots=config.get_default('crawler.ignoreRobots'),
                        url_path=config.get_default('crawler.urllistpath'),
                        custom_wget=config.get_default('crawler.customWgetParms'),
                        db_file=config.get_default('db.path'),
                        sql_source=config.get_default('db.sqlSource'),
                        server_port=config.get_default('server.port'),
                        notify_in_min=config.get_default('server.notify.interval'),
                        javadapter_port=config.get_default('javadapter.port'))
            # Create base structure
            for folder in ['content', 'tmp', 'filter', 'logs', 'pickle_cache', 'xml']:
                os.mkdir(os.path.join(base_path, folder))

            # Default url.txt
            files = [('url.txt', DEFAULT_URLS),
                     (os.path.join('xml', 'file.xsd'), xsd.XSD_TEMPLATE),
                     ('webarchive.conf.xml', ACTUAL_CONFIG)]

            for item in files:
                with open(os.path.join(base_path, item[0]), 'w') as item_handle:
                    item_handle.write(item[1])

            print('Initialized new archive at', base_path)
        except OSError as err:
            print(err)
    else:
        print('Archive-Root', base_path, 'already exists.')


if __name__ == '__main__':
    import sys
    import logging

    def main():
        if len(sys.argv) > 1:
            init_archive(sys.argv[1])
        else:
            logging.warn('Usage: init <folder>')

    main()
