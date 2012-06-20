from distutils.core import setup

setup(
        name = 'archive',
        version = "1.0",
        packages = ['archive',
                    'archive.cli',
                    'archive.crawler',
                    'archive.cmanager',
                    'archive.config',
                    'archive.util',
                    'archive.javadapter',
                    'archive.dbrecover',
                    'archive.init'
                    ],
        url = "www.github.com/studentkittens/webarchive",
        author = "Christopher Pahl, Christoph Piechula",
        author_email = "cpahl@hof-university.de, cpiechula@hof-university.de"
        )
