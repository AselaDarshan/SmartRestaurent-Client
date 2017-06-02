package com.company;

import java.util.Vector;

/**
 * Created by asela on 5/20/17.
 */
public class CommuncationBus {
    private static Vector messages = new Vector();
    public static synchronized void putMessage(String m) {

        messages.addElement(m);

    }

    // Called by Consumer
    public static synchronized String getMessage()  {

        while (messages.size() == 0)
           return null;
        String message = (String) messages.firstElement();
        messages.removeElement(message);
        return message;
    }
}
