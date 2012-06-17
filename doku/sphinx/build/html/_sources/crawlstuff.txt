Crawljob and Crawerlmanager
============================

Overview
--------
Both CrawlJob and CrawlerManager are internal modules that encapsulate the
whole crawling procedure. The CrawlJob is a job that starts with crawling and
ends with database insert/update, hence it handles all the needed stuff from
downloading to archiving. 
The CrawlerManager just manages a threadpool with those jobs and limits the
maximum number of parallel running jobs at a time. For more details see
implementation.
    
Implementation
--------------

**Internal usage of the Crawljob:**

.. autoclass:: cmanager.crawlmanager.CrawlerManager
  :members:



**Internal usage of the Crawlmanager:**

.. autoclass:: crawler.crawljob.CrawlJob 
  :members:
