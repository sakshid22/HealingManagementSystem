import pyaudio
import socket
import wave
from threading import Thread
#RECORD_SECONDS=15
#WAVE_OUTPUT_FILENAME = "output.wav"
#frames = []

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
			#frames1.append(data)
			#frames.append(stream.read(CHUNK))
			#frames1.append(stream.read(chunk1))

		

if __name__ == "__main__":
    frames=[]
    frames1=[]
    CHUNK = 1024
    FORMAT = pyaudio.paInt16
    CHANNELS = 2
    RATE = 44100
    RECORD_SECONDS=20
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
