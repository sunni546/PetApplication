import socket
import cv2
import glob
import base64
import numpy as np
from time import sleep

TCP_IP = ######
TCP_PORT = ######

image_dataset = ######
image_num = ######
image_source = ######

image_source_list = [cv2.imread(images) for images in glob.glob(image_source)]

socket = socket.socket()
socket.connect((TCP_IP, TCP_PORT))

for i in range(len(image_source_list)):

    encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 90]
    result, imgencode = cv2.imencode('.jpg', image_source_list[i], encode_param)
    data = np.array(imgencode)
    stringData = base64.b64encode(data)
    length = str(len(stringData))

    socket.sendall(length.encode('utf-8').ljust(64))
    socket.send(stringData)
    
    sleep(0.04)

socket.close()