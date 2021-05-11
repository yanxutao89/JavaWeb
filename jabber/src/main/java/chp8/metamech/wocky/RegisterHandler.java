package chp8.metamech.wocky;

import chp3.metamech.jabber.xml.Packet;
import chp3.metamech.jabber.xml.PacketListener;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class RegisterHandler implements PacketListener {

// called only on register results/errors
  JabberModel jabberModel;
  public RegisterHandler(JabberModel model){
    jabberModel = model;
  }

  public void notify(Packet packet){
    try {
      if (packet.getType().equals("result")){
        jabberModel.authenticate();
      } else {
        String message = "Failed to register";
        if (packet.getType().equals("error")){
          message = message + ": " + packet.getChildValue("error");
        }
        System.out.println("Register Handler: " + message);
      }
    } catch (Exception ex){
      ex.printStackTrace();
    }
  }
}