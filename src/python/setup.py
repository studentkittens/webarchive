from distutils.core import setup
from shutil import copyfile
import os


if __name__ == '__main__':
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

    # This is a quick hack, as I couldn't find any nicer way
    # Check if we are the root user
    if os.getuid() != 0:
        copyfile('archive.py', '/usr/bin/archive.py')
