package chp4.metamech.vorpal;

import java.util.Hashtable;
import java.util.LinkedList;

import chp2.metamech.jabber.JabberID;
import chp3.metamech.jabber.Session;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class SessionIndex {
  // user -> Session LinkedList
  Hashtable userIndex = new Hashtable();
  // jid string -> Session
  Hashtable jidIndex = new Hashtable();

  public Session getSession(String jabberID){
    return getSession(new JabberID(jabberID));
  }

  public Session getSession(JabberID jabberID){
    String jidString = jabberID.toString();
    Session session = (Session)jidIndex.get(jidString);
    if (session == null){
      LinkedList resources = (LinkedList)userIndex.get(jabberID.getUser());
      if (resources == null){ // user offline
        return null;
      }
      session = (Session)resources.getFirst();
    }
    return session;
  }

  public void removeSession(Session session){

    String jidString = session.getJID().toString();
    String user = session.getJID().getUser();
    // Remove from server indexes
    if (jidIndex.containsKey(jidString)){
      jidIndex.remove(jidString);
    }
    LinkedList resources = (LinkedList)userIndex.get(user);
    if (resources == null){
      return;
    }
    if (resources.size() <= 1){
      userIndex.remove(user);
      return;
    }
    resources.remove(session);
  }

  public void addSession(Session session){
    jidIndex.put(session.getJID().toString(),session);
    String user = session.getJID().getUser();
    LinkedList resources = (LinkedList)userIndex.get(user);
    if (resources == null){
      resources = new LinkedList();
      userIndex.put(user,resources);
    }
    resources.addLast(session);
  }
}