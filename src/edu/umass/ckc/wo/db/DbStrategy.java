package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.lc.DbLCRule;
import edu.umass.ckc.wo.lc.LCRuleset;
import edu.umass.ckc.wo.strat.*;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelectorParam;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelectorSpec;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.LearningCompanion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marshall on 6/14/17.
 */
public class DbStrategy {


    public static List<TutorStrategy> getStrategies (Connection conn, int classId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String q = "select s.id, s.name, s.lcid " +
                    "from strategy s where s.classid = ?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, classId);
            rs = ps.executeQuery();
            List<TutorStrategy> all = new ArrayList<TutorStrategy>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int lcid = rs.getInt("lcid");
                TutorStrategy ts = new TutorStrategy();
                ts.setId(Integer.toString(id));
                ts.setName(name);
                ts.setLcid(lcid);
                all.add(ts);
            }
            return all;
        } finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
    }
    /**
     * Load the TutorStrategy and its learning companion stuff.
     * @param conn
     * @param stratId
     * @return
     * @throws Exception
     */
    public static TutorStrategy getStrategy (Connection conn, int stratId) throws Exception {
         ResultSet rs = null;
          PreparedStatement ps = null;
          try {
              String q = "select name, lcid, login_sc_id, lesson_sc_id, tutor_sc_id from strategy s where s.id = ?";
              ps = conn.prepareStatement(q);
              ps.setInt(1, stratId);
              rs = ps.executeQuery();
              if (rs.next()) {
                  String name = rs.getString(1);
                  int lcid = rs.getInt(2);
                  TutorStrategy ts = new TutorStrategy();
                  ts.setId(Integer.toString(stratId));
                  ts.setName(name);

                  int login_sc_id = rs.getInt(3);
                  int lesson_sc_id = rs.getInt(4);
                  int tutor_sc_id = rs.getInt(5);
                  ts.setLogin_sc(getClassStrategyComponent(conn,login_sc_id));
                  ts.setLesson_sc(getClassStrategyComponent(conn,lesson_sc_id));
                  ts.setTutor_sc(getClassStrategyComponent(conn,tutor_sc_id));
                  ts.setLcid(lcid);
                  loadLC(conn, ts);
                  return ts;
              }
              else return null;
          } finally {
                if (ps != null)
                     ps.close();
                if (rs != null)
                     rs.close();
          }
    }

    /**
     * If its a simple LC (non rule-based), we'll just wind up with a classname, o/w we will be loading up all the rulesets
     * and the other junk that is part of a learning companion.
     * @param conn
     * @param ts
     * @throws Exception
     */
    public static void loadLC (Connection conn, TutorStrategy ts) throws Exception {
         ResultSet rs = null;
          PreparedStatement ps = null;
          try {
              String q = "select name,charName,classname from lc where id=?";
              ps = conn.prepareStatement(q);
              ps.setInt(1, ts.getLcid());
              rs = ps.executeQuery();
              if (rs.next()) {
                  LC lc = new LC();
                  ts.setLc(lc);
                  lc.setId(ts.getLcid());
                  lc.setName(rs.getString(1));
                  lc.setCharacter(rs.getString(2));
                  lc.setClassName(rs.getString(3));
                  loadRuleSets(conn,lc);
              }
          }  finally {
                if (ps != null)
                     ps.close();
                if (rs != null)
                     rs.close();
          }

    }

    /**
     * If the lc has rule sets mapped to it, load them up.
     * @param conn
     * @param lc
     * @throws Exception
     */
    private static void loadRuleSets(Connection conn, LC lc) throws Exception {
         ResultSet rs = null;
          PreparedStatement ps = null;
          try {
              String q = "select s.id, s.name, s.description, s.notes from lc_ruleset_map m, ruleset s where lcid=?";
              ps = conn.prepareStatement(q);

              ps.setInt(1, lc.getId());
              rs = ps.executeQuery();
              while (rs.next()) {
                  LCRuleset lcrs = new LCRuleset();
                  int id = rs.getInt(1);
                  String name = rs.getString(2);
                  String descr = rs.getString(3);
                  String notes = rs.getString(4);
                  lcrs.setId(id);
                  lcrs.setName(name);
                  lcrs.setSource("db");
                  lcrs.setDescription(descr);
                  lcrs.setNotes(notes);
                  DbLCRule.readRuleSet(conn,id,lcrs);
                  lc.addRuleset(lcrs);

              }
          } finally {
                if (ps != null)
                     ps.close();
                if (rs != null)
                     rs.close();
          }
    }



    private static ClassStrategyComponent getClassStrategyComponent(Connection conn, int scId) throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            // retrieve info about the strategy component
            String q = "select name, classname from strategy_component where id=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, scId);
            rs = ps.executeQuery();
            boolean flag = true;
            ClassStrategyComponent sc=null;
            if (rs.next())
                sc= new ClassStrategyComponent(scId,rs.getString(1), rs.getString(2));
            List<SCParam> params = getSCParams(conn,scId);
            sc.setParams(params);
            List<InterventionSelectorSpec> is_specs = getSCInterventionSelectors(conn,scId);
            sc.setInterventionSelectors(is_specs);
            return sc;

        } finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
    }

    private static List<InterventionSelectorSpec> getSCInterventionSelectors(Connection conn, int scId) throws Exception {
        // get the sc's intervention selectors from the scismap
        PreparedStatement ps = conn.prepareStatement("select id, config, intervention_selector_id from sc_is_map where strategy_component_id=? and isactive=1");
        ResultSet rs = null;
        List<InterventionSelectorSpec> specs = new ArrayList<InterventionSelectorSpec>();
        try {
            ps.setInt(1,scId);
            rs = ps.executeQuery();
            // Build each intervention selector spec
            while (rs.next()) {
                int scisId = rs.getInt(1);
                String config = rs.getString(2);
                int iselId = rs.getInt(3);
                InterventionSelectorSpec interventionSelector = getClassSCInterventionSelector(conn, iselId, scisId);
                interventionSelector.setConfig(config);
                specs.add(interventionSelector);
            }
            return specs;
        } finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
    }

    /**
     * For a given strategy component and class, get all the sc params.
     * @param conn
     * @param scId
     * @return
     */
    private static List<SCParam> getSCParams(Connection conn, int scId) throws SQLException {
         ResultSet rs = null;
          PreparedStatement ps = null;
          try {
              String q = "select m.sc_param_id, p.name, p.value from sc_param_map m, sc_param p " +
                      "where m.strategy_component_id=? and m.sc_param_id=p.id and p.isactive=1";
              ps = conn.prepareStatement(q);
              ps.setInt(1, scId);
              rs = ps.executeQuery();
              List<SCParam> params = new ArrayList<SCParam>();
              while (rs.next()) {
                  int id = rs.getInt(1);
                  String name = rs.getString(2);
                  String value = rs.getString(3);
                  SCParam p = new SCParam(id,name,value);
                  params.add(p);
              }
              return params;
          } finally {
                if (ps != null)
                     ps.close();
                if (rs != null)
                     rs.close();
          }
    }

    /**
     * Create a ClassSCInterventionSelector by gettings its parts from the db.
     * @param conn
     * @param iselId
     * @return
     */
    private static InterventionSelectorSpec getClassSCInterventionSelector(Connection conn, int iselId, int scisId) throws SQLException {
         ResultSet rs = null;
          PreparedStatement ps = null;
          try {
              String q = "select name,className,onEvent from intervention_selector isel where id=?";
              ps = conn.prepareStatement(q);
              ps.setInt(1, iselId);
              rs = ps.executeQuery();
              if (rs.next()) {
                  String name=rs.getString(1);
                  String className=rs.getString(2);
                  String onEvent=rs.getString(3);
                  InterventionSelectorSpec isel = new InterventionSelectorSpec(true);
                  isel.setId(iselId);
                  isel.setName(name);
                  isel.setClassName(className);
                  isel.setOnEvent(onEvent);
                  List<InterventionSelectorParam> params = getISParams(conn,scisId);
                  isel.setParams(params);
                  return isel;
              }
              return null;

          } finally {
                if (ps != null)
                     ps.close();
                if (rs != null)
                     rs.close();
          }
    }

    /**
     * Return a list of ISParams that belong to the intervention selector and class.
     * @param conn
     * @param scisId
     * @return
     */
    private static List<InterventionSelectorParam> getISParams(Connection conn, int scisId) throws SQLException {
         ResultSet rs = null;
          PreparedStatement ps = null;
          try {
              String q = "select p.id, p.name, p.value from is_param_sc p where p.isActive=1 and p.sc_is_map_id=?";
              ps = conn.prepareStatement(q);
              ps.setInt(1, scisId);
              rs = ps.executeQuery();
              List<InterventionSelectorParam> params = new ArrayList<InterventionSelectorParam>();
              while (rs.next()) {
                  int paramId = rs.getInt(1);
                  String n = rs.getString(2);
                  String v = rs.getString(3);
                  InterventionSelectorParam p = new InterventionSelectorParam(paramId,n,v);
                  params.add(p);
              }
              return params;
          } finally {
                if (ps != null)
                     ps.close();
                if (rs != null)
                     rs.close();
          }
    }

    public static void main(String[] args) {
        try {
            Connection conn = DbUtil.getAConnection("localhost");
            TutorStrategy ts = DbStrategy.getStrategy(conn,5);
            System.out.println(ts.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
