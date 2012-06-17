Database Generation
===================

Overview
--------

On the very end of every run of the crawler an update is done on the database, by 
iterating over all data in the internal metadata-list and builiding SQL Statements from this.

Insertion, for every:

1. ... domain a new row is inserted to the *domain* table. (Already existent domains are ignored)
2. ... mimeType a new row is inserted to the *mimeType* table.
3. ... url and path a new row is inserted to the *metaData* table.
4. ... commit a new row is inserted to the *commitTag* table, with a reference to the corresponding domain.
5. ... new file committed to the archive a new row is inserted into the *history* table.

If a row with this data already exists it is ignored.


Implementation
--------------

For Peformance-reasons only very simple insert-statements are used in combination
with as simple select statements, instead of insert-statements with sub-selects.

--------------

.. automodule:: crawler.dbgen 
   :members:
