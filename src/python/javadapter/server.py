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

# Stdlib
import socket
import socketserver
import threading
import sys
import cmd

# Locking
import util.filelock as lock
import util.paths as paths

# Git Interaction
from crawler.git import Git

###########################################################################
#                             Error Handling                              #
###########################################################################

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

###########################################################################
#            Actual Handlers that do something with the input             #
###########################################################################

# The Lock that can be set via lock and unlock
# It is global for sake of simplicity - there may not be more than one
global_lock = None

def lock_domain(domain, lock_timeout = 100, wait = True):
    """
    Implementation for lock/try_lock, very common 
    and therefore in an own function
    """
    global global_lock
    try:
        if global_lock is None:
            global_lock = lock.FileLock(
                    file_name = domain,
                    folder = paths.get_content_root(),
                    timeout = lock_timeout)
        else:
            if global_lock.is_locked and wait is False:
                raise ProtocolError('Already locked.')

        global_lock.acquire()
    except lock.FileLockException as err:
        raise ProtocolError(str(err))
    except OSError as err:
        global_lock = None
        raise ProtocolError(str(err))

def lock_handler(args):
    """
    Lock a domain and wait to a max. time
    of 5 minutes, will return a timeout then

       lock [domain]

       * domain is e.g. www.heise.de
       * Returns nothing (but OK or ACK ...)
    """
    return lock_domain(args[0])

def try_lock_handler(args):
    """
    Lock a domain, but do not wait 

       try_lock [domain]

       * domain is e.g. www.heise.de
       * Returns nothing (but OK or ACK ...)
    """
    return lock_domain(args[0], wait = False)

def unlock_handler(args):
    """
    Unlock a previous lock

       unlock [domain]

       * domain is e.g. www.heise.de
       * Returns nothing (but OK or ACK ...)
    """
    global global_lock

    if global_lock is None:
        raise ProtocolError('No previous lock.')
    else:
        global_lock.release()
        if global_lock.is_locked:
            raise ProtocolError('Unlocking failed.')
        global_lock = None

def checkout_handler(args):
    """
    Checkout a certain branch (usually a commitTag or master)

       checkout [domain] {branch_name}

       * domain is e.g. www.heise.de
       * branch_name the entity to checkout,
                     if omitted only the path is returned
                     and not git work is done

       * Returns: The Path to the checkout'd domain

       Note: You should always checkout master when you're done!
    """
    domain = args[0]

    try: 
        # May raise IndexError
        branch = args[1]
    except IndexError:
        branch = None

    if branch is not None:
        wrapper = Git(domain)
        rcode = wrapper.checkout(branch)
        if rcode is not 0:
            raise ProtocolError('checkout returned {rc}'.format(rc = rcode))

    return paths.get_domain_path(domain) + '\n'

def commit_handler(args):
    """
    Make a commit on a certain domain:
      
       commit [domain] {message}

       * domain is e.g. www.heise.de
       * message is the commit message (optional, 'edit' by default)
       * Returns nothing (but OK or ACK ...)
    """
    domain = args[0]

    try:
        message = args[1]
    except IndexError:
        message = 'edit'

    wrapper = Git(domain)
    rcode = wrapper.commit(message)
    if rcode is not 0:
        raise ProtocolError('commit returned {rc}'.format(rc = rcode))

###########################################################################
#                              Protocolspec                               #
###########################################################################

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
            'takes': 1,
            'func': checkout_handler
            },
        'commit': {
            'takes': 1,
            'func': commit_handler
            }
        }

###########################################################################
#                              Actual Server                              #
###########################################################################

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
            if len(self.__arg) < handler['takes']:
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
            try:
                # Convert input to a list of trimmed strings
                line = [str(x, 'UTF-8') 
                        for x in self.rfile.readline().split()]
            except UnicodeDecodeError:
                line = ['<invalid_utf8>']

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

###########################################################################
#                           Testing / Starting                            #
###########################################################################

if __name__ == "__main__":
    class ServerShell(cmd.Cmd):
        intro = 'Javadapter Shell: Type help or ? to list commands\nUse Ctrl-P and Ctrl-N to repeat the last commands'
        prompt = '>>> '

        def do_quit(self, arg):
            'Quits the server'
            return True

        def do_EOF(self, arg):
            return True

    def main():
        """
        main() for cmd use, pass a port as only arg
        """
        if len(sys.argv) < 2:
            print('usage: {prog} port'.format(prog = sys.argv[0]))
            sys.exit(-1)
        try:
            server = start('localhost', int(sys.argv[1]))
            ServerShell().cmdloop()
            server.shutdown()
        except lock.FileLockException:
            print('Server seems to be running already!')
            print('Remove /your/archive/javadapter.lock if you')
            print('are sure that it is not.')
        except KeyboardInterrupt:
            print('Quitting Server because of Ctrl-C')
        except socket.error as err:
            print(str(err))
            print('Wait 30 seconds or change your port')
    main()
