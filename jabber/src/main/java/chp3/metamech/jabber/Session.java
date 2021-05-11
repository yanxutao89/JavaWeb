package chp3.metamech.jabber;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.ListIterator;

import chp3.metamech.io.LoggingReader;
import chp3.metamech.io.LoggingWriter;
import chp3.metamech.io.XercesReader;

/**
A central data structure to track information related to a single
client-server jabber session.

The important information includes:
<ul>
<li>The connection status of the session
<li>The socket associated with the session
<li>The name of the server we're attached to
<li>The StreamID for the Jabber XML stream (established by the server)
</ul>

@author Iain Shigeoka (iainshigeoka@yahoo.com)
*/
public class Session{

  public Session(Socket socket) { setSocket(socket);       }
  public Session()              { setStatus(DISCONNECTED); }

  int priority;
  public int  getPriority()          { return priority;  }
  public void setPriority(int level) { priority = level; }

  Presence presence = new Presence();
  public Presence getPresence(){return presence;}

  JabberID jid;
  public JabberID getJID()               { return jid;  }
  public void     setJID(JabberID newID) { jid = newID; }

  String sid;
  public String getStreamID()                { return sid;     }
  public void   setStreamID(String streamID) { sid = streamID; }

  Socket sock;
  public Socket getSocket()              { return sock;  }
  public void   setSocket(Socket socket) {
    sock = socket;
    in = null;
    out = null;
    setStatus(CONNECTED);
  }

  LinkedList statusListeners = new LinkedList();
  public boolean addStatusListener(StatusListener listener){
    return statusListeners.add(listener);
  }
  public boolean removeStatusListener(StatusListener listener){
    return statusListeners.remove(listener);
  }

  public static final int DISCONNECTED  = 1;
  public static final int CONNECTED     = 2;
  public static final int STREAMING     = 3;
  public static final int AUTHENTICATED = 4;

  int status;
  public int getStatus() { return status;  }

  public synchronized void setStatus(int newStatus){
    status = newStatus;
    ListIterator iter = statusListeners.listIterator();
    while (iter.hasNext()){
      StatusListener listener = (StatusListener)iter.next();
      listener.notify(status);
    }
  }

  public void closeStream() throws IOException {
    getWriter().write("</stream:stream>");
    out.flush();
    setStatus(CONNECTED);
  }

  public void disconnect() throws IOException{
    sock.close();
    setStatus(DISCONNECTED);
  }

  Writer out;
  public Writer getWriter() throws IOException {
    if (out == null){
      out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    }
    return out;
  }

  Reader in;
  public Reader getReader() throws IOException {
    if (in == null){
      in = new XercesReader(sock.getInputStream());
    }
    return in;
  }

  public void setLoggers(Writer inLogger, Writer outLogger) throws IOException {
    in = new LoggingReader(getReader(),inLogger);
    out = new LoggingWriter(getWriter(),outLogger);
  }
}
