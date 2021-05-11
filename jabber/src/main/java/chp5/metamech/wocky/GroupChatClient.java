package chp5.metamech.wocky;

import chp5.metamech.jabber.xml.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
class GroupChatClient {

  public static void main(String[] args){
    new GroupChatClient();
  }

  public GroupChatClient(){
    String server =  System.getProperty("jab.server.name",   "shigeoka.com");
    String address = System.getProperty("jab.server.address","127.0.0.1");
    String port =    System.getProperty("jab.server.port",   "5222");

    IainTestThread iainTT = new IainTestThread();
    JabberModel iainModel = new JabberModel(iainTT);
    HieuTestThread hieuTT = new HieuTestThread();
    JabberModel hieuModel = new JabberModel(hieuTT);

    iainModel.setServerName(server);
    iainModel.setServerAddress(address);
    iainModel.setPort(port);

    iainModel.setUser("iain");
    iainModel.setResource("home");

    hieuModel.setServerName(server);
    hieuModel.setServerAddress(address);
    hieuModel.setPort(port);

    hieuModel.setUser("hieu");
    hieuModel.setResource("work");
    
    iainTT.setModel(iainModel);
    iainTT.start();
    hieuTT.setModel(hieuModel);
    hieuTT.start();
  }

  public class IainTestThread extends TestThread {

    public void run(){
      try {    
        model.connect();
        waitFor("stream:stream",null);
        String groupName = "java-users.group@" + model.getServerName();
        model.sendPresence(groupName + "/smirk",
                        null,
                        null,
                        null,
                        null);
        for (Packet packet = waitFor("presence",null);
             !packet.getFrom().endsWith("RunningMan");
             packet = waitFor("presence",null)){
        }
        model.sendMessage(groupName + "/RunningMan",
                        null,
                        null,
                        "groupchat",
                        null,                   
                        "Hi RunningMan!");
        waitFor("presence","unavailable");
        model.disconnect();
      } catch (Exception ex){
        ex.printStackTrace();
      }
    } // run()
  }
  public class HieuTestThread extends TestThread {

    public void run(){
      try {    
        model.connect();
        waitFor("stream:stream",null);
        sleep(1000);  // Ensure smirk join first
        String groupName = "java-users.group@" + model.getServerName();
        model.sendPresence(groupName + "/RunningMan",
                        null,
                        null,
                        null,
                        null);
        sleep(1000);  // Ensure smirk has a chance to say hi.
        for (Packet packet = waitFor("message",null);
             packet.getFrom().endsWith("smirk");
             packet = waitFor("message",null)){
        }
        model.sendMessage(groupName,
                        null,
                        null,
                        "groupchat",
                        null,	                   
                        "Anyone there?");
        sleep(1000);
        model.sendPresence(groupName + "/RunningMan",
                        "unavailable",
                        null,
                        null,
                        null);
        sleep(1000);
        model.disconnect();
      } catch (Exception ex){
        ex.printStackTrace();
      }
    } // run()
  }
}
