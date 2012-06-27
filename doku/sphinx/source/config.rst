Configuration
=============

Overview
--------

Many details of the archive can be configured in the configuration file,
which is generated on ``archive.py init /your/path`` - so the default-config
is in ``/your/path/webarchive.conf.xml``.  

Additional Notes:

- Inside the Archive, values may be identified by an URL like ``server.notify.interval``
- Except for the ``general.root`` all paths are relative to ``general.root``
- If a value cannot be found a dictionary with default-values is used internally.

.. code-block:: xml 

    <webarchive>
        <general>
            <root>/tmp/archive</root>
            <filterpath>filter/</filterpath>
        </general>
        <!-- modules -->
        <crawler>
            <depth>1</depth>
            <interval>1</interval>
            <maxInst>10</maxInst>
            <userAgent>webarchive/0.1 +https://github.com/studentkittens/webarchive</userAgent>
            <tempRoot>tmp/</tempRoot>
            <ignoreRobots>true</ignoreRobots>
            <urllistpath>url.txt</urllistpath>
            <customWgetParms></customWgetParms>
        </crawler>
        <db>
            <path>metadata.db</path>
        </db>
        <server>
            <port>42420</port>
            <notify>
                <interval>15</interval>
            </notify>
        </server>
        <xml>
            <autoValidatingMode>after update</autoValidatingMode>update
            <schemaPath>xml/file.xsd</schemaPath>
        </xml>
        <javadapter>
            <port>42421</port>
        </javadapter>
    </webarchive>

Implementation
--------------

Acquiring:

.. autofunction:: archive.config.handler.get

Acquiring Defaults:

.. autofunction:: archive.config.handler.get_default

Setting Values:

.. autofunction:: archive.config.handler.set
