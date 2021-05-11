package chp4.metamech.wocky;

import chp4.metamech.jabber.xml.Packet;
import chp4.metamech.jabber.xml.PacketListener;
import chp4.metamech.jabber.Session;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class CloseStreamHandler implements PacketListener {

  public void notify(Packet packet){
    Session session = null;
    try {
      packet.getSession().setStatus(Session.DISCONNECTED);
    } catch (Exception ex){
      ex.printStackTrace();
    }
  }
}