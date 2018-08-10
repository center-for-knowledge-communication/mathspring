package edu.umass.ckc.wo.content;

import org.jdom.Element;
import org.jdom.CDATA;

import java.sql.Blob;


/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jul 11, 2005
 * Time: 11:09:23 AM
 */
public class PrePostProblemDefn extends Problem {
    public static final int SHORT_ANSWER = 0;
    public static final int MULTIPLE_CHOICE = 1;
    public static final int LONG_ANSWER = 2;
    private int id;
    private String name;
    private String descr;
    private String url;
    private boolean hasImage;
    private Blob img;
    private int ansType;
    private String answer;
    private int problemSet;
    private String aAns;
    private String bAns;
    private String cAns;
    private String dAns;
    private String eAns;
    private String aURL;
    private String bURL;
    private String cURL;
    private String dURL;
    private String eURL;
    private int numProbsInTest;   // these two variables really don't belong here but at the time we
    private int numProbsSeen;     // create the problem we know these two things and need a package for returning them from the selector
    private int preNumProbsCorrect=0;
    private int postNumProbsCorrect=0;
    private int waitTimeSecs;  // how many seconds we wait before warning student they need to answer.
    private String answerString;
    private String imageFilename;

    public PrePostProblemDefn () {
        id = -1;
    }

    public PrePostProblemDefn(int id, String name, String descr, String url, int ansType, String answer, int problemSet,
                              String aAns, String bAns, String cAns, String dAns, String eAns, String aURL,
                              String bURL, String cURL, String dURL, String eURL, int waitTimeSecs, String imgFilename) {
        this.id = id;
        this.name = name;
        this.descr = descr;
        this.url = url;
        this.ansType = ansType;
        this.answer = cleanAns(answer);
        this.problemSet = problemSet;
        this.aAns = cleanAns(aAns);
        this.bAns = cleanAns(bAns);
        this.cAns = cleanAns(cAns);
        this.dAns = cleanAns(dAns);
        this.eAns = cleanAns(eAns);
        this.aURL = cleanAns(aURL);
        this.bURL = cleanAns(bURL);
        this.cURL = cleanAns(cURL);
        this.dURL = cleanAns(dURL);
        this.eURL = cleanAns(eURL);
        this.waitTimeSecs = waitTimeSecs;
        this.imageFilename = imgFilename;
        this.hasImage= (imgFilename != null);
    }

