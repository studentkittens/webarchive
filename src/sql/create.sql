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
	mimeId INTEGER NOT NULL,
	domainId INTEGER NOT NULL,
	path TEXT UNIQUE NOT NULL,
	FOREIGN KEY (mimeId) REFERENCES mimeType(mimeId),
	FOREIGN KEY (domainId) REFERENCES domain(domainId)
);
CREATE TABLE history (
	commitTime TEXT NOT NULL,
	metaId INTEGER NOT NULL,
	-- date time as "%yyyy-%MM-%ddT%hh:%mm:%ss"
	createTime TEXT NOT NULL,
	title TEXT, --abstract title according to mimeType
	PRIMARY KEY (commitTime), 
	FOREIGN KEY (metaId) REFERENCES metaData(metaId)
);

