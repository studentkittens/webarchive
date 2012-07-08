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
            <!-- archive root path -->
            <root>/tmp/archive</root>
            <!-- archive filter path -->
            <filterpath>filter/</filterpath>
        </general>
        <!-- crawler magic -->
        <crawler>
            <!-- crawler depth  -->
            <depth>1</depth>
            <!-- time interval in minutes for crawler -->
            <interval>15</interval>
            <!-- max parallel crawler instances -->
            <maxInst>100</maxInst>
            <!-- custom useragent -->
            <userAgent>webarchive/0.1 +https://github.com/studentkittens/webarchive</userAgent>
            <!-- crawler tmp folder-->
            <tempRoot>tmp/</tempRoot>
            <!-- should robots.txt get ignored? -->
            <ignoreRobots>true</ignoreRobots>
            <!-- file with urls you want to crawl -->
            <urllistpath>url.txt</urllistpath>
            <!-- want some custom wget parms? - just do it -->
            <customWgetParms></customWgetParms>
        </crawler>
        <javadapter>
            <port>42421</port>
        </javadapter>
        <db> 
            <!-- place where sqlite db is stored -->
            <path>metadata.db</path>
        </db>
        <xml>
            <!-- This determines when xml files will be validated.
                 There are following possible values:
                    "never": documents will never be validated
                    "always": documents will be validated after each change and after each built of Dom-object
                    "after update": documents will be validated after changes
                    "after built dom": documents will be validated after each built of the dom
            -->
            <autoValidatingMode>after update</autoValidatingMode>
            <!-- sets the path to the global xsd-schema. Relative paths are located in the general.root path --> 
            <schemaPath>xml/file.xsd</schemaPath>
        </xml>
        <server>
            <port>42420</port>
            <notify>
                <interval>15</interval>
            </notify>
        </server>
    </webarchive>

Implementation
--------------

Acquiring:

.. autofunction:: archive.config.handler.get

Acquiring Defaults:

.. autofunction:: archive.config.handler.get_default

Setting Values:

.. autofunction:: archive.config.handler.set
