Crawling Program (WGET)
=======================

Overview
--------

Wget is used with the following params:

.. code-block:: bash 

    'wget "{user_agent}" \
          -e robots={rob} \
          --recursive \
          -l {depth} \
          --exclude-domains "{ex_domains}" \
          {custom_parms} \
          -P {folder} \
          {url}'

In ``custon_parms`` custom options from the config are inserted.
They may replace options stated **before**. Use at your own risk.

For more information see :manpage:`wget(1)`.

Implementation
--------------

.. autoclass:: crawler.wget.Wget
  :members:
