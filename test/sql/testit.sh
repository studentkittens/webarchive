#!/bin/bash
testdb='testdb';
if [ -e $testdb ]; then
	rm $testdb;
fi;
sqlite3 $testdb < ../../src/sql/create.sql;
sqlite3 $testdb < test.sql;
