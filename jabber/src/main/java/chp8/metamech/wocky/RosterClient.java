package chp8.metamech.wocky;

import java.util.LinkedList;

import chp8.metamech.jabber.xml.*;
import chp8.metamech.jabber.Authenticator;
import chp8.metamech.jabber.Session;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
class RosterClient {

  public static void main(String[] args){
    RosterClient client = new RosterClient();
  }

  public RosterClient(){
    System.out.print("Initializing authenticator...");
    Authenticator.randomToken();
    System.out.println(" initialized.  Starting client...");
    String server =  System.getProperty("jab.server.name",   "localhost");
    String address = System.getProperty("jab.server.address","127.0.0.1");
    String port =    System.getProperty("jab.server.port",   "5222");
    String auth =		 System.getProperty("jab.server.auth",   "plain");

    IainTestThread iainTT = new IainTestThread();
    JabberModel iainModel = new JabberModel(iainTT);
    HieuTestThread hieuTT = new HieuTestThread();
    JabberModel hieuModel = new JabberModel(hieuTT);

    iainModel.setServerName(server);
    iainModel.setServerAddress(address);
    iainModel.setPort(port);
    iainModel.setAuthMode(auth);

    iainModel.setUser("iain");
    iainModel.setPassword("pass");
    iainModel.setResource("dev");

    hieuModel.setServerName(server);
    hieuModel.setServerAddress(address);
    hieuModel.setPort(port);
    hieuModel.setAuthMode(auth);

    hieuModel.setUser("hieu");
    hieuModel.setPassword("pass");
    hieuModel.setResource("dev");

    iainTT.setModel(iainModel);
    iainTT.start();
    hieuTT.setModel(hieuModel);
    hieuTT.start();
  }

  public class IainTestThread extends TestThread {

    public void run(){
      try {
        model.connect();
        model.register();
        model.authenticate();
        do {
          notifyHandlers(packetQueue.pull());
        } while (model.getSessionStatus() != Session.AUTHENTICATED);
        model.sendPresence(null,null,null,null,null);

        String hieuName = "hieu@" + model.getServerName();
        model.sendPresence(hieuName,
                        "subscribe",
                        null,
                        null,
                        null);
        waitFor("presence","subscribed");
        LinkedList groups = new LinkedList();
        groups.add("friends");
        model.sendRosterSet(hieuName,
                        "RunningMan",
                        groups.iterator());
        waitFor("iq","set"); // roster push
        model.sendPresence(null,
                        "unavailable",
                        null,
                        null,
                        null);
        model.disconnect();
      } catch (Exception ex){
        ex.printStackTrace();
      }
    }
  }
  public class HieuTestThread extends TestThread {

    public void run(){
      try {
        model.connect();
        model.register();
        model.authenticate();
        do {
          notifyHandlers(packetQueue.pull());
        } while (model.getSessionStatus() != Session.AUTHENTICATED);
        model.sendPresence(null,null,null,null,null);

        waitFor("presence","subscribe");
        System.out.println("**** Got subscribe request");
        String iainName = "iain@" + model.getServerName();
        model.sendPresence(iainName,
                        "subscribed",
                        null,
                        null,
                        null);
        waitFor("presence","unavailable");
        model.disconnect();
      } catch (Exception ex){
        ex.printStackTrace();
      }
    }
  }
}
