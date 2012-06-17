Filtering crawled content
=========================

Overview
--------

One may filter the crawled data by Python-source files that are placed inside ``/archive-root/filter/``.
These are called *Filters* and are executed once for each file that is supposed to be synched to ther archive.
Filtering is done after collecting all metadata (except the commitTag), so *Filters* may take 
advantage of e.g. the mimeType and other stuff. An example filter may look like this:

.. code-block:: python

  """
  Filter module to demonstrate filter subsystem
  Example: Filtering *.png files by mime type
  """
  __author__ = 'Sam Rockwell'
  
  import re
  if re.match('.*/png', filter_input['mimeType']):
      filter_result = False

There are two global variables defined in the *Filters* (they are not in Standard-Python, that's why you cant execute them directly!):

**filter_input:**
  Input Variable. A dictionary with all metadata collected at this point. 
  You may safely modify it - it's just a copy.

**filter_result:**
  Out Variable. This will be read after executing the filter. 
  If the var was set to **False** the file will be deleted,
  and no other filters will be called. By default this is on **True**.

Implementation
--------------

**Internal usage of the Filtersystem:**

.. autoclass:: crawler.filter.FilterSystem
  :members:
