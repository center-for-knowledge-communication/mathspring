package edu.umass.ckc.wo.lc;

import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.event.tutorhut.TutorHutEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Runs the rules that are part of a pedagogy's learning companion (a character and a group of rule-sets)
 * User: david
 * Date: 5/10/16
 * Time: 8:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class InferenceEngine {
    private static Logger logger = Logger.getLogger(InferenceEngine.class);
    private SessionManager smgr;
    private StudentDataCache cache;

    public InferenceEngine (SessionManager smgr) {
        this.smgr=smgr;
        this.cache = new StudentDataCache(); // place to store computed values so that we don't repeat work
    }

    /**
     * When an event comes in from the user, we run the rules to find a possible response from the
     * learning companion to include with the data returned to the client.
     *
     * Steps:
     * Gather all rulesets for user
     * For each ruleset:
     *     for each rule that applies to the given event:
     *         if rule satisfies the rulesets meta-rules
     *             add to candidates list
     * sort candidates list by priority
     * for each rule in candidates list:
     *    if rule applies, fire it.
     * @param event
     */
    public void runLCRules (SessionEvent event) {

    }

    private void logRuleFiring (LCRule rule, String info) throws Exception {
        ResultSet rs=null;
        PreparedStatement ps=null;
        Connection conn = smgr.getConnection();
        try {
            String q = "insert into lcrulefirelog (ruleid, sessid, time, info) values (?,?,?,?)";
            ps = conn.prepareStatement(q);
            ps.setInt(1,rule.getId());
            ps.setInt(2,smgr.getSessionNum());
            ps.setTimestamp(3,new Timestamp(System.currentTimeMillis()));
            ps.setString(4, info);
            ps.execute();
        }

        finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    /**
     * Given a bunch of rulesets and an event, return the rule that applies.   A Pedagogy is defined to have a learning companion
     * and a group of rule-sets.   Each rule-set has meta-rules that control the rules in that set.
     *
     * When a user creates an event in the tutor, we use this class to run the rules in the associated with the pedagogy.
     * It finds rules that apply to event type and that satisfy the meta-rules and then checks each rule and stops at the first one that matches.
     *
     * When an event comes in from the user, we run the rules to find a possible response from the
     * learning companion to include with the data returned to the client.
     *
     * Steps:
     * Gather all rulesets for user
     * For each ruleset:
     *     for each rule that applies to the given event:
     *         if rule satisfies the rulesets meta-rules (or if the rule overrides the meta-rule, it should satisfy the override condition)
     *             add to candidates list
     * sort candidates list by priority
     * for each rule in candidates list:
     *    if rule applies, fire it.
     *
     *    Issues with the above:  if there is more than one rule-set, how do we decide what order to go through them?
     *
     * @param event
     * @param rulesets
     * @return
     * @throws Exception
     */
    public LCRule runRulesForEvent(TutorHutEvent event, List<LCRuleset> rulesets) throws Exception {
        List<LCRule> rulesThatApply = new ArrayList<LCRule>();
        for (LCRuleset rs: rulesets) {
            List<LCRule> eventRules = rs.getRulesForEvent(event); // get the rules that apply to the event
            // add rules to rulesThatApply - add the ones that satisfy the meta ruels.
            rulesThatSatisfyMetaRules(rs, eventRules, event, rulesThatApply);

        }
        Collections.sort(rulesThatApply); // sorts rules according to their priority
        for (LCRule r : rulesThatApply) {
            r.setup(smgr,event, cache);
            StringBuilder ruleFireInfo = new StringBuilder();
            boolean res = r.test(ruleFireInfo);
            // if the result of testing the rule is true, then we stop testing rules and return it
            if (res) {
                logger.debug("Rule" + r.getName() + " is true");
                LCAction act = r.getAction();
                logger.debug("Action: " + act.getMsgText());
                logRuleFiring(r, ruleFireInfo.toString());
                RuleHistoryCache.getInstance().addRuleInstantiation(smgr.getSessionNum(),r);
                return r;
            }
        }

        return null;
    }

    /**
     * From a list of candidates we go through the rulesets meta-rules and return the list of rules
     * that satisfy all the meta ruels.
     * @param ruleset
     * @param candidates rules for a given event
     * @param event
     * @return
     */
    private void rulesThatSatisfyMetaRules(LCRuleset ruleset, List<LCRule> candidates, TutorHutEvent event, List<LCRule> rulesThatApply) throws Exception {
        rulesThatApply.addAll(candidates); // start out with all the rules being in the list.
        List<LCMetaRule> metaRules = ruleset.getMetaRules();
        long now = System.currentTimeMillis();
        // Nota Bene:  We only look at rules for this user's CURRENT SESSION
        StudentRuleHistory hist = RuleHistoryCache.getInstance().getStudentHistory(smgr.getSessionNum());
        if (hist != null) {
            // TODO need priority on meta rules.
            for (LCMetaRule mr : metaRules) {
                // Remove rules from rulesThatApply that do not satisfy all the meta-rule.
                applyMetaRule(mr, hist, rulesThatApply, now);
                // rulesThatApply.addAll(rulesThatSatisfyMetaRule(mr, hist, candidates, now));
            }
        }

    }


    // Delete from the rulesThatApply those that do not satisfy the meta-rule.
    // The given meta-rule may be overridden with different values in the rule itself so we check to see if the rule
    // has a metarule-override for the named meta-rule and use the value in the rule rather than in the meta-rule if it does.
    private void applyMetaRule(LCMetaRule mr, StudentRuleHistory hist, List<LCRule> rulesThatApply, long now) {
        ListIterator<LCRule> iter = rulesThatApply.listIterator();
        while(iter.hasNext()){
            LCRule r = iter.next();
            // The isSatisfied method will take into account rule r's override values of mr
            if (! mr.isSatisfied(hist,r,now))
                iter.remove();
        }

    }

}
