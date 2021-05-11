package chp8.metamech.wocky;

import chp8.metamech.jabber.xml.Packet;
import chp8.metamech.jabber.xml.PacketListener;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class RosterHandler implements PacketListener {

  // handle incoming presence and roster packets
  public void notify(Packet packet) {
    System.out.print("roster: ");
    System.out.println(packet.toString());
  }
}