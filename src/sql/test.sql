--test values
insert into mimeType(name) values('text/html');
insert into domain(name) values('www.heise.de');
select * from mimeType;
select * from domain;
insert into metaData (
	url,
	mimeId,
	domainId,
	path
)
values(
	'www.heise.de/index.html', 
	1, 
	1,
	'www.heise.de/index.html'
);
select * from metaData;

insert into history values(
	'2012-05-15T17:30:00', 
	1,
	'2012-05-15T17:28:42',
	'heise'
);
select * from history;
insert into history values(
	'2012-05-15T18:30:00', 
	1, 
	'2012-05-15T18:28:42',
	'Heise'       
);
select * from history;

