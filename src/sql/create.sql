PRAGMA foreign_keys = ON;
CREATE TABLE mimeType (
	mimeId INTEGER PRIMARY KEY AUTOINCREMENT,
	name TEXT UNIQUE NOT NULL
);
CREATE TABLE domain (
	domainId INTEGER PRIMARY KEY AUTOINCREMENT,
	name TEXT UNIQUE NOT NULL
);
CREATE TABLE metaData ( -- static meta data part
	metaId INTEGER PRIMARY KEY AUTOINCREMENT,
	url TEXT UNIQUE NOT NULL,
	mimeId INTEGER NOT NULL,
	path TEXT UNIQUE NOT NULL,
	FOREIGN KEY (mimeId) REFERENCES mimeType(mimeId)
);
CREATE TABLE commitTag ( -- commit tag on domain scale
	commitId INTEGER PRIMARY KEY AUTOINCREMENT,
	commitTime TEXT NOT NULL, -- date time as "%yyyy-%MM-%ddT%hh:%mm:%ss"
	domainId INTEGER NOT NULL,
	FOREIGN KEY (domainId) REFERENCES domain(domainId)
);
CREATE TABLE history ( -- dynamic meta data changes
	histId INTEGER PRIMARY KEY AUTOINCREMENT,
	metaId INTEGER NOT NULL,
	commitId INTEGER NOT NULL,
	createTime TEXT NOT NULL, -- date time as "%yyyy-%MM-%ddT%hh:%mm:%ss"
	title TEXT, --abstract title according to mimeType
	FOREIGN KEY (metaId) REFERENCES metaData(metaId),
	FOREIGN KEY (commitId) REFERENCES commitTag(commitId)
);

