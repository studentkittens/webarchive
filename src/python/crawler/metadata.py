#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import util.times as utl
import crawler.extractor as extractor
import util.mimextractor as mime 
import crawler.extractor as extractor

class MetaData(dict):
    """
    Metadata Builder
    """
    def __init__(self,**kwargs):
        super().__init__(kwargs)
    
    @classmethod
    def get_domain(cls):
        """
        splits path to reterive domain
        """
        return cls.__post_path.split('/',1)[0]

    @classmethod
    def get_url(cls):
        """
        cuts path to reterive content url
        """
        return cls.__post_path[:-len('data')]

    @classmethod
    def build_metadata_from_file(cls, tmp_crawler_folder, abs_data_path, commitTime):
        """
        reterives metadata and return object
        :tmp_crawler_folder: expects wget temp folder
        """
        m = MetaData()

        cls.__post_path = abs_data_path[len(tmp_crawler_folder):]
        if cls.__post_path[0] == '/':
            cls.__post_path = cls.__post_path[1:]
        
        m['mimeType'] = mime.get_mime(abs_data_path)
        m['url'] = cls.get_url()
        m['domain'] = cls.get_domain()
        m['abspath'] = abs_data_path
        m['createTime'] = utl.get_ctime(abs_data_path) 
        m['commitTime'] = commitTime
        m['title'] =  extractor.get_title(abs_data_path, m['mimeType'])
        return m


if __name__ == '__main__':
    m = MetaData.build_metadata_from_file('/home/christoph/','/home/christoph/quickndirty/xml/meta.xml')
    liste = ['url','domain','abspath','title','createtime','committime','mime']
    for item in liste:
        print(item + ': ' + m[item])
