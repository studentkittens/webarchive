#!/usr/bin/env python
# encoding: utf-8
import magic

def get_mime(filepath):
    """@todo: Docstring for get_mime

    :filepath: @todo
    :returns: @todo

    """
    result = "application/octet-stream"
    with magic.Magic(flags=magic.MAGIC_MIME_TYPE) as m:
        result = m.id_filename(filepath)
    return result

    
