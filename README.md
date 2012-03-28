Einricht-Crashkurs:

Für SSH Auth:

    git clone git@github.com:studentkittens/webarchive.git

Für HTTP Auth:

    git clone https://studentkittens@github.com/studentkittens/webarchive.git

Dann email/name eintragen:

    git config --global user.name "Nick"
    git config --global user.email "vor.nachname@hof-university.de"

...Dann:

    [die Änderungen machen]
    git commit -a -m "Aussagekräftige Zusammenfassung der Änderunge"
    git push
    [Pwd eingeben - bei ssh für den key, oder bei HTTP c1-c3 nach pwd fragen]

Builden mit dem antfile:
    
    ant compile
    ant run

---

Für Import in Eclipse & Netbeans siehe:

[Netbeans HowTo](http://forums.netbeans.org/ptopic13537.html)
[Eclipse HowTo](http://www.vogella.de/articles/ApacheAnt/article.html)

Möge eure IDE gut funktionieren.

Source ist in src/; .jar files werden in build/jar/ abgelegt.
Liste der Targets:

    $ ant -p
    ant clean
    ant run 
    ant doc
    ant test
    ant compile
