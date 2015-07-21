#client.py
import socket
import threading
import time,datetime
import sys,os,subprocess,traceback
import base64
import RPi.GPIO as GPIO




#socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s_i = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
port = 28015
host = "192.168.0.145"#"70.24.238.226"
server = (host,port)
try:
    s.connect(server)
    s_i.connect((host,port+1))
except:
    print("Connection Failed:")
    print(traceback.format_exc())
else:
    s.send("Connected!\n".encode('ascii'))
    print("connected!")
#

#GPIO
GPIO.setmode(GPIO.BOARD)
impulsetime = 0.1
pins = (11,13,16,18)
motors = {"m1":(pins[0],pins[1]),"m2":(pins[2],pins[3])}
current_motor_pos = ((0,0),(0,0))

#


##DATA TREATMENT
def getimage_as_data(path):
    path = str(path)
    i = open(path,"rb")
    return i.read()
##

##GPIO
def reset_pins():
    
    try:
        GPIO.setup(11, GPIO.OUT)
        GPIO.setup(13, GPIO.OUT)
        GPIO.setup(18, GPIO.OUT)
        GPIO.setup(16, GPIO.OUT)
        GPIO.output(13, False)
        GPIO.output(11, False)
        GPIO.output(16, False)
        GPIO.output(18,False)
    except:
        print("Error while reseting pins, exiting.")
        s.send(str("[ERRORLOG]\n----------------\n"+traceback.format_exc()+"\n----------------").encode('ascii'))
        _exit()
    else:
        print("Pins "+str(pins)+" all resetted correctly.")
        s.send("[PIN REPPORT] All pins have benn reset\n")

def movemotor1(m1):
    m1 = float(m1)
    try:
        if m1 < 0:
            GPIO.output(motors["m1"][1],True)
            time.sleep(abs(m1))#to do
            GPIO.output(motors["m1"][1],False)
        else:
            GPIO.output(motors["m1"][0],True)
            time.sleep(abs(m1))
            GPIO.output(motors["m1"][0],False)
    except :
        print("Error while moving motor1. Did not move them.")
        msg = "[ERRORLOG]\n----------------\n"+traceback.format_exc()+"\n----------------"
        s.send(str(msg).encode('ascii'))
        _exit()
    else:
        print("Motor 1 moved.")
def movemotor2(m2):
    m2 = float(m2)
    try:
        if m2 < 0:
            GPIO.output(motors["m2"][1],True)
            time.sleep(abs(m2))
            GPIO.output(motors["m2"][1],False)
        else:
            GPIO.output(motors["m2"][0],True)
            time.sleep(abs(m2))
            GPIO.output(motors["m2"][0],False)
    except :
        print("Error while moving motor2. Did not move them.")
        msg = "[ERRORLOG]\n----------------\n"+traceback.format_exc()+"\n----------------"
        s.send(str(msg).encode('ascii'))
        _exit()
    else:
        print("Motor 2 moved.")
##

##GENERAL
def _exit():
    s.send("\nExiting\n".encode('ascii'))
    try:
        s.close()
        GPIO.cleanup()
    except:
        pass
    sys.exit(0)
##

reset_pins()

while True:
    received = s.recv(1024)
    received = received.decode('ascii')
    if received is not None:
        rec_array = received.split(':')
        if rec_array[0] == "M":
            print("Moving based on "+received[2:])
            m1 = float(rec_array[1])
            m2 = float(rec_array[2])
            print(str(m1)+","+str(m2))
            #motors
            threading.Thread(target=movemotor1, args=(m1,)).start()
            threading.Thread(target=movemotor2, args=(m2,)).start()
                
                
            
                
        elif rec_array[0] == "Q":
            print("Exit request from server. The program will exit.")
            break
        elif rec_array[0] == "R":
            print("Image requested from server.")
            #take image
            p = subprocess.Popen(["raspistill","-h","500","-w","500","-vf","-hf","-t","5","-o","lastFrame.jpg"])
            out,err = p.communicate()
            print("Should be done")
            if err is not None:
                print("["+datetime.datetime.now()+"] Error while taking a picture:"+str(err))
                print("Exiting.")
                break
            else:#get image path
                path = "./lastFrame.jpg"
                #read and send
                s_i.send((base64.b64encode(getimage_as_data(path))+"\n").encode('ascii'))
                print("Image sent")
        else:
            print("[Server:]"+received)
            
    time.sleep(0.2)

shutDown = True
_exit()
