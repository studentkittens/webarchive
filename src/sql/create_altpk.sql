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
	url TEXT NOT NULL,
	mimeId INTEGER NOT NULL,
	title TEXT,
	path TEXT UNIQUE NOT NULL,
	domainId INTEGER NOT NULL,
	-- date time as "%yyyy-%MM-%ddT%hh:%mm:%ss"
	commitTime TEXT NOT NULL,
	createTime TEXT NOT NULL,
	PRIMARY KEY (url, commitTime),
	FOREIGN KEY (mimeId) REFERENCES mimeType(mimeId),
	FOREIGN KEY (domainId) REFERENCES domain(domainId)
);
--test values
insert into mimeType(name) values('text/html');
insert into domain(name) values('www.heise.de');
select * from mimeType where mimeId=1;
select * from domain where domainId=1;
insert into metaData (
	url,
	mimeId,
	title,
	path,
	domainId,
	commitTime,
	createTime
)
values(
	'www.heise.de/index.html', 
	1, 
	'Heise', 
	'www.heise.de/index.html', 
	1,        
	'2012-05-15T17:30:00', 
	'2012-05-15T17:28:42'
);
select * from metaData;
insert or replace into metaData (
	url,
	mimeId,
	title,
	path,
	domainId,
	commitTime,
	createTime
)
values(
	'www.heise.de/index.html', 
	1, 
	'Heise', 
	'www.heise.de/index.html', 
	1,        
	'2012-05-15T18:30:00', 
	'2012-05-15T18:28:42'
);
select * from metaData;

