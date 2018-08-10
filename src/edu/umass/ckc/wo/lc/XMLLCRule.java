package edu.umass.ckc.wo.lc;

import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.xml.JDOMUtils;
import org.apache.commons.cli.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * An XML parser for reading in a ruleset from an XML stream.
 * User: david
 * Date: 4/8/16
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class XMLLCRule {

    private static Connection conn;

    /**
     * Given an XML file containing rules, read in all the rules and build LCRule objects
     *
     * @param inputStream
     * @throws JDOMException
     * @throws IOException
     */
    public static List<LCRule> loadRuleFile (InputStream inputStream) throws JDOMException, IOException {
        Document d = JDOMUtils.makeDocument(inputStream);
        Element rulesElt = d.getRootElement();
        List<Element> rules= rulesElt.getChildren("lc_r");
        List<LCRule> rulelist = new ArrayList<LCRule>();
        for (Element rule: rules) {
            LCRule r = parseRule(rule);
            rulelist.add(r);
        }
        return rulelist;

    }

    /**
     * A ruleset file contains one ruleset element.  The ruleset element contains
     * meta-rules and some rule elements which merely link to rules (by name) that are
     * defined in the rule files.   This will create a LCRuleset object
     * that is not fully initialized.  It will have a list of XML Elements defining
     * the rules in the ruleset.
     *
     *
     * @param inputStream
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public static LCRuleset loadRulesetFile (InputStream inputStream) throws JDOMException, IOException {
        Document d = JDOMUtils.makeDocument(inputStream);
        Element ruleset = d.getRootElement();
        LCRuleset rs = new LCRuleset();
        if (ruleset != null) {
            String rsName = ruleset.getAttributeValue("name");
            String descr = ruleset.getAttributeValue("description");
            String notes = ruleset.getAttributeValue("notes");
            rs.setName(rsName);
            rs.setDescription(descr);
            rs.setNotes(notes);
            List<Element> metaRules = ruleset.getChildren("meta_rule");
            for (Element mr: metaRules) {
                LCMetaRule r = parseMetaRule(mr);
                rs.addMetaRule(r);
            }
            List<Element> rules= ruleset.getChildren("rule");
            // put all the rule Elements into the ruleset object.
            // Later we do a pass over this and convert each Element to an actual
            // LCRule.
            rs.setRuleElements(rules);

        }
        return rs;
    }

    public static LCRule getRule (String name, List<LCRule> rules) {
        for (LCRule r : rules) {
            if (r.getName().equals(name))
                return r;
        }
        return null;
    }

    /**
     * Finish initializing a ruleset by looking up the rules that are in the ruleElement list and
     * sticking LCRule rule objects into the LCRuleset.
     * @param rs
     * @param rules
     */
    public static void putRulesInRuleSet (LCRuleset rs, List<LCRule> rules) {
            // simple Elements that are just names of rules
            List<Element> ruleElts = rs.getRuleElements();
            for (Element r : ruleElts) {
                String ruleName = r.getTextTrim();
                LCRule lcr = getRule(ruleName,rules);
                if (lcr != null)
                    rs.addRule(lcr);
            }

    }

    public static void loadRuleSet(Connection conn, LCRuleset rs, InputStream inputStream) throws JDOMException, IOException {
        XMLLCRule.conn = conn;
        String filename = rs.getSource(); // a file in the resources directory. e.g. lc_curProblem.xml
        Document d = JDOMUtils.makeDocument(inputStream);
        Element ruleset = d.getRootElement();
        String rsName = ruleset.getAttributeValue("name");
        String descr = ruleset.getAttributeValue("description");
        String notes = ruleset.getAttributeValue("notes");
        rs.setName(rsName);
        rs.setDescription(descr);
        rs.setNotes(notes);
        List<Element> metaRules = ruleset.getChildren("meta_rule");
        for (Element mr: metaRules) {
            LCMetaRule r = parseMetaRule(mr);
            rs.addMetaRule(r);
        }

        List<Element> rules= ruleset.getChildren("lc_r");
        for (Element rule: rules) {
            LCRule r = parseRule(rule);
            rs.addRule(r);
        }

    }

    private static LCMetaRule parseMetaRule(Element mr) {
        String n = mr.getAttributeValue("name");
        String v = mr.getAttributeValue("value");
        String u = mr.getAttributeValue("units");
        String st = mr.getAttributeValue("status");
        LCMetaRule r = new LCMetaRule(n,u,v, st != null?Boolean.parseBoolean(st): true);
        return r;
    }

    public static LCRule parseRule (Element ruleElt) {
        String name,priority,onEvent,descr;
        name = ruleElt.getAttributeValue("name");
        try {
            priority = ruleElt.getAttributeValue("priority");
            onEvent = ruleElt.getAttributeValue("onEvent");
            descr = ruleElt.getAttributeValue("description");
            List<Element> conditions = ruleElt.getChildren("lc_c");
            Element action = ruleElt.getChild("lc_a");
            LCRule rule = new LCRule(-1,name,descr,onEvent,Double.parseDouble(priority));
            for (Element cond: conditions) {
                LCCondition condition = parseCondition(cond);
                rule.addCondition(condition);
            }
            List<Element> mrOverrides = ruleElt.getChildren("override_metarule");
            for (Element ov : mrOverrides) {
                LCMetaRule mr = LCMetaRule.createFromRuleXML(ov);
                rule.addMetaRuleOverride(mr);
            }
            LCAction a = parseAction(action);
            rule.setAction(a);
            return rule;
        } catch (Exception e) {
            System.out.println("Failed to parse rule " + name);
            e.printStackTrace();
        }
        return null;
    }

    private static LCAction parseAction(Element actElt) throws SQLException {
        String media, text;
        String actionType = actElt.getAttributeValue("actionType");
        String msgId = actElt.getAttributeValue("messageId");
        int msgIdi = -1;
        // if an lcmessageId is given, then we need to fetch the text and media file from the lcmessage table
        if (msgId != null && !msgId.equals(""))  {
            msgIdi = Integer.parseInt(msgId);
            LCMessage lcm = DbLCRule.getLCMessage(conn, msgIdi);
            text = lcm.getText();
            media = lcm.getMedia();
        }
        else {
            text = actElt.getTextTrim();
            media = actElt.getAttributeValue("media");
        }
        return new LCAction(-1,text,media, actionType,msgIdi);
    }

    private static LCCondition parseCondition(Element condElt) {
        String fname,relop,val,type,not;
        fname = condElt.getAttributeValue("fnname");
        relop = condElt.getAttributeValue("relop");
        val = condElt.getAttributeValue("val");
        type = condElt.getAttributeValue("type");
        if (type == null)
            type = "boolean";
        not = condElt.getAttributeValue("not"); // if anything is in the not attribute, it will negate the condition
        return new LCCondition(-1,fname,relop,val,type,not != null);
    }

    // Files are structured as follows:
    // rules:
    // <rules>
    //     <lcrule name="a".../>
    //     <lcrule name="b".../>
    //     <lcrule .../>
    //     <lcrule .../>
    // </rules>
    // Many of these files, ruleset:
    // <ruleset>
    //     <meta-rule ... />
    //     <meta-rule ... />
    //     <meta-rule ... />
    //     <rule>a</rule>
    //     <rule>b</rule>
    //     <rule>...</rule>
    // <ruleset>

    /*
    To call this use switches as below:
      XMLLCRule -c  -w -db <server> -f <ruleFile> [ruleSetFile [ruleSetFile2 [ ruleSetFile3 [...]]]]
      loads definitions of rules from rules files.  Rulesets are defined in rulesetFile.

      Typical uses:
      XMLLCRule -c -w -db localhost -f f:\\dev\\mathspring\\woServer\\resources\\lcrules.xml f:\\dev\\mathspring\\woServer\\resources\\lc_curProblem.xml f:\\dev\\mathspring\\woServer\\resources\\lc_lastProblem.xml f:\\dev\\mathspring\\woServer\\resources\\oldLcBehavior.xml
     */
    public static void mainCommand(String[] args) {
        Options options = new Options();
        // add c option
        options.addOption("c", false, "Clear the database of rules and related data");
        options.addOption("w", false, "Write the rulesets and rules to the db");
        // add db option
        options.addOption("db", true, "Database to use:  rose or localhost");
        options.addOption("l", false, "List the rules in the system") ;
        options.addOption("rs", true, "Optional.  The name of the ruleset to list. ") ;
        options.addOption("f", true, "XML File that defines rules");
        options.addOption("help", false, "Prints this message");
        // create the parser
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse( options, args );
            // get db option value
            String db = line.getOptionValue("db");
            if (db == null || db.startsWith("local"))
                db = "localhost";
            else
                db = db + ".cs.umass.edu";



            Connection conn = DbUtil.getAConnection(db);
            XMLLCRule.conn = conn;
            // a command to list the rules in a ruleset.  Exit afterwards because this command should only be used on its own
            if (line.hasOption("l")) {
                String rulesetName = line.getOptionValue("rs");
                if (rulesetName != null) {
                    int id = DbLCRule.getRuleSetId(conn, rulesetName);
                    LCRuleset rs = DbLCRule.getRuleSet(conn, rulesetName);
                    System.out.println("Ruleset: " + rs.getName() + " : " + rs.getId());
                    for (LCRule r : rs.getRules())
                        System.out.println(r + "\n");
                }
                // no rs option given , print them all
                else {
                    System.out.println("All rules in the system");
                    for (LCRule r : DbLCRule.getAllRules(conn))
                        System.out.println(r+"\n");
                }
                System.exit(0);
            }
            if ( line.hasOption( "c" ) )
                DbLCRule.clearRuleTables(conn); // blow away all the contents of rule tables and reset the id counters

            String ruleFilename = line.getOptionValue("f");
            List<LCRule> rules = new ArrayList<LCRule>();
            if (ruleFilename != null) {
                File rfile = new File(ruleFilename);
                FileInputStream str = new FileInputStream(rfile);
                rules= loadRuleFile(str);
            }
            // if no rules file is given, then get a list of rules from the db.
            else {
                System.out.println("Warning: Using existing rules from Db!");
                DbLCRule.getAllRules(conn);
            }
            String[] rulesetFiles = line.getArgs();
            List<LCRuleset> rulesets = new ArrayList<LCRuleset>();
            // Complete the initialization of each LCRuleset by looking up
            // the LCRule objects that should go into it.
            for (String filename : rulesetFiles) {
                File rsfile = new File(filename);
                FileInputStream str = new FileInputStream(rsfile);
                LCRuleset rs = loadRulesetFile(str);
                putRulesInRuleSet(rs,rules);
                rulesets.add(rs);
                if ( line.hasOption( "w" ) )
                    DbLCRule.writeRuleset(conn, rs);
            }


        }
        catch( ParseException exp ) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        }
        catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static List<LCRule> testRuleLoad() {

        File rfile = new File("f:\\dev\\mathspring\\woServer\\resources\\lcrules.xml");
        try {
            Connection conn = DbUtil.getAConnection("localhost");
            XMLLCRule.conn = conn;
            FileInputStream str = new FileInputStream(rfile);
            return loadRuleFile(str);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LCRuleset testLoadRuleset(String fname) {

        File rfile = new File(fname);
        try {
            Connection conn = DbUtil.getAConnection("localhost");
            XMLLCRule.conn = conn;
            FileInputStream str = new FileInputStream(rfile);
            return loadRulesetFile(str);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void testLoad() {
        String f1 = "f:\\dev\\mathspring\\woServer\\resources\\oldLcBehavior.xml";
        String f2 = "f:\\dev\\mathspring\\woServer\\resources\\lc_curProblem.xml";
        String f3 = "f:\\dev\\mathspring\\woServer\\resources\\lc_lastProblem.xml";
        List<LCRule> rules = testRuleLoad() ;
        LCRuleset rs1= testLoadRuleset(f1);
        LCRuleset rs2= testLoadRuleset(f2);
        LCRuleset rs3= testLoadRuleset(f3);
        putRulesInRuleSet(rs1,rules);
        putRulesInRuleSet(rs2,rules);
        putRulesInRuleSet(rs3,rules);
        for (LCRule r: rs1.getRules())
            System.out.println(r);
    }

    public static void main(String[] args) {
//        testLoad();
        mainCommand(args);
    }
}
