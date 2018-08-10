package edu.umass.ckc.wo.xml;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.util.List;
import java.util.Iterator;

import edu.umass.ckc.wo.exc.DeveloperException;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Sep 17, 2009
 * Time: 12:48:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class JDOMUtils {
 public JDOMUtils() {
    }

    public static void formatFile (String inFile, String outFile) throws Exception {
        SAXBuilder builder = new SAXBuilder(false);
        Document doc = null;
        doc = builder.build(new FileReader(inFile));
        Element root = doc.getRootElement();
        Format f = Format.getPrettyFormat();
        XMLOutputter xout = new XMLOutputter(f);
        FileWriter fw = new FileWriter(outFile);
        fw.write(xout.outputString(root));
        fw.close();

    }


    /**
     * Make a JDOM Document out of the file.
     * @param str
     * @return  JDOM Document
     */
    public static Document makeDocumentOld (InputStream str) {

        SAXBuilder parser = new SAXBuilder();
        try {
            Document doc = parser.build(str);
            return doc;
        } catch (JDOMException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static Document makeDocument (InputStream str) throws JDOMException, IOException {

        SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(str);
            return doc;
    }

    public static Element getRoot (String xml) throws Exception {
        if (xml == null || xml.trim().equals(""))
            return null;
        SAXBuilder builder = new SAXBuilder(false);
        Document doc = null;
        doc = builder.build(new StringReader(xml));
        Element root = doc.getRootElement();
        return root;
    }

    /**
     * Get the Element's attribute value as a String
     * @param e
     * @param attribute
     * @return the value of the attribute.  null if attribute not present.
     */
    public static String getAttribute (Element e, String attribute) {
        Attribute a = e.getAttribute(attribute);
        if (a != null)
            return a.getValue();
        else return null;
    }

    public static String getAttribute (Element e, String attribute, String defaultVal) {
        Attribute a = e.getAttribute(attribute);
        if (a != null)
            return a.getValue();
        else return defaultVal;
    }

    /**
     *  Get the Element's attribute value as an Integer
     * @param e
     * @param attribute
     * @return
     */
    public static Integer getAttributeInteger (Element e, String attribute) {
        Attribute a = e.getAttribute(attribute);
        if (a != null)
            return new Integer(a.getValue());
        else return null;
    }

    public static Boolean getAttributeBoolean(Element e, String attribute){
        Attribute a = e.getAttribute(attribute);
        if (a != null){
            try {
                return a.getBooleanValue();
            }
            catch(Exception ex){
                ex.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static void setAttribute(Element e, String attribute, String value, boolean ignoreEmptyString) {
        if (value == null || (ignoreEmptyString && value.equals("")))
            return;
        else
            e.setAttribute(attribute, value);
    }

    public static void setAttribute(Element e, String attribute, String value) {
        setAttribute(e,attribute,value,false);
    }

    public static void setAttribute(Element e, String attribute, int value) {
        e.setAttribute(attribute, Integer.toString(value));
    }

    public static String toXML(Element e) {
        Format f = Format.getPrettyFormat();
        XMLOutputter o = new XMLOutputter(f);
        return o.outputString(e);
    }

    public static Element warn(String s) {
        Element element = new Element("warning");
        element.addContent(new CDATA(s));
        return element;
    }

    public static Element success() {
        return new Element("success");
    }

    /**
     * Given a parent element and the type of its sub-element which contains its content wrapped in CDATA, fetch the content out
     * of the child and return it.
     * @param parent
     * @param subElementType
     * @param defaultval
     * @return defaultval if no child of this type is present or if CDATA is not present in the child.
     */
    public static String getChildCDATA (Element parent, String subElementType, String defaultval) {
        return getChildCDATATextTrim(parent.getChild(subElementType),defaultval);
    }

    /**
     * Given a parent element and the type of its sub-element which contains its content wrapped in CDATA, fetch the content out
     * of the child and return it.
     * @param parent
     * @param subElementType
     * @return
     * @throws edu.umass.ckc.wo.exc.DeveloperException
     */
    public static String getChildCDATA (Element parent, String subElementType) throws DeveloperException {
        return getChildCDATATextTrim(parent.getChild(subElementType));
    }

    public static String getChildCDATATextTrim(Element elt, String defaultval) {
        if (elt != null) {
            List l = elt.getContent();
            // no content, return the default
            if (l.size() == 0)
                return defaultval;
            Iterator itr = l.iterator();
            // cycle through content and return first CDATA's text
            while (itr.hasNext()) {
                Object o = (Object) itr.next();
                if (o instanceof CDATA) {
                    return (((CDATA) o).getTextTrim());
                }
            }
            // no CDATA children, return the default
            return defaultval;
        }
        return defaultval;
    }

    public static String getChildCDATATextTrim(Element elt) throws DeveloperException {
        if (elt != null) {
            List l = elt.getContent();
            // no content, return the default
            if (l.size() == 0)
                throw new DeveloperException("Element " + elt.getName() + " must have CDATA content");
            Iterator itr = l.iterator();
            // cycle through content and return first CDATA's text
            while (itr.hasNext()) {
                Object o = (Object) itr.next();
                if (o instanceof CDATA) {
                    return (((CDATA) o).getTextTrim());
                }
            }
            // no CDATA children, return the default
            throw new DeveloperException("Element " + elt.getName() + " must have CDATA content");
        } else
            throw new DeveloperException("Element cannot be null");
    }

    public static Element failure() {
       return new Element("failure");
    }

    public static void main(String[] args) {
        try {
            formatFile(args[0],args[1]);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
