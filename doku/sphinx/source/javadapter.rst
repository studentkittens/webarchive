Python to Java Interface (aka Javadapter)
=========================================

Description
-----------

The Javadapter is a simple TCPServer that will listen by default on port ``42421`` on ``localhost``.
One may connect to this server and send one of the commands below.
On success the server will send a reponse that is terminated with ``OK``, otherwise
``ACK Some Error Description.`` is send.

The Server may be started via:

.. code-block:: bash

  $ archive.py javadapter --start
  # This will enter a special shell.
  # Use `help` to see what you can do there interactively

List of Protocol-Commands
-------------------------

**lock**
  *description:*
    Lock a domain and wait to a maximal time
    of 5 minutes, will return a timeout then.
  *usage:*
    ``lock [domain]``

    - domain is e.g. www.heise.de
    - Returns nothing
  *examples:*
    .. code-block:: bash 

      $ lock www.heise.de
      OK
      $ lock www.heise.de
      # (... will timeout after 5 mins ...)
      ACK Timeout occured.
      $ unlock www.heise.de
      OK 
      $ lock www.heise.de
      OK

--------

**try_lock**
  *description:*
    As ``lock``, but return immediately with 
    ``ACK Already locked.`` if already locked previously.
  *usage:*
    ``try_lock [domain]``

    - domain is e.g. www.heise.de
    - Returns nothing 
  *examples:*
    .. code-block:: bash 

      $ try_lock www.heise.de
      OK
      $ try_lock www.heise.de
      ACK Already locked.
    
--------

**unlock**
  *description:*
    Unlock a previous lock.
  *usage:*
    ``unlock [domain]``

    - domain is e.g. www.heise.de
    - Returns nothing 
  *examples:*
    .. code-block:: bash 

      $ unlock www.heise.de
      OK
      $ unlock www.youporn.com
      ACK Invalid Domain.

    
--------

**checkout**
  *description:*
    Checkout a certain branch (usually a commitTag or ``master``).
    This does *NOT* lock itself, you always should lock yourself before.
  *usage:*
       ``checkout [domain] {branch_name}``

       - domain is e.g. www.heise.de
       - branch_name the entity to checkout, if omitted only
         the path is returned (if valid) and no git work is done.
       - Returns: The Path to the checkout'd domain
  *warning:*
       **Note:** You should always checkout ``master`` when done!
  *examples:*
    .. code-block:: bash 

      $ checkout www.hack.org 2012-06-15T19:08:15
      /tmp/archive/content/www.hack.org
      OK
      # This domain does not exist (we do not have an adults-filter :-P)
      $ checkout www.youporn.com 
      ACK Invalid Domain.
      $ checkout www.hack.org no_branch_name
      ACK checkout returned 1

--------

**commit**
  *description:*
    Make a commit on a certain domain.
  *usage:*
       ``commit [domain] {message}``

       - domain is e.g. www.heise.de
       - message is the commit message (optional, ``edit`` by default)
       - Returns nothing
  *examples:*
    .. code-block:: bash 

      $ commit www.hack.org HelloWorld
      ACK commit returned 1
      # Uh-Oh, nothing to commit - add some content manually
      user@arc $ touch /tmp/archive/content/www.hack.org/new_file
      # Now commiting works:
      $ commit www.hack.org
      OK


--------

**list_commits:**
  *description:*
    List all commits on a certain domain and its current branch.
  *usage:*
        ``list_commits [domain]``

        - domain is e.g. www.heise.de
        - Returns a newline seperated list of commithashes
  *examples:*
    .. code-block:: bash 

      $ list_commits www.hack.org
      6309b01f5b04b4e60c19f5dd147f935f40d94840
      942f9a1da172592228d22ca638dd3f5ae583d285
      OK

--------

**list_branches:**
  *description:*
    List all branches on a certain domain.
  *usage:*
        ``list_branches [domain]``

        - domain is e.g. www.heise.de
        - Returns a newline seperated list of branchnames 
  *examples:*
    .. code-block:: bash 

       $ list_branches www.hack.org
       2012-06-13T23:02:18
       2012-06-15T19:07:46
       2012-06-15T19:08:15
       2012-06-15T21:57:35
       2012-06-15T21:57:43
       # (..snip..)
       OK


Implementation
--------------

**Actual function to start the server:**

.. autofunction:: archive.javadapter.server.start

---------

**Convienience class to show a servershell:**

.. autoclass:: archive.javadapter.server.ServerShell
  :members:
