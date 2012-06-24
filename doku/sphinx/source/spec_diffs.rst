Differences from Spec to actual Program
=======================================

Notes
-----

- Own Log has been discarded, Python's ``logging`` is used for compatibility reasons.
- Intervalmanager was not mentioned in Spec - It's a wrapper around CrawlManager.
- Crawlmanager:

  - ``reload()`` has been discarded.
  - ``shutdown()`` takes no arguments now. Only hard shutdown possible,
    Soft-Shutdown was moved to Intervalmanager. (Same for CrawlJob)
- ``Wget`` has a ``poll()`` method, that returns it's exit-code, or ``None`` if still running.
- ``DBGenerator`` does not produce one big blob of SQL, but breaks it up in little pieces. 
  For safety manual locking of the DB is done.
- ``Git`` has more methods like ``list_branches`` and ``list_commits`` (which are needed for XML-Recover)
- Recovering is also possible with pickled metalists.
- Javadapter protocol has been a little streamlined.
- No sql subfolder anymore, sql statements were moved directly to backend (crawler/statements.py) and frontend 

