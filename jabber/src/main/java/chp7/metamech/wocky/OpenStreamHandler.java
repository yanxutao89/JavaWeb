package chp7.metamech.wocky;

import chp7.metamech.jabber.xml.PacketListener;
import chp7.metamech.jabber.xml.Packet;
import chp7.metamech.jabber.Session;
import chp7.metamech.jabber.JabberID;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class OpenStreamHandler implements PacketListener{

  public void notify(Packet packet){
    Session session = packet.getSession();
    session.setStreamID(packet.getID());
    session.setJID(new JabberID(packet.getTo()));
    session.setStatus(Session.STREAMING);
  }
}