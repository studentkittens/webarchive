Managing crawl intervals
=========================

Overview
--------

The interval manager is used to manage the crawl intervals by managing
different states of the crawl manager.

.. code-block:: python
    
    """
    Starting the crawling procedure
    """
    archive.py [--loglevel=<severity>] crawler [--start]

After the crawler is started by the commandline interface a command shell will
raise in which you can start, stop, get status of the crawling sub system just
by typing the command (type help for more information). All these operations are
invoked by a crawler shell that controls the interval manager as a result the 
crawl manager is triggered.
    
Implementation
--------------

**Internal usage of the Interval Manager:**

.. autoclass:: cmanager.intervalmanager.IntervalManager
  :members:



**Internal usage of the Crawler Command Shell:**

.. autoclass:: cmanager.intervalmanager.CrawlerShell
  :members:
