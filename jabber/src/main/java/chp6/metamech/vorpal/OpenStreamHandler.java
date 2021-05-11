package chp6.metamech.vorpal;

import java.io.Writer;

import chp3.metamech.jabber.xml.PacketListener;
import chp3.metamech.jabber.xml.Packet;
import chp3.metamech.jabber.Session;
import chp2.metamech.jabber.JabberID;
import chp2.metamech.log.Log;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class OpenStreamHandler implements PacketListener{

  static int streamID = 0;
  UserIndex userIndex;

  public OpenStreamHandler(UserIndex index) {
    userIndex = index;
  }

  public void notify(Packet packet){
    Log.trace("Opening stream connection");

    try {
      Session session = packet.getSession();
      session.setJID(new JabberID(packet.getFrom()));

      session.setStreamID(Integer.toHexString(streamID++));

      Writer out = session.getWriter();
      out.write("<?xml version='1.0' encoding='UTF-8' ?><stream:stream xmlns='jabber:client' from='");
      out.write(Server.SERVER_NAME);
      out.write("' id='");
      out.write(session.getStreamID());
      out.write("' xmlns:stream='http://etherx.jabber.org/streams'>");
      out.flush();

      session.setStatus(Session.STREAMING);
    } catch (Exception ex){
      ex.printStackTrace();
    }
  }
}