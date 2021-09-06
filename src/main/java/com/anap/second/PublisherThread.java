package com.anap.second;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * The publisher thread.
 */
public class PublisherThread extends Thread{
        private String threadName = null;
        private MsgQueue obj = null;

    /**
     * Instantiates a new Publisher thread.
     *
     * @param threadName the thread name
     * @param obj        the message as given from the subscriber
     */
    public PublisherThread(String threadName, MsgQueue obj){
            this.threadName = threadName;
            this.obj = obj;
        }

    /**
     * Publishes messages to topic "Android/device id",
     * where device id is different for each android device.
     * Infinite do-while loop to publish messages to the correct topic.
     * The publisher synchronizes with the subscriber, as seen in MsqQueue.java.
     */
        @Override
        public void run() {

            System.out.println("Hello from " + threadName);

            String broker = "tcp://localhost:1883";
            String clientId = "JavaSamplePublisher";
            MemoryPersistence persistence = new MemoryPersistence();
            String st_topic = "Android/";
            String topic ;
            int qos =	2;
            MqttClient sampleClient = null;


            ArrayList<String> content = obj.get();

            if(content.size() != 17){
                System.out.println("The data is not valid. Program will terminate.");
                System.exit(-1);
            }
            List<String> date_time_list = new ArrayList<String>();
            String Date, Time;
            String dev_id  = content.get(0);
            System.out.println(dev_id);
            String lightsensor_value1 = content.get(2);
            String lightsensor_value2 = content.get(3);
            String lightsensor_value3 = content.get(4);
            String lightsensorr_value1 = lightsensor_value1.replace(',', '.');
            String lightsensorr_value2 = lightsensor_value2.replace(',', '.');
            String lightsensorr_value3 = lightsensor_value3.replace(',', '.');
            String lightsensor_merged = lightsensorr_value1 + "/" + lightsensorr_value2 + "/" +lightsensorr_value3 ;
            String accsensor_value1 = content.get(6);
            String accsensor_value2 = content.get(7);
            String accsensor_value3 = content.get(8);
            String accsensorr_value1 = accsensor_value1.replace(',', '.');
            String accsensorr_value2 = accsensor_value2.replace(',', '.');
            String accsensorr_value3 = accsensor_value3.replace(',', '.');
            String gravsensor_value1 = content.get(10);
            String gravsensor_value2 = content.get(11);
            String gravsensor_value3 = content.get(12);
            String gravsensorr_value1 = gravsensor_value1.replace(',', '.');
            String gravsensorr_value2 = gravsensor_value2.replace(',', '.');
            String gravsensorr_value3 = gravsensor_value3.replace(',', '.');
            String gravsensor_merged = accsensorr_value1 + "/" + accsensorr_value2 + "/" + accsensorr_value3 + "/" +  gravsensorr_value1 + "/" +  gravsensorr_value2 + "/" +  gravsensorr_value3 ;
            String date = content.get(13);
            date_time_list.add(0, date);
            String[] split = date_time_list.get(0).split(" ");
            Date = split[0];
            Time = split[1];
            System.out.println("time is " + Time);

            String[] units = Time.split(":"); //will break the string up into an array
            int minutes = Integer.parseInt(units[1]); //first element
            int seconds = Integer.parseInt(units[2]); //second element
            int duration = 60 * minutes + seconds; //add up our values
            String longitude= content.get(14);
            String latitude = content.get(15);

            float lightsensor_value1_num = Float.parseFloat(lightsensorr_value1);
            float lightsensor_value2_num =Float.parseFloat(lightsensorr_value2);
            float lightsensor_value3_num =Float.parseFloat(lightsensorr_value3);
            float accsensor_value3_num =Float.parseFloat(accsensorr_value3);
            float gravsensor_value3_num =Float.parseFloat(gravsensorr_value3);

            int prev_duration = -1;
            String prev_id;
            prev_id = "start";
            String content_send = "empty";

            do {

                if(
                        ( (lightsensor_value1_num *(100/100.0) > lightsensor_value2_num && lightsensor_value3_num*(100/100.0) >
                                lightsensor_value1_num && (accsensor_value3_num - gravsensor_value3_num) > 0.6 )
                                 || (accsensor_value3_num - gravsensor_value3_num) > 15)
                                && (duration - prev_duration < 1)
                                && !prev_id.equals("start")
                                && !dev_id.equals( prev_id)
                        )


                {
                    try {


                        topic = st_topic + content.get(0);



                        content_send = "1"; // 1= verified
                        System.out.println("content_send is " + content_send);
                        //Connect	to	MQTT	Broker
                        sampleClient = new MqttClient(broker, clientId, persistence);
                        MqttConnectOptions connOpts = new MqttConnectOptions();
                        connOpts.setCleanSession(true);
                        System.out.println("Connecting	to	broker:	" + broker);
                        sampleClient.connect(connOpts);
                        System.out.println("Connected");

                        //Publish	message	to	MQTT	Broker
                        System.out.println("Publishing	message:	" + content_send);
                        MqttMessage message = new MqttMessage(content_send.getBytes());
                        message.setQos(qos);
                        sampleClient.publish(topic, message);
                        System.out.println("Message	published");
                        sampleClient.disconnect();
                        System.out.println("Disconnected");
                    } catch (MqttException me) {
                        System.out.println("reason	" + me.getReasonCode());
                        System.out.println("msg " + me.getMessage());
                        System.out.println("loc " + me.getLocalizedMessage());
                        System.out.println("cause	" + me.getCause());
                        System.out.println("excep " + me);
                        me.printStackTrace();
                    }

                }
                else if(
                        ((lightsensor_value1_num *(100/100.0) > lightsensor_value2_num && lightsensor_value3_num*(100/100.0) >
                                lightsensor_value1_num && (accsensor_value3_num - gravsensor_value3_num) > 0.6 ) ||
                                (accsensor_value3_num - gravsensor_value3_num) > 15 )
                        ) {

                        try {

                            if(lightsensor_value1_num *(100/100.0) > lightsensor_value2_num && lightsensor_value3_num*(100/100.0) >
                                    lightsensor_value1_num && (accsensor_value3_num - gravsensor_value3_num) > 0.6 )
                            {
                                // light
                                Main.db.sqlConnect();
                                Main.db.table_insert(dev_id,"Light Sensor", lightsensor_merged, Date, Time, latitude,
                                        longitude);
                                try{
                                    Main.db.sqlDisconnect();
                                }
                                catch (SQLException ex) {
                                System.out.println("SQLException:\n" + ex.toString());
                                ex.printStackTrace();
                                }
                            } else {
                                // gravity-accelaration
                                Main.db.sqlConnect();
                                Main.db.table_insert(dev_id, "Gravity-Acceleration Sensors", gravsensor_merged, Date, Time, latitude,
                                        longitude);
                                try {
                                    Main.db.sqlDisconnect();
                                } catch (SQLException ex) {
                                    System.out.println("SQLException:\n" + ex.toString());
                                    ex.printStackTrace();

                                }
                            }
                        topic = st_topic + content.get(0);


                        content_send = "0";
                        System.out.println("content_send is " + content_send);

                        //Connect	to	MQTT	Broker
                        sampleClient = new MqttClient(broker, clientId, persistence);
                        MqttConnectOptions connOpts = new MqttConnectOptions();
                        connOpts.setCleanSession(true);
                        System.out.println("Connecting	to	broker:	" + broker);
                        sampleClient.connect(connOpts);
                        System.out.println("Connected");

                        //Publish	message	to	MQTT	Broker
                        System.out.println("Publishing	message:	" + content_send);
                        MqttMessage message = new MqttMessage(content_send.getBytes());
                        message.setQos(qos);
                        sampleClient.publish(topic, message);
                        System.out.println("Message	published");
                        sampleClient.disconnect();
                        System.out.println("Disconnected");
                    } catch (MqttException me) {
                        System.out.println("reason	" + me.getReasonCode());
                        System.out.println("msg " + me.getMessage());
                        System.out.println("loc " + me.getLocalizedMessage());
                        System.out.println("cause	" + me.getCause());
                        System.out.println("excep " + me);
                        me.printStackTrace();
                    }
                 }


                prev_id = dev_id;
                prev_duration = duration;
                content = obj.get();
                if(content.size() != 17){
                    System.out.println("The data is not valid. Program will terminate.");
                    System.exit(-1);
                }
                dev_id  = content.get(0);
                lightsensor_value1 = content.get(2);
                lightsensor_value2 = content.get(3);
                lightsensor_value3 = content.get(4);
                lightsensorr_value1 = lightsensor_value1.replace(',', '.');
                lightsensorr_value2 = lightsensor_value2.replace(',', '.');
                lightsensorr_value3 = lightsensor_value3.replace(',', '.');
                lightsensor_merged = lightsensorr_value1 + "/" + lightsensorr_value2 + "/" +lightsensorr_value3 ;

                accsensor_value1 = content.get(6);
                accsensor_value2 = content.get(7);
                accsensor_value3 = content.get(8);
                accsensorr_value1 = accsensor_value1.replace(',', '.');
                accsensorr_value2 = accsensor_value2.replace(',', '.');
                accsensorr_value3 = accsensor_value3.replace(',', '.');
                gravsensor_value1 = content.get(10);
                gravsensor_value2 = content.get(11);
                gravsensor_value3 = content.get(12);
                gravsensorr_value1 = gravsensor_value1.replace(',', '.');
                gravsensorr_value2 = gravsensor_value2.replace(',', '.');
                gravsensorr_value3 = gravsensor_value3.replace(',', '.');
                gravsensor_merged = accsensorr_value1 + "/" + accsensorr_value2 + "/" + accsensorr_value3 + "/" +  gravsensorr_value1 + "/" +  gravsensorr_value2 + "/" +  gravsensorr_value3 ;
                date = content.get(13);
                date_time_list.add(0, date);
                split = date_time_list.get(0).split(" ");
                Date = split[0];
                Time = split[1];
                units = Time.split(":"); //will break the string up into an array
                minutes = Integer.parseInt(units[1]); //first element
                seconds = Integer.parseInt(units[2]); //second element
                duration = 60 * minutes + seconds; //add up our values
                longitude= content.get(14);
                latitude = content.get(15);

                lightsensor_value1_num = Float.parseFloat(lightsensorr_value1);
                lightsensor_value2_num =Float.parseFloat(lightsensorr_value2);
                lightsensor_value3_num =Float.parseFloat(lightsensorr_value3);
                accsensor_value3_num =Float.parseFloat(accsensorr_value3);
                gravsensor_value3_num =Float.parseFloat(gravsensorr_value3);

            } while (true);
        }
    }
