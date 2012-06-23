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
        data_files=[('/usr/bin', ['archive.py'])],
        url = "www.github.com/studentkittens/webarchive",
        author = "Christopher Pahl, Christoph Piechula",
        author_email = "cpahl@hof-university.de, cpiechula@hof-university.de"
        )
