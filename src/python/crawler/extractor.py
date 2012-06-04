#!/usr/bin/env python
# encoding: utf-8

from bs4 import BeautifulSoup


def html(file_path):
    """
    Title extractor for text/html files. 

    :file_path: path to html file
    :returns: extracted title or empty string
    if no title is found

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



# extractor list, please add new extractor 'plugins' to this list
extractors = {
        "text/html" : html
}




# ----------------------------------------------------------------------------|
def get_title(file_path, mime):
    """
    Extractor 'interface' to delegate file by
    mime type
    
    :file_path: path to content file
    :mime: mime type of that file to determinate extractor
    :returns: extracted title as string if extraction is
    successful else an empty string will be returned

    """
    title = " "
    try:
        title = extractors[mime](file_path)
    finally:
        return str(title)



    
