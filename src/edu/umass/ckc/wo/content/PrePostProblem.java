package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.tutormeta.Activity;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Aug 17, 2005
 * Time: 12:28:09 PM
 * <p/>
 * PrePostProblem inherits everything from the PrePostProblemDefn .  It also adds the
 * two methods that make it implement the Problem interface.
 * <p/>
 * This keeps the dependency from WO -> WPA a one way project dependency.  We want to avoid a dependency of
 * WPA -> WO so that we can package up a very small jar file with just the files that WPA needs to run.
 */
public class PrePostProblem extends PrePostProblemDefn implements Activity {

    public static final int END_OF_TEST = 0;
    public static final int TESTING_COMPLETE = 1;
    public static final int NORMAL = 2;
    public static final String EOT = "END_OF_TEST";
    public static final String TC = "PREPOST_TESTING_COMPLETE";
    private int type=NORMAL;
    public static final String PRE = "pretestProblem"; // for transaction logging
    public static final String POST = "posttestProblem"; // for transaction logging
    public static final String DEFAULT = "prepostProblem"; // for transaction logging
    private String logEventName=DEFAULT;
//    private static final String BUTTON_ACTIVE_DELAY = "10000";
    private static final String BUTTON_ACTIVE_DELAY = "0";

    private int numProbsInTest;   // these two variables really don't belong here but at the time we
    private int numProbsSeen;     // create the problem we know these two things and need a package for returning them from the selector
    private int numCorrect=0;


    /**
     * Two special-case problem objects may be built: END_OF_TEST and TESTING_COMPLETE.  The problem selector will
     * build them as appropriate.
     * @param type
     */
    public PrePostProblem (int type) {
        this.type = type;
    }


    public PrePostProblem (int type, int numCorrect, int numProbsInTest) {
        this.type = type;
        this.numCorrect = numCorrect;
        this.numProbsInTest = numProbsInTest;
    }


    public PrePostProblem(PrePostProblemDefn p, int numProbsInTest, int numProbsSeen) {
        this(p);
        this.numProbsInTest = numProbsInTest;
        this.numProbsSeen = numProbsSeen;
    }
    /**
     * The reason we have this constructor is so that we can cast an object of type model.PrePostProblem into content.PrePostProblem
     *
     * @param p
     */
    public PrePostProblem(PrePostProblemDefn p) {

            this.setaAns(p.getaAns());
            this.setAnsType(p.getAnsType());
            this.setAnswer(p.getAnswer());
            this.setaURL(p.getaURL());
            this.setbAns(p.getbAns());
            this.setbURL(p.getbURL());
            this.setcAns(p.getcAns());
            this.setcURL(p.getcURL());
            this.setdAns(p.getdAns());
            this.setdURL(p.getdURL());
            this.seteAns(p.geteAns());
            this.seteURL(p.geteURL());
            this.setDescr(p.getDescr());
            this.setId(p.getId());
            this.setName(p.getName());
            this.setProblemSet(p.getProblemSet());
            this.setUrl(p.getUrl());
            type = NORMAL;

    }

    public String getFlashOut() {
        if (type == END_OF_TEST)
            return "&numProbsInTest=" + this.numProbsInTest + "&numProbsCorrect=" + this.numCorrect + "&probXML=" + this.getXML();
        else return "&numProbsInTest=" + this.numProbsInTest + "&numProbsSeen=" + this.numProbsSeen + "&probXML=" + this.getXML();
    }

    public void setLogEventName (String s) {
        this.logEventName = s;
    }

    public String logEventName() {
        return this.logEventName;
    }

    /**
     * Sample XML
     * <PrePostProblem name="Problem 1" ansType="0" answer="180"
     * problemSet="2">
     * <description url="images/1figure.jpg">
     * <![CDATA[Each of the small squares in the figure above has an area of 4.  If the shortest side of the triangle is equal in length to 2 sides of a smal square, what is the area of the shaded triangle?]]>
     * </description>
     * </PrePostProblem>
     * <p/>
     * <p/>
     * <PrePostProblem name="Problem 1" ansType="1" answer="d" problemSet="2">
     * <description url="images/figure2.jpg"><![CDATA[In the figure above, what is the value of b?]]></description>
     * <choice label="a"><![CDATA[9]]></choice>
     * <choice label="b"><![CDATA[18]]></choice>
     * <choice label="c"><![CDATA[27]]></choice>
     * <choice label="d"><![CDATA[36]]></choice>
     * <choice label="e"><![CDATA[45]]></choice> </PrePostProblem>
     * <p/>
     * <p/>
     * <p/>
     * <PrePostProblem name="Problem 1" ansType="1" answer="e" problemSet="2">
     * <description url="images/figure3.jpg"><![CDATA[In the figure above, if
     * the perimeter of square ABCD is 8, what is the perimeter of square
     * RSTU?]]></description>
     * <choice label="a" url="images/answer1.jpg"/>
     * <choice label="b" url="images/answer2.jpg"/>
     * <choice label="c" url="images/answer3.jpg"/>
     * <choice label="d" url="images/answer4.jpg"/>
     * <choice label="e" url="images/answer5.jpg"/>
     * </PrePostProblem>
     */

