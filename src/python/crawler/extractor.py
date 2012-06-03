#!/usr/bin/env python
# encoding: utf-8

from bs4 import BeautifulSoup

#poor man's implementation of a title extractor module

###########################################################################
#                        concrete implementations                         #
###########################################################################

def html(file_path):
    """@todo: Docstring for get_title

    :file_path: path to content
    :returns: extracted title or empty string

    """
    doc = None
    title = " "
    
    try:
        with open(file_path,'r') as f:
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
    """
    returns 'title' for a given file with
    a specific mime type if an extractor isavaiable
    :file_path: path to content file
    :mime: mime type of that file to determinate extractor
    :returns: extracted title as string

    """
    title = " "
    try:
        title = extractors[mime](file_path)
    finally:
        return str(title)



    
