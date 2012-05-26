#!/usr/bin/env python
# encoding: utf-8
# wget module
#
import subprocess 

class Wget(object):
    """
    Simple wget wrapper class
    """

    def __init__(self, url, tmp_folder):
        """
        :url: url that will be fetched
        :tmp_folder: folder to save website content
        """
        self.__url = url
        self.__tmp_folder = tmp_folder
        self.__process = None
        
    def start(self):
        """
        starts the wget crawl process
        :returns: wget process exit code

        """
        self.__process = subprocess.Popen(
                                    ['wget', '-e robots=off', '-rH', '-l 1', '--directory-prefix=', self.__tmp_folder, self.__url],
                                    shell=False)

    def stop(self):
        """
        kills a still running wget process.
        :returns: wget process exit code

        """
        if self.__process != None:
            try:
                self.__process.terminate()
            except:
                out, err = __self.__process.communicate()
                print(out + err)
            finally:
                out, err = self.__process.communicate()
                print(out + err)
        else:
            print("no process running.")


    def __del__(self):
        """
        Exits wget wrapper
        """
        print('wget module exit.')


