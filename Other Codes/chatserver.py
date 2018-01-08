from socket import *
import time, thread, random
Clients, Clientsonline, Port, Port2, dummy, HostIP = {}, [], 50005, 50003, "", ""
Outtube, Intube, Worktube = [], [], []
 ###################################### DATABASE ################################
def writedatabase():
    try:
        f = open("database", "r+")
    except:
        f = open("database", "w")
        f.close()
        print "database created"
        f = open("database", "r+")
    for element in Clients:
        savestring = ""
        for item in Clients[element][0]:
            savestring = "%s%s " % (savestring, item)
        savestring = "%s\n" % savestring
        f.write(savestring)
    f.close()
    print "database saved"
def readdatabase():
    try:
        f = open("database", "r+")
        for line in f:
            g = line.split()
            name = g[0]
            Clients[name] = [[name], ["conn", "addr"], ["conn2", "addr2"]]
            g.pop(0)
            for element in g:
                Clients[name][0].append(element)
    except:
        print "couldnt read database"
 
def saveloop(dummy):
    while 1:
        time.sleep(120)
        writedatabase()
 
####################################### COMMUNICATION ##########################
 
def acceptconthread(dummy):
    soc = socket(AF_INET, SOCK_STREAM)
    soc.bind((HostIP, Port))
    soc.listen(100)
    while 1:
        conn, addr = soc.accept()
        thread.start_new_thread(passwordthread, (conn, addr))
 
def passwordthread(conn, addr):
    while 1:
        name = conn.recv(1024)
        name2 = name.split()
        name, password = name2[0], name2[1]
        aaa = name in Clients
        if aaa == True:
            if password == Clients[name][0][1]:
                Clients[name] = [[name, password], [conn, addr], ["conn2", "addr2"]]
                print "%s connected with password %s" %(name, password)
                conn.send("welcome")
                Clientsonline.append(name)
                Worktube.append([name, "upd"])
                break
            if password != Clients[name][0][1]:
                conn.send("wrong password")
        if aaa == False:
            Clients[name] = [[name, password], [conn, addr], ["conn2", "addr2"]]
            print "%s connected with password %s" %(name, password)
            conn.send("welcome")
            Clientsonline.append(name)
            Worktube.append([name, "upd"])
            break
    soc2 = socket(AF_INET, SOCK_STREAM)
    soc2.bind((HostIP, Port2))
    soc2.listen(100)
    conn2, addr2 = soc2.accept()
    Clients[name][2]=[conn2, addr2]
    thread.start_new_thread(sendloop, (name, conn, addr))
    thread.start_new_thread(recvloop, (name, conn2, addr2))
 
def sendloop(name, conn, addr):
    try:
        while 1:
            if len(Outtube) > 0:
                if Outtube[0][0] == name:
                    sendstring = "%s %s" % (Outtube[0][1], Outtube[0][2])
                    conn.send(sendstring)
                    Outtube.pop(0)
    except:
        bbb = Clientsonline.remove(name)
 
def recvloop(name, conn2, addr2):
    try:
        while 1:
            data = conn2.recv(1024)
            data2 = data.split()
            jobname = data2[0]
            data2.pop(0)
            Intube.append([name, jobname, data2])
    except:
        print name, "has disconnected"
 
def workthrough():
    while 1:
        if len(Worktube) > 0:
            if Worktube[0][1] == "upd":
                newlist = Clientsonline[:]
                newlist2 = " ".join(newlist)
                Outtube.append([Worktube[0][0], "upd", newlist2])
                Worktube.pop(0)
        if len(Worktube) > 0:
            if Worktube[0][1] == "say":
                Worktube[0][2] = "%s %s" % (Worktube[0][0], Worktube[0][2])
                for client in Clientsonline:
                    Outtube.append([client, "say", Worktube[0][2]])
                Worktube.pop(0)
        if len(Intube) > 0:
            if Intube[0][1] == "say":
                naostring = " ".join(Intube[0][2])
                Worktube.append([Intube[0][0], "say", naostring])
                Intube.pop(0)
 
#################################### ACTION ####################################
 
readdatabase()
thread.start_new_thread(acceptconthread, (dummy, ))
thread.start_new_thread(saveloop, (dummy, ))
####thread.start_new_thread(workthrough, (dummy, ))
while 1:
    workthrough()


