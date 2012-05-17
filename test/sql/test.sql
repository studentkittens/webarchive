--test values
insert into mimeType(mimeName) values('text/html');
insert into mimeType(mimeName) values('application/pdf');

insert into domain(domainName) values('www.heise.de');
insert into domain(domainName) values('www.wikipedia.de');

insert into commitTag(commitTime, domainId) values('2012-05-15T17:30:00', 1 );
insert into commitTag(commitTime, domainId) values('2012-05-15T17:35:00', 2);
insert into commitTag(commitTime, domainId) values('2012-05-15T18:30:00', 1);

insert into metaData ( url, mimeId, path) 
	values( 'www.heise.de/index.html', 1, 'www.heise.de/index.html');
insert into metaData ( url, mimeId, path) 
	values( 'www.heise.de/a/a.html', 1, 'www.heise.de/a/a.html');
insert into metaData ( url, mimeId, path) 
	values( 'www.heise.de/a/a.pdf', 2, 'www.heise.de/a/a.pdf');
insert into metaData ( url, mimeId, path) 
	values( 'www.wikipedia.de/index.html', 1, 'www.wikipedia.de/index.html');

insert into history(metaId, commitId,  createTime, title) 
	values( 1, 1, '2012-05-15T17:28:42', 'heise online');
insert into history (metaId, commitId, createTime, title)
	values( 2, 1, '2012-05-15T17:29:15', null);
insert into history(metaId, commitId, createTime, title) 
	values( 3, 1, '2012-05-15T17:33:00', null);
insert into history(metaId, commitId, createTime, title) 
	values( 4, 2, '2012-05-15T17:35:27', 'wiki startseite');
insert into history(metaId, commitId, createTime, title) 
	values( 1, 3, '2012-05-15T18:32:42', 'Heise online');


select * from mimeType;
select * from domain;
select * from metaData;
select * from history;

select * from mimeType join (
	select * from metaData join (
		select * from domain join (
			select * from commitTag join history using (commitId)
		) using (domainId)
	) using (metaId)
) using (mimeId);

select * from mimeType join (
	select * from metaData join(
		select * from history where commitId in (1)
	) using (metaId)
) using (mimeId);
