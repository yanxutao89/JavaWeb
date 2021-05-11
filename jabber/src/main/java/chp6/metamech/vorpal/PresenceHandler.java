package chp6.metamech.vorpal;

import chp6.metamech.jabber.xml.Packet;
import chp6.metamech.jabber.xml.PacketListener;
import chp6.metamech.log.Log;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class PresenceHandler implements PacketListener {

  UserIndex userIndex;
  GroupChatManager chatMan = GroupChatManager.getManager();
  public PresenceHandler(UserIndex index) { userIndex = index; }

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