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
import os
import cmd

# Locking
import util.filelock as lock
import util.paths as paths

# Git Interaction
from crawler.git import Git
import config.reader as config

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
        :msg: a string message, describing the error
        """
        super(ProtocolError, self).__init__(msg)
        self.failure = b'ACK ' + bytes(msg, 'UTF-8')

###########################################################################
#            Actual Handlers that do something with the input             #
###########################################################################

# The Lock that can be set via lock and unlock
# It is global for sake of simplicity - there may not be more than one
GLOBAL_LOCK = None


def lock_domain(domain, lock_timeout=300, wait=True):
    """
    Implementation for lock/try_lock, very common
    and therefore in an own function
    """
    global GLOBAL_LOCK
    try:
        if GLOBAL_LOCK is None:
            # Create a new lock
            GLOBAL_LOCK = lock.FileLock(
                    file_name=domain,
                    folder=paths.get_content_root(),
                    timeout=lock_timeout)
        else:
            # We do not want to wait, so raise a ProtocolError immediately
            if GLOBAL_LOCK.is_locked and wait is False:
                raise ProtocolError('Already locked.')

        # Wait.
        GLOBAL_LOCK.acquire()

    # Convert other exceptions to a ProtocolError
    except lock.FileLockException as err:
        raise ProtocolError(str(err))
    except OSError as err:
        GLOBAL_LOCK = None
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
    return lock_domain(args[0], wait=False)


def unlock_handler(args):
    """
    Unlock a previous lock

       unlock [domain]

       * domain is e.g. www.heise.de
       * Returns nothing (but OK or ACK ...)
    """
    global GLOBAL_LOCK

    if GLOBAL_LOCK is None:
        raise ProtocolError('No previous lock.')
    else:
        GLOBAL_LOCK.release()
        if GLOBAL_LOCK.is_locked:
            raise ProtocolError('Unlocking failed.')
        GLOBAL_LOCK = None


def checkout_handler(args):
    """
    Checkout a certain branch (usually a commitTag or master)

       checkout [domain] {branch_name}

       * domain is e.g. www.heise.de
       * branch_name the entity to checkout,
                     if omitted only the path is returned
                     and no git work is done

       * Returns: The Path to the checkout'd domain

       Note: You should always checkout master when you're done!
    """
    domain = args[0]
    domain_path = paths.get_domain_path(domain)

    if os.path.exists(domain_path) is False:
        raise ProtocolError('Invalid Domain.')

    try:
        branch = args[1]
    except IndexError:
        branch = None

    if branch is not None:
        wrapper = Git(domain)

        try_lock_handler([domain])
        rcode = wrapper.checkout(branch)
        unlock_handler([domain])
        if rcode is not 0:
            raise ProtocolError('checkout returned {rc}'.format(rc=rcode))

    return domain_path + '\n'


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
        raise ProtocolError('commit returned {rc}'.format(rc=rcode))


def list_branches_handler(args):
    """
    List all branches on a certain domain:

        list_branches [domain]

        * domain is e.g. www.heise.de
        * Returns a newline seperated list of branchnames,
          excluding 'master' and 'empty'
    """
    domain = args[0]

    branch_list = Git(domain).list_branches()
    if branch_list is not None:
        return '\n'.join(branch_list) + '\n'
    else:
        raise ProtocolError('Invalid Domain.')


def list_commits_handler(args):
    """
    List all commits on a certain domain and its current branch:

        list_commits [domain]

        * domain is e.g. www.heise.de
        * Returns a newline seperated list of commithashes
    """
    domain = args[0]

    cmt_list = Git(domain).list_commits()
    if cmt_list is not None:
        return '\n'.join(cmt_list) + '\n'
    else:
        raise ProtocolError('Invalid Domain.')


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
            },
        'list_branches': {
            'takes': 1,
            'func': list_branches_handler
            },
        'list_commits': {
            'takes': 1,
            'func': list_commits_handler
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
                        cmd=self.__cmd,
                        arg=handler['takes']))

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


def start(host='localhost', port=config.get('javadapter.port')):
    """
    Start the Javadapter server, and exit once done

    :host: the host to start the server on (does anythinh but localhost work?)
    :port: the port on which the server listens on
    :returns: a server, on which shutdown() can be called
    """
    # Spawn a new thread for each connection
    server = ThreadedTCPServer((host, port), AdapterHandler)
    server_thread = threading.Thread(target=server.serve_forever)
    server_thread.daemon = True
    server_thread.start()

    return server


class ServerShell(cmd.Cmd):
    """
    Command shell to manage javadapter
    """
    intro = 'Javadapter Shell: Type help or ? to list commands\nUse Ctrl-P and Ctrl-N to repeat the last commands'
    prompt = '>>> '

    def __init__(self, host='localhost', port='42421', server_instance=None):
        super(ServerShell, self).__init__()
        self.host = host
        self.port = port
        self.server = server_instance

    def do_start(self, arg):
        try:
            self.server = start()
        except socket.error as err:
            print('Cannot start:', err)
            self.server = None

    def do_stop(self, arg):
        if self.server is not None:
            self.server.shutdown()
            self.server = None

    def do_status(self, arg):
        'Print current status of the Server'
        if self.server is not None:
            print('Server listening on', self.host, ':', self.port)
        else:
            print('Server is not running')
        return False

    def do_quit(self, arg):
        'Quits the server'
        self.do_stop(None)
        print('')
        return True

    def do_EOF(self, arg):
        'Shortcut for quit (Press CTRL+D)'
        return self.do_quit(arg)

###########################################################################
#                           Testing / Starting                            #
###########################################################################

if __name__ == "__main__":

    def main():
        """
        main() for cmd use, pass a port as only arg
        """
        if len(sys.argv) < 2:
            print('usage: {prog} port'.format(prog=sys.argv[0]))
            sys.exit(-1)
        try:
            host, port = 'localhost', int(sys.argv[1])
            server = start(host, port)
            ServerShell(host, port).cmdloop()
            server.shutdown()
        except KeyboardInterrupt:
            print('Quitting Server because of Ctrl-C')
        except socket.error as err:
            print(str(err))
            print('Wait 30 seconds or change your port')
    main()
