package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.servlet.servbase.UserException;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.xml.JDOMUtils;
import org.jdom.Element;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 2/10/14
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 * 
 * Frank 05-13-23	Issue {@link #buildIS(SessionManager)}763 make LCs selectable by class
 */
public class InterventionSelectorSpec implements Comparable<InterventionSelectorSpec> {
    private int id;
    private String name;
    private String onEvent;
    private String className;
    private boolean hasFullClassName=true;
    private List<InterventionSelectorParam> params;
    private Element configXML;
    private int weight;
    private String runFreq;
    private InterventionSelector selector;
    private boolean turnedOn = true;

    public static final String ALWAYS="always";
    public static final String ONCE="once";
    public static final String ONCE_PER_SESSION="oncepersession";
    public static final String ONCE_PER_TOPIC="oncepertopic";

    // When constructing from within a tutor strategy this is used.  Defaults are set for runFreq and weight if not in the definition.
    public InterventionSelectorSpec(boolean hasFullClassPath) {

        this.hasFullClassName = hasFullClassPath;
        this.runFreq=ALWAYS;
        this.weight = 1;
    }

    public InterventionSelectorSpec(String className, List<InterventionSelectorParam> paramSpecs, Element config, boolean hasFullClassPath) {
        this(hasFullClassPath);
        this.className = className;
        this.params = paramSpecs;
        this.configXML=config;
    }



    public InterventionSelectorSpec(Element intervSel, boolean hasFullClassPath) throws UserException {
        this(hasFullClassPath);
        this.onEvent = intervSel.getAttributeValue("onEvent");
        this.className = intervSel.getAttributeValue("class");
        String w = intervSel.getAttributeValue("weight");

        if (w != null)
            this.weight = Integer.parseInt(w);
        else this.weight = 1;
        String freq = intervSel.getAttributeValue("runFreq");  // usually null
        if (freq == null)
            this.runFreq=ALWAYS;
        else if (checkValidFreq(freq))
            this.runFreq = freq.toLowerCase();
        else throw new UserException("runFrequency is not a valid value:" + freq);
        this.configXML = intervSel.getChild("config");
    }

    private boolean checkValidFreq (String inputFreq) {
        inputFreq = inputFreq.toLowerCase();
        return inputFreq.equals(ONCE) || inputFreq.equals(ONCE_PER_SESSION) || inputFreq.equals(ONCE_PER_TOPIC)  || inputFreq.equals(ALWAYS);
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

    public void setOnEvent(String onEvent) {
        this.onEvent = onEvent;
    }

    public String getClassName() {
        return className;
    }

    public List<InterventionSelectorParam> getParams() {
        return params;
    }

    public void setParams(List<InterventionSelectorParam> params) {
        this.params = params;
        //  extract from the params the two special ones that are required to make an intervention selector run.
        for (InterventionSelectorParam p : params) {
            if (p.getName().equalsIgnoreCase("weight"))
                this.weight = Integer.parseInt(p.getValue());
            else if (p.getName().equalsIgnoreCase("runFreq"))
                this.runFreq = p.getValue();
        }
    }



    public void setClassName(String className) {
        this.className = className;
    }


    public String getFullyQualifiedClassname () {
        if (hasFullClassName)
            return this.className;
        else {
            String className = Pedagogy.getFullyQualifiedClassname(Pedagogy.defaultClasspath + ".intervSel2", this.getClassName());
            return className;
        }
    }

    public void setConfig (String config) throws Exception {

        this.configXML = JDOMUtils.getRoot(config);
    }

    public Element getConfigXML() {
        return configXML;
    }

    public String getOnEvent() {
        return onEvent;
    }

    public int getWeight() {
        return weight;
    }

    public String getRunFreq() {
        return runFreq;
    }

    public boolean isRunOnce () {
        if (runFreq == null)
            System.out.println(this.className + " needs to have a runFreq setting");
        return runFreq.equals(ONCE);
    }

    public InterventionSelector buildIS (SessionManager smgr) throws Exception {

        InterventionSelector sel=null;
        sel = (InterventionSelector) Class.forName(this.getFullyQualifiedClassname()).getConstructor(SessionManager.class).newInstance(smgr);
        sel.setParams(this.getParams());
        this.setSelector(sel);
        sel.setConfigXML(this.getConfigXML());
        //sel.setServletInfo(smgr,smgr.getPedagogicalModel());  // Want to put off the call to setServletInfo til just before we call .selectIntervention()
        return sel;
    }


    @Override
    public int compareTo(InterventionSelectorSpec interventionSpec) {
        if (this.getWeight() < interventionSpec.getWeight())
            return -1;
        else if (this.getWeight() > interventionSpec.getWeight())
            return 1;
        else return 0;
    }

    public void setSelector(InterventionSelector selector) {
        this.selector = selector;
    }

    public InterventionSelector getSelector() {
        return selector;
    }

    public void setTurnedOn(boolean on){
        turnedOn = on;
    }

    public boolean getTurnedOn(){
        return turnedOn;
    }

    public String toString () {
        StringBuilder sb = new StringBuilder("InterventionSelector " + id + " " + name + "\n");
        for (InterventionSelectorParam p : this.params) {
            sb.append("\t\t\t" + p.toString() + "\n");
        }
        return sb.toString();
    }

}
