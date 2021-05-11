package chp3.metamech.jabber.xml;

import chp3.metamech.jabber.xml.*;
import chp3.metamech.jabber.Session;
import chp3.metamech.log.Log;

public class ProcessThread extends Thread {

  Session session;
  PacketQueue packetQueue;

  public ProcessThread(PacketQueue queue, Session session){
    packetQueue = queue;
    this.session = session;
  }

  public void run(){
    try {
      // Processing incoming
      JabberInputHandler handler = new JabberInputHandler(packetQueue);
      handler.process(session);
    } catch (Exception ex){
      Log.error("ProcessThread: ", ex);
      if (session.getStatus() > Session.CONNECTED){
        try {
          session.disconnect();
        } catch (Exception eex){
        }
      }
    }
  }
}
