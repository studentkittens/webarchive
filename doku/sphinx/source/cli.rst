Commandlineinterface
====================

Quick-Reference
---------------

.. code-block:: bash

    Usage:
    archive.py [--loglevel=<severity>] init [<path>]
    archive.py [--config=<path>] [--loglevel=<severity>] crawler [--start]
    archive.py [--config=<path>] [--loglevel=<severity>] javadapter [--start]
    archive.py [--config=<path>] [--loglevel=<severity>] db (--rebuild|--remove)
    archive.py [--config=<path>] [--loglevel=<severity>] repair
    archive.py [--config=<path>] config (--get=<confurl>|--set=<confurl> <value>|--default=<confurl>)
    archive.py -h | --help
    archive.py --version

    General Options:
    -h --help                Show this screen.
    --version                Show version.
    --loglevel=<loglevel>    Set the loglevel to any of debug, info, warning, error, critical.

    Service Options:
    --start                  Start service automatically and enter shell.

    DB Options:
    --rebuild                Rebuild Databse completely from XML Data.
    --remove                 Remove the Database completely.

    Config Options:
    --config=<path>
    --set=<confurl> <value>  Set a Value in the config permanently.
    --get=<confurl>          Acquire a Value in the config by it's url.
    --default=<confurl>      Acquire the Default-Value of this url.


Additional Notes
----------------

- The Commandline interfaces relies on submodules like *crawler*, *config* ...
- Submodules may have own options
- Before stating the submodule common options may be set (e.g. *--loglevel*)
- The submodules *javadapter* and *crawler* start a special shell
- In order to locate the config you either have to pass it explicitly, or your current working directory is at the archive root.
- URLs are stored in a file like this: 

  .. code-block:: bash

    # Comments go like this
    www.0pointer.de
    www.hack.org
    blog.fefe.de
    #www.stackoverflow.com
    #www.blendpolis.de
    #www.golem.de
    #www.heise.de
  
  The path to this file is found in the config.


Implementation
--------------

.. autoclass:: archive.cli.cmdparser.Cli
   :members:
