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
          <interval>0.5</interval>
          <maxInst>10</maxInst>
          <userAgent>webarchive/0.1 +https://github.com/studentkittens/webarchive</userAgent>
          <tempRoot>tmp/</tmpRoot>
          <ignoreRobots>true</ignoreRobots>
          <urllistpath>url.txt</urllistpath>
          <customWgetParms></customWgetParms>
      </crawler>
      <db>
          <path>metadata.db</path>
          <sqlSource>../sql/</sqlSource>
      </db>
      <xml>
          <schemaPath>xml/</schemaPath>
      </xml>
      <server>
          <port>42420</port>
          <notify>
              <interval>15</interval>
          </notify>
      </server>
      <javadapter>
        <port>42421</port>
      </javadapter>
  </webarchive>


Implementation
--------------

.. autofunction:: archive.config.reader.get

.. autofunction:: archive.config.reader.get_default
