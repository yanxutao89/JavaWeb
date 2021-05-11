package chp6.metamech.wocky;

import com.metamech.jabber.xml.*;
import chp2.metamech.log.Log;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
class SimpleMessageClient {

  public static void main(String[] args){
    new SimpleMessageClient();
  }

  public SimpleMessageClient(){
    String server =  System.getProperty("jab.server.name",   "localhost");
    String address = System.getProperty("jab.server.address","127.0.0.1");
    String port =    System.getProperty("jab.server.port",   "5222");
    String auth =		 System.getProperty("jab.server.auth",   "plain");

    BuffyTestThread buffyTT = new BuffyTestThread();
    JabberModel buffyModel = new JabberModel(buffyTT);
    AngelTestThread angelTT = new AngelTestThread();
    JabberModel angelModel = new JabberModel(angelTT);

    buffyModel.setServerName(server);
    buffyModel.setServerAddress(address);
    buffyModel.setPort(port);
    buffyModel.setAuthMode(auth);

    buffyModel.setUser("buffy");
    buffyModel.setResource("bronze");
    buffyModel.setPassword("buffPass");

    angelModel.setServerName(server);
    angelModel.setServerAddress(address);
    angelModel.setPort(port);
    angelModel.setAuthMode(auth);

    angelModel.setUser("angel");
    angelModel.setResource("graveyard");
    angelModel.setPassword("angelPass");
    
    buffyTT.setModel(buffyModel);
    buffyTT.start();
    angelTT.setModel(angelModel);
    angelTT.start();
  }

  public class BuffyTestThread extends TestThread {

    public void run(){
      try {    
        Log.info("Buffy: Connecting");
        model.connect();
        Log.info("Buffy: registering");
        model.register();
        Log.info("Buffy: Sending message");
        model.sendMessage("angel@" + model.getServerName(),
                          "Want to patrol?",
                          "thread_id",
                          "normal",
                          "msg_id_buffy",
                          "Hey, wondered if you wanted to patrol with me tonight?");
        Log.info("Buffy: waiting for message from angel");
        waitFor("message",null);
        Log.info("Buffy: disconnecting");
        model.disconnect();
      } catch (Exception ex){
        ex.printStackTrace();
      }
    } // run()
  }
    public class AngelTestThread extends TestThread {

    public void run(){
      try {    
        Log.info("Angel: connecting");
        model.connect();
        Log.info("Angel: registering");
        model.register();
        sleep(500);
        Log.info("Angel: waiting for message from buffy");
        for (Packet packet = waitFor("message",null);
             !packet.getFrom().startsWith("buffy");
             packet = waitFor("message",null)){
          Log.info("Angel: message from " + packet.getFrom());
        }
        Log.info("Angel: sending message to buffy");
        model.sendMessage("buffy@" + model.getServerName(),
                          "Re: Want to patrol?",
                          "thread_id",
                          "normal",
                          "msg_id_angel",
                          "Sure, I'd love to go.");
        Log.info("Angel: disconnecting");
        model.disconnect();
      } catch (Exception ex){
        ex.printStackTrace();
      }
    } // run()
  }
}
