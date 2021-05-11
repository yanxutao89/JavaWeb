package chp4.metamech.jabber.xml;

import java.util.HashMap;
import java.util.Iterator;

import chp4.metamech.log.Log;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class QueueThread extends Thread {

  PacketQueue packetQueue;
  public QueueThread(PacketQueue queue) { packetQueue = queue;  }

  HashMap packetListeners = new HashMap();

  public boolean addListener(PacketListener listener, String element){
    if (listener == null || element == null){
      return false;
    }
    packetListeners.put(listener,element);
    return true;
  }

  public boolean removeListener(PacketListener listener){
    packetListeners.remove(listener);
    return true;
  }

  public void run(){

    for( Packet packet = packetQueue.pull();
         packet != null;
         packet = packetQueue.pull()) {

      try {
        if (packet.getFrom() == null){
          packet.setFrom(packet.getSession().getJID().toString());
        }
        String matchString = packet.getElement();

        synchronized(packetListeners){
          Iterator iter = packetListeners.keySet().iterator();
          boolean wasHandled = false;
          PacketListener defaultListener = null;
          while (iter.hasNext()){
            PacketListener listener = (PacketListener)iter.next();
            String listenerString = (String)packetListeners.get(listener);
            if (listenerString.length() == 0){
              defaultListener = listener;
            } else if (listenerString.equals(matchString)){
              listener.notify(packet);
              wasHandled = true;
            } // if
          } // while
          // Use the default listener (match string == "")
          // to handle any packets not handled by a regular
          // packet listener.
          if (!wasHandled && defaultListener != null){
            defaultListener.notify(packet);
          }
        } // sync
      } catch (Exception ex){
        Log.error("QueueThread: ", ex); // Soldier on - no matter what
      }
    } // for
  } // run()
} // class QueueThread
