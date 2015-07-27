#Client version of tracking-ai, supposed to run on the pie (or chip or wathever)

import socket

address="127.0.0.1"
port=28015
bufferSize=1024

print("Attempt to connect at" + address + ":" + str(port) + "?")
userIn = input("Press <ENTER> to continue and attempt to connect")

s = socket.socket();
s.connect((address,port));



userIn = input("Press <ENTER> to close")
