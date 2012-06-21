Introduction
============

Welcome. 
--------

This is the documentation of the final Archive-Backend.
It is seperated in External (What you gonna use) and Internal (What we used) documentation.
Both sides try to shed some light onto the internal stuff happening.

Getting Started
---------------

Install the following modules from PyPI:

- ``filemagic`` - Determining MIME Type
- ``docopt`` - Intelligent argument Parsing
- ``beautifulsoup4`` - Title Extraction from HTML

**Listen and Repeat:**

.. code-block:: bash

  $ archive.py init /tmp         # Create a new Archive-Root
  $ cd /tmp/archive/             # cd to it, so it can find the config
  $ archive.py crawler --start   # Start a crawl
  >>> stop
  >>> (Ctrl+D)
  $ ls -l
  -rw-r--r-- webarchive.conf.xml # configuration for this archive-root
  drwxr-xr-x content             # crawled content
  drwxr-xr-x filter              # filters may be placed here
  drwxr-xr-x logs                # logs are stored here
  -rw-r--r-- metadata.db         # SQLite DB 
  drwxr-xr-x pickle_cache        # Additional Cache for metalists, explained later
  drwxr-xr-x tmp                 # tmp-Root
  $ ls -l content/
  drwxr-xr-x blog.fefe.de
  drwxr-xr-x www.0pointer.de
  drwxr-xr-x www.hack.org
  ...
  $ rm metadata.db               # Oops. This was an accident. I swear.
  $ archive.py db --rebuild      # But luckily we can just rebuild it.
