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

import socketserver
import threading
import sys

def lock_handler(*args):
    """
    Returns 'true' or 'false'
    """
    return 'true\n'
def try_lock_handler(*args):
    """
    Returns path to domain
    """
    return 'false\n'
def unlock_handler(*args):
    """
    Returns 'true' or 'false'
    """
    return 'maybe\n' 
def checkout_handler(*args):
    """
    Returns 'true' or 'false'
    """
    return 'heise.de\n'
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
    """
    Simple Exception that offers
    a sendable bytebuffer for errors 
    via self.failure
    """
    def __init__(self, msg):
        super(ProtocolError, self).__init__(msg)
        self.failure = b'ACK ' + bytes(msg, 'UTF-8')

class AdapterHandler(socketserver.StreamRequestHandler):
    """
    The RequestHandler class for the Javadapter

    It is instantiated once per connection to the server, and must
    override the handle() method to implement communication to the
    client.
    """
    def serve_request(self):
        """
        Try to see a meaning in a command,
        raise an ProtocolError or return a 
        bytebuffer with a suitable reponse,
        which is ended with a linefeed
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
            response = bytes(handler['func']() or '', 'UTF-8')
        except KeyError:
            raise ProtocolError('Unknown command: ' + self.__cmd)
        else:
            return response 

    def handle(self):
        """
        Implemented from RequestHandler
        """
        # Convert input to a list of trimmed strings
        line = [str(x, 'UTF-8') 
                for x in self.rfile.readline().split()]

        # Got EOF, so we better quit
        #if len(line) == 0:
        #    print('Quitting Server')
        #    return

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

def start(host, port):
    """
    Start the Javadapter server, and exit once done

    :returns: a server, on which shutdown() can be called
    """
    server = ThreadedTCPServer((host, port), AdapterHandler)

    # Start a thread with the server -- that thread will then start one
    # more thread for each request
    server_thread = threading.Thread(target=server.serve_forever)
    # Exit the server thread when the main thread terminates
    server_thread.daemon = True
    server_thread.start()
    print("Server loop running in thread:", server_thread.name)

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
