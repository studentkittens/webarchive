Synchronisation between Temp and Archive
========================================

Overview
--------

Rsync is used with the following parameters:

.. code-block:: bash 

  'rsync -acP <source> <dest>'

For more information see :manpage:`rsync(1)`.

Implementation
--------------

.. autoclass:: crawler.rsync.Rsync
  :members:
