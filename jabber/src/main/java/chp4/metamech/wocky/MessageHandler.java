package chp4.metamech.wocky;

import chp4.metamech.jabber.xml.Packet;
import chp4.metamech.jabber.xml.PacketListener;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class MessageHandler implements PacketListener {

  public void notify(Packet packet){
    String type = packet.getType() == null ? "normal" : packet.getType();
    System.out.println("Received " + type + " message: " 
                       + packet.getChildValue("body"));
    System.out.println("    To: " + packet.getTo());
    System.out.println("  From: " + packet.getFrom());
  }
  /*
  public void notify(Packet packet){
    String type = packet.getAttribute("type");
    if (type.equals("chat")){
      System.out.println("Chat message: " + packet.toString());
    } else if (type.equals("groupchat")){
      System.out.println("Groupchat message: " + packet.toString());
    } else if (type.equals("normal")){
      showNormalMessage(packet);
    } else if (type.equals("headline")){
      System.out.println("Headline message: " + packet.toString());
    } else {
      showNormalMessage(packet);
    }
  }

  void showNormalMessage(Packet packet) {
    StringBuffer message = new StringBuffer("From: ");
    if (packet.getAttribute("from") == null){
      message.append("<none>");
    } else {
      message.append(packet.getAttribute("from"));
    }
    message.append("\nTo: ");
    if (packet.getAttribute("to") == null){
      message.append("<none>");
    } else {
      message.append(packet.getAttribute("to"));
    }
    message.append("\nSubject: ");
    if (packet.getChildValue("subject") == null){
      message.append("<none>");
    } else {
      message.append(packet.getChildValue("subject").trim());
    }
    message.append("\nThread: ");
    if (packet.getChildValue("thread") == null){
      message.append("<none>");
    } else {
      message.append(packet.getChildValue("thread").trim());
    }
    message.append("\nBody:\n");
    if (packet.getChildValue("body") == null){
      message.append("<none>");
    } else {
      message.append(packet.getChildValue("body"));
    }
//    JOptionPane.showMessageDialog(null,message.toString());
    System.out.println("normal message: " + message.toString());
  }
  */
}