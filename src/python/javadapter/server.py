#!/usr/bin/env python
# encoding: utf-8

"""
###########################################################################
#                               Javadapter                                #
###########################################################################

A simple Server with a textprotocol, representing the API to Java.

Usage: Start with python server.py <port>,
       test with telnet localhost <port>, or
       python test_client <port>

       Type 'quit' on the server-cmd to exit
"""

__author__ = 'Christopher Pahl'

import socketserver
import threading
import sys

import util.filelock as lock

global_lock = None

def lock_handler(*args):
    """
    Returns 'true' or 'false'
    """

    return 'true\n'
def try_lock_handler(*args):
    """
    Returns 'true' or 'false'
    """
    return 'false\n'
def unlock_handler(*args):
    """
    Returns 'true' or 'false'
    """
    return 'maybe\n' 
def checkout_handler(*args):
    """
    Returns path to domain
    """
    return '\n'.join(*args) + '\n'
def commit_handler(*args):
    """
    Returns 'true' or 'false'
    """
    return 'sure\n'

# Simple dictionary based description of the protocol
# command
#     => takes: how many arguments to take
#     => func: a handler that does something with the args
#              and return some bytebuffer
PROTOCOL = {
        'lock': {
            'takes': 1,
            'func': lock_handler
            },
        'try_lock': {
            'takes': 1,
            'func': try_lock_handler
            },
        'unlock': {
            'takes': 1,
            'func': unlock_handler
            },
        'checkout': {
            'takes': 2,
            'func': checkout_handler
            },
        'commit': {
            'takes': 2,
            'func': commit_handler
            }
        }

class ProtocolError(Exception):
    """raise a 'ACK cause'

    Simple Exception that offers
    a sendable bytebuffer for errors 
    via self.failure
    """
    def __init__(self, msg):
        """
        :param msg: a string message, describing the error
        """
        super(ProtocolError, self).__init__(msg)
        self.failure = b'ACK ' + bytes(msg, 'UTF-8')

class AdapterHandler(socketserver.StreamRequestHandler):
    """The RequestHandler class for the Javadapter

    It is instantiated once per connection to the server, and must
    override the handle() method to implement communication to the
    client. It is never instantiated explicitely.
    """
    def serve_request(self):
        """Handle a certain command

        Try to see a meaning in a command,
        raise an ProtocolError or return a 
        bytebuffer with a suitable reponse,
        which is ended with a linefeed in any case
        """
        response = b''
        try:
            # Try to get a handler for this command,
            # if not part of the protocol raise a ProtocolError
            # and send an 'ACK ...' back to the caller
            handler = PROTOCOL[self.__cmd]
            if len(self.__arg) != handler['takes']:
                # More error checking might be done here,
                # but currently only the number of args is checked
                raise ProtocolError(
                        '"{cmd}" takes exactly {arg} argument(s)'.format(
                        cmd = self.__cmd,
                        arg = handler['takes']))

            # the handlers simply return strings, but for send()
            # we need bytes. If func() returns a False value, 
            # an empty string is used as response instead
            response = bytes(handler['func'](self.__arg) or '', 'UTF-8')
        except KeyError:
            raise ProtocolError('Unknown command: ' + self.__cmd)
        else:
            return response 

    def handle(self):
        """
        Implemented from RequestHandler
        """
        while True:
            # Convert input to a list of trimmed strings
            line = [str(x, 'UTF-8') 
                    for x in self.rfile.readline().split()]

            # Got EOF, so we better quit
            if len(line) == 0:
                print('Quitting connection "%s" to client' %
                        threading.current_thread().name)
                return

            if len(line) > 0:
                self.__cmd = line[0]
                self.__arg = line[1:]

                # No matter what, something is send always,
                # even it's a ProtocolError
                try:
                    response = self.serve_request()
                    send_data = response + b'OK'
                except ProtocolError as err:
                    send_data = err.failure
                finally:
                    self.wfile.write(send_data + b'\n')


class ThreadedTCPServer(socketserver.ThreadingMixIn, socketserver.TCPServer):
    """
    A 'own' socketserver, with ThreadingMixIn

    Reference: http://docs.python.org/py3k/library/socketserver.html 
    """
    pass

def start(host = 'localhost', port = 42421):
    """
    Start the Javadapter server, and exit once done

    :param host: the host to start the server on (does anythinh but localhost work?)
    :param port: the port on which the server listens on
    :returns: a server, on which shutdown() can be called
    """
    # Spawn a new thread for each connection
    server = ThreadedTCPServer((host, port), AdapterHandler)
    server_thread = threading.Thread(target=server.serve_forever)
    server_thread.daemon = True
    server_thread.start()

    return server   

if __name__ == "__main__":
    def main():
        """
        main() for cmd use, pass a port as only arg
        """
        if len(sys.argv) < 2:
            print('usage: {prog} port'.format(prog = sys.argv[0]))
            sys.exit(-1)
        try:
            server = start('localhost', int(sys.argv[1]))
            while True:
                server_cmd = input('>>> ').strip()
                if server_cmd == 'quit':
                    break
                else:
                    print('Unknown command')
            server.shutdown()
        except KeyboardInterrupt:
            print('Quitting Server because of Ctrl-C')
            sys.exit(0)
    main()
