#!/usr/bin/env python
# encoding: utf-8
import subprocess
import os
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
        self.__basecmd = 'git --git-dir {git_dir} --work-tree {git_cwd} '.format(
                git_dir = self.__gitdir,
                git_cwd = self.__domain)

    def __call(self, command):
        """
        Execute a git script

        Prepend the basecmd to each line in command,
        and subprocess.call() the resulting string synchronously

        :param command: a multiline git script
        :param forkShell: call subprocess.call with shell=True
        :returns: the returncode of the last command in line
        """
        command = '\n'.join([self.__basecmd + line.strip()
                            for line in command.splitlines()
                            if len(line) > 0]) 

        print('\n//////////////////\n')
        print(command)
        return subprocess.call(command, shell=True)

    def init(self):
        """
        Create a new archive at specified domain path
        
        The target directory does not need to exit yet 

        :returns: 0 on success, another rc on failure
        """
        # TODO: Check if already exists
        self.__call("""
            init {domain_path}
            checkout -fb 'empty'
            """.format(domain_path = self.__domain))

        # Create a dummy empty file (needed to add a commit)
        with open(os.path.join(self.__domain, 'empty_file'), 'w') as dummy:
            dummy.write('Dummy File on default empty branch')

        self.__call("""
            add empty_file
            commit -am 'Initialiazed'
            git checkout -b master
            """)

    def checkout(self, target = 'master'):
        """
        checkout a certain point (tag, branch or commit)

        :target: the target to visit
        :returns: 0 on success, another rc on failure
        """
        return self.__call('checkout {}'.format(target))

    def branch(self, branch_name = 'empty'):
        """
        create a new named branch

        :branch_name: the name of the new branch, may not exist yet
        :returns: 0 on success, another rc on failure
        """
        return self.__call('checkout -fb {}'.format(branch_name))

    def commit(self, message = 'edit'):
        """
        commit any changes made 

        git add . and git commit -am <message> is done

        :message: The commit message
        :returns: 0 on success, another rc on failure
        """
        return self.__call("""
                   add {path} &&
                   commit -am '{msg}'""".format(
                       path = self.__domain,
                       msg  = message))

    def recreate_master(self):
        return self.__call("""
                branch -d master
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
