package chp5.metamech.vorpal;

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

public class PresenceHandler implements PacketListener {

  // Session index is not used yet...  Had to keep it here to keep in sync
  // with the book code.
  SessionIndex sessionIndex;
  GroupChatManager chatMan = GroupChatManager.getManager();
  public PresenceHandler(SessionIndex index) { sessionIndex = index; }

  public void notify(Packet packet){
    if (chatMan.isChatPacket(packet)){
      Log.trace("PresenceHandler: groupchat presence");
      chatMan.handleChatPresence(packet);
    } else {
      Log.trace("PresenceHandler: delivering presence packet " + packet.toString());
      MessageHandler.deliverPacket(packet);
    }
  }
}