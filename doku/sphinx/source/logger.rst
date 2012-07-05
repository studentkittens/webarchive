Logging
=======

Overview
--------

Removed by Python Logger

Logs all Warning messages.
All messages are written to Log File.
You can set a loglevel. This Level is only set for shell written logs.
All messages obove loglevel are written to shell.

Additional Notes:

- Possible Log levels: debug, info, warning, error and critical

.. code-block:: 

Implementation
--------------

Setting Severity:

.. autofunction:: archive.logger.loghandler.severity

Setting Logfile:

.. autofunction:: archive.logger.loghandler.file

Acquiring All Logs:

.. autofunction:: archive.logger.loghandler.log_read
