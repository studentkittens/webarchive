Overview over the unittests
============================

Tested Modules
--------------

``config.reader:``
  Test if XML Parsing, getting a defaultvalue, and invalid url works.
``crawler.cleaner:``
  Test if a sample file structure is tranlsated correctly.
``crawler.dbgen:``
  Test if sample-metalist is converted the same way like provided database.
``crawler.extractor:``
  Test with dummy data; check with unix utility ``file`` for match.
``crawler.filter:``
  Test with 2 sample filters, and do harmful things like ``del filter_result``.
``crawler.git:``
  Test various git wrapping.
``crawler.rsync:``
  Test if process was created correctly.
``crawler.xmlgen:``
  Test if valid XML was generated; also for title's like ``&`` (should be escaped with ``&amp``).
``crawler.xmlreader:``
  Test if a sample XML File is transformed correctly to a metadict.
``util:``
  Very simple tests for each function.

Untested Modules
----------------

  - ``cli.cmdparser`` - Hard to test efficiently
  - ``cmanager.crawlmanager`` - Model for Crawljobs
  - ``cmanager.intervalmanager`` - Hard to test efficiently
  - ``crawler.crawljob`` - Just calls other modules
  - ``crawler.wget`` - Tested since 30 years, very thin wrapper
  - ``crawler.metadata`` - Model
  - ``recover`` - Hard to test, only calls dbgen actually
  - ``init`` - Does only a few mkdirs
