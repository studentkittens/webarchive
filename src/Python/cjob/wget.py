#!/usr/bin/env python
# encoding: utf-8

import subprocess

class Wget(object):
    def __init__(self, url, tmp_folder):
        ret = subprocess.call(['wget','-e robots=off','-rH','-l 1','--directory-prefix=' + tmp_folder,url])
        if ret == 0:
            print('download erfolgreich.')
        else:
            print('error beim kraulen.')

