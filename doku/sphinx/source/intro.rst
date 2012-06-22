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
- ``docopt`` - Intelligent argument Parsing from Help Text

You can do this via ``pip``, assuming you have Python 3.x installed:

.. code-block:: bash

  sudo pip install filemagic docopt
    

The program will most likely not work for Python 2.x; make sure you have a newer series installed:

.. code-block:: bash

   python --version

Listen and Repeat
-----------------

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

More Notes
----------

Available commands in the crawlershell:

``start``
  Starts the Intervalmanager; does nothing if started already.
  Intervalmanager will start Crawlmanager repeatedly, while not stopped.
``stop``
  Stop Intervalmanager softly - Wait for Crawljobs to finish.
  If you want to stop the Crawljobs immediately: Press ``CTRL-C`` in order to
  shutdown Crawljobs cleanly or let finished Crawljobs sync their data.
  Does nothing if no Intervalmanagers is started.
``quit``
  Quit the Shell (Same as ``CTRL+D``), will call ``stop``!
``status``
  Print current status of Intervalmanager. One of:
    - ``ready`` - waits for you to type ``start``.
    - ``active`` - Crawling, ``stop`` will take effect.
    - ``stop```- Was stopped, will switch to ``ready`` when done.
