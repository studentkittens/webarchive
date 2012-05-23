#!/usr/bin/env python
# encoding: utf-8
# metadata module
#
import mimetypes as mime
import time
import os

class MetaData(dict):
    def __init__(self,**kwargs):
        super().__init__(kwargs)
    
    @classmethod
    def get_domain(cls):
        return cls.__post_path.split('/',1)[0]

    @classmethod
    def get_url(cls):
        return cls.__post_path[:-len('__data')]

    @classmethod
    def build_metadata_from_file(cls, tmp_folder, file_path):
        m = MetaData()
        cls.__post_path = file_path[len(tmp_folder):]
        if cls.__post_path[0] == '/':
            cls.__post_path = cls.__post_path[1:]

        m['url'] = cls.get_url()
        m['domain'] = cls.get_domain()
        m['abspath'] = file_path
        m['title'] = "not set"
        m['createtime'] = time.strftime("%Y-%m-%dT%H:%M:%S", time.localtime(os.path.getctime(file_path))) 
        m['committime'] = "not set" 
        m['mime'] = mime.guess_type(file_path)[0]
        return m

    #overwrite 'tostring' method
    #def __repr__(self):
    #    return 'MetaData'
    
        

if __name__ == '__main__':
    m = MetaData.build_metadata_from_file('/home/christoph/','/home/christoph/quickndirty/xml/meta.xml')
    liste = ['url','domain','abspath','title','createtime','committime','mime']
    for item in liste:
        print(item + ': ' + m[item])
