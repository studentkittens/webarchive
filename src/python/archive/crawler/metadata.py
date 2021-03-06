#!/usr/bin/env python
# encoding: utf-8

"""
Metadata object module
"""

__author__ = 'Christoph Piechula'

import os

import archive.util.times as utl
import archive.crawler.extractor as extractor
import archive.util.mimextractor as mime


class MetaData(dict):
    """
    Metadata Builder Class, generates a meta object
    from a given file
    """
    def __init__(self, **kwargs):
        super().__init__(kwargs)

    # Class methods:

    def get_domain_from_path(post_path):
        """
        Splits path to reterive domain
        """
        return post_path.split('/', 1)[0]

    def get_url_from_path(post_path):
        """
        Cuts path to reterive content url
        """
        url_start = post_path[:-len('data')]
        if url_start.endswith('/'):
            url_start = url_start[:-1]
        return url_start

    def get_content_path_from_tmp(tmp_path, domain):
        """
        /tmp/archive/tmp/www.golem.de/www.golem.de/...
        =>
        /tmp/archive/content/www.golem.de/...
        """
        return tmp_path.replace(os.path.join('tmp', domain), 'content')


    def build_metadata_from_file(tmp_crawler_folder, abs_data_path, commitTime):
        """
        Retrieves metadata and returns object
        Note: This needs to be threadsafe code.

        :abs_data_path: path of content file
        :commitTime: commit time string to tag with
        :tmp_crawler_folder: wget temp folder
        """
        m = MetaData()

        post_path = abs_data_path[len(tmp_crawler_folder):]
        if post_path[0] == '/':
            post_path = post_path[1:]

        m['mimeType'] = mime.get_mime(abs_data_path)
        m['url'] = MetaData.get_url_from_path(post_path)
        m['domain'] = MetaData.get_domain_from_path(post_path)
        m['path'] = MetaData.get_content_path_from_tmp(abs_data_path, m['domain'])
        m['tmpPath'] = abs_data_path
        m['createTime'] = utl.get_ctime(abs_data_path)
        m['commitTime'] = commitTime
        m['title'] = extractor.get_title(abs_data_path, m['mimeType'])

        if not m['url'] in m['path']:
            print('WARNING, url not in path!')
        return m
