from socket import *
import time, random, thread
Port, Port2, HostIP, myname, mypw, newdata = 50005, 50003, "localhost", "", "", ""
gamemode = "lobby"
Clientsonline = []
 
def recvloop(soc):
    global newdata
    while 1:
        newdata = soc.recv(1024)
        newlist = newdata.split()
        newjob = newlist[0]
        newlist.pop(0)
        if newjob == "say":
            newname = newlist[0]
            newlist.pop(0)
            newlist2 = " ".join(newlist)
            print newname, newjob, newlist2
        if newjob == "upd":
            newlist2 = newlist[0]
            Clientsonline = newlist2.split()
            print "Clients online: ", Clientsonline
def sendloop(soc2):
    while 1:
        inputtxt = raw_input("")
        inputtxt = "say %s" %inputtxt
        soc2.send(inputtxt)
        
soc = socket(AF_INET, SOCK_STREAM)
soc.connect((HostIP, Port))
while 1:
    print "only 1 word is taken for each name and password"
    myname = raw_input("Your name please: ")
    myname = myname.split()
    myname = myname[0]
    mypw = raw_input("Your password? ")
    mypw = mypw.split()
    mypw = mypw[0]
    soc.send("%s %s" %(myname, mypw))
    incoming = soc.recv(1024)
    if incoming == "welcome":
        print "welcome, you are online now"
        break
    if incoming == "wrong password":
        print "wrong password or name is taken already"
soc2 = socket(AF_INET, SOCK_STREAM)
soc2.connect((HostIP, Port2))
thread.start_new_thread(recvloop, (soc, ))
thread.start_new_thread(sendloop, (soc2, ))
while 1:
    pass
