package chp6.metamech.vorpal;

import chp6.metamech.jabber.*;
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

public class RegisterHandler implements PacketListener {

  static UserIndex userIndex;
  Packet required;

  public RegisterHandler(UserIndex index) {
    userIndex = index;
    required = new Packet("iq");
    required.setFrom(Server.SERVER_NAME);
    required.setType("result");
    new Packet("username").setParent(required);
    new Packet("password").setParent(required);
    new Packet("hash").setParent(required);
  }

  // 2 types of register
  //
  // set - tries to register a new user
  // get - obtains required fields
  public void notify(Packet packet){
    Log.trace("Register handling " + packet.toString());

    String type = packet.getType();
    Packet query = packet.getFirstChild("query");

    if (type.equals("get")){
      required.setSession(packet.getSession());
      required.setID(packet.getID());
      MessageHandler.deliverPacket(required);
      return;

    } else if (type.equals("set")) { // type == set
      String username = query.getChildValue("username");
      User user = userIndex.getUser(username);
      if (user != null){ // user exists
        if (packet.getSession().getStatus() != Session.AUTHENTICATED ||
            !username.equals(packet.getSession().getJID().getUser())){
          Packet iq = new Packet("iq");
          iq.setSession(packet.getSession());
          iq.setID(packet.getID());
          ErrorTool.setError(iq,401,"User account already exists");
          MessageHandler.deliverPacket(iq);
          return;
        }
      } else {
        user = userIndex.addUser(username);
      }
      user.setPassword(query.getChildValue("password"));
      user.setHash(query.getChildValue("hash"));
      user.setSequence(query.getChildValue("sequence"));
      user.setToken(query.getChildValue("token"));
      if (user.getHash() == null || user.getSequence() == null || user.getToken() == null){
        if (user.getPassword() != null){
          user.setToken("randomtoken");
          user.setSequence("99");
          user.setHash("hashValue");
        }
      } else {
        // Adjust sequence number to be ready for next request.
        // Book readers.  In the book this was listed earlier resulting in a thrown
        // exception if 0k registration was not used.  This fixes it.  :)
        user.setSequence(Integer.toString(Integer.parseInt(user.getSequence()) - 1));
      }
      Packet iq = new Packet("iq");
      iq.setSession(packet.getSession());
      iq.setID(packet.getID());
      iq.setType("result");   // success
      MessageHandler.deliverPacket(iq);
      
      // Temporarily needed as we'll use registration as authentication until Chp7.
      packet.getSession().getJID().setResource("none");
      userIndex.addSession(packet.getSession());
      Log.trace("Register successfully registered " + username + " with password " + query.getChildValue("password"));
    } else {
      Log.info("Register ignoring " + packet.toString());
    }
  }
}
