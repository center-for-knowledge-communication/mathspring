package edu.umass.ckc.wo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 25, 2007
 * Time: 2:48:22 PM
 */
public class ProblemDifficultyTablePopulator {

/*
*
*  The following 2 methods compute new problem difficulty factors for all problems in the OverallProbDifficulty table, based on
*  a list of valid student IDs.
*
*/

    void updateOverallProbDifficultyTable(Connection conn) throws Exception {
      SqlQuery q = new SqlQuery();
      ResultSet rs;

      String s =
          "select problemid from OverallProbDifficulty ";
      String validStudents = "(819,820,821,822,823,824,825,826,827,828,829,830,831,833,834,835,836,842,843,844,845,846,847,848,849,850,851,852,869,871,1025,1026,1027,1028,1029,1030,1031,1032,1033,1034,1035,1036,1037,1038,1039,1040,1041,1056,1057,1059,1061,1062,1063,1064,1065,1066,1067,1068,1069,1070,1074,1075,1214,1215,1216,1219,1220,1221,1222,1225,1226,1354,1355,1355,1356,1357,1358,1359,1362,1363,1364,1365,1367,1370,1371,1372,1373,1376,1377,1378,1379,1381,1384,1385,1386,1389,1390,1391,1392,1393,1394,1395,1396,1397,1398,1399,1400,1401,1402,1403,1404,1405,1406,1407,1408,1409,1410,1411,1425,1426,1427,1428,1429,1430,1431,1432,1433,1434,1435,1436,1437,1438,1439,1442,1443,1444,1445,1447,1448,1449,1450,1452,1453,1454,1455,1456,1457,1459,1460,1461,1462,1463,1464,1465,1466,1467,1468,1469,1470,1471,1472,1473,1474,1475,1476,1477,1478,1479,1480,1481,1482,1483,1484,1485,1486,1487,1488,1489,1491,1503,1504,1506,1507,1508,1509,1511,1513,1514,1515,1516,1521,1522,1523,1525,1526,1527,1528,1589,1591,1592,1593,1594,1595,1596,1597,1598,1599,1600,1601,1603,1605,1606,1608,1609,1610,1611,1612,1613,1614,1615,1616,1618,1619,1620,1621,1622,1624,1625,1626,1629,1630,1631,1632,1633,1634,1635,1636,1638,1639,1640,1642,1643,1645,1646,1648,1649,1650,1651,1652,1653,1655,1656,1657,1658,1659,1661,1662,1663,1664,1665,1667,1668,1670,1671,1672,1673,1674,1675,1676,1680,1681,1682,1683,1684,1686,1690,1691,1693,1695,1696,1698,1703,1705,1706,1707,1708,1709,1710,1711,1712,1713,1714,1715,1716,1717,1718,1720,1721,1723,1724,1725,1727,1728,1730,1735,1736,1737,1739,1740,1741,1742,1743,1745,1746,1747,1749,1750,1755,1760,1778,1779,1780,1781,1782,1783,1784,1785,1786,1787,1788,1789,1906,1907,1908,1909,1910,1911,1912,1913,1914,1916,1917,1918,1919,1920,1921,1924,1926,1927,1928,1929,1930,1933,1934,1936,1937,1938,1940,1941,1943,1944,1953,1960,2017,2022,2023,2027,2030,2036,2039,2047,2051,2054,2057,2058,2061,2064,2065,2069,2070,2072,2074,2078,2084,2088,2089,2091,2095,2099,2100,2101,2102,2112,2114,2116,2119,2120,2125,2126,2132,2135,2141,2143,2144,2151,2155,2160,2167,2170,2172,2173,2178,2185,2188,2189,2192,2193,2194,2195,2012,2018,2019,2020,2028,2032,2040,2042,2044,2046,2049,2060,2066,2067,2073,2079,2080,2082,2083,2086,2090,2092,2093,2094,2106,2107,2122,2124,2127,2128,2134,2137,2142,2145,2149,2154,2156,2163,2164,2177,2181,2182,2184,2187,1645,1696,2015,2016,2021,2024,2025,2026,2034,2035,2043,2045,2048,2050,2052,2055,2059,2062,2068,2075,2076,2077,2081,2085,2087,2096,2097,2098,2103,2104,2108,2109,2110,2111,2117,2121,2123,2129,2130,2133,2136,2138,2146,2148,2161,2162,2166,2171,2175,2179,2180,2183,2190,2191,2277,2286,2289,2296,2299,2300,2301,2308,2316,2318,2320,2321,2322,2327,2330,2332,2335,2405,2407,2411,2413,2421,2425,2429,2430,2431,2435,2447,2457,2458,2459,2465,2466,2279,2280,2281,2282,2283,2284,2294,2298,2312,2313,2314,2319,2323,2324,2325,2415,2416,2424,2427,2428,2432,2436,2437,2438,2439,2440,2441,2442,2443,2444,2448,2453,2454,2456,2467,2278,2285,2290,2291,2292,2293,2295,2297,2303,2305,2306,2309,2310,2311,2315,2317,2326,2329,2358,2401,2406,2409,2410,2412,2414,2420,2422,2423,2426,2433,2445,2446,2450,2455,2464,2468,2475)" ;
      rs = q.read(conn, s);

      while (rs.next()) {
        int problemid = rs.getInt("problemid");

        //---------  Set totalproblems
        SqlQuery q_tprobs = new SqlQuery();
        String s_tprobs =
            "select count(*) as totalprobs from episodicdata2 where problemid=" +
            problemid +
            " and action='endProblem' and studid in " + validStudents;
        ResultSet rs_tprobs = q_tprobs.read(conn, s_tprobs);

        while (rs_tprobs.next()) {
          int totalprobs = rs_tprobs.getInt("totalprobs");
          String s_upd = "update OverallProbDifficulty set totalprobs= " +
              totalprobs + " where problemid=" + problemid;
          PreparedStatement ps = conn.prepareStatement(s_upd);
          ps.executeUpdate(s_upd);
        }

        //--------- Set sumincorrect
        SqlQuery q_sumincorrect = new SqlQuery();
        String s_sumincorrect =
            "select count(*) as sumincorrect from episodicdata2 where problemid=" +
            problemid +
            " and action='attempt' and isCorrect=0 and studid in " + validStudents;
        ResultSet rs_sumincorrect = q_sumincorrect.read(conn, s_sumincorrect);

        if (rs_sumincorrect.next()) {
          int sumincorrect = rs_sumincorrect.getInt("sumincorrect");
          String s_upd = "update OverallProbDifficulty set sumincorrect= " +
              sumincorrect + " where problemid=" + problemid;
          PreparedStatement ps = conn.prepareStatement(s_upd);
          ps.executeUpdate(s_upd);

          s_upd = "update overallprobdifficulty set avgincorrect=sumincorrect/totalprobs where problemid=" +
              problemid;
          ps = conn.prepareStatement(s_upd);
          ps.executeUpdate(s_upd);
        }

        //--------- Set sumhints
        SqlQuery q_sumhints = new SqlQuery();
        String s_sumhints =
            "select count(*) as sumhints from episodicdata2 where problemid=" +
            problemid +
            " and (action='hint'  or action='hintAccepted') and studid in " +
            validStudents;
        ResultSet rs_sumhints = q_sumhints.read(conn, s_sumhints);

        if (rs_sumhints.next()) {
          int sumhints = rs_sumhints.getInt("sumhints");
          String s_upd = "update OverallProbDifficulty set sumhints= " + sumhints +
              " where problemid=" + problemid;
          PreparedStatement ps = conn.prepareStatement(s_upd);
          ps.executeUpdate(s_upd);

          s_upd = "update overallprobdifficulty set  avghints=sumhints/totalprobs   where problemid=" +
              problemid;
          ps = conn.prepareStatement(s_upd);
          ps.executeUpdate(s_upd);
        }

        //--------- Set avgsecsinproblem
        SqlQuery q_avgsecsprob = new SqlQuery();
        String s_avgsecsprob =
            "select avg(probElapsed) as avgsecsprob from episodicdata2 where problemid=" +
            problemid +
            " and action='endProblem' and probElapsed>0 and probelapsed<1200000 and studid in " +
            validStudents;
        ResultSet rs_avgsecsprob = q_avgsecsprob.read(conn, s_avgsecsprob);

        if (rs_avgsecsprob.next()) {
          float avgsecsprob = rs_avgsecsprob.getFloat("avgsecsprob") / 1000;
          String s_upd = "update OverallProbDifficulty set avgsecsprob= " +
              avgsecsprob + " where problemid=" + problemid;
          PreparedStatement ps = conn.prepareStatement(s_upd);
          ps.executeUpdate(s_upd);
        }
        System.out.println("Problem " + problemid + " done.");
      }

    }

