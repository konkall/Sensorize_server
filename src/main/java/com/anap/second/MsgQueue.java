package com.anap.second;
import java.util.*;

/**
 * The Msg queue class. A queue to store messages between subscriber and publisher.
 */
public class MsgQueue {

    /**
     * The Valueset variable. It provides synchronization. Acts like a semaphore.
     */
    boolean valueSet = false;

    /**
     * The Queue.
     */
    Queue<ArrayList<String>> queue = new LinkedList<ArrayList<String>>();


    /**
     * Gets the first item from the queue for the publisher to use.
     * Here we also synchronise the publisher-subscriber, as the queue is shared between threads.
     *
     * @return the first item from the queue.
     */
    public synchronized ArrayList<String> get () {
        if (!valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }

        }
        valueSet = false;
        ArrayList<String> msg = queue.poll();
        notify();
        return msg;

    }

    /**
     *
     * Pushes the message that arrived from the android devices to the queue.
     * Here we also synchronise the publisher-subscriber, as the queue is shared between threads.
     * @param msg       the message to send to the publisher
     */
    public synchronized void put (ArrayList<String> msg ) {
        if (valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }

        }
        valueSet = true;
        queue.add(msg);
        notify();
    }


}
