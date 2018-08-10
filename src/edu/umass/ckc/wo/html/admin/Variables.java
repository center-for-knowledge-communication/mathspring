package edu.umass.ckc.wo.html.admin;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Variables {


  private String host;
  private int port;
  private String servletPath;
  private String uri;




  public Variables(String host, String path, int port) {
    this.host = "http://" + host;
    this.port = port;
    this.servletPath = path;
    setUri(getHostAndPort() + "/woj2" + path);
    System.out.println(uri);
  }

  private String getHostAndPort () {
    String p = (port == 80) ? "" : new Integer(port).toString();
    return host + ":" + p;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }
  public String getUri() {
    return uri;
  }

  public String getFlashUri (String swf) {
    return getHostAndPort() + "/wayang/flash/" + swf;
  }

}