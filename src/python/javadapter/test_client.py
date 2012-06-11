#!/usr/bin/env python
# encoding: utf-8

__author__ = 'Christopher Pahl'

import unittest
import socket


class TestJavadapter(unittest.TestCase):
    def setUp(self):
        host, port = "localhost", 42424
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

    def test_wrong_command(self):
        self.assertEqual(self.dialog('bad'),
                b'ACK Unknown command: bad\n')

    def test_bad_argnum(self):
        self.assertEqual(self.dialog('lock'),
                b'ACK "lock" takes exactly 1 argument(s)\n')
        self.assertEqual(self.dialog('checkout a b c'),
                b'ACK "checkout" takes exactly 2 argument(s)\n')

    def test_multiline(self):
        self.assertEqual(self.dialog('checkout heise.de now'),
                b'heise.de\nnow\nOK\n')

if __name__ == '__main__':
    unittest.main()