    private String cleanAns (String a) {
        if (a == null) return null;
        if (a.trim().equals(""))
            return null;
        else return a;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getAnsType() {
        return ansType;
    }

    public void setAnsType(int ansType) {
        this.ansType = ansType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getProblemSet() {
        return problemSet;
    }

    public void setProblemSet(int problemSet) {
        this.problemSet = problemSet;
    }

    public String getaAns() {
        return aAns;
    }

    public void setaAns(String aAns) {
        this.aAns = aAns;
    }

    public String getbAns() {
        return bAns;
    }

    public void setbAns(String bAns) {
        this.bAns = bAns;
    }

    public String getcAns() {
        return cAns;
    }

    public void setcAns(String cAns) {
        this.cAns = cAns;
    }

    public String getdAns() {
        return dAns;
    }

    public void setdAns(String dAns) {
        this.dAns = dAns;
    }

    public String geteAns() {
        return eAns;
    }

    public void seteAns(String eAns) {
        this.eAns = eAns;
    }

    public String getaURL() {
        return aURL;
    }

    public void setaURL(String aURL) {
        this.aURL = aURL;
    }

    public String getbURL() {
        return bURL;
    }

    public void setbURL(String bURL) {
        this.bURL = bURL;
    }

    public String getcURL() {
        return cURL;
    }


    public void setcURL(String cURL) {
        this.cURL = cURL;
    }

    public String getdURL() {
        return dURL;
    }

    public void setdURL(String dURL) {
        this.dURL = dURL;
    }

    public String geteURL() {
        return eURL;
    }

    public void seteURL(String eURL) {
        this.eURL = eURL;
    }

    public int getWaitTimeSecs() {
        return waitTimeSecs;
    }

    public void setWaitTimeSecs(int waitTimeSecs) {
        this.waitTimeSecs = waitTimeSecs;
    }



    /**
     * <PrePostProblem name="Problem 1" ansType="0" answer="180" problemSet="2">
     *     <description url="http://localhost/someimage.jpg"><![CDATA[What is the angle ABC?]]></description>
     * </PrePostProblem>
     *
     * or
     * <PrePostProblem name="Problem 1" ansType="1" answer="c" problemSet="2">
     *   <description url="http://localhost/someimage.jpg"><![CDATA[What is the angle ABC?]]></description>
     *   <choice label="a"><![CDATA[34]]></choice>
     *   <choice label="b"><![CDATA[35]]></choice>
     *   <choice label="c"><![CDATA[180]]></choice>
     *   <choice label="d"><![CDATA[90]]></choice>
     *   <choice label="e"><![CDATA[360]]></choice>
     * </PrePostProblem>
     * or
     * <PrePostProblem name="Problem 1" ansType="1" answer="e" problemSet="2">
     *   <description url="http://localhost/someimage.jpg"><![CDATA[Which answer is the best?]]></description>
     *   <choice label="a" url="http://localhost/choiceA.jpg"/>
     *   <choice label="b" url="http://localhost/choiceB.jpg"/>
     *   <choice label="c" url="http://localhost/choiceC.jpg"/>
     *   <choice label="d" url="http://localhost/choiceD.jpg"/>
     *   <choice label="e" url="http://localhost/choiceE.jpg"/>
     * </PrePostProblem>
     *
     * @return
     */
    public Element toJDOM () {
        Element e = new Element("PrePostProblem");
        e.setAttribute("name",this.getName());
        e.setAttribute("ansType",Integer.toString(this.getAnsType()));
        e.setAttribute("answer",this.getAnswer());
        e.setAttribute("problemSet",Integer.toString(this.getProblemSet()));
        e.setAttribute("delayTime","20000");
        Element descr = new Element("description");
        descr.setContent(new CDATA(this.getDescr()));
        descr.setAttribute("url",this.getUrl());
        e.addContent(descr);
        if (!this.isShortAnswer()) {
            Element choice = new Element("choice");
            choice.setAttribute("label","a");
            choice.setContent(new CDATA(this.getaAns()));
            choice.setAttribute("url",this.getaURL());
            e.addContent(choice);
            choice = new Element("choice");
            choice.setAttribute("label","b");
            choice.setContent(new CDATA(this.getbAns()));
            choice.setAttribute("url",this.getbURL());
            e.addContent(choice);
            choice = new Element("choice");
            choice.setAttribute("label","c");
            choice.setContent(new CDATA(this.getcAns()));
            choice.setAttribute("url",this.getcURL());
            e.addContent(choice);
            choice = new Element("choice");
            choice.setAttribute("label","d");
            choice.setContent(new CDATA(this.getdAns()));
            choice.setAttribute("url",this.getdURL());
            e.addContent(choice);
            choice = new Element("choice");
            choice.setAttribute("label","e");
            choice.setContent(new CDATA(this.geteAns()));
            choice.setAttribute("url",this.geteURL());
            e.addContent(choice);
        }
        return e;
    }

    public String getHTML (String uri) {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getName() + "<br> <img src=\"");
        sb.append(uri+this.getUrl());
        sb.append("\" >");
        sb.append("<br>" + this.getDescr());
        if (this.ansType == MULTIPLE_CHOICE) {
            sb.append("<p>");
            sb.append("<input type=\"radio\" name=\"group1\" value=\"A\">");
            sb.append((this.aAns != null) ? (this.aAns + "<br>") : ("<img src=\"" + uri+this.aURL+ "\"><br>"));
            sb.append("<input type=\"radio\" name=\"group1\" value=\"B\">");
            sb.append((this.bAns != null) ? (this.bAns + "<br>") : ("<img src=\"" + uri+this.bURL+ "\"><br>"));
            sb.append("<input type=\"radio\" name=\"group1\" value=\"C\">");
            sb.append((this.cAns != null) ? (this.cAns + "<br>") : ("<img src=\"" + uri+this.cURL+ "\"><br>"));
            sb.append("<input type=\"radio\" name=\"group1\" value=\"D\">");
            sb.append((this.dAns != null) ? (this.dAns + "<br>") : ("<img src=\"" + uri+this.dURL+ "\"><br>"));
            if (eAns!=null || eURL!=null) {
                sb.append("<input type=\"radio\" name=\"group1\" value=\"E\">");
                sb.append((this.eAns != null) ? (this.eAns + "<br>") : ("<img src=\"" + uri+this.eURL+ "\"><br>"));
            }
        }
        else {
            sb.append("<p>Free Entry Text:__________  (answer is " + this.answer + ")");
        }
        return sb.toString();
    }

    public  boolean isShortAnswer() {
        return this.ansType == SHORT_ANSWER;
    }
    public boolean isMultiChoice () {
        return getAnsType() == MULTIPLE_CHOICE;
    }
    public boolean isLongAnswer () {
        return getAnsType() == LONG_ANSWER;
    }


    public String toString () {
        return id + ":" + name;
    }

    public boolean isNew() {
        return id == -1;
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

    public void setPrePostNumProbsCorrect(int prenumCorrect, int postnumCorrect) {
        this.preNumProbsCorrect = prenumCorrect;
        this.postNumProbsCorrect = postnumCorrect;
    }

    public int getPreNumProbsCorrect() {
        return preNumProbsCorrect;
    }

    public int getPostNumProbsCorrect() {
        return postNumProbsCorrect;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public Blob getImg() {
        return img;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public String getAnswerString() {
        String correctChoice = this.answer;
        if (correctChoice.equalsIgnoreCase("a"))
            return this.aAns;
        else if (correctChoice.equalsIgnoreCase("b"))
            return this.bAns;
        else if (correctChoice.equalsIgnoreCase("c"))
            return this.cAns;
        else if (correctChoice.equalsIgnoreCase("d"))
            return this.dAns;
        else if (correctChoice.equalsIgnoreCase("e"))
            return this.eAns;
        else return "";
    }

    public boolean isImageAnswer () {
        String ans = getAnswerString();
        return ans.toLowerCase().contains("<img");
    }
}
