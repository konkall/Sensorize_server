package com.anap.second;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;




/**
 * The Subscriber thread.
 */
public class SubscriberThread extends Thread {
    private String threadName = null;
    private MsgQueue obj = null;


    /**
     * Instantiates a new Subscriber thread.
     *
     * @param threadName the thread name
     * @param obj        the message to send to the publisher
     */
    public SubscriberThread(String threadName, MsgQueue obj){
        this.threadName = threadName;
        this.obj = obj;
    }

    /**
     *Subscribes to topic "Android/LocationData",
     * The subscriber synchronizes with the publisher, as seen in MsqQueue.java.
     */

    @Override
    public void run() {

        System.out.println("Hello from " + threadName);
        String	topic								=	"Android/LocationData";
        int qos
                =	2;
        String	broker							=	"tcp://localhost:1883";
        String	clientId =	"JavaSampleSubscriber";
        MemoryPersistence persistence	=	new	MemoryPersistence();
        try	{
//Connect	client	to	MQTT	Broker
            MqttClient sampleClient =	new	MqttClient(broker,	clientId,	persistence);
            MqttConnectOptions connOpts =	new	MqttConnectOptions();
            connOpts.setCleanSession(true);
//Set	callback
            Subscriber	main	=	new	Subscriber(obj);
            sampleClient.setCallback(main);
            System.out.println("Connecting	to	broker:	"+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
//Subscribe	to	a	topic
            System.out.println("Subscribing	to	topic	\""+topic+"\"	qos "+qos);
            sampleClient.subscribe(topic,	qos);
        }	catch(MqttException me)	{
            System.out.println("reason	"	+	me.getReasonCode());
            System.out.println("msg "	+	me.getMessage());
            System.out.println("loc "	+	me.getLocalizedMessage());
            System.out.println("cause	"	+	me.getCause());
            System.out.println("excep "	+	me);
            me.printStackTrace();
        }

    }
}
