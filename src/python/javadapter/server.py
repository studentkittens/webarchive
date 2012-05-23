#!/usr/bin/env python
# encoding: utf-8

"""
###########################################################################
#                               Javadapter                                #
###########################################################################

A simple Server with a textprotocoll, representing the API to Java.

TODO: Turn into a Object

Usage: Start with python server.py <port>,
       test with telnet localhost <port>
"""

import socket
import sys

class UnknownCommand(Exception):
    """
    A sendable Exception :-P
    """
    def __init__(self, msg):
        super(UnknownCommand, self).__init__(msg)
        self.failure = b'ACK ' + msg 

def build_error(*message):
    """
    Take an array of errors and turn it into
    a valid error message suitable for sending back
    """
    return bytes('ACK ' + str(message), 'UTF-8')

def handle_command(command, *args):
    """
    Take a command and it's arguments,
    and try to see a meaning in them.

    Returns: A 2 element tuple with a retcode string ('OK' / 'ACK ...'),
             and response message that may be empty
    """
    print('Command received: ', command, '#', args)

    response = b''

    if command == b'checkout':
        response = 'Doing a checkout, Sir.'
    else:
        raise UnknownCommand(b'Unknown command: ' + command)
    return bytes(response, 'UTF-8')

def send(conn, send_bytes):
    """
    Send a command to the clients
    """
    print('Sending:', send_bytes)
    conn.sendall(send_bytes)

def main():
    """
    Needs to be turned into a Javadapter object
    """
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
        sock.bind(('localhost', int(sys.argv[1])))
        sock.listen(1)

        print("Server started on: {addr}".format(addr = sock.getsockname()))

        # Warte auf eine Verbindung zum Server
        conn, (remotehost, remoteport) = sock.accept()
        print('Verbunden mit %s:%s' % (remotehost, remoteport))

        while True:
            cmdlist = [x.strip() 
                      for x in conn.recv(4096).split() 
                      if len(x) > 0]

            if not cmdlist:
                print('Got EOF')
                break

            try:
                message = handle_command(cmdlist[0], *cmdlist[1:]) 
            except UnknownCommand as exc:
                send(conn, exc.failure + b'\n')
            except Exception as exc:
                print('Internal Error:', exc)
                raise exc
            else:
                send(conn, message + b'\nOK\n')

if __name__ == '__main__':
    main()
