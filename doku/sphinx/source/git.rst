Git-Handling
============

Overview
--------

**Initialization:**

.. code-block:: bash

  $ git init 
  $ git checkout -b empty
  # At least one commit is needed for a valid branch
  $ echo 'This is Empty' > EMPTY
  $ git add EMPTY
  $ git commit -a -m 'Init'
  # master will be used to track
  # the most recent branch
  
**Synchronization:**

.. code-block:: bash

  ... lock ...
  # Go back to 'empty' (otherwise the new branch inherits a bad history)
  $ git checkout empty
  # New branch with the date as tagname 
  $ git checkout -b 24052012T1232
  ... rsync ...
  $ git add . 
  $ git commit -am 'Seite xyz.com wurde gekrault'
  # Make 'master' identical to the most recent branch:
  $ git branch -d master
  $ git checkout -b master
  ... unlock ...
  
**Reading/Writing on most recent data:**

.. code-block:: bash

  # No git-work required
  ... lock ...
  ... read ...
  ... unlock ...

**Reading/Writing from/in old checkouts:**

.. code-block:: bash
    
  ... lock ...
  # Use -f to overwrite unallowed data
  $ git checkout -f old_date
  ... read / write ...
  # In case of writing:
  $ git add .
  $ git commit -am 'Edited old Kraul'
  # HEAD of the new branch points to the new commit
  $ git checkout master
  ... unlock ...
  
**Rough schema as ASCII-Art:**

.. code-block:: bash
  
            -- Kraul1 -> edit <- branch '03052012T1232'
          / 
  Init -- ---- Kraul2 <- branch '15052012T1232'
  |       \
  |         -- Kraul3 <- branch '24052012T1232' <- branch 'master'
  |
  |
  \-> branch 'empty'
  
**Previously, with the ``tag`` approach: (outdated)**

.. code-block:: bash
  
  Kraul1 -> Kraul2 -> Kraul3 -> Kraul4 <- branch 'master'
  |         |         |         |
  |         |         |          \
  |         |         \            -- Tag 04
  |         \           -- Tag 03
  \           -- Tag 02
    -- Tag 01
  
-------------

**Advantages:**
  - Old branches are editable
  - *Check-If-Current* not needded
  - For some strange reason: Less Disk Usage

**Disadvantages:**
  - ``checkout`` may be slower.
  - Traversing over all branches may be harder - but possible.

Implementation
--------------

.. automodule:: crawler.git
    :members:
