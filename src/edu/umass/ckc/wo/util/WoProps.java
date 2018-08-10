package edu.umass.ckc.wo.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Oct 18, 2005
 * Time: 1:09:30 PM
 */
public class WoProps extends State {
    private Map map;

    public WoProps (Connection conn) {
        this.conn = conn;
    }

    /**
     * Each prop/val is inserted into the map.  The first time it inserts a given prop
     * the value goes in as a String.  If a given prop is inserted a second time, the String is removed
     * and replaced with a List.  If a given prop is inserted beyond 2 times, the List is just appended to.
     *
     * The end result is a map that maps String keys to either String values or Lists of String values.
     *
     * @param m
     * @param prop
     * @param val
     */
    protected void insertMapProp(HashMap m, String prop, String val) {
        Object o = m.get(prop);

        if (o == null)
            m.put(prop,val);
        else if (o instanceof String) {
            List<String> l = new ArrayList<String>();
            l.add((String) o);
            l.add(val);
            m.remove(prop);
            m.put(prop,l);
        }
        else ((List<String>) o).add(val);
    }

    public Map loadProps (int id) throws SQLException {
        HashMap m = new HashMap(519);
        String q = "select property, value, position from woproperty where objid=? order by property, position";
        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = null;
        try {
            ps.setInt(1,id);
            rs = ps.executeQuery();
            // this loads ALL properties into the map (even ones that are not requested)
            while (rs.next()) {
                String prop = rs.getString(1);
                String val = rs.getString(2);
                insertMapProp(m,prop,val);
            }
            return m;
//            return eliminateNonRequestedProps(m,props);
        } finally {
            if (rs != null)
                rs.close();
            ps.close();
        }
    }


    public void load (int id) throws SQLException {
        super.load(this.conn,id);
        map = loadProps(id);
    }

    public Map getMap () {
        return map;

    }
}
