package edu.umass.ckc.wo.xml;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class XMLDocumentParser {

  public Document parse (String doc) throws Exception {
        try {
          SAXBuilder builder = new SAXBuilder(false);
//          InputSource ip = new InputSource(new StringReader(doc));
          Document d = builder.build(new StringReader(doc));
          return d;
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        return null;


  }



  public static void main (String[] args) {
    XMLDocumentParser p = new XMLDocumentParser();
    try {
      String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <results><result time=1515 isCorrect=true probName=1b/><result time=594 isCorrect=true probName=2b/><result time=547 isCorrect=true probName=3c/><result time=563 isCorrect=false probName=1c/><result time=562 isCorrect=true probName=2c/><result time=875 isCorrect=false probName=1a/><result time=1016 isCorrect=false probName=4b/></results>";
//      String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <results><result time=\"1515\" isCorrect=\"true\" probName=\"1b\"/></results>";
      Document d = p.parse(s);
      System.out.println(d.toString());

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }


  }
}