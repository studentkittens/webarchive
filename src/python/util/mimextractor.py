#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christoph Piechula'

import magic


def get_mime(filepath):
    """
    Determinates the mime type of a given file

    :filepath: path to file
    :returns: mime type as string

    """
    result = "application/octet-stream"
    with magic.Magic(flags=magic.MAGIC_MIME_TYPE) as m:
        result = m.id_filename(filepath)
    return result

###########################################################################
#                                unittest                                 #
###########################################################################
