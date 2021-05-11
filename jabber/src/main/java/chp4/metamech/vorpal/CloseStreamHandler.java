package chp4.metamech.vorpal;

import chp4.metamech.jabber.xml.Packet;
import chp4.metamech.jabber.xml.PacketListener;
import chp4.metamech.jabber.Session;
import chp4.metamech.log.Log;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class CloseStreamHandler implements PacketListener {

  SessionIndex sessionIndex;
  public CloseStreamHandler(SessionIndex index) { sessionIndex = index; }

  public void notify(Packet packet){
    try {
      Log.trace("Closing session");
      Session session = packet.getSession();
      session.getWriter().write("</stream:stream>");
      session.getWriter().flush();
      session.getSocket().close();
      sessionIndex.removeSession(session);
      Log.trace("Closed session");
    } catch (Exception ex){
      Log.error("CloseStreamHandler: ",ex);
      sessionIndex.removeSession(packet.getSession());
      Log.trace("Exception closed session");
    }
  }
}