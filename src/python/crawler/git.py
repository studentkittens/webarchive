#!/usr/bin/env python
# encoding: utf-8
import subprocess
import os
import logging
import util.paths as paths

__author__ = 'Christopher Pahl'

class Git(object):
    """A (overly-simple) Wrapper for the git binary"""
    def __init__(self, domain):
        """Instance a new Git Wrapper for a certain domain

        :param domain: A domain found in the archive
        """
        self.__domain = paths.get_domain_path(domain)
        self.__gitdir = os.path.join(self.__domain, '.git')
        self.__empty  = os.path.join(self.__domain, 'empty_file')
        self.__basecmd = 'git --git-dir {git_dir} --work-tree {git_cwd} '.format(
                git_dir = self.__gitdir,
                git_cwd = self.__domain)

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

        logging.debug('Executing:\n%s' % command)

        proc = subprocess.Popen(command, shell=True,
                stdout = subprocess.PIPE, stderr = subprocess.PIPE)

        out, err = proc.communicate()
        rc = proc.poll()

        if rc is not 0:
            logging.warn('Previous git command returned nonzero-returncode! {}'.format(err))

        return rc 

    def init(self):
        """
        Create a new archive at specified domain path
        
        The target directory does not need to exit yet 

        :returns: 0 on success, another rc on failure
        """
        rc = 0

        if not os.path.exists(self.__gitdir):
            rc = self.__call_script("""
                init . 
                checkout -b 'empty'
                """)

            if rc == 0:
                # Create a dummy empty file (needed to add a commit)
                with open(self.__empty, 'w') as dummy:
                    dummy.write('Dummy File on default empty branch - do not delete!')

                rc = self.__call_script("""
                    add empty_file
                    commit -am 'Initialiazed'
                    checkout -b master
                    """)
            else:
                rc = -2
        else:
            rc = -1

        return rc

    def checkout(self, target = 'master'):
        """
        checkout a certain point (tag, branch or commit)

        :target: the target to visit
        :returns: 0 on success, another rc on failure
        """
        return self.__call_script('checkout {}'.format(target))

    def branch(self, branch_name = 'empty'):
        """
        create a new named branch

        :branch_name: the name of the new branch, may not exist yet
        :returns: 0 on success, another rc on failure
        """
        rc = self.__call_script('checkout -fb {}'.format(
            Git.convert_branch_name(branch_name)))

        # remove old emptyfile
        if rc == 0:
            try:
                os.remove(self.__empty)
            except OSError as err:
                # TODO: Log err
                print('Cannot delete empty_file:', err)

        return rc

    def commit(self, message = 'edit'):
        """
        commit any changes made 

        git add . and git commit -am <message> is done

        :message: The commit message
        :returns: 0 on success, another rc on failure
        """
        return self.__call_script("""
                   add {path} &&
                   commit -am '{msg}'""".format(
                       path = self.__domain,
                       msg  = message))

    def recreate_master(self):
        return self.__call_script("""
                branch -D master
                checkout -b master
                """)

if __name__ == '__main__':
    import unittest

    TEST_DIR = '/tmp/git_test/'
    class GitTest(unittest.TestCase):
        def setUp(self):
            self.__repo = Git(TEST_DIR)
            self.__repo.init()

        def add_file(self, name, mode):
            with open(os.path.join(TEST_DIR, name), mode) as dummy:
                dummy.write('Hello Kitteh!')

        def test_commit(self):
            self.add_file('file', 'w')
            self.assertEqual(self.__repo.commit(message = 'init'), 0)

        def test_branch(self):
            # make empty master
            self.add_file('empty', 'w')
            self.assertEqual(self.__repo.commit(), 0)

            # branch to a new branch and commit something
            self.assertEqual(self.__repo.branch('2405TT2323'), 0)
            self.add_file('file_new', 'w')
            self.assertEqual(self.__repo.commit(), 0)

            # change back to master
            self.assertEqual(self.__repo.checkout(), 0)
            self.add_file('file_new', 'w')
            self.assertEqual(self.__repo.commit(), 0)
            
        def tearDown(self):
            subprocess.call(['rm', '-rf', TEST_DIR])
            pass
            
    unittest.main()
