#!/usr/bin/env python
# encoding: utf-8

CONFIG_TEMPLATE = """
<webarchive>
    <general>
        <!-- archive root path -->
        <root>{archive_path}</root>
        <!-- archive filter path -->
        <filterpath>{filter_path}</filterpath>
    </general>
    <!-- crawler magic -->
    <crawler>
        <!-- crawler depth  -->
        <depth>{depth}</depth>
        <!-- time interval in minutes for crawler -->
        <interval>{interval_in_min}</interval>
        <!-- max parallel crawler instances -->
        <maxInst>{max_inst}</maxInst>
        <!-- custom useragent -->
        <userAgent>{user_agent}</userAgent>
        <!-- crawler tmp folder-->
        <tempRoot>{temp_dir}</tempRoot>
        <!-- should robots.txt get ignored? -->
        <ignoreRobots>{robots}</ignoreRobots>
        <!-- file with urls you want to crawl -->
        <urllistpath>{url_path}</urllistpath>
        <!-- want some custom wget parms? - just do it -->
        <customWgetParms>{custom_wget}</customWgetParms>
    </crawler>
    <db>
        <!-- place where sqlite db is stored -->
        <path>{db_file}</path>
    </db>
    <server>
        <port>{server_port}</port>
        <notify>
            <interval>{notify_in_min}</interval>
        </notify>
    </server>
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
    <javadapter>
        <port>{javadapter_port}</port>
    </javadapter>
</webarchive>
"""