    public String getXML() {
        // If the constructor for this object was passed null, that represents the end of the problem set and thus we
        // have created a dummy PrePostProblem whose XML is simply 'END'
        if (type == END_OF_TEST)
            return EOT;
        else if (type == TESTING_COMPLETE)
            return TC;
        Element ppp = new Element("PrePostProblem");
        ppp.setAttribute("id", Integer.toString(this.getId()));
        ppp.setAttribute("name", this.getName() == null ? "" : this.getName());
        ppp.setAttribute("ansType", Integer.toString(this.getAnsType()));
        // dm 9/14/11 stop sending answer out as attribute and make it go as CDATA
//        if (this.getAnswer() != null)
//            ppp.setAttribute("answer", this.getAnswer());
        ppp.setAttribute("timeDelay", BUTTON_ACTIVE_DELAY);
        ppp.setAttribute("problemSet", Integer.toString(this.getProblemSet()));
        // dm 9/14/11 made answer go out in CDATA so it is in same form as the choices which also
        // go out as CDATA (Flash may be parsing them differently and string comparisons fail)
          if (this.getAnswer() != null) {
            Element answer = new Element("answer");
            answer.setContent(new CDATA(this.getAnswer()));
            ppp.addContent(answer);
        }
        Element descr = new Element("description");
        if (this.getUrl() != null)
            descr.setAttribute("url", this.getUrl());
        descr.setContent(new CDATA(this.getDescr()));
        ppp.addContent(descr);
        if (this.isMultiChoice()) {
            String ansURL;
            Element choice;
            choice = new Element("choice");
            choice.setAttribute("label", "a");
            ansURL = this.getaURL();
            if (ansURL != null)
                choice.setAttribute("url", ansURL);
            else
                choice.setContent(new CDATA(this.getaAns()));
            ppp.addContent(choice);
            choice = new Element("choice");
            choice.setAttribute("label", "b");
            ansURL = this.getbURL();
            if (ansURL != null)
                choice.setAttribute("url", ansURL);
            else
                choice.setContent(new CDATA(this.getbAns()));
            ppp.addContent(choice);
            choice = new Element("choice");
            choice.setAttribute("label", "c");
            ansURL = this.getcURL();
            if (ansURL != null)
                choice.setAttribute("url", ansURL);
            else
                choice.setContent(new CDATA(this.getcAns()));
            ppp.addContent(choice);
            choice = new Element("choice");
            choice.setAttribute("label", "d");
            ansURL = this.getdURL();
            if (ansURL != null)
                choice.setAttribute("url", ansURL);
            else
                choice.setContent(new CDATA(this.getdAns()));
            ppp.addContent(choice);
            choice = new Element("choice");
            choice.setAttribute("label", "e");
            ansURL = this.geteURL();
            if (ansURL != null)
                choice.setAttribute("url", ansURL);
            else
                choice.setContent(new CDATA(this.geteAns()));
            // choice E may be omitted
            if (ansURL != null || this.geteAns() != null)
                ppp.addContent(choice);

        }
        XMLOutputter xout = new XMLOutputter(Format.getCompactFormat());
        String res = xout.outputString(ppp);
        return res;
    }

    public boolean isEndOfTest () {
        return type == END_OF_TEST;
    }

    public boolean isTestingComplete () {
        return type == TESTING_COMPLETE;
    }


    public int getNumProbsInTest() {
        return numProbsInTest;
    }

    public void setNumProbsInTest(int numProbsInTest) {
        this.numProbsInTest = numProbsInTest;
    }

    public int getNumProbsSeen() {
        return numProbsSeen;
    }

    public void setNumProbsSeen(int numProbsSeen) {
        this.numProbsSeen = numProbsSeen;
    }

}
