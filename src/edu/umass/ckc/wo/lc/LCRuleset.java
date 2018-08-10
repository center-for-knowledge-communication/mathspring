package edu.umass.ckc.wo.lc;

import edu.umass.ckc.wo.event.tutorhut.EventType;
import edu.umass.ckc.wo.event.tutorhut.TutorHutEvent;
import org.apache.log4j.Logger;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * A ruleset is a group of rules that are read from the db or from XML.  The ruleset allows keeping a certain group of
 * rules together.  This class keeps rules in separate lists indexed by event type.  This way running rules for
 * a particular user event can be faster.
 * User: david
 * Date: 1/25/16
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class LCRuleset {
    private static Logger logger = Logger.getLogger(LCRuleset.class);
    private int id = -1;
    private String name;
    private String source;
    private boolean fromDb;
    private boolean fromXML;
//    List<LCRule> rules;
    List<List<LCRule>> rules; // an array indexed by EventType enum to get list of rules for that type of event.
    List<LCMetaRule> metaRules;
    private String description;
    private String notes;
    private List<Element> ruleElements;

    public LCRuleset() {
//        rules = new ArrayList<LCRule>();
        // create the array of LCRule lists one for each event type.
        rules = new ArrayList<List<LCRule>>(EventType.values().length);
        for (int i = 0; i < EventType.values().length; i++) {
            rules.add(i,new ArrayList<LCRule>());

        }
        metaRules = new ArrayList<LCMetaRule>();
    }

    public LCRuleset (String name, int id) {
        this();
        this.id = id;
        this.name = name;
        this.source = "db";
    }

    // to get all the rules in a ruleset must go through the array of events and put togeter
    // a single list.
    public List<LCRule> getRules() {
        List<LCRule> l = new ArrayList<LCRule>();
        for (List<LCRule> evRules : this.rules)
            l.addAll(evRules);
        return l;
    }

//    public void addRule (LCRule r) {
//        rules.add(r);
//    }

    /**
     * We keep an array of rule lists.  The array is indexed by the onEvent type.
     * @param r a rule
     */
    public void addRule (LCRule r) {
        String onEvent = r.getOnEvent();
        EventType t = EventType.fromString(onEvent);
        int ix = t.ordinal();
        rules.get(ix).add(r);
    }

    /**
     * Return the rules from this ruleset that apply to a given type of event
     * @param e event
     * @return list of rules that apply to this event
     */
    public List<LCRule> getRulesForEvent (TutorHutEvent e) {
        EventType onEventType = EventType.getType(e);
        List<LCRule> eventRules = rules.get(onEventType.ordinal());
        return eventRules;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public boolean isFromDb () {
        return this.fromDb;
    }


    public void setSource(String source) {
        this.source = source;
        if (!source.equalsIgnoreCase("DB"))    {
            this.fromXML = true;
            this.fromDb = false;
        }
        else {
            this.fromDb = true;
            this.fromXML = false;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addMetaRule(LCMetaRule r) {
        this.metaRules.add(r);
    }

    public List<LCMetaRule> getMetaRules() {
        return metaRules;
    }

    public void setRuleElements(List<Element> ruleElements) {
        this.ruleElements = ruleElements;
    }

    public List<Element> getRuleElements() {
        return ruleElements;
    }
}
