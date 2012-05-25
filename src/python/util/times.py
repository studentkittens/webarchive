#!/usr/bin/env python
# encoding: utf-8

import time
import os

def get_ctime(file_path):
    """
    gets ISO 8601 formated timestamp (create time) of a given file

    :file_path: file of which you want to get ctime
    :returns: ISO 8601 formated timestamp as string

    """
    return time.strftime("%Y-%m-%dT%H:%M:%S",
                        time.localtime(
                        os.path.getctime(file_path)))

def get_sys_time():
    """@
    localtime as ISO 8601 timestamp
    :returns: ISO 8601 formated timestamp as string

    """
    return time.strftime("%Y-%m-%dT%H:%M:%S",
                        time.localtime())

if __name__ == '__main__':
    print("times {0} und {1}", get_ctime('/dev/null'), get_sys_time())
