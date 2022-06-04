from machine import Pin,PWM
from umqtt.simple import MQTTClient
from machine import Pin, PWM, I2C, ADC
import machine
from sgp40 import SGP40
import dht
import esp32
import utime as time
import network
from machine import Pin
import json
import _thread

WiFi_SSID = "WIFI SSID"
WiFi_PASS = "WIFI PASSWORD"

MQTT_BROKER_IP = "MQTT IP"
MQTT_BROKER_PORT = "MQTT PORT"

MQTT_USER = "iot"
MQTT_PASSWORD = "123456789"

led_B = machine.PWM(machine.Pin(22), freq=1000)
led_G = machine.PWM(machine.Pin(23), freq=1000)
led_R = machine.PWM(machine.Pin(21), freq=1000)
led_R.duty(0)
led_G.duty(0)
led_B.duty(0)

i2c = I2C(0)
d = dht.DHT11(Pin(25))
fot=ADC(Pin(34))

def do_connect():
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    if not wlan.isconnected():
        print('connecting to network...')
        wlan.connect(WiFi_SSID, WiFi_PASS)
        while not wlan.isconnected():
            pass
    print('network config:', wlan.ifconfig())


def sub_callback(topic, msg):
    print((topic, msg))
    if topic == b'ask_data':
        print('Ask data')
        air = AirQualityTest(i2c)
        tmp_hum = TempAndHumidityTest(d)
        tmp = tmp_hum.temperature()
        hum = tmp_hum.humidity()
        foto = FOTOTest(fot)
        data_set = '{ "air":'+ str(air)+', "tmp":'+ str(tmp)+', "hum" :'+ str(hum)+', "foto":'+ str(foto) +'}'
        print(data_set)
        ask_data.publish(topic='return_data', msg=data_set)
    elif topic == b'put_led':
        print('Put led')
        msg = str(msg).replace("rgb(","")
        msg = msg.replace(")","")
        msg = msg.replace(" ","")  
        msg = msg.replace("'","")
        msg = msg.replace("b","")
        tab_rgb = msg.split(",")
        led_R.duty(4*int(tab_rgb[0]))
        led_G.duty(4*int(tab_rgb[1]))
        led_B.duty(4*int(tab_rgb[2]))
    elif topic == b'ask_led':
        print(str(led_R.duty()) + " " + str(led_G.duty()) + " " + str(led_B.duty()))
        ask_led.publish(topic='return_led', msg='{"r":'+str(led_R.duty())+', "g":'+str(led_G.duty())+', "b":'+str(led_B.duty())+'}')


#AIR QUALITY
def AirQualityTest(i2c):
    address = i2c.scan()
    sgp40 = SGP40(i2c, 0x59)
    return sgp40.measure_raw()
    

#TEMP AND HUMIDITY
def TempAndHumidityTest(d):
    
    d.measure()
    return d   
        
       

#FOTO
def FOTOTest(foto):   
    
    foto.atten(ADC.ATTN_11DB)
    lightLevel=foto.read_u16()
    return str(lightLevel)

#AskData
def threadAskData():
    while True:
        ask_data.check_msg()
        time.sleep(0.1)
    
#AskLed
def threadAskLed():
    while True:
        ask_led.check_msg()
        time.sleep(0.1)

#PutLed
def threadPutLed():
    while True:
        put_led.check_msg()
        time.sleep(0.1)



#Start code execution
do_connect()

#Define ask_data client
ask_data = MQTTClient(client_id = "1" , server = MQTT_BROKER_IP, port=MQTT_BROKER_PORT, user = MQTT_USER, password= MQTT_PASSWORD)
ask_data.set_callback(sub_callback)
ask_data.connect()
ask_data.subscribe("ask_data", qos=1)
print('ask_data connected to MQTT broker')


#Define ask_led client
ask_led = MQTTClient(client_id = "2" , server = MQTT_BROKER_IP, port=MQTT_BROKER_PORT, user = MQTT_USER, password= MQTT_PASSWORD)
ask_led.set_callback(sub_callback)
ask_led.connect()
ask_led.subscribe("ask_led", qos=1)
print('ask_led connected to MQTT broker')


#Define put_led client
put_led = MQTTClient(client_id = "3" , server = MQTT_BROKER_IP, port=MQTT_BROKER_PORT, user = MQTT_USER, password= MQTT_PASSWORD)
put_led.set_callback(sub_callback)
put_led.connect()
put_led.subscribe("put_led", qos=1)
print('put_led connected to MQTT broker')

_thread.start_new_thread(threadAskData, ())
_thread.start_new_thread(threadAskLed, ())
_thread.start_new_thread(threadPutLed, ())

while True:
    ask_led.check_msg()
    ask_data.check_msg()
    put_led.check_msg()
