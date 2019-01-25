
from socket import *
import time
from SunFounder_TB6612 import TB6612
import RPi.GPIO as GPIO

GPIO.setwarnings(False)
timeout = 0.20


GPIO.setmode(GPIO.BCM)
HOST = ''
PORT = 9996
BUFSIZE = 2048
ADDR = (HOST,PORT)
tcpSerSock = socket(AF_INET, SOCK_STREAM)
tcpSerSock.bind(ADDR)
tcpSerSock.listen(5)
GPIO.setup((27, 22), GPIO.OUT)
a = GPIO.PWM(27, 60)
b = GPIO.PWM(22, 60)
a.start(0)
b.start(0)
def a_speed(value):

		a.ChangeDutyCycle(value)



def b_speed(value):

		b.ChangeDutyCycle(value)



	

motorA = TB6612.Motor(17)

motorB = TB6612.Motor(18)

motorA.debug = True

motorB.debug = True

motorA.pwm = a_speed

motorB.pwm = b_speed
flag = False


delay = 0.05

while True:
        print ('Waiting for connection')
        tcpCliSock,addr = tcpSerSock.accept()
        print ('...connected from :', addr)
        
        try:
                while True:
                        data = ''
                        data = tcpCliSock.recv(BUFSIZE)
                        print(data.decode('utf-8'))
                        if(data.decode('utf-8') == 'up'):
                            print('move up')
                            motorA.backward()
                            motorB.forward()
                            for i in range(90, 101):

                                    motorA.speed = 60
                                    motorB.speed = 70

                            
                                    GPIO.setup(14, GPIO.OUT)
                                    #cleanup output
                                    GPIO.output(14, 0)

                                    time.sleep(0.000002)

                                    #send signal
                                    GPIO.output(14, 1)

                                    time.sleep(0.000005)

                                    GPIO.output(14, 0)

                                    GPIO.setup(14, GPIO.IN)
                                    
                                    goodread=True
                                    watchtime=time.time()
                                    while GPIO.input(14)==0 and goodread:
                                            starttime=time.time()
                                            if (starttime-watchtime > timeout):
                                                    goodread=False

                                    if goodread:
                                            watchtime=time.time()
                                            while GPIO.input(14)==1 and goodread:
                                                    endtime=time.time()
                                                    if (endtime-watchtime > timeout):
                                                            goodread=False
                                    
                                    if goodread:
                                            duration=endtime-starttime
                                            distance=duration*34000/2
                                            print distance
                                            if(distance < 10):
                                                tcpCliSock.send('h')
                                                flag=True
                                                motorA.stop()

                                                motorB.stop()
                                                break
                                            


                            

                            
                            motorA.stop()

                            motorB.stop()
                            if(flag == False):
                                tcpCliSock.send('a')

                            
                        if(data.decode('utf-8') == 'left'):
                            print('move left')
                            motorA.backward()

                            for i in range(95, 101):

                                motorA.speed = 90

                                
                            
                            motorA.stop()

                            motorB.stop()
                            tcpCliSock.send('a')


                            
                        if(data.decode('utf-8') == 'right'):
                            print('move right')
                            motorB.forward()

                            for i in range(95, 101):

                                        motorB.speed = 90

                                        
                        
                            motorA.stop()

                            motorB.stop()
                            tcpCliSock.send('a')


                            
                        if(data.decode('utf-8') == 'down'):
                            print('move down')
                            motorA.forward()
                            motorB.backward()
                            for i in range(90, 101):

                                motorA.speed = 90
                                motorB.speed = 100

    
                            motorA.stop()

                            motorB.stop()
                            tcpCliSock.send('a')
                        else:
                            tcpCliSock.send('a')                        
                        break;
        except KeyboardInterrupt:
                GPIO.cleanup()
                print('Intrupt')
                tcpSerSock.close();
