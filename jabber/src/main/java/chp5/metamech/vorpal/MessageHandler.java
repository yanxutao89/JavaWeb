package chp5.metamech.vorpal;

import chp5.metamech.jabber.xml.Packet;
import chp5.metamech.jabber.xml.PacketListener;
import chp5.metamech.jabber.Session;
import chp5.metamech.log.Log;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class MessageHandler implements PacketListener {

  static SessionIndex sessionIndex;
  GroupChatManager chatMan = GroupChatManager.getManager();
  public MessageHandler(SessionIndex index) { sessionIndex = index; }

  public void notify(Packet packet){
    String recipient = packet.getTo();

    if (recipient == null){ // to server
      Log.trace("Dropping packet: " + packet.toString());
      return;
    }

    if (recipient.equalsIgnoreCase(Server.SERVER_NAME)){ // to server
      Log.trace("Dropping packet: " + packet.toString());
      return;
    }

    // Fill in sender as resource that sent message (anti-spoofing)
    packet.setFrom(packet.getSession().getJID().toString());

    if (packet.getAttribute("type").equals("groupchat")){
      if (chatMan.isChatPacket(packet)){
        chatMan.handleChatMessage(packet);
      } else {
        Log.trace("Dropping packet: " + packet.toString());
      }
      return;
    }
    deliverPacket(packet);
  }

  static public void deliverPacket(Packet packet){
    try {
      Session session = sessionIndex.getSession(packet.getTo());
      if (session != null){
        packet.writeXML(session.getWriter());
      } else {
        Log.info("Dropping packet (not home) " + packet.toString());
        return;
      }
    } catch (Exception ex){
      Log.error("MessageHandler: ", ex);
    }
  }
}