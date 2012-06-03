#!/usr/bin/env python
# encoding: utf-8

from bs4 import BeautifulSoup

#poor man's implementation of a title extractor module

#globale path exchange variable
__file_path = " "


###########################################################################
#                        concrete implementations                         #
###########################################################################

def html():
    """@todo: Docstring for get_title

    :file_path: @todo
    :mime: @todo
    :returns: @todo

    """
    doc = None
    title = None
    global __file_path
    try:
        with open(__file_path,'r') as f:
            doc = f.read()
        soup = BeautifulSoup(doc)
        title = soup.title.string
    finally:
        return title

###########################################################################
#                             extractors list                             #
###########################################################################

extractors = {
        "text/html" : html
}




def get_title(file_path, mime):
    """@todo: Docstring for get_title

    :file_path: @todo
    :mime: @todo
    :returns: @todo

    """
    global __file_path
    __file_path = file_path

    title = " "
    try:
        title = extractors[mime]()
    finally:
        return str(title)



    
