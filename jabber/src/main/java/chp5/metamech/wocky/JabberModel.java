package chp5.metamech.wocky;

import java.io.IOException;
import java.io.Writer;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;

import chp5.metamech.jabber.*;
import chp5.metamech.jabber.xml.*;
import chp5.metamech.io.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class JabberModel {

  JabberModel(TestThread qThread) {
    packetQueue = qThread.getQueue();
    qThread.addListener(new OpenStreamHandler(),"stream:stream");
    qThread.addListener(new CloseStreamHandler(),"/stream:stream");
    qThread.addListener(new MessageHandler(),"message");
  }

  // Create the global queue and session everyone works with
  Session session = new Session();
  PacketQueue packetQueue;
  public int getSessionStatus() {
    return session.getStatus();
  }

  String version = "v. 1.0 - ch. 4";
  public String getVersion(){ return version; }

  String sName;
  public String getServerName()               {return sName;}
  public void   setServerName(String name)    {sName = name;}

  String sAddress;
  public String getServerAddress()            {return sAddress;}
  public void   setServerAddress(String addr) {sAddress = addr;}

  String sPort;
  public String getPort()                     {return sPort;}
  public void   setPort(String port)          {sPort = port;}

  String user;
  public String getUser()                     {return user;}
  public void   setUser(String usr)           {user = usr; }

  String resource;
  public String getResource()                 {return resource;}
  public void   setResource(String res)       {resource = res;}
  
  public void addStatusListener(StatusListener listener){
    // Create the sesssion and get it setup
    session.addStatusListener(listener);
  }

  public void removeStatusListener(StatusListener listener){
    session.removeStatusListener(listener);
  }

  public void connect() throws IOException {

    // Create a socket
    session.setSocket(new Socket(sAddress,Integer.parseInt(sPort)));
    session.setStatus(Session.CONNECTED);

    // Process incoming messages
    (new ProcessThread(packetQueue,session)).start();

    // Send our own "open stream" packet with the server name
    Writer out = session.getWriter();
    session.setJID(new JabberID(user,sName,resource));
    out.write("<?xml version='1.0' encoding='UTF-8' ?><stream:stream to='");
    out.write(sName);
    out.write("' from='");
    out.write(user + "@" + sName);
    out.write("' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>");
    out.flush();
  }

  // Send close stream fragment
  public void disconnect() throws IOException {
    session.closeStream();
  }

  public void sendMessage(String recipient,
                          String subject,
                          String thread,
                          String type,
                          String id,
                          String body) throws IOException {
    Packet packet = new Packet("message");

    if (recipient != null){
      packet.setTo(recipient);
    }
    if (id != null){
      packet.setID(id);
    }
    if (type != null){
      packet.setType(type);
    }
    if (subject != null){
      packet.getChildren().add(new Packet("subject",subject));
    }
    if (thread != null){
      packet.getChildren().add(new Packet("thread",thread));
    }
    if (body != null){
      packet.getChildren().add(new Packet("body",body));
    }
    packet.writeXML(session.getWriter());
  }

  public void sendPresence(String recipient,
                           String type,
                           String show,
                           String status,
                           String priority) throws IOException {
    Packet packet = new Packet("presence");

    if (recipient != null){
      packet.setTo(recipient);
    }
    if (type != null){
      packet.setType(type);
    }
    if (show != null){
      packet.getChildren().add(new Packet("show",show));
    }
    if (status != null){
      packet.getChildren().add(new Packet("status",status));
    }
    if (priority != null){
      packet.getChildren().add(new Packet("priority",priority));
    }
    packet.writeXML(session.getWriter());
  }
}