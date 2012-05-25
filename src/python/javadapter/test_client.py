import unittest
import socket

class TestJavadapter(unittest.TestCase):
    def setUp(self):
        host, port = "localhost", 42424 
        self.__sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.__sock.connect((host,port))

    def tearDown(self):
        self.__sock.close()

    def dialog(self, message):
        sent = bytes(message, 'UTF-8') + b'\n'
        self.__sock.sendall(sent)
        recv = self.__sock.recv(1024)
        print('Sent:', sent)
        print('Recv:', recv)
        print('-----------')

        return recv

    def test_wrong_command(self):
        self.assertEqual(self.dialog('bad'),
                b'ACK Unknown command: bad\n')
    
    def test_bad_argnum(self):
        self.assertEqual(self.dialog('lock'), 
                b'ACK "lock" takes exactly 1 argument(s)\n')
        self.assertEqual(self.dialog('lock a b'), 
                b'ACK "lock" takes exactly 1 argument(s)\n')

def main():
    unittest.main()

if __name__ == '__main__':
    main()
