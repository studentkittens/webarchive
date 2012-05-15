CREATE TABLE mimeType (
	mimeType TEXT UNIQUE NOT NULL
);
CREATE TABLE domain (
	domain TEXT UNIQUE NOT NULL
);
CREATE TABLE metaData (
	url TEXT UNIQUE NOT NULL,
	mimeTypeId INTEGER,
	FOREIGN KEY (mimeTypeId) REFERENCES mimeType(rowid),
	title TEXT,
	path TEXT UNIQUE NOT NULL,
	domainId INTEGER,
	FOREIGN KEY (domainId) REFERENCES domainId(rowid),
	-- date time as "%yyyy-%MM-%ddT%hh:%mm:%ss"
	commitTime TEXT NOT NULL,
	createTime TEXT NOT NULL
);
