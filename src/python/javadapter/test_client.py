#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christopher Pahl, Christoph Piechula'

import unittest
import socket
import os

import  util.paths as paths


TESTDOMAIN = 'www.hack.org'


def checkAck(msg):
    # helper for ack
    return msg.startswith(b'ACK')


def checkOk(msg):
    # helper for ok
    return msg.startswith(b'OK')


class TestJavadapter(unittest.TestCase):
    def setUp(self):
        host, port = "localhost", 42421
        self.__sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.__sock.connect((host, port))

    def tearDown(self):
        self.__sock.close()

    def dialog(self, message):
        sent = bytes(message, 'UTF-8') + b'\n'
        self.__sock.sendall(sent)
        recv = self.__sock.recv(1024)
        print('-----------')
        print('Sent:', sent)
        print('Recv:', recv)
        return recv

    def checkout(self, domain, branch):
        # checking out given domain + branch
        # checks with assertEquals for plausibility
        self.assertEqual(bytes(paths.get_domain_path(domain), 'UTF-8') + b'\nOK\n',
             self.dialog('checkout ' + domain + ' ' + branch))

    def commit(self, domain):
        # commit helper
        return self.dialog('commit ' + domain)

    def test_lock(self):
        # checks lock ack
        self.assertTrue(checkAck(self.dialog('lock')))
        # checks lock ok
        self.assertTrue(checkOk(self.dialog('lock ' + TESTDOMAIN)))
        # checks try_lock ack
        self.assertTrue(checkAck(self.dialog('try_lock ' + TESTDOMAIN)))
        # checks unlock ok
        self.assertTrue(checkOk(self.dialog('unlock ' + TESTDOMAIN)))
        # checks unlock ack
        self.assertTrue(checkAck(self.dialog('unlock ' + TESTDOMAIN)))

    def test_checkout(self):
        # returns path of branch
        self.checkout(TESTDOMAIN, '')
        # checks out master branch
        self.checkout(TESTDOMAIN, 'master')
        # checkout non existing branch
        self.assertTrue(checkAck(self.dialog('checkout ' + TESTDOMAIN + ' non_existing')))

    def test_branches(self):
        # gets list with raw branches
        raw_branches = self.dialog('list_branches ' + TESTDOMAIN)
        # checks if there are branches
        self.assertFalse(checkAck(raw_branches))
        # checks out each of given branches
        branches = [str(branch, 'UTF-8') for branch in raw_branches.splitlines()][:-1]
        for branch in branches:
            self.checkout(TESTDOMAIN, branch)
        # return to master
        self.checkout(TESTDOMAIN, 'master')

    def test_commit(self):
        # Test if commit fails without committable data
        self.assertTrue(checkAck(self.commit(TESTDOMAIN)))

        # checkout empty testbranch (doesn't harm)
        self.checkout(TESTDOMAIN, 'empty')

        # Get a commitlist, and check if valid
        commit_list_before = self.dialog('list_commits ' + TESTDOMAIN)
        self.assertFalse(checkAck(commit_list_before))

        # make some committable changes
        with open(os.path.join(paths.get_domain_path(TESTDOMAIN),
            'testfile'), 'a') as test_file:
            test_file.write('hello world\n')

        # check if commit works
        self.assertTrue(checkOk(self.commit(TESTDOMAIN)))

        # after the first commit, another commit shouldn't work anymore
        self.assertTrue(checkAck(self.commit(TESTDOMAIN)))

        # get another list of commits, and check for growth
        commit_list_after = self.dialog('list_commits ' + TESTDOMAIN)
        self.assertFalse(checkAck(commit_list_after))
        self.assertTrue(len(commit_list_after.splitlines()) ==
                len(commit_list_before.splitlines()) + 1)

        # Checkout maste again
        self.checkout(TESTDOMAIN, 'master')


if __name__ == '__main__':
    unittest.main()
