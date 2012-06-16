#!/usr/bin/env python
# encoding: utf-8

"""
Metadata object module
"""

__author__ = 'Christoph Piechula'

import util.times as utl
import crawler.extractor as extractor
import util.mimextractor as mime


class MetaData(dict):
    """
    Metadata Builder Class, generates a meta object
    from a given file
    """
    def __init__(self, **kwargs):
        super().__init__(kwargs)

    @classmethod
    def get_domain(cls):
        """
        Splits path to reterive domain
        """
        return cls.__post_path.split('/', 1)[0]

    @classmethod
    def get_url(cls):
        """
        Cuts path to reterive content url
        """
        return cls.__post_path[:-len('data')]

    @classmethod
    def build_metadata_from_file(cls, tmp_crawler_folder, abs_data_path, commitTime):
        """
        Retrieves metadata and returns object

        :abs_data_path: path of content file
        :commitTime: commit time string to tag with
        :tmp_crawler_folder: wget temp folder
        """
        m = MetaData()

        cls.__post_path = abs_data_path[len(tmp_crawler_folder):]
        if cls.__post_path[0] == '/':
            cls.__post_path = cls.__post_path[1:]

        m['mimeType'] = mime.get_mime(abs_data_path)
        m['url'] = cls.get_url()
        m['domain'] = cls.get_domain()
        m['path'] = abs_data_path
        m['createTime'] = utl.get_ctime(abs_data_path)
        m['commitTime'] = commitTime
        m['title'] = extractor.get_title(abs_data_path, m['mimeType'])
        return m

###########################################################################
#                                unittest                                 #
###########################################################################

if __name__ == '__main__':
    m = MetaData.build_metadata_from_file('/home/christoph/',
            '/home/christoph/quickndirty/xml/meta.xml')
    liste = ['url', 'domain', 'path', 'title', 'createtime', 'committime', 'mime']
    for item in liste:
        print(item + ': ' + m[item])
