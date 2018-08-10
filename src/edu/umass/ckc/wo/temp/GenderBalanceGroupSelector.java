package edu.umass.ckc.wo.temp;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

/**
The student is placed in a group by gender.  The names of the groups are passed into the constructor.
Whenever a student is assigned to a group we try to keep each group balanced for each gender.*/

public class GenderBalanceGroupSelector  {
  private String[] groupNames;
  private static Random ran = new Random();


  public GenderBalanceGroupSelector(String[] groupNames) {
    this.groupNames = groupNames;
  }

  /** GIven the studId and the gender, determine the group that has the least number of
   * members for this gender and return its id.  Then set the UserProfile group to be this id.
   */
  public int assignToGroup (Connection conn, int studId, String gender) throws Exception {

    int groupid;
    if (gender == null)
      groupid = randomGroup(conn);
    else  groupid = smallestGroup(conn,gender);
    String q = "update UserProfile set ProblemSequenceGroup=? where userId=?";
    PreparedStatement ps = conn.prepareStatement(q);
    ps.setInt(1,groupid);
    ps.setInt(2,studId);
    ps.executeUpdate();
    return groupid;
  }



  private int minIx (int[] a) {
    int min=Integer.MAX_VALUE;
    int minix=0;
    for (int i=0;i<a.length;i++) {
      if (a[i] < min) {
        min = a[i];
        minix = i;
      }
    }
    return minix;
  }

  private int randomGroup (Connection conn) throws Exception {
    int n = groupNames.length;
    int ix = ran.nextInt(n);
    String s = "select id from ProblemSequenceGroup where name=?";
    PreparedStatement ps = conn.prepareStatement(s);
    ps.setString(1,groupNames[ix]);
    ResultSet rs = ps.executeQuery();
    if (rs.next())
      return rs.getInt(1);
    throw new Exception("Failed to assign to a random group");

  }

  // HACK Something is not working when running the above on Tomcat.  Group is NULL
  // This forces it to pick from the ids of the two rows we know exist
//  private int randomGroup (Connection conn) throws Exception {
//    int n = groupNames.length;
//    int ix = ran.nextInt(n);
//    if (ix == 0)
//      return 5;
//    else return 6;
//  }

  // GIven the gender determine how many students are in each group.  THen
  // return the id of the group that the student should be placed in (the one with
  // lowest number of members.).
  private int smallestGroup (Connection conn, String gender) throws Exception {
    int[] results = new int[groupNames.length];
    int[] ids = new int[groupNames.length];
    for (int i=0;i<groupNames.length;i++) {
      String group = groupNames[i];
      String q = "select count(*) from UserProfile u, ProblemSequenceGroup p where p.name=? " +
                 "and p.id=u.problemSequenceGroup and u.gender=?";
      PreparedStatement ps = conn.prepareStatement(q);
      ps.setString(1,group);
      ps.setString(2,gender);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        results[i] = rs.getInt(1);
        q = "select id from ProblemSequenceGroup where name=?";
        ps = conn.prepareStatement(q);
        ps.setString(1,group);
        rs = ps.executeQuery();
        if (rs.next())
          ids[i] = rs.getInt(1);
      }
    }
    // now we have an array of the number of members of each group for the given gender
    int min = minIx(results); // index of the min
    return ids[min];
  }
}