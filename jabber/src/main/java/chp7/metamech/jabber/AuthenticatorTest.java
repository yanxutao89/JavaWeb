package chp7.metamech.jabber;

import junit.framework.TestCase;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class AuthenticatorTest extends TestCase {

  public AuthenticatorTest(String name) {
    super(name);
  }
  
  void doTst(){
    Authenticator auth = new Authenticator();
    String token = "f56ec4d7";
    String pass = "pass";
    String hash = auth.getZeroKHash(500,
      token.getBytes(),
      pass.getBytes());
    if (auth.isHashAuthenticated(hash,"ad880f830c4e7efdb184fbc584933822f7fcc1ef")){
      System.out.println("Victory!");
    } else {
      System.out.println("Failure!");
    }
  }
  public void testAuth(){
  }
  static public void main(String [] args){
    new AuthenticatorTest("authtest");
  }
}