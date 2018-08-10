package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.handler.ClassAdminHelper;
import edu.umass.ckc.wo.beans.PedagogyBean;
import edu.umass.ckc.wo.util.Lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jan 22, 2008
 * Time: 11:12:59 AM
 */
public class DbClassPedagogies {

    /**
     * add <classid, pedagogyid> to the classpedagogy table
     * @param conn
     * @param classid
     * @param pedagogyId
     * @throws SQLException
     */
    public static void setClassPedagogy(Connection conn, int classid, String pedagogyId) throws SQLException {
        try {
            String q = "insert into classpedagogies values (?,?)";
            PreparedStatement s = conn.prepareStatement(q);
            s.setInt(1,classid);
            s.setInt(2,Integer.parseInt(pedagogyId));
            s.execute();
        }
        catch (SQLException e) {
            System.out.println(e.getErrorCode());
            if (e.getErrorCode() == Settings.duplicateRowError ||e.getErrorCode() == Settings.keyConstraintViolation )
                ;
            else throw e;
        }
    }

    /*
    Given a list of pedagogy ids (which are what the class is set to be using), return the Pedagogy objects.
    A fairly common error with older classes is that they reference a pedagogy that no longer exists.
     */
    public static List<Pedagogy> getPedagogiesFromIds(List<String> pedagogyIds) throws DeveloperException {
        List<Pedagogy> pedagogyList = new ArrayList<Pedagogy>();
        List<Pedagogy> defaultpeds = ClassAdminHelper.getDefaultPedagogies() ;

        for (int i = 0; i < pedagogyIds.size(); i++) {
            String pid = (String) pedagogyIds.get(i);
            Pedagogy ped=null;
            if (pid.equalsIgnoreCase("0"))      {
                pedagogyList = defaultpeds ;
                break ;
            }
            else ped = Settings.pedagogyGroups.get(pid);
            // if we don't find the pedagogy, its likely to be one that has been deleted.  So
            // skip over it.
            if (ped != null)
                pedagogyList.add(ped);

        }
        return pedagogyList;
    }

    public static void removeClassPedagogy(Connection conn, int classId, int pedId) throws SQLException {
        String q = "delete from classpedagogies where classid=? and pedagogyid=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,classId);
        ps.setInt(2,pedId);
        ps.executeUpdate();
    }

    public static PedagogyBean[] getClassPedagogyBeans(Connection conn, int classId) throws SQLException {
        PedagogyBean[] pedagogies = DbClassPedagogies.getAllPedagogies();
        List<String> pedIds = DbClassPedagogies.getClassPedagogyIds(conn,classId);
            for (PedagogyBean b: pedagogies) {
               if (Lists.inList(b.getId(),pedIds))
                   b.setSelected(true);
            }
        return pedagogies;

    }

    public static List<PedagogyBean> getClassSimpleConfigPedagogyBeans(Connection conn, int classId) throws SQLException {
        // Goes through the pedagogies and takes the ones marked with a simpleConfigName tag
        Pedagogy[] peds = Settings.pedagogyGroups.values().toArray(new Pedagogy[Settings.pedagogyGroups.values().size()]);
        List<PedagogyBean> beans = new ArrayList<PedagogyBean>(peds.length);
        List<String> pedIds = DbClassPedagogies.getClassPedagogyIds(conn,classId);

        for (Pedagogy p: peds) {
            String simpleConfigName = p.getSimpleConfigName();
            boolean isDefault = p.isDefault();
            if (simpleConfigName != null)  {
                PedagogyBean b =new PedagogyBean(Integer.parseInt(p.getId()),simpleConfigName);
                // If the class doesn't have pedagogies assigned, set pedagogies marked "default" as selected
                if (pedIds.size() == 0)
                    b.setSelected(isDefault);
                // otherwise set the pedagogy to selected if its one of the ones assigned to the class
                else if (Lists.inList(Integer.parseInt(p.getId()),pedIds))
                    b.setSelected(true);
                beans.add(b);
            }

        }
        // Not sure about this part of things.  Its marking beans as selected if they are
//        List<String> pedIds = DbClassPedagogies.getClassPedagogyIds(conn,classId);
//        for (PedagogyBean b: beans) {
//            if (Lists.inList(b.getId(),pedIds))
//                b.setSelected(true);
//        }
        return beans;

    }



    public static List<Pedagogy> getClassPedagogies (Connection conn, int classId) throws SQLException, DeveloperException {

        List<String> pedagogyIds= getClassPedagogyIds(conn,classId); // note some of these may have been deleted if class is old.
        return getPedagogiesFromIds(pedagogyIds);
    }

    public static List<String> getClassPedagogyIds (Connection conn, int classid) throws SQLException {
        String q = "select pedagogyId from classpedagogies where classId=?";
        PreparedStatement s = conn.prepareStatement(q);
        s.setInt(1,classid);
        ResultSet rs = s.executeQuery();
        List<String> ids = new ArrayList<String>();
        while (rs.next()) {
            ids.add(Integer.toString(rs.getInt(1)));
        }
        return ids;

    }

    /**
     * Delete all rows from pedagogytable for a given classid
     * @param conn
     * @param classId
     * @throws SQLException
     */
    public static void removeClassPedagogies(Connection conn, int classId) throws SQLException {
            String q = "delete from classpedagogies where classid=?";
            PreparedStatement ps = conn.prepareStatement(q);
            ps.setInt(1,classId);
            ps.executeUpdate();
    }
    /**
     * Pull all the Pedagogy objects out of the map and return them in order sorted by ID.
     * @return
     */
    public static PedagogyBean[] getAllPedagogies () {
        Pedagogy[] peds = (Pedagogy[]) Settings.pedagogyGroups.values().toArray(new Pedagogy[Settings.pedagogyGroups.values().size()]);
        Arrays.sort(peds);
        PedagogyBean[] beans = new PedagogyBean[peds.length];
        for (int i=0;i<peds.length;i++) {
            beans[i] = new PedagogyBean(Integer.parseInt(peds[i].getId()),peds[i].getName());
            beans[i].setId(Integer.parseInt(peds[i].getId()));

        }
        return beans;
   }

    public static void clonePedagogies(Connection conn, int classId, int newClassId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        PreparedStatement ps=null;
        try {
            String q = "select pedagogyId from classpedagogies where classid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,classId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int pedId= rs.getInt(1);
                String q2 = "insert into classpedagogies (classid, pedagogyid) values (?,?)";
                ps = conn.prepareStatement(q2);
                ps.setInt(1,newClassId);
                ps.setInt(2,pedId);
                ps.executeUpdate();
                ps.close();
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static Pedagogy getAssistmentsCommonCorePedagogy() throws DeveloperException {
        List<String> l = new ArrayList<String>();
        l.add("17");  // the ID of the pedagogy to be used for Assistments CC work for overriding the pedagogy normally assigned to that class.
        List<Pedagogy> lp= getPedagogiesFromIds(l);
        return lp.get(0);
    }
}
