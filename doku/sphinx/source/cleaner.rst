Cleaning crawled content
=========================

Overview
--------

The Cleaner submodule ist used to cleanup and normalize crawled content. After
content is fetched by the wget submodule the cleaner module starts its work. It
cleans the downlaoded content by deleting empty files and folders and
normalizes the downloaded contend in the following way:

.. code-block:: python

    """
    Before normalization
    """
    www.heise.de/ 
        index.html
        news/
            newsfeeds.html
            favicon.png
        ...
    


    """
    After normalization
    """
    www.heise.de/ 
        index.html/
            data
        news/
            newsfeeds.html/
                data
            favicon.png/
                data
        ...


The cleaner is collecting metadata in a list during the restructure procedure.
To decide which files to keep, the filter subsystem is used by the celaner.
The metadata object is used to get all meta information needed. Later the
XML-metadata is placed as data.xml inside the content folder, so there is a
unique structure for the complete archive.

Implementation
--------------

**Internal usage of the Cleaner:**

.. autoclass:: archive.crawler.cleaner.Cleaner
  :members:
