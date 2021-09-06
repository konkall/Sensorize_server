package com.anap.second;

import java.util.*;
import  java.sql.Timestamp;

import	org.eclipse.paho.client.mqttv3.*;


/**
 *  Class for the callbacks of the subscriber thread.
 */
public class Subscriber implements	MqttCallback {

    private MsgQueue obj = null;

    /**
     * Instantiates a new Subscriber.
     *
     * @param obj the obj
     */
    public Subscriber( MsgQueue obj){

        this.obj = obj;
    }

    /**
     * 	This	method	is	called	when	the	connection	to	the	server	is	lost.
     */
    public	void	connectionLost(Throwable cause)	{
        System.out.println("Connection	lost!"	+	cause);
        System.exit(1);
    }
    /**
     * Called when delivery for	a message has been completed,
     * and all acknowledgments  have been received
     */

    public	void	deliveryComplete(IMqttDeliveryToken token)	{
    }

    /**
     *This	method	is	called	when	a	message	arrives	from the	server.
      */
    public	void	messageArrived(String	topic,	MqttMessage message)	throws	MqttException {

       long	time1	=	new	Timestamp(System.currentTimeMillis()).getTime();
        String time = Long.toString(time1);
        String message23 = new	String(message.getPayload());

        ArrayList<String> ar = new ArrayList<String>();

        String[] tokens = message23.split(";");
        for( int i = 0; i < tokens.length; i++)
        {
            ar.add(tokens[i]);
        }
        ar.add(time);
        obj.put(ar);
    }
}

