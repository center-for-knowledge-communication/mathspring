package edu.umass.ckc.wo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Formatter;
import java.util.Locale;
import java.io.OutputStream;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 25, 2007
 * Time: 2:48:22 PM
 */
public class UsernameCreator {

/*
*
*  Creates up to 1000 users, for a specific class, from a neck format and a starting user number.
*  Also creates as many test users as groups there are, for each class
*
*/
    void createUsers(Connection conn, String neck, int numDigits,  String password, int classid, int[] groups,
                     int numUsers, int startingAt, String testUsersNeck) throws Exception {

        for ( int thisUser=startingAt, counterGroups=0; thisUser<numUsers+startingAt; thisUser++, counterGroups++) {

            if ( counterGroups == groups.length )
                counterGroups=0 ;

            int thisUserDigits = 3 ;
            if ( thisUser<100 ) thisUserDigits=2 ;
            if ( thisUser<10 ) thisUserDigits=1 ;

            StringBuffer username = new StringBuffer(neck) ;

            for ( int j=0; j<numDigits-thisUserDigits; j++ )
                    username.append("0") ;

            username.append(thisUser) ;

            System.out.println("INSERT into STUDENT (username, password, classid, pedagogyid) values ('" + username + "','" + password
                                            + "'," + classid + "," + groups[counterGroups] + "); ") ;

        }

    /*
        String s_upd = "INSERT into STUDENT (username, password, classid, pedagogyid) values ('" + username + "','" + password
                                            + "'," + classid + "," + groups[counterGroups] + ") " ;
        PreparedStatement ps = conn.prepareStatement(s_upd);
        ps.executeUpdate(s_upd);
      */

        for ( int thisUser=0; thisUser<groups.length; thisUser++) {

            StringBuffer testUser = new StringBuffer(testUsersNeck + (thisUser+ 1)) ;

            System.out.println("INSERT into STUDENT (username, password, classid, pedagogyid) values ('" + testUser + "','" + password
                                            + "'," + classid + "," + groups[thisUser] + "); ") ;

        }

  }

    void createUsers2(Connection conn, String neck, int numDigits,  String password, int classid, int[] groups,
                        int startingAt, int endingAt, String testUsersNeck) throws Exception {

           for ( int thisUser=startingAt, counterGroups=0; thisUser<=endingAt; thisUser++, counterGroups++) {

               if ( counterGroups == groups.length )
                   counterGroups=0 ;

               int thisUserDigits = 3 ;
               if ( thisUser<100 ) thisUserDigits=2 ;
               if ( thisUser<10 ) thisUserDigits=1 ;

               StringBuffer username = new StringBuffer(neck) ;

               for ( int j=0; j<numDigits-thisUserDigits; j++ )
                       username.append("0") ;

               username.append(thisUser) ;

               System.out.println("INSERT into STUDENT (username, password, classid, pedagogyid) values ('" + username + "','" + password
                                               + "'," + classid + "," + groups[counterGroups] + "); ") ;

           }

       /*
           String s_upd = "INSERT into STUDENT (username, password, classid, pedagogyid) values ('" + username + "','" + password
                                               + "'," + classid + "," + groups[counterGroups] + ") " ;
           PreparedStatement ps = conn.prepareStatement(s_upd);
           ps.executeUpdate(s_upd);
         */

           for ( int thisUser=0; thisUser<groups.length; thisUser++) {

               StringBuffer testUser = new StringBuffer(testUsersNeck + (thisUser+ 1)) ;

               System.out.println("INSERT into STUDENT (username, password, classid, pedagogyid) values ('" + testUser + "','" + password
                                               + "'," + classid + "," + groups[thisUser] + "); ") ;

           }

     }



 public static void main(String[] args) {

     Connection con = null;
//      try {
//        Class.forName("com.mysql.jdbc.Driver").newInstance();
//        con = DriverManager.getConnection("jdbc:mysql://cadmium.cs.umass.edu/wayangoutpostdb","WayangServer", "jupiter");
//        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wayangoutpostdb","root", null);

//        if(!con.isClosed())
//          System.out.println("Successfully connected to MySQL server using TCP/IP...");
//      } catch(Exception e) {
//        e.printStackTrace();
//      }

    UsernameCreator unameCreator = new UsernameCreator() ;
      try {
        unameCreator.createARMS_20111021(con); ;
      } catch(Exception e) {
        e.printStackTrace();
      }
    }

