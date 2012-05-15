PRAGMA foreign_keys = ON;
CREATE TABLE mimeType (
	mimeId INTEGER PRIMARY KEY AUTOINCREMENT,
	name TEXT UNIQUE NOT NULL
);
CREATE TABLE domain (
	domainId INTEGER PRIMARY KEY AUTOINCREMENT,
	name TEXT UNIQUE NOT NULL
);
CREATE TABLE metaData (
	metaId INTEGER PRIMARY KEY AUTOINCREMENT,
	url TEXT UNIQUE NOT NULL,
	path TEXT UNIQUE NOT NULL,
	title TEXT,
	mimeId INTEGER NOT NULL,
	domainId INTEGER NOT NULL,
	FOREIGN KEY (mimeId) REFERENCES mimeType(mimeId),
	FOREIGN KEY (domainId) REFERENCES domain(domainId)
);
CREATE TABLE commitTag (
	metaId INTEGER NOT NULL,
	-- date time as "%yyyy-%MM-%ddT%hh:%mm:%ss"
	commitTime TEXT NOT NULL,
	createTime TEXT NOT NULL,
	PRIMARY KEY (commitTime), 
	FOREIGN KEY (metaId) REFERENCES metaData(metaId)
);

