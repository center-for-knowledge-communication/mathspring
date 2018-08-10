package edu.umass.ckc.wo.util;

import edu.umass.ckc.wo.smgr.SessionManager;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.Statement;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Aug 18, 2005
 * Time: 9:13:01 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class State {
    protected List properties;
    protected Connection conn;
    protected int objid;

    private static final Object lock = new Object(); // used for synchronization in the setProp method below



    public void load (Connection conn, int id) throws SQLException {
        this.conn = conn;
        objid = id;
    }

    public Connection getConnection () {
        return conn;
    }

//    /**
//     * Given an id and an some properties, load them all into a Map that maps properties to String or List (of String)
//     * @param id
//     * @param props
//     * @return
//     * @throws SQLException
//     */
//    public Map loadProps (int id, String[] props) throws SQLException {
//        HashMap<String,String> m = new HashMap<String,String>(props.length*5);
//        String q = "select property, value, position from woproperty where objid=? order by property, position";
//        PreparedStatement ps = conn.prepareStatement(q);
//        ResultSet rs = null;
//        try {
//            ps.setInt(1,id);
//            rs = ps.executeQuery();
//            // this loads ALL properties into the map (even ones that are not requested)
//            while (rs.next()) {
//                String prop = rs.getString(1);
//                String val = rs.getString(2);
//                insertMapProp(m,prop,val);
//            }
//            return m;
////            return eliminateNonRequestedProps(m,props);
//        } finally {
//            if (rs != null)
//                rs.close();
//            ps.close();
//        }
//    }

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

    private Map eliminateNonRequestedProps(HashMap m, String[] props) {
        Iterator itr = m.keySet().iterator();
        ArrayList rejects = new ArrayList();
        // gather all the rejected keys
        while (itr.hasNext()) {
            String k = (String) itr.next();
            if (!isRequestedProp(k,props))
                rejects.add(k);
        }
        // remove rejected key/values from map
        for (int i = 0; i < rejects.size(); i++) {
            String s = (String) rejects.get(i);
            m.remove(s);
        }
        return m;
    }



    private boolean isRequestedProp (String k, String[] props) {
        for (int i = 0; i < props.length; i++) {
            String prop = props[i];
            if (prop.equals(k))
                return true;
        }
        return false;
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
    private void insertMapProp(HashMap m, String prop, String val) {
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

    protected String mapGetPropString (Map m, String prop) {
        String s = (String) m.get(prop);
        return s;
    }

    protected String mapGetPropString (Map m, String prop, String def) {
        String s = (String) m.get(prop);
        return (s == null) ? def : s;
    }

    protected int mapGetPropInt (Map m, String prop, int def) {
        String s = (String) m.get(prop);
        if (s == null)
            return def;
        else return Integer.parseInt(s);
    }

    protected long mapGetPropLong (Map m, String prop, long def) {
        String s = (String) m.get(prop);
        if (s == null)
            return def;
        else return Long.parseLong(s);
    }

    protected double mapGetPropDouble (Map m, String prop, double def) {
        String s = (String) m.get(prop);
        if (s == null)
            return def;
        else return Double.parseDouble(s);
    }

    protected boolean mapGetPropBoolean (Map m, String prop, boolean def) {
        String s = (String) m.get(prop);
        if (s == null)
            return def;
        else return Boolean.valueOf(s).booleanValue();
    }

    protected List<String> mapGetPropList (Map m, String prop) {
        Object o =  m.get(prop);
        if (o== null)
            return new ArrayList<String>();
        if (o instanceof List) return (List<String>) o;
        // if there is only 1 value, it will be a String.  So build a list with the one item in it.
        else {
            String s = (String) o;
            List<String> l = new ArrayList<String>();
            l.add(s);
            return l;
        }

    }

    /**
     * Given two properties (keyProp and valProp) that form two parallel lists, return an element from vals that corresponds to the element key found in keys
     * @param m
     * @param keyProp
     * @param valProp
     * @param key
     * @return
     */
    protected String mapGetPropKeyValue (Map m, String keyProp, String valProp, String key) {
        List<String> keys = mapGetPropList(m,keyProp);
        String[] keysA = keys.toArray(new String[keys.size()]);
        List<String> vals = mapGetPropList(m,valProp);
        int ix = -1;
        for (int i = 0; i <keysA.length; i++) {
            if (keysA[i].equals(key))
                ix = i;
        }
        if (ix >= 0)
            return vals.get(ix);
        else return null;
    }


    /**
     * Given
     * @param m
     * @param keyProp
     * @param valProp
     * @param key
     * @param val
     * @throws SQLException
     */
    protected void mapSetPropKeyValue (Map m, String keyProp, String valProp, String key, String val) throws SQLException {
        List<String> keys = mapGetPropList(m,keyProp);
        String[] keysA = keys.toArray(new String[keys.size()]);
        int ix = -1;
        for (int i = 0; i <keysA.length; i++) {
            if (keysA[i].equals(key))
                ix = i;
        }
        // ix is the position in the array (zero based) of the key we want.   Now we set the corresponding value
        // in the values.   This is a 1-based sequence (using seqPos in woproperties) so we add 1 to the ix
        if (ix >= 0)
            setProp(this.objid,valProp,val,ix+1);
        else {
            addProp(this.objid,keyProp,key); // key wasn't found, so add new key
            addProp(this.objid,valProp,val); // add the val in the corresponding place of valProp

        }

    }

    public boolean setProp (int id, String property, List<String> vals) throws SQLException {
        clearProp(id,property);
        for (String v : vals)
            addProp(id,property,v);
        return true;
    }

    public boolean setProp (int id, String property, boolean val) throws SQLException {
        return setProp(id,property,Boolean.toString(val),0);
    }

    public boolean addProp (int id, String property, boolean val) throws SQLException {
        return addProp(id,property,Boolean.toString(val));
    }


    public boolean setProp (int id, String property, int val) throws SQLException {
        return setProp(id,property,val,0);
    }

    public boolean addProp (int id, String property, int val) throws SQLException {
        return addProp(id,property,Integer.toString(val));
    }

    public boolean setProp (int id, String property, int val, int position) throws SQLException {
        return setProp(id,property,Integer.toString(val),position);
    }


    public boolean setProp (int id, String property, long val) throws SQLException {
        return setProp(id,property,val,0);
    }

    public boolean addProp (int id, String property, long val) throws SQLException {
        return addProp(id,property,Long.toString(val));
    }

    public boolean setProp (int id, String property, long val, int position) throws SQLException {
        return setProp(id,property,Long.toString(val),position);
    }



    public boolean setProp (int id, String property, double val) throws SQLException {
        return setProp(id,property,val,0);
    }

    public boolean addProp (int id, String property, double val) throws SQLException {
        return addProp(id,property,Double.toString(val));
    }

    public boolean setProp (int id, String property, double val, int position) throws SQLException {
        return setProp(id,property,Double.toString(val),position);
    }

    public boolean setProp (int id, String property, String value) throws SQLException {
        return setProp(id,property,value,0);
    }

    public String getPropString (int id, String property, String defaultValue) throws SQLException {
        String s = getProp(id,property,0);
        if (s == null)
            return defaultValue;
        else return s;
    }

    public int getPropInt (int id, String property, int defaultValue) throws SQLException {
        String s = getProp(id,property,0);
        if (s == null)
            return defaultValue;
        else return Integer.parseInt(s);
    }

    public double getPropDouble (int id, String property, double defaultValue) throws SQLException {
        String s = getProp(id,property,0);
        if (s == null)
            return defaultValue;
        else return Double.parseDouble(s);
    }

    public boolean getPropBoolean  (int id, String property, boolean defaultValue) throws SQLException {
        String s = getProp(id,property,0);
        if (s == null)
            return defaultValue;
        else return Boolean.valueOf(s).booleanValue();
    }

    public long getPropLong  (int id, String property, long defaultValue) throws SQLException {
        String s = getProp(id,property,0);
        if (s == null)
            return defaultValue;
        else return Long.parseLong(s);
    }

    // Get rid of the old values and replace with the new values
    public void overwritePropVals (int id, String property, List<String> vals) throws SQLException {
        for (int i=0;i<vals.size();i++) {
            setProp(id,property,vals.get(i),i);
        }
    }

    /**
     * Given an objid,property and position return the value of the property at that position.
     * @param id
     * @param property
     * @param position
     * @return
     * @throws SQLException
     */
    public String getProp (int id, String property, int position) throws SQLException {
        String q = "select value from woproperty where objid=? and property=? and position=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = null;
        try {
            ps.setInt(1,id);
            ps.setString(2,property);
            ps.setInt(3,position);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            else return null;
        } finally {
            if (rs != null)
                rs.close();
            ps.close();
        }
    }



    /**
     * Given the objId and the property return an array of values for that property.
     * The array is ordered by the position field of the property in the db.
     * @param id
     * @param property
     * @return
     * @throws SQLException
     */
    public List<String> getProp (int id, String property) throws SQLException {
        String q = "select value from woproperty where objid=? and property=? order by position";
        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = null;
        List<String> values = new ArrayList<String>();
        try {
            ps.setInt(1,id);
            ps.setString(2,property);
            rs = ps.executeQuery();
            while (rs.next()) {
                values.add(rs.getString(1));
            }
            return values;
        } finally {
            if (rs != null)
                rs.close();
            ps.close();
        }
    }

    /**
     * Add the property/value for the objid.  Will add the property at the next position or 0 if the property
     * has not yet been set.
     * @param id
     * @param property
     * @param value
     * @return
     * @throws SQLException
     */
    public boolean addProp (int id, String property, String value) throws SQLException {
        int ix=-1; // set to -1 so that if no other props are found then the insert
                    // will happen at position 0 (i.e. ix + 1)
        String q = "select max(position) from woproperty where objid=? and property=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,id);
        ps.setString(2,property);
        ResultSet rs = ps.executeQuery();
        try {
            if (rs.next()) {
                ix = rs.getInt(1);
                rs.close();
                ps.close();
            }
            q = "insert woproperty (value,objid,property,position) values (?,?,?,?)";
            ps = conn.prepareStatement(q);
            ps.setString(1,value);
            ps.setInt(2,id);
            ps.setString(3,property);
            ps.setInt(4,ix+1);
            return ps.executeUpdate() > 0;
        } finally {
            ps.close();
        }


    }



    /**
     * Set the property/value at the given position
     * @param id
     * @param property
     * @param value
     * @param position
     * @return
     * @throws SQLException
     */
    public boolean setPropOld (int id, String property, String value, int position) throws SQLException {
        String q = "select value from woproperty where objid=? and property=? and position=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,id);
        ps.setString(2,property);
        ps.setInt(3,position);
        ResultSet rs = ps.executeQuery();
        try {
            if (rs.next()) {
                rs.close();
                ps.close();
                q = "update woProperty set value=? where objid=? and property=? and position=?";
                ps = conn.prepareStatement(q);
                if (value == null)
                    ps.setNull(1,Types.VARCHAR);
                else ps.setString(1,value);
                ps.setInt(2,id);
                ps.setString(3,property);
                ps.setInt(4,position);
                return ps.executeUpdate() > 0;
            }
            else {
                ps.close();
                q = "insert into woProperty (objid,property,value,position) values (?,?,?,?)";
                ps = conn.prepareStatement(q);
                ps.setInt(1,id);
                ps.setString(2,property);
                if (value==null)
                    ps.setNull(3,Types.VARCHAR);
                else ps.setString(3,value);
                ps.setInt(4,position);
                return ps.executeUpdate() > 0;
            }
        } finally {
            ps.close();
        }
    }

    public int clearUserProperties (int id) throws SQLException {
        String q = "delete from woproperty where objid=?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(q);
            ps.setInt(1,id);
            int n = ps.executeUpdate();
            return n;
        } finally {
            if (ps != null)
                ps.close();
        }
    }

    public int clearProp (int id, String property) throws SQLException {
        String q = "delete from woproperty where objid=? and property=?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(q);
            ps.setInt(1,id);
            ps.setString(2,property);
            int n = ps.executeUpdate();
            return n;
        } finally {
            if (ps != null)
                ps.close();
        }
    }

    public static int clearProp (Connection conn, int id, String property) throws SQLException {
        String q = "delete from woproperty where objid=? and property=?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(q);
            ps.setInt(1,id);
            ps.setString(2,property);
            int n = ps.executeUpdate();
            return n;
        } finally {
            if (ps != null)
                ps.close();
        }
    }

    // The body of this method is a critical section because occasionally Flash generates duplicate
    // events that arrive simultaneously.  This method gets called twice, from two separate threads,
    // trying to set the same values in the table.   So we have a common static object (lock) that
    // is used as the basis of synchronization.
    public boolean setProp (int id, String property, String value, int position) throws SQLException {
        synchronized (lock) {
            String q = "delete from woproperty where objid=? and property=? and position=?";
            PreparedStatement ps = conn.prepareStatement(q);
            ps.setInt(1,id);
            ps.setString(2,property);
            ps.setInt(3,position);
            ps.executeUpdate();
            try {
                ps.close();
                q = "insert into woproperty (objid,property,value,position) values (?,?,?,?)";
                ps = conn.prepareStatement(q);
                ps.setInt(1,id);
                ps.setString(2,property);
                if (value==null) {
                    //ps.setNull(3,Types.VARCHAR);
                    ps.setInt(3,0) ;
                }
                else ps.setString(3,value);
                ps.setInt(4,position);
                return ps.executeUpdate() > 0;
            } finally {
                ps.close();
            }
        }
    }



    public void setObjid(int objid) {
        this.objid = objid;

    }



//     public boolean setProp2 (int id, String property, String value, int position) throws SQLException {
//        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//         String q = "select value from woproperty where objid="+id+" and property='"+property+"' and position="+position;
//         ResultSet rs = stmt.executeQuery(q);
//
//     }

}
