--test values
insert into mimeType(name) values('text/html');
insert into domain(name) values('www.heise.de');
select * from mimeType;
select * from domain;
insert into metaData (
	url,
	mimeId,
	title,
	path,
	domainId
)
values(
	'www.heise.de/index.html', 
	1, 
	'Heise', 
	'www.heise.de/index.html', 
	1
);
select * from metaData;

insert into commitTag values(
	1,
	'2012-05-15T17:30:00', 
	'2012-05-15T17:28:42'
);
select * from commitTag;
insert into commitTag values(
	1,        
	'2012-05-15T18:30:00', 
	'2012-05-15T18:28:42'
);
select * from commitTag;

