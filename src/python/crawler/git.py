#!/usr/bin/env python
# encoding: utf-8

"""
Wrapper for Git

This is highly simplified, and may be replaced
by a faster, native implementation using Dunwhich.
But that's not on the plan due our limited time.

Git commands (init e.g.) are tailored for use
in this archive, less for general use.
"""

import subprocess
import os
import re
import logging
import util.paths as paths
import shlex

__author__ = 'Christopher Pahl'


class Git(object):
    """A (overly-simple) Wrapper for the git binary"""

    def __init__(self, domain):
        """Instance a new Git Wrapper for a certain domain

        :param domain: A domain found in the archive
        """
        self.__domain = paths.get_domain_path(domain)
        self.__gitdir = os.path.join(self.__domain, '.git')
        self.__empty = os.path.join(self.__domain, 'empty_file')

        # Error checking via regex matches
        self.__branch_pattern = re.compile(
                '[0-9]{4}(H[0-9]{2}){2}T[0-9]{2}(C[0-9]{2}){2}$')
        self.__commit_pattern = re.compile('[0-9a-z]{40}$')
        self.__basecmd = 'git --git-dir {gitdir} --work-tree {gitcwd} '.format(
                gitdir=self.__gitdir,
                gitcwd=self.__domain)

    @property
    def domain(self):
        """Return the domain, to which this wrapper belongs"""
        return self.__domain

    @classmethod
    def convert_branch_name(cls, date_string):
        """
        Convert a datestring suitably to a branch name

        Git does not allow special characters such as : or -
        in branchnames for whatever reason

        :param date_string: the string to convert
        :returns: the new, converted string
        """
        return date_string.translate(str.maketrans({':': 'C', '-': 'H'}))

    def __call_script(self, script):
        """
        Execute a git script

        Prepend the basecmd to each line in command,
        and subprocess.call() the resulting string synchronously

        :param command: a multiline git script
        :param forkShell: call subprocess.call with shell=True
        :returns: the returncode of the last command in line
        """
        command = ''
        for line in script.splitlines():
            line = line.strip()
            if len(line) > 0:
                command += ''.join([self.__basecmd, line, '\n'])

        logging.debug('Executing: ' + command)

        proc = subprocess.Popen(command, shell=True,
                stdout=subprocess.PIPE, stderr=subprocess.PIPE)

        # Get stderr output
        err = proc.communicate()[1]
        rcode = proc.poll()

        if rcode is not 0:
            logging.warn('Previous git-cmd failed: {}'.format(err))

        return rcode

    def init(self):
        """
        Create a new archive at specified domain path

        The target directory does not need to exit yet

        :returns: 0 on success, another rcode on failure
        """
        rcode = 0

        if not os.path.exists(self.__gitdir):
            rcode = self.__call_script("""
                init .
                checkout -b 'empty'
                """)

            if rcode == 0:
                # Create a dummy empty file (needed to add a commit)
                with open(self.__empty, 'w') as dummy:
                    dummy.write('Hi, Sam!')

                rcode = self.__call_script("""
                    add empty_file
                    commit -am 'Initialiazed'
                    checkout -b master
                    """)
            else:
                rcode = -2
        else:
            rcode = -1

        return rcode

    def checkout(self, target='master'):
        """
        checkout a certain point (tag, branch or commit)

        :target: the target to visit
        :returns: 0 on success, another rcode on failure
        """
        return self.__call_script('checkout {}'.format(target))

    def branch(self, branch_name='empty'):
        """
        create a new named branch

        :branch_name: the name of the new branch, may not exist yet
        :returns: 0 on success, another rcode on failure
        """
        rcode = self.__call_script('checkout -fb {}'.format(
            Git.convert_branch_name(branch_name)))

        # remove old emptyfile
        if rcode == 0:
            try:
                os.remove(self.__empty)
            except OSError:
                logging.exception('Cannot delete empty_file:')

        return rcode

    def commit(self, message='edit'):
        """
        commit any changes made

        git add . and git commit -am <message> is done

        :message: The commit message
        :returns: 0 on success, another rcode on failure
        """
        return self.__call_script("""
                   add {path} &&
                   commit -am '{msg}'""".format(
                       path=self.__domain,
                       msg=message))

    def recreate_master(self):
        """
        A very special helper.

        It deletes the current master branch,
        and recreates it. So, the master always points
        to the most recently created branch
        """
        return self.__call_script("""
                branch -D master
                checkout -b master
                """)

    def __list_data(self, command):
        """
        Internal helper for list_branches() and list_commits()

        Opens a git command and buffers it's output.
        """
        try:
            popen_command = shlex.split(self.__basecmd + command)
            data_list = subprocess.check_output(popen_command)
            return [str(item, 'ascii') for item in data_list.split()]
        except subprocess.CalledProcessError as err:
            logging.error('Cannot get a list from $({ptype}); \
                           Returncode: {rc}'.format(
                pytype=command,
                rc=err.returncode
                ))
            return None

    def list_branches(self):
        """
        List all branches in this repo, which conform to the 'date'-regex.

        This means, Empty and master branch are not mentioned. If you want
        to checkout those, just checkout 'empty' or 'master'

        :returns: a list of branchestrings
        """
        branch_list = self.__list_data('branch')
        if branch_list is not None:
            return list(filter(self.__branch_pattern.match, branch_list))
        else:
            return None

    def list_commits(self):
        """
        List all commits in this repo and branch

        :returns: a list of commithashestrings
        """
        cmt_list = self.__list_data('--no-pager log --pretty="%h" --no-abbrev')
        if cmt_list is not None:
            return list(filter(self.__commit_pattern.match, cmt_list))
        else:
            return None

###########################################################################
#                                unittests                                #
###########################################################################


if __name__ == '__main__':
    import unittest

    TEST_DIR = '/tmp/git_test/'

    def add_file(name, mode):
        'Util for several testcases'
        with open(os.path.join(TEST_DIR, name), mode) as dummy:
            dummy.write('Hello Kitteh!')

    class GitTest(unittest.TestCase):
        'Very basic testcases for this git wrapper'
        def setUp(self):
            'Set up a dir'
            self.__repo = Git(TEST_DIR)
            self.__repo.init()

        def test_commit(self):
            'Test ability to commit files initially'
            add_file('file', 'w')
            self.assertEqual(self.__repo.commit(message='init'), 0)

        def test_branch(self):
            'test branching of repo'
            # make empty master
            add_file('empty', 'w')
            self.assertEqual(self.__repo.commit(), 0)

            # branch to a new branch and commit something
            self.assertEqual(self.__repo.branch('2405TT2323'), 0)
            add_file('file_new', 'w')
            self.assertEqual(self.__repo.commit(), 0)

            # change back to master
            self.assertEqual(self.__repo.checkout(), 0)
            add_file('file_new', 'w')
            self.assertEqual(self.__repo.commit(), 0)

        def tearDown(self):
            'Clean again'
            subprocess.call(['rm', '-rf', TEST_DIR])

    unittest.main()
