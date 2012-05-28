import socket
import sys

HOST, PORT = "localhost", int(sys.argv[1])

# Create a socket (SOCK_STREAM means a TCP socket)
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

try:
    sock.connect((HOST, PORT))
    while True:
        data = bytes(input('>>> '), 'UTF-8')

        # Connect to server and send data
        sock.sendall(data + b"\n")

        # Receive data from the server and shut down
        received = sock.recv(1024)
        
        print("Sent:     {}".format(data))
        print("Received: {}".format(received))
except EOFError as err:
    print('Quitting')
finally:
    sock.close()
