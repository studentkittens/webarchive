#!/usr/bin/env python
# encoding: utf-8

import os
import os.path
import default_cfg

__author__ = 'Christopher Pahl'

def init_archive(path):
    base_path = os.path.join(path, 'archive')
    if not os.path.exists(base_path):
        try:
            os.mkdir(base_path)
            os.mkdir(os.path.join(base_path, 'content'))
            with open(os.path.join(base_path, 'archive.cfg'), 'w') as cfg_handle:
                cfg_handle.write(default_cfg.CONFIG_TEMPLATE.format(
                    archive_path = base_path
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
