#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

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
    """
    localtime as ISO 8601 timestamp
    :returns: ISO 8601 formated timestamp as string
    """
    return time.strftime("%Y-%m-%dT%H:%M:%S",
                          time.localtime())


def get_localtime_sec():
    """
    :returns: localtime in sec from the epoch
    """
    return int(time.strftime("%s", time.localtime()))


def sec_to_timestamp(form, seconds):
    """
    Converts seconds to a timestamp defined by form

    :form: format for timestamp
    :seconds: seconds to convert
    :returns: time as formatted string

    """
    return time.strftime(form.format(time.gmtime(seconds)))


###########################################################################
#                                unittest                                 #
###########################################################################

if __name__ == '__main__':
    print("times {0} und {1}", get_ctime('/dev/null'), get_sys_time())
