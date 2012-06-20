Synchronisation between Temp and Archive
========================================

Overview
--------

Rsync is used with the following parameters:

.. code-block:: bash 

  'rsync -ac <source> <dest>'

For more information see :manpage:`rsync(1)`.

Implementation
--------------

``rsync()`` basically works like on the commandline:

.. autofunction:: archive.crawler.rsync.rsync
