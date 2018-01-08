import socket

s=socket.socket()
host=socket.gethostname()
port=12346

s.connect((host,port))
print (s.recv(1024))
print (s.recv(1024))
s.send("saadiq")
s.close()
