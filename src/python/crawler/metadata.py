#!/usr/bin/env python
# encoding: utf-8
# metadata module
#

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
        return m

    #def __repr__(self):
    #    return 'MetaData'
    
        

if __name__ == '__main__':
    m = MetaData(hello = 'world')
    print(m,m['hello'])

