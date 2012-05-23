#!/usr/bin/env python
# -*- encoding: latin1 -*-

# Dieser TCP-Server sendet alle Daten, die er empfängt, an den
# Client als Antwort zurück

from socket import *

# Der Standard-Echo-Port ist nicht sehr nützlich, da er root
# Rechte erfordert. Wir nehmen den Port 50007
ECHO_PORT = 50007
BUFSIZE = 1024

def main():
    # TCP-Socket erstellen und an Port binden
    with socket(AF_INET, SOCK_STREAM) as s:
        s.bind(('localhost', ECHO_PORT))
        s.listen(1)

        print("Server started on: {addr}".format(addr = s.getsockname()))

        # Warte auf eine Verbindung zum Server
        conn, (remotehost, remoteport) = s.accept()
        print('Verbunden mit %s:%s' % (remotehost, remoteport))

        while 1:
            # Empfange daten vom Client
            response = ''
            cmdlist = conn.recv(BUFSIZE).rstrip().split()
            if cmdlist == 'checkout':
                response = 'doing a ckout: %s' % cmdlist[1:]
            elif cmdlist == 'commit':
                response = 'doing a commit: %s' % cmdlist[1:]
            else:
                response = 'unknown command'
            
            if not cmdlist:
                # Die Verbindung wurde getrennt
                # Abbruch
                print('Got EOF')
                break

            # Sende die Daten zurück zum client
            conn.send(bytes(response,'UTF-8'))

if __name__ == '__main__':
    main()
