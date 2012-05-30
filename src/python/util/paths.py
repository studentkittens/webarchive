#!/usr/bin/env python
# encoding: utf-8

import os
import config.reader as config

def get_domain_path(domain):
    """
    creates domain path from archive path an given domain

    :domain: domain to which you want to get a path
    :returns: domain path as string

    """
    return os.path.join(config.get('general.root'),'content',domain)


if __name__ == '__main__':
    print(get_domain_path('www.heise.de'))
