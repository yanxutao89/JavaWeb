package chp6.metamech.wocky;

import chp3.metamech.jabber.xml.Packet;
import chp3.metamech.jabber.xml.PacketListener;
import chp2.metamech.log.Log;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class IQHandler implements PacketListener {

  JabberModel jabberModel;
  
  public IQHandler(JabberModel model){
    jabberModel = model;
  }  

  public void notify(Packet packet) {
    if (packet.getID() != null){
      PacketListener listener = jabberModel.removeResultHandler(packet.getID());
      if (listener != null){
        listener.notify(packet);
        return;
      }
    }
    Log.trace("Dropping : " + packet.toString());
  }
}