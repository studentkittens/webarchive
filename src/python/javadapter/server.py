#!/usr/bin/env python
# -*- encoding: latin1 -*-

# Dieser TCP-Server sendet alle Daten, die er empfängt, an den
# Client als Antwort zurück

from socket import *
import sys

# Der Standard-Echo-Port ist nicht sehr nützlich, da er root
# Rechte erfordert. Wir nehmen den Port 50007
ECHO_PORT = 50007
BUFSIZE = 1024

def main():
    # TCP-Socket erstellen und an Port binden
    with socket(AF_INET, SOCK_STREAM) as s:

        try:
            s.bind(('localhost', int(sys.argv[1])))
            s.listen(1)

            print("Server started on: {addr}".format(addr = s.getsockname()))

            # Warte auf eine Verbindung zum Server
            conn, (remotehost, remoteport) = s.accept()
            print('Verbunden mit %s:%s' % (remotehost, remoteport))

            while True:
                rc = 'OK\n'
                response = ''
                cmdlist = conn.recv(BUFSIZE).rstrip().split()
                print('Got command:', cmdlist)
                if cmdlist[0] == [b'checkout']:
                    response = 'doing a ckout: %s' % cmdlist[1:]
                elif cmdlist[0] == [b'commit']:
                    response = 'doing a commit: %s' % cmdlist[1:]
                else:
                    rc = 'ACK unknown command\n'
                     

                
                if not cmdlist:
                    # Die Verbindung wurde getrennt
                    # Abbruch
                    print('Got EOF')
                    break

                if rc == 'OK':
                    conn.send(bytes('\n'.join((response, rc)), 'UTF-8'))
                else:
                    conn.send(bytes(rc, 'UTF-8'))

        except BaseException as e:
            print('Error:', e)


if __name__ == '__main__':
    main()
