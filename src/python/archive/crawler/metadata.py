#!/usr/bin/env python
# encoding: utf-8

"""
Metadata object module
"""

__author__ = 'Christoph Piechula'

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
        return post_path[:-len('data')]

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
        m['path'] = abs_data_path
        m['createTime'] = utl.get_ctime(abs_data_path)
        m['commitTime'] = commitTime
        m['title'] = extractor.get_title(abs_data_path, m['mimeType'])

        if not m['url'] in m['path']:
            print('WARNING, url not in path!')
        return m
