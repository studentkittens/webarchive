#!/usr/bin/env python
# encoding: utf-8

CONFIG_TEMPLATE = """
<webarchive>
    <general>
        <root>{archive_path}</root>
        <filterpath>{filter_path}</filterpath>
    </general>
    <!-- modules -->
    <crawler>
        <depth>{depth}</depth>
        <interval>{interval_in_min}</interval>
        <maxInst>{max_inst}</maxInst>
        <userAgent>{user_agent}</userAgent>
        <tempRoot>{temp_dir}</tempRoot>
        <ignoreRobots>{robots}</ignoreRobots>
        <urllistpath>{url_path}</urllistpath>
        <customWgetParms>{custom_wget}</customWgetParms>
    </crawler>
    <db>
        <path>{db_file}</path>
        <sqlSource>{sql_source}</sqlSource>
    </db>
    <server>
        <port>{server_port}</port>
        <notify>
            <interval>{notify_in_min}</interval>
        </notify>
    </server>
    <xml>
        <autoValidatingMode>after update</autoValidatingMode>update
        <schemaPath>xml/file.xsd</schemaPath>
    </xml>
    <javadapter>
        <port>{javadapter_port}</port>
    </javadapter>
</webarchive>
"""
