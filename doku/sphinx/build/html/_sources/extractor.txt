Extracting data from crawled content
====================================

Overview
--------

One Requirement was to extract the title from HTML Documents. There is no Plugin-System as for the *Filters*,
but one may write an extractor function that will be called for a specific mimeType and register it like this:

.. code-block:: python
  :emphasize-lines: 4 

  # In crawler.extractor.py
  EXTRACTORS = {
        "text/html": extract_html
        "application/pdf": extract_pdf
  } 

Implementation
--------------

.. automodule:: crawler.extractor
  :members:
