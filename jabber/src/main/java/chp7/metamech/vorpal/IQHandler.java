package chp7.metamech.vorpal;

import java.io.Writer;

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

public class IQHandler implements PacketListener {

  UserIndex userIndex;
  public IQHandler(UserIndex index) { userIndex = index; }

  public void notify(Packet packet){
    Log.trace("Delivering packet: " + packet.toString());
    String recipient = packet.getAttribute("to");
    if (recipient.equalsIgnoreCase(Server.SERVER_NAME) || recipient == null){
      Log.trace("Dropping packets for server: " + packet.toString());
      return;
    }
    try {
      Writer out = userIndex.getWriter(recipient);
      if (out != null){
        packet.writeXML(out);
      } else {
        Log.info("Could not deliver: " + packet.toString()); // Will eventually store&forward
      }
    } catch (Exception ex){
      ex.printStackTrace();
    }
    /*
    Log.trace("[QT] Handling IQ packet");

    // Ensure packet is addressed to us
    if (packet.getTo().equals(Server.SERVER_NAME)){

      IQPacket reply = new IQPacket(packet.getSession());
      reply.setTo(packet.getFrom());
      reply.setFrom(packet.getTo());
      reply.setID(packet.getID());
      reply.setType("result");

      // Grab all queries and handle them
      LinkedList queries = (LinkedList)packet.getQueries();
      while(queries.size() > 0){
        Query query = (Query)queries.removeFirst();
        query.addResultQuery(reply);
        if (reply.getType().equals("error")){
          break;
        }
      }

      // All queries recognized and handled so we just need to send the reply
      reply.writeXML();
      return;
    }

    // packet addressed to someone else, send to addressee
    // TODO: We don't know how to do this yet...
    Log.error("*Server TODO* delivery to other clients not supported");
    return;
    */
  }
}