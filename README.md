# Projet_SLO
Projet Slovaquie ESP32

To use the application :

  - Import /other/flow.json to your node-red server, change the ip and port of your MQTT Broker

  - In the /app/src/main/res/values/strings.xml file, change the value of 
  `<string name="BROKER_IP">IP:PORT</string>` to the IP and the Port of your Node-red MQTT Broker.

  - In /other/esp32.py file : change the values of :<br>
      `WiFi_SSID = "WIFI SSID"` <br>
      `WiFi_PASS = "WIFI PASSWORD"`<br>

      `MQTT_BROKER_IP = "MQTT IP"`<br>
      `MQTT_BROKER_PORT = "MQTT PORT"`<br>

  - Put /esp32.py in your ESP32 with Thonny.    
