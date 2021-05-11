package chp8.metamech.vorpal;

import chp8.metamech.jabber.xml.Packet;
import chp8.metamech.jabber.xml.PacketListener;
import chp8.metamech.jabber.ErrorTool;
import chp8.metamech.jabber.Session;
import chp8.metamech.log.Log;

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
    if (packet.getSession().getStatus() != Session.AUTHENTICATED){
      Log.info("PresenceHandler: Not authenticated" + packet.toString());
      packet.setTo(null);
      packet.setFrom(null);
      ErrorTool.setError(packet,401,"You must be authenticated to send presence");
      MessageHandler.deliverPacket(packet);
    } else if (chatMan.isChatPacket(packet)){
      Log.trace("PresenceHandler: groupchat presence");
      chatMan.handleChatPresence(packet);
    } else {
      Log.trace("PresenceHandler: user presence sending to user roster: " +
                packet.getSession().getJID().getUser());
      User user = userIndex.getUser(packet.getSession().getJID().getUser());
      user.getRoster().updatePresence(packet);
    }
  }
}