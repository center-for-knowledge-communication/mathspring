package edu.umass.ckc.wo.woreports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 22, 2005
 * Time: 11:02:27 AM
 *
 * Maps low-level skills to semi-abstract skills
 */
public class SkillMap {
    private Map idMap = new HashMap();  // maps ids of low level skills to names semiabstract skills
    private Map nameMap = new HashMap(); // maps names of lowlevel skills to names of abstract skills

    public void addMapping(int lowLevelSkillId, int highLevelSkillId, String lowLevelSkillName, String highLevelSkillName) {
        idMap.put(new Integer(lowLevelSkillId), highLevelSkillName);
        nameMap.put(lowLevelSkillName,highLevelSkillName);
    }

    public String getHighLevelSkillName (String lowLevelSkillName) {
        return (String) nameMap.get(lowLevelSkillName);
    }

    public String getHighLevelSkillName(int lowLevelSkillId) {
        return (String) idMap.get(new Integer(lowLevelSkillId));
    }


    public static SkillMap makeSkillMap(Connection conn) throws SQLException {
        SkillMap skmap = new SkillMap();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            String q = "select skillid, semiabsskillid, s.name, a.name from linktosemiabstractskill l, " +
                    "semiabstractskill a, skill s where a.id=l.semiabsskillid and s.id=l.skillid";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            while (rs.next()) {
                int skId = rs.getInt(1);
                int absId = rs.getInt(2);
                String skName = rs.getString(3);
                String absName = rs.getString(4);
                skmap.addMapping(skId,absId,skName,absName);
            }
            return skmap;
        } finally {
            ps.close();
            rs.close();

        }
    }

    public Set getAbstractSkillNames () {
        Collection c =nameMap.values();
        TreeSet s = new TreeSet();
        Iterator itr = c.iterator();
        while (itr.hasNext()) {
            String s1 = (String) itr.next();
            s.add(s1);
        }
        return s;
    }


}