    private void computeDifficultyFactors(Connection conn) throws Exception {
        //---- Now, compute difficulty factors (normalized)
        SqlQuery q_max = new SqlQuery();
        String s_max = "select max(avgsecsprob) as maxsecs, max(avghints) as maxhints, max(avgincorrect) as maxinc " +
            "from OverallProbDifficulty";
        ResultSet rs_max = q_max.read(conn, s_max);

        if (rs_max.next()) {
          float maxsecs = rs_max.getFloat("maxsecs") ;
          float maxhints = rs_max.getFloat("maxhints") ;
          float maxinc = rs_max.getFloat("maxinc") ;

          SqlQuery q = new SqlQuery();
          String s = "select problemid, avgsecsprob, avghints, avgincorrect from OverallProbDifficulty";
          ResultSet rs = q.read(conn,s) ;

          while ( rs.next() ) {
            int problemid = rs.getInt("problemid") ;
            float avgsecsprob = rs.getFloat("avgsecsprob") ;
            float avghints = rs.getFloat("avghints") ;
            float avgincorrect = rs.getFloat("avgincorrect") ;

            float diff_incorr = (avgincorrect/maxinc) ;
            String s_diff_inc = "update OverallProbDifficulty set diff_incorr= "+ diff_incorr + " where problemid=" + problemid;
            PreparedStatement ps1 = conn.prepareStatement(s_diff_inc);
            ps1.executeUpdate(s_diff_inc);

            float diff_hints = (avghints/maxhints) ;
            String s_diff_hnt = "update OverallProbDifficulty set diff_hints= "+ diff_hints + " where problemid=" + problemid;
            PreparedStatement ps2 = conn.prepareStatement(s_diff_hnt);
            ps2.executeUpdate(s_diff_hnt);

            float diff_time = (avgsecsprob/maxsecs) ;
            String s_diff_tm = "update OverallProbDifficulty set diff_time= "+ diff_time + " where problemid=" + problemid;
            PreparedStatement ps3 = conn.prepareStatement(s_diff_tm);
            ps3.executeUpdate(s_diff_tm);

            String diff_upd = "update OverallProbDifficulty set diff_level= "+ (diff_time+diff_hints+diff_incorr)/3 + " where problemid=" + problemid;
            PreparedStatement ps4 = conn.prepareStatement(diff_upd);
            ps4.executeUpdate(diff_upd);

            System.out.println("Diff_level for problem " + problemid + " is " + diff_time + " + " + diff_hints +
                              " + " + diff_incorr + " / 3 " + " = " + (diff_time+diff_hints+diff_incorr)/3.0 );
          }
        }
    }

 public static void main(String[] args) {

      Connection con = null;

      try {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        con = DriverManager.getConnection("jdbc:mysql://cadmium.cs.umass.edu/wayangoutpostdb","WayangServer", "jupiter");

        if(!con.isClosed())
          System.out.println("Successfully connected to " +
                             "MySQL server using TCP/IP...");
      } catch(Exception e) {
        e.printStackTrace();
      }

      ProblemDifficultyTablePopulator ps = new ProblemDifficultyTablePopulator() ;

      try {
        ps.updateOverallProbDifficultyTable(con);
        ps.computeDifficultyFactors(con);
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
}
