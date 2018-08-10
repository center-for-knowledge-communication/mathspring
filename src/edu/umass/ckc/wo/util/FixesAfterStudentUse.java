package edu.umass.ckc.wo.util;

import java.sql.*;
import java.util.List;
import java.util.Formatter;
import java.util.Locale;
import java.io.OutputStream;

/**
 * Copyright (c) University of Massachusetts
 * Written by: Ivon Arroyo
 * Date: Jun 25, 2007
 * Time: 2:48:22 PM
 */
public class FixesAfterStudentUse {

/*
*
Moves the data around of students who used a new POSTTTEST FAKE user, appartently running the pretest
but actually counting as a true posttest. This is generally done because their
posttest could not get started  with their true user name.

The CONDITION for this script to work is that the student only did the pretest with that fake user.
It will fail if the fake user was used to see posttest problems also.
*
*/
    void FixPretestAsPostest(Connection conn, int trueStudentId, int posttestStudentId) throws Exception {


    //Delete any record of the posttest of the trueStudentId, if there is any, as this student should not have any
    DeletePosttestTrueStudent(trueStudentId, conn) ;

    //Look up the episodicdata of the fake student that has posttestdata recorded as pretest data
        String EP_SQL = "select sessnum, min(id), max(id), count(*) from episodicdata2 " +
                    " where studid=" + posttestStudentId + " and activityName like 'pretest%' " +
                    " group by sessnum  order by sessNum" ;

        Statement episodicSt = conn.createStatement();
        ResultSet episodicRS = episodicSt.executeQuery(EP_SQL);

        // For each row in each session involving pretest data, rename the activityname and the studID
        while ( episodicRS.next() ) {
            int sessnum = episodicRS.getInt("sessnum") ;
            int minEPId = episodicRS.getInt(2) ;
            int maxEPId = episodicRS.getInt(3) ;
            setPosttestInEpisodicDataRows(conn, sessnum, minEPId, maxEPId) ;
            setTrueStudIDInEpisodicDataRows(conn, sessnum, trueStudentId) ;
            setTrueStudIDInOtherTables(conn, sessnum, trueStudentId) ;
        }

    // For each row in the WoProperty involving pretest data, rename the studID and property to posttest
        String WO_SQL = "select property,value from WoProperty " +
                    " where objid=" + posttestStudentId + " and property like '%pretest%' " ;

        Statement WOSt = conn.createStatement();
        ResultSet WORS = WOSt.executeQuery(WO_SQL);

        while ( WORS.next() ) {     //For each Property
            String property = WORS.getString("property") ;
            ChangeWOPropertyToPosttest(conn, posttestStudentId, property) ;
        }

        ChangeWOOBJID(conn, posttestStudentId, trueStudentId) ;
  }


   void ChangeWOPropertyToPosttest(Connection conn, int posttestStudentId, String property) throws Exception {
       String newPropertyName = property.replace("pretest", "posttest") ;
       String ModifyEP_SQL = "UPDATE woproperty set property='" + newPropertyName +  "'" +
                   " where property='" + property + "' and objid=" + posttestStudentId  ;

       PreparedStatement psEpisodicSt = conn.prepareStatement(ModifyEP_SQL);
       psEpisodicSt.executeUpdate(ModifyEP_SQL) ;

   }

   void ChangeWOOBJID(Connection conn, int posttestStudentId, int trueStudentId) throws Exception {
       //Change the OBJID also
       String ModifyWO_SQL = "UPDATE woproperty set objid=" + trueStudentId +
                   " where property like'%posttest%'" +
                   " and objid=" + posttestStudentId  ;

       PreparedStatement psWOSt = conn.prepareStatement(ModifyWO_SQL);
       psWOSt.executeUpdate(ModifyWO_SQL) ;
   }

   void setPosttestInEpisodicDataRows(Connection conn, int sessnum, int minEPId, int maxEPId) throws Exception{

       String ModifyEP_SQL = "UPDATE episodicdata2 set activityName='posttestProblem' " +
                   " where sessnum=" + sessnum + " and activityName='pretestProblem' " +
                   " and id>=" + minEPId + " and id<=" + maxEPId ;

       PreparedStatement psEpisodicSt = conn.prepareStatement(ModifyEP_SQL);
       psEpisodicSt.executeUpdate(ModifyEP_SQL) ;

   }

   void setTrueStudIDInEpisodicDataRows(Connection conn, int sessnum, int trueStudentId) throws Exception{

       String ModifyEP_SQL = "UPDATE episodicdata2 set studid=" + trueStudentId +
                   " where sessnum=" + sessnum ;

       PreparedStatement psEpisodicSt = conn.prepareStatement(ModifyEP_SQL);
       psEpisodicSt.executeUpdate(ModifyEP_SQL) ;

   }

    void setTrueStudIDInOtherTables(Connection conn, int sessnum, int trueStudentId) throws Exception{

        String Modify_SQL = "UPDATE session set studid=" + trueStudentId +
                    " where id=" + sessnum ;

        PreparedStatement psSt = conn.prepareStatement(Modify_SQL);
        psSt.executeUpdate(Modify_SQL) ;

    }

   public void DeletePosttestTrueStudent(int trueStudentId, Connection conn) throws Exception {
       //Delete any record of the posttest of the trueStudentId, if there is any, as this student should not have any
       String DeleteEP_SQL = "delete from episodicdata2 " +
                   " where studid=" + trueStudentId + " and activityName like 'posttest%' " ;

       PreparedStatement psEpisodicSt = conn.prepareStatement(DeleteEP_SQL);
       psEpisodicSt.executeUpdate(DeleteEP_SQL) ;

       String DeleteWO_SQL = "delete from woproperty " +
                   " where objid=" + trueStudentId + " and property like '%posttest%' " ;

       PreparedStatement psWOPSt = conn.prepareStatement(DeleteWO_SQL);
       psWOPSt.executeUpdate(DeleteWO_SQL) ;
   }




 public static void main(String[] args) {

     Connection con = null;
      try {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
//        con = DriverManager.getConnection("jdbc:mysql://cadmium.cs.umass.edu/wayangoutpostdb","WayangServer", "jupiter");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wayangoutpostdb","root", null);

        if(!con.isClosed())
          System.out.println("Successfully connected to MySQL server using TCP/IP...");
      } catch(Exception e) {
        e.printStackTrace();
      }

      FixesAfterStudentUse fixes = new FixesAfterStudentUse() ;
      int trueStudentID= 4863;  int posttestStudentID=4899;

//     trueStudentID= 4863;     posttestStudentID=4899;         //Algebra2
//     trueStudentID= 4687;     posttestStudentID=4898;         //Albegra2
//     trueStudentID= 4721;     posttestStudentID=4867;         //MacLeod
//     trueStudentID=4728;      posttestStudentID=4871;         //MacLeod
//     trueStudentID=4723;      posttestStudentID=4869;         //MacLeod
//     trueStudentID= 4724;     posttestStudentID=4868;         //MacLeod
//     trueStudentID= 4722;     posttestStudentID=4870;         //MacLeod
//     trueStudentID= 4653;     posttestStudentID=4901;         //Mako Geometry
//     trueStudentID= 4751;     posttestStudentID=4757;         //Mako Precalc
//     trueStudentID= 4797;     posttestStudentID=5123;         //Algebra 2X

      try {
//          fixes.FixPretestAsPostest(con, trueStudentID, posttestStudentID) ;
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
}
