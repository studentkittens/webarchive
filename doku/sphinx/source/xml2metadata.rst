Translation between XML and internal Metadata
=============================================

Overview
--------

In the Backend, a dictionary with the same keys as in the XML is used.

For example, this XML File:

.. code-block:: xml

  <?xml version="1.0" encoding="UTF-8"?>
  <wa:file
      xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
      xmlns:wa='http://www.hof-university.de/webarchive'
      xsi:schemaLocation='http://www.hof-university.de/webarchive file:/home/ccwelich/git/webarchive/xml/file.xsd'>
      <wa:meta
          wa:url="blog.fefe.de/faq.html/"
          wa:mimeType="text/html"
          wa:path="/tmp/archive/tmp/blog.fefe.de/blog.fefe.de/faq.html/data"
          wa:createTime="2012-06-15T23:04:50"
          wa:title="Fefe Blog FAQ"
      >
          <wa:commitTag
              wa:commitTime="2012-06-15T23:04:50"
              wa:domain="blog.fefe.de"
          />
      </wa:meta>
      <wa:data>
      </wa:data>
  </wa:file>


... is translated to this:

.. code-block:: python

 {'commitTime': '2012-06-15T23:04:50',
  'createTime': '2012-06-15T23:04:50',
  'domain': 'blog.fefe.de',
  'mimeType': 'text/html',
  'path': '/tmp/archive/tmp/blog.fefe.de/blog.fefe.de/faq.html/data',
  'title': 'Fefe Blog FAQ',
  'url': 'blog.fefe.de/faq.html/'}


Implementation
--------------

**Reading:**

.. autoclass:: crawler.xmlreader.XmlReader
  :members:

----------

**Writing:**

.. autoclass:: crawler.xmlgen.XmlGenerator
  :members:
