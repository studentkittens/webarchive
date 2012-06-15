#!/usr/bin/env python
# encoding: utf-8

import os
import os.path

import init.default_cfg as default_cfg
import config.reader as config

__author__ = 'Christopher Pahl'


def init_archive(init_path=os.getcwd()):
    base_path = os.path.join(init_path, 'archive')
    if not os.path.exists(base_path):
        try:
            os.mkdir(base_path)

            os.mkdir(os.path.join(base_path, 'content'))
            os.mkdir(os.path.join(base_path, 'tmp'))
            os.mkdir(os.path.join(base_path, 'filter'))
            os.mkdir(os.path.join(base_path, 'pickle_cache'))

            with open(os.path.join(base_path, 'archive.cfg'), 'w') as cfg_handle:
                cfg_handle.write(default_cfg.CONFIG_TEMPLATE.format(
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
                    schema_path=config.get_default('xml.schemaPath'),
                    server_port=config.get_default('server.port'),
                    notify_in_min=config.get_default('server.notify.interval'),
                    javadapter_port=config.get_default('javadapter.port')
                    ))

            print('Initialized new archive at', base_path)
        except OSError as err:
            print(err)
    else:
        print('Archive-Root', base_path, 'already exists.')


if __name__ == '__main__':
    import sys

    def main():
        init_archive(sys.argv[1])

    main()
