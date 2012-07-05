Differences from Spec to actual Program
=======================================

Notes
-----

**Logging:**

  - Own Log has been discarded, Python's ``logging`` is used for compatibility reasons.

**Crawlmanager:**

  - ``reload()`` has been discarded.
  - ``shutdown()`` takes no arguments now. Only hard shutdown possible,
    Soft-Shutdown was moved to Intervalmanager. (Same for CrawlJob)
  - Intervalmanager was not mentioned in Spec - It's a wrapper around CrawlManager.
  - Simple commandline shell added.

**WGET:**

  - ``Wget`` has a ``poll()`` method, that returns it's exit-code, or ``None`` if still running.
  - IRI support needed.

**Rsync:**
  - rsync class has been downstripped to a method; transfer options of rsync
    changed to ``-ac``

**Database:**

  - ``DBGenerator`` does not produce one big blob of SQL, but breaks it up in little pieces. 
    For safety manual locking of the DB is done.
  - DBGen ``commit`` was renamed to ``batch``

**Git:**

- ``Git`` has more methods like ``list_branches`` and ``list_commits`` (which are needed for XML-Recover)

**Recovering:**

- Recovering is also possible with pickled metalists.

**Javadapter:**

  - Javadapter protocol has been a little streamlined (see Javadapter-Section).
  - Javadapter implemented as a threaded TCP/IP server instead of a daemon
  - Simple commandline shell added.

**Init:**

  - folder structure for ``archive init`` was extended (see introduction)
  - No sql subfolder anymore, sql statements were moved directly to backend (crawler/statements.py) and frontend

**Commandline-Interace:**
  - Repair commandline submodule added; simply traverses all branches and checks
    out master, deleting invalid locks

**Misc:**
  - ``XmlGenerator`` takes now a meta object list instead of a single meta
    dictonary; ``write`` method was renamed to ``dump_all``
