import tkSimpleDialog
from Tkinter import *
import tkFont
import Tkinter as tk
import socket,thread
import sys
import MySQLdb
import getpass
import os
import filecmp
import commands
import datetime
import time
import tkMessageBox
import re
import pyaudio
import socket
import wave
from threading import Thread


class MainWindow(tk.Frame):
    counter = 0
        
    
    def __init__(self, *args, **kwargs):
        frame=tk.Frame(root,width=1000,height=500,bg="lightblue")
        frame.pack()  
        tk.Frame.__init__(self, width=1000, height=160, bg="lightblue")
        abc=tkFont.Font(family="Calibari", size=86)
        abc2=tkFont.Font(family="Calibari", size=15 )
        abc1=tkFont.Font(family="Calibari", size=10 )

        l=tk.Label(frame, text="",font=abc , bg="lightblue",fg="brown",width=100)
        l.pack()
        l.place(relx=0.53, rely=0.5, anchor=CENTER)
        l=tk.Label(frame, text="",font=abc , bg="lightblue",fg="dark blue",width=100)
        l.pack()
        l.place(relx=0.53, rely=0.55, anchor=CENTER)
        
        l=tk.Label(frame, text=".",font=abc , bg="lightblue",fg="maroon",width=100)
        l.pack()
        l.place(relx=0.53, rely=0.60, anchor=CENTER)
        l=tk.Label(frame, text="",font=abc , bg="lightblue",fg="dark blue",width=100)
        l.pack()
        l.place(relx=0.53, rely=0.65, anchor=CENTER)

        self.button = tk.Button(self, text="RECORD", command=self.rec_start,width=40,height=3,bg="dark blue",fg="white",font=abc1)
        self.button.pack(side="left",padx=15,pady=15)
        self.button.place(relx=0.33 ,rely=0.50,anchor=CENTER)

        self.button = tk.Button(self, text="EXIT", command=self.quit,width=40,height=3,bg="dark blue",fg="white",font=abc1)
        self.button.pack(side="left",padx=15,pady=15)
        self.button.place(relx=0.80 ,rely=0.50,anchor=CENTER)


        l=Label(frame, text="", font=abc, bg="lightblue")
        l.pack()
        l.place(relx=0.50, rely=0.15, anchor=CENTER)
        l=Label(frame, text="", font=abc, bg="lightblue")  
        l.pack()
        l.place(relx=0.60, rely=0.15, anchor=CENTER)
        l=Label(frame, text="", font=abc, bg="lightblue")  
        l.pack()
        l.place(relx=0.70, rely=0.15, anchor=CENTER)
        l=tk.Label(frame, text="NOTICE OF THE DAY !!! ..RECORD HERE ",font=abc2 , bg="lightblue",fg="black",width=100,height=2)
        l.pack()
        l.place(relx=0.60, rely=0.345, anchor=CENTER)     
        photo=PhotoImage(file="logo.gif")
        ph=Label(frame,image=photo)
        ph.photo=photo
        ph.pack()
        ph.place(relx=0.03,rely=0.03)
        


    '''def create_window(self):
        self.counter += 1
        t = tk.Toplevel(self,bg="azure")
        t.geometry("400x250+300+300")
        abc=tkFont.Font(family="Lucida calligraphy",size=15)
        
       
        t.wm_title("login")
        l = tk.Label(t, text="USERNAME",bg="yellow",font=abc)
        l.pack()
        l.place(relx=0.028, rely=0.15)
        custName = StringVar(None)
        self.yourName = Entry(t,textvariable=custName)
        self.yourName.pack()
        self.yourName.place(relx=0.52, rely=0.15)

        l = tk.Label(t, text="PASSWORD",bg="yellow",font=abc)
        l.pack()
        l.place(relx=0.028, rely=0.35)
        custName = StringVar(None)
        self.yourPass = Entry(t,show="*")
        self.yourPass.pack()
        self.yourPass.place(relx=0.52, rely=0.35)

        self.button = tk.Button(t, text="OK",command=self.rec_start)
        self.button.pack(side="bottom",padx=15,pady=15)
        self.button.place(relx=0.53 ,rely=0.55,anchor=CENTER)'''

    def rec_start(self):
        frames=[]
        frames1=[]
        CHUNK = 1024
        FORMAT = pyaudio.paInt16
        CHANNELS = 2
        RATE = 44100
        RECORD_SECONDS=15
        WAVE_OUTPUT_FILENAME = "output.wav"

        p = pyaudio.PyAudio()

        stream = p.open(format = FORMAT,
                        channels = CHANNELS,
                        rate = RATE,
                        input = True,
                        frames_per_buffer = CHUNK,
                        )
        Tr = Thread(target = record, args = (stream, CHUNK,))
        Ts = Thread(target = udpStream)
    #Td = Thread(target=makeAudio)
    #Tr.setDaemon(True)
    #Ts.setDaemon(True)
    
        Tr.start()
    #Td.start()
        Ts.start()
    
    
        Tr.join()
    #Td.join()
        Ts.join()
    #Td.setDaemon(True)
    #Td.start()
    #Td.join()

def udpStream():
    udp = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)    

    while True:
        if len(frames) > 0:
            #frames1.append(frames)
            udp.sendto(frames.pop(0), ("localhost", 12345))

    udp.close()

def record(stream, CHUNK):
    
    for i in range(0, int(RATE / CHUNK * RECORD_SECONDS)):
        
                    
			#chunk1=CHUNK
			#c=RATE / CHUNK * RECORD_SECONDS
        data = stream.read(CHUNK)
        frames.append(data)
	#frames.append(data)
        
if __name__ == "__main__":
    CHUNK = 1024
    FORMAT = pyaudio.paInt16
    CHANNELS = 2
    RATE = 44100
    RECORD_SECONDS=15
    WAVE_OUTPUT_FILENAME = "output.wav"

    frames=[]
    frames1=[]
    root = tk.Tk()
    main = MainWindow(root)
    main.pack(side="top", fill="both", expand=True)
    root.mainloop()

print "\n\n-----------------------LOG-----------------------\n\n"

