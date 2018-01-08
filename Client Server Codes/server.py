import socket

s=socket.socket()

host=''
port=12346

s.bind((host,port))

s.listen(1)
print ("here")

while True:
    c,addr = s.accept()
    print("got connection from",addr)
    c.send("thanks for connecting")
    p=raw_input("enter your name")
    c.send(p)
    x=c.recv(1024)
    print(x.upper())
    c.close()   
s.close()    
    
  