 private void createPhilCooperGeom(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {31, 37, 38, 30} ;
        String neck = "DT_" ;
        String password = "DT" ;

        int classid = 437  ;  //G2 group
        int numUsers = 25 ;
        int numberDigits = 1 ;
        int startingAt = 200 ;
        String testUsersNeck = "TestDT_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid = 440  ;  //G2 group
        numUsers = 25 ;
        numberDigits = 1 ;
        startingAt = 225 ;
        testUsersNeck = "TestDT_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid = 441  ;  //G2 group
        numUsers = 25 ;
        numberDigits = 1 ;
        startingAt = 250 ;
        testUsersNeck = "TestDT_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }

private void createARMS_20111021(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {31, 37, 38, 30} ;
        String neck = "ARMS_" ;
        String password = "ARMS" ;

        int classid = 459  ;  //Miller 1

        int numberDigits = 1 ;
        int startingAt = 200 ;
        int endingAt = 227 ;
        String testUsersNeck = "TestARMS_" ;
        createUsers2(con, neck, numberDigits, password, classid, groups, startingAt,endingAt, testUsersNeck);

        classid = 460  ;  //Wozny 1
        numberDigits = 1 ;
        startingAt = 228 ;
        endingAt = 249 ;
        testUsersNeck = "TestARMS_" ;
        createUsers2(con, neck, numberDigits, password, classid, groups,  startingAt,endingAt, testUsersNeck);
    }

private void createFRHS_20111021(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {31, 37, 38, 30} ;
        String neck = "FRHS_" ;
        String password = "FRHS" ;

        int classid = 453  ;  //Miller 1

        int numberDigits = 1 ;
        int startingAt = 300 ;
        int endingAt = 319 ;
        String testUsersNeck = "TestFRHS_" ;
        createUsers2(con, neck, numberDigits, password, classid, groups, startingAt,endingAt, testUsersNeck);

        classid = 454  ;  //Milller 2
        numberDigits = 1 ;
        startingAt = 320 ;
        endingAt = 341 ;
        testUsersNeck = "TestFRHS_" ;
        createUsers2(con, neck, numberDigits, password, classid, groups,  startingAt,endingAt, testUsersNeck);

        classid = 455  ;  //Wozny 1
        numberDigits = 1 ;
        startingAt = 342 ;
        endingAt = 365 ;
        testUsersNeck = "TestFRHS_" ;
        createUsers2(con, neck, numberDigits, password, classid, groups,  startingAt,endingAt, testUsersNeck);

        classid = 456  ;  //Wozny 2
        numberDigits = 1 ;
        startingAt = 392 ;
        endingAt = 415 ;
        testUsersNeck = "TestFRHS_" ;
        createUsers2(con, neck, numberDigits, password, classid, groups,  startingAt,endingAt, testUsersNeck);

        classid = 457  ;  //hosley 1
        numberDigits = 1 ;
        startingAt = 416 ;
        endingAt = 438 ;
        testUsersNeck = "TestFRHS_" ;
        createUsers2(con, neck, numberDigits, password, classid, groups,  startingAt,endingAt, testUsersNeck);

        classid = 458  ;  //hosley 2
        numberDigits = 1 ;
        startingAt = 366 ;
        endingAt = 391 ;
        testUsersNeck = "TestFRHS_" ;
        createUsers2(con, neck, numberDigits, password, classid, groups,  startingAt,endingAt, testUsersNeck);
    }


    private void createUsersGirlsIncDemo(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {31, 37, 38, 30} ;
        String neck = "FRACTION_" ;
        String password = "GirlsInc" ;

        int classid = 398  ;  //G2 group
        int numUsers = 9 ;
        int numberDigits = 1 ;
        int startingAt = 1 ;
        String testUsersNeck = "TestFRACTION_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        neck = "GEOM_" ;
        classid =  399 ;  //H1 group
        numUsers = 9 ;
        numberDigits = 1 ;
        startingAt = 1 ;
        testUsersNeck = "TestGEOM_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        neck = "STATS_" ;
        classid =  400 ;  //H1 group
        numUsers = 9 ;
        numberDigits = 1 ;
        startingAt = 1 ;
        testUsersNeck = "TestSTATS_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        neck = "ADVENT_" ;
        classid =  401 ;  //H1 group
        numUsers = 9 ;
        numberDigits = 1 ;
        startingAt = 1 ;
        testUsersNeck = "TestADVENT_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }

    private void createUsersNormanPierceTeamAmherstMiddleSchool(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {31, 37, 38, 30} ;
        String neck = "ARMS_" ;
        String password = "ARMS" ;

        int classid = 353  ;  //G2 group
        int numUsers = 46 ;
        int numberDigits = 3 ;
        int startingAt = 100 ;
        String testUsersNeck = "TestG2_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

         classid =  354 ;  //H1 group
         numUsers = 36 ;
         numberDigits = 3 ;
         startingAt = 150 ;
         testUsersNeck = "TestH1_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        neck = "ARMSx_" ;
        
        classid = 353  ;  //G2 group
        numUsers = 46 ;
        numberDigits = 3 ;
        startingAt = 100 ;
        testUsersNeck = "TestG2x_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  354 ;  //H1 group
        numUsers = 36 ;
        numberDigits = 3 ;
        startingAt = 150 ;
        testUsersNeck = "TestH1x_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }

    private void createUsersDonnaFowlerSpring2011(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {30, 31, 37, 38} ;
        String neck = "TFx_" ;
        String password = "TF" ;

        int classid = 351  ;  //Jim Thomas, Geometry
        int numUsers = 15 ;
        int numberDigits = 3 ;
        int startingAt = 200 ;
        String testUsersNeck = "TestGeom_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

         classid =  352 ;  //Jim Thomas, Geometry
         numUsers = 15 ;
         numberDigits = 3 ;
         startingAt = 215 ;
         testUsersNeck = "TestMCASPrep_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }

    private void createUsersIreneLarocheTeamAmherstMiddleSchool(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {30, 31, 37, 38} ;
        String neck = "ARMSx_" ;
        String password = "ARMS" ;

        int classid =  333 ;  //Jim Thomas, Geometry
        int numUsers = 40 ;
        int numberDigits = 3 ;
        int startingAt = 1 ;
        String testUsersNeck = "TestxF1_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

         classid =  334 ;  //Jim Thomas, Geometry
         numUsers = 40 ;
         numberDigits = 3 ;
         startingAt = 41 ;
         testUsersNeck = "TestxF2_" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }

    private void createUsersSouthAmherst(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {32, 33, 34, 35} ;
        String neck = "ARPS_" ;
        String password = "ARPS" ;

        int classid =  296 ;  //Jim Thomas, Geometry
        int numUsers = 10 ;
        int numberDigits = 3 ;
        int startingAt = 1 ;
        String testUsersNeck = "JimGeom" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  295 ;  //Jim Thomas, Pre-Geometry
        numUsers = 10 ;
        numberDigits = 3 ;
        startingAt = 100 ;
        testUsersNeck = "JimPreGeom" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  300 ;  //karenmurphy, Geometry
        numUsers = 10 ;
        numberDigits = 3 ;
        startingAt = 200 ;
        testUsersNeck = "KarenGeom" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  299 ;  //karenmurphy, Pre-Geometry
        numUsers = 10 ;
        numberDigits = 3 ;
        startingAt = 300 ;
        testUsersNeck = "KarenPreGeom" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  303 ;  //karsinbaker, Geometry
        numUsers = 10 ;
        numberDigits = 3 ;
        startingAt = 400 ;
        testUsersNeck = "KarenGeom" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  301 ;  //karsinbaker, Pre-Geometry
        numUsers = 10 ;
        numberDigits = 3 ;
        startingAt = 500 ;
        testUsersNeck = "KarenPreGeom" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }

    private void createUsersTurnersSpring10(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {32, 33, 34, 35} ;
        String neck = "TF_" ;
        String password = "TF" ;

        int classid =  218 ;  //Lindenberg Geometry
        int numUsers = 15 ;
        int numberDigits = 3 ;
        int startingAt = 150 ;
        String testUsersNeck = "Lindenberg" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        int groups2[] = {35, 32, 33, 34} ;
        classid =  219 ;  //Fowler Geometry
        numUsers = 21 ;
        numberDigits = 3 ;
        startingAt = 165 ;
        testUsersNeck = "Fowler" ;
        createUsers(con, neck, numberDigits, password, classid, groups2, numUsers, startingAt, testUsersNeck);
    }

    private void createUsersHolyokeSpring10(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {32, 33, 34, 35} ;
        String neck = "HHS_" ;
        String password = "HHS" ;

        int classid =  214 ;  //SAT-Prep B
        int numUsers = 25 ;
        int numberDigits = 3 ;
        int startingAt = 1 ;
        String testUsersNeck = "SpinebergB" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        int groups2[] = {33, 34, 35, 32} ;
        classid =  215 ;  //SAT-Prep D
        numUsers = 15 ;
        numberDigits = 3 ;
        startingAt = 26 ;
        testUsersNeck = "SpinebergD" ;
        createUsers(con, neck, numberDigits, password, classid, groups2, numUsers, startingAt, testUsersNeck);

        int groups3[] = {34, 35, 32, 33} ;
        classid =  216 ;  //SAT-Prep D
        numUsers = 30 ;
        numberDigits = 3 ;
        startingAt = 41 ;
        testUsersNeck = "KosanovicC" ;
        createUsers(con, neck, numberDigits, password, classid, groups3, numUsers, startingAt, testUsersNeck);

        int groups4[] = {35, 32, 33, 34} ;
        classid =  217 ;  //SAT-Prep D
        numUsers = 30 ;
        numberDigits = 3 ;
        startingAt = 71 ;
        testUsersNeck = "KosanovicG" ;
        createUsers(con, neck, numberDigits, password, classid, groups4, numUsers, startingAt, testUsersNeck);

    }

    private void createUsersUMassSpring10(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {21, 22, 16, 14, 15, 16} ;
        String neck = "_Math114_" ;
        String password = "_Math114" ;

        int classid =  208 ;  //UMass_114 Brian Emond
        int numUsers = 61 ;
        int numberDigits = 3 ;
        int startingAt = 100 ;
        String testUsersNeck = "_Emond" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }

    private void createUsersASUSpring10(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {21, 22, 16, 14, 15, 16} ;
        String neck = "ASU_" ;
        String password = "ASU" ;

        int classid =  206 ;  //Kasia's study in ASU
        int numUsers = 100 ;
        int numberDigits = 3 ;
        int startingAt = 1 ;
        String testUsersNeck = "ASU" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }

    private void createUsersFTechSpring10(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {21, 22, 25, 26} ;
        String neck = "FCTS_" ;
        String password = "FCTS" ;

        int classid =  205 ;  ////??????????????????????
        int numUsers = 40 ;
        int numberDigits = 3 ;
        int startingAt = 50 ;
        String testUsersNeck = "Marino" ; /////??????????????
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }

    private void createUsersFTechFall09(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {21, 22, 25, 26} ;
        String neck = "FCTS_" ;
        String password = "FCTS" ;

        int classid =  196 ;
        int numUsers = 10 ;
        int numberDigits = 3 ;
        int startingAt = 1 ;
        String testUsersNeck = "Marino" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  197 ;
        numUsers = 10 ;
        numberDigits = 3 ;
        startingAt = 11 ;
        testUsersNeck = "HendersonA" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  198 ;
        numUsers = 15 ;
        numberDigits = 3 ;
        startingAt = 21 ;
        testUsersNeck = "HendersonB" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  199 ;
        numUsers = 10 ;
        numberDigits = 3 ;
        startingAt = 36 ;
        testUsersNeck = "HendersonC" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }

    private void createUsersGemini(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {14, 15} ;
        String neck = "FM_" ;
        String password = "FM" ;

        int classid =  174 ;
        int numUsers = 25 ;
        int numberDigits = 3 ;
        int startingAt = 101 ;
        String testUsersNeck = "Atla" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        //Set up parameters to pass to create the users in each class
        classid =  175 ;
        startingAt = 126 ;
        testUsersNeck = "Reds" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        //Set up parameters to pass to create the users in each class
        classid =  176 ;
        startingAt = 151 ;
        testUsersNeck = "Tita" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        //Set up parameters to pass to create the users in each class
        classid =  177 ;  
        startingAt = 176 ;
        testUsersNeck = "Satu" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }

    private void createUsersFrontierHighSchool(Connection con) throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {21, 24, 25, 22, 23, 26} ; //Alternate 3 conditions and 2 genders
        //Jane empath, Jake random, Jane nonEmotional, Jake empath, Jane Random, Jake nonemotional
        String neck = "FRHS_" ;
        String password = "FRHS" ;


        //EMOTIONS G2 EXPERIMENT
/*        int classid =  158 ;  //MacLeod Geometry
        int numUsers = 25 ;
        int numberDigits = 3 ;
        int startingAt = 1 ;
        String testUsersNeck = "MacLGeom" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        //STILL IN THE G1 EXPERIMENT
        int groupsG1E1[] = {14, 15, 16} ;
        classid =  159 ;  //MakoGeometry
        numUsers = 25 ;
        startingAt = 26 ;
        testUsersNeck = "MakoGeom" ;
        numberDigits = 3 ;
        createUsers(con, neck, numberDigits, password, classid, groupsG1E1, numUsers, startingAt, testUsersNeck);


        //EMOTIONS G2 EXPERIMENT
        int classid =  160 ;  //Pike Alg 1B - Block B
        int numUsers = 15 ;
        int numberDigits = 3 ;
        int startingAt = 51 ;
        String testUsersNeck = "PikeAlg1BB" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  161 ;  //Blinder Algebra 2
        numUsers = 15 ;
        numberDigits = 3 ;
        startingAt = 66 ;
        testUsersNeck = "BlinAlg2" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
        
        classid =  162 ;  //MacLeod Algebra 1 B - Block A
        numUsers = 15 ;
        numberDigits = 3 ;
        startingAt = 81 ;
        testUsersNeck = "MacLAlg1BA" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
        */

        int classid =  163 ;  //Mako Pre-Calculus
        int numUsers = 20 ;
        int numberDigits = 3 ;
        int startingAt = 96 ;
        String testUsersNeck = "MakoPrec" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  164 ;  //DEANE Algebra 1
        numUsers = 20 ;
        numberDigits = 3 ;
        startingAt = 116 ;
        testUsersNeck = "DeanAlg1" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid = 165 ;  //Blinder Algebra 2x
        numUsers = 15 ;
        numberDigits = 3 ;
        startingAt = 136 ;
        testUsersNeck = "BlinAlg2x" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid = 166 ;  //MacLeod Algebra 1B - Block C
        numUsers = 15 ;
        numberDigits = 3 ;
        startingAt = 151 ;
        testUsersNeck = "MaclAlg1BC" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid = 167 ;  //Pike Algebra 1B - Block C
        numUsers = 20 ;
        numberDigits = 3 ;
        startingAt = 166 ;
        testUsersNeck = "PikeAlg1BC" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);


        classid = 168 ;  //Extra users. Assign to Sarah Mitchell
        numUsers = 11 ;
        numberDigits = 3 ;
        startingAt = 200 ;
        testUsersNeck = "saraextr" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid = 169 ;  //Guertin Algebra 1A 
        numUsers = 14 ;
        numberDigits = 3 ;
        startingAt = 186 ;
        testUsersNeck = "GuerAlg1A" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }

    private void createUsersTurnersFalls(Connection con)       throws Exception
    {
        //Set up parameters to pass to create the users in each class
        int groups[] = {14, 15, 16} ;
        String neck = "TF_" ;
        String password = "TF" ;

        int classid =  144 ;  //Mr. Franklin Geometry
        int numUsers = 15 ;
        int numberDigits = 3 ;
        int startingAt = 1 ;
        String testUsersNeck = "FranklinGeom" ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  147 ;  //Mrs. Prasol Algebra 1 Block E
        numUsers = 12 ;
        startingAt = 16 ;
        testUsersNeck = "PrasolAlg1E" ;
        numberDigits = 3 ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  146 ;  //Mr. Franklin Math in Real Life Block F
        numUsers = 16 ;
        startingAt = 28 ;
        testUsersNeck = "FranklinMRLF" ;
        numberDigits = 3 ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  145 ;  //Mr. Franklin Math in Real Life Block G
        numUsers = 12 ;
        startingAt = 44 ;
        testUsersNeck = "FranklinMRLG" ;
        numberDigits = 3 ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  148 ;  //Mrs. Prasol Algebra 1 Block G
        numUsers = 20 ;
        startingAt = 56 ;
        testUsersNeck = "PrasolAlg1E" ;
        numberDigits = 3 ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  149 ;  //Mrs. Fowler MCAS Prep
        numUsers = 17 ;
        startingAt = 76 ;
        testUsersNeck = "FowlerMCAS" ;
        numberDigits = 3 ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);

        classid =  150 ;  //Ms. Clark Pre Calculus
        numUsers = 28 ;
        startingAt = 93 ;
        testUsersNeck = "ClarkPreCalc" ;
        numberDigits = 3 ;
        createUsers(con, neck, numberDigits, password, classid, groups, numUsers, startingAt, testUsersNeck);
    }
}
