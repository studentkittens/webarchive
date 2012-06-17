Recovering of the Database
==========================

Strategies
----------

Currently, there are two strategies to re-generate the Database:

**Reading all XML Files:**
  With this method the whole archive is traversed like this:

  Iterate over all domains.
    Iterate over all branches of this domain (excluding ``empty`` branch)
      Iterate over all commits (excluding ``Init`` commit)
        Iterate over all XML Files in there and build metadata-dicts from them


  From the generated metadata-list a new Database can be generated.

  *Advantages:*
    
    - Works always, unless the archive is not totally broken
    - Also works for XML-Files that were modified somehow (also their baseattribs shouldn't)
  
  *Disadvantages:*

    - May not be fast enough.

**Using Cached .pickle files:**

  Instead of converting each XML costly to the internal representation, an object dump of the metadata-list
  is written to ``/archive-root/pickle_cache/`` on each crawl-run. If a recover is desired all of these 
  `pickled` lists are joined, and the DB is regnerated.

  .. code-block:: bash

    # Files are named like this:
    # <system-date-on-write>_<uuid>.pickle
    2012-06-15T22:10:29_7cc2292a-80a6-4fcf-98fc-376953b387ca.pickle
    2012-06-15T22:10:41_e2b1ebb2-1b13-4fb4-bd1c-7fe06aff2758.pickle
    2012-06-15T23:04:35_59dc7790-5f65-47af-99fe-099610099ea4.pickle
    2012-06-15T23:04:36_e58bf4c4-2639-4950-a788-6c84e1c4d1a6.pickle
    2012-06-15T23:04:51_360107b5-d946-4c66-95c8-0d6ceb7a8c8a.pickle
    ...

  *Advantages:*

    - Much faster.

  *Disadvantages:*

    - Changes in the internal representation may break things
    - If Base-Attributes of the XML Files are changed manually, they will not be found.


Implementation
--------------

**Actual functions to use:**

.. autofunction:: dbrecover.recover.rebuild

.. autofunction:: dbrecover.recover.remove

.. autofunction:: dbrecover.repair.repair

----------------

.. autoclass:: dbrecover.xml_recover.XMLDBRecover
  :members:

----------------

.. autoclass:: dbrecover.pickle_recover.PickleDBRecover
  :members:
