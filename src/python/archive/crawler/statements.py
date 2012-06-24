#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christopher Pahl, Christoph Cwelich'


_create_sql = """
PRAGMA foreign_keys = ON;
BEGIN IMMEDIATE;
CREATE TABLE IF NOT EXISTS mimeType (
    mimeId INTEGER PRIMARY KEY AUTOINCREMENT,
    mimeName TEXT UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS domain (
    domainId INTEGER PRIMARY KEY AUTOINCREMENT,
    domainName TEXT UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS metaData ( -- static meta data part
    metaId INTEGER PRIMARY KEY AUTOINCREMENT,
    url TEXT UNIQUE NOT NULL,
    mimeId INTEGER NOT NULL,
    path TEXT UNIQUE NOT NULL,
    FOREIGN KEY (mimeId) REFERENCES mimeType(mimeId)
);
CREATE TABLE IF NOT EXISTS commitTag ( -- commit tag on domain scale
    commitId INTEGER PRIMARY KEY AUTOINCREMENT,
    commitTime TEXT NOT NULL, -- date time as "%yyyy-%MM-%ddT%hh:%mm:%ss"
    domainId INTEGER NOT NULL,
    FOREIGN KEY (domainId) REFERENCES domain(domainId)
);
CREATE TABLE IF NOT EXISTS history ( -- dynamic meta data changes
    metaId INTEGER NOT NULL,
    commitId INTEGER NOT NULL,
    createTime TEXT NOT NULL, -- date time as "%yyyy-%MM-%ddT%hh:%mm:%ss"
    title TEXT, --abstract title according to mimeType
    FOREIGN KEY (metaId) REFERENCES metaData(metaId),
    FOREIGN KEY (commitId) REFERENCES commitTag(commitId)
    PRIMARY KEY (commitId, metaId)
);
COMMIT;
"""

_insert_committag_sql = """
INSERT OR IGNORE INTO commitTag (commitTime, domainId) VALUES (?,?);"""

_insert_domain_sql = """
INSERT OR IGNORE INTO domain (domainName) VALUES (?);"""

_insert_history_sql = """
INSERT OR IGNORE INTO history (metaId, commitId, createTime, title) VALUES (?,?,?,?);"""

_insert_metadata_sql = """
INSERT OR IGNORE INTO metaData (url, mimeId, path) VALUES (?,?,?);"""

_insert_mimetype_sql = """
INSERT OR IGNORE INTO mimeType (mimeName) VALUES (?);"""


# You can use this to
statements = {
    'create'           : _create_sql,
    'insert_committag' : _insert_committag_sql,
    'insert_domain'    : _insert_domain_sql,
    'insert_history'   : _insert_history_sql,
    'insert_metadata'  : _insert_metadata_sql,
    'insert_mimetype'  : _insert_mimetype_sql
}
