package chp7.metamech.jabber;

import junit.framework.TestCase;

public class JabberIDTest extends TestCase{

  JabberID id1;
  JabberID id2;
  JabberID id3;
  JabberID id4;

  public JabberIDTest(String name){
    super(name);
  }

  public void setUp(){
    id1 = new JabberID("user1@jabini.org/howdy");
    id2 = new JabberID("user2@JaBini.org/doody");
    id3 = new JabberID("user1@jabini.com");
    id4 = new JabberID("jabini.org");
  }

  public void testEqualsDomain(){
    assertTrue("Basic != test",      !id1.equalsDomain("other.com"));
    assertTrue("Resource stripped?",  id1.equalsDomain("jabini.org"));
    assertTrue("Case sensitive",      id1.equalsDomain("Jabini.org"));
    assertTrue("overloaded + case + resource", id1.equalsDomain(id2));
    assertTrue("overloaded + diff. domain", !id1.equalsDomain(id3));
  }

  public void testEqualsUser(){
    assertTrue("basic != test",   !id1.equalsUser("person"));
    assertTrue("case sensitive",   id1.equalsUser("UseR1"));
    assertTrue("overloaded",      !id1.equalsUser(id2));
    assertTrue("overloaded",       id1.equalsUser(id3));
    assertTrue("null user",       !id4.equalsUser("user1"));
  }
}
