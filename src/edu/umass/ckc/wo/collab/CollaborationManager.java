package edu.umass.ckc.wo.collab;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.smgr.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 2/26/15
 * Time: 7:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationManager {
    //TODO: A way to clean up this map; currently states will just stick around forever
    //Collaboration states for each student: student ID -> CollaborationState
    private static Map<Integer, CollaborationState> collaborationStates = new HashMap<Integer, CollaborationState>();

    //TODO: Refactor pass on these maps and their handling
    //Students waiting for a partner: originator student ID -> WaitingStudent object
    private static Map<Integer, WaitingStudent> requesters = new HashMap<Integer, WaitingStudent>();
    //Students who have someone waiting for them: partner student ID -> set of originator student IDs
    private static Map<Integer, HashSet<Integer>> requestees_requesters = new HashMap<Integer, HashSet<Integer>>();
    //Existing collaboration matches: partner student ID -> originator student ID
    private static Map<Integer, Integer> current_matches = new HashMap<Integer, Integer>();

    //Mutators should definitely be synchronized here, but should accessors be?

    /**
     * For an originator (a person that needs help) this adds a WaitingStudent object into the cache.   The object contains a
     * list of his possible helpers (neighbors).   When the collaboration begins, the WaitingStudent object has its partner property set.
     * When the collaboration ends, the WaitingStudent element is removed from the requesters hash map.
     * @param conn
     * @param studId
     * @throws SQLException
     */
    public synchronized static void addRequest(Connection conn, int studId) throws SQLException{
        WaitingStudent waiter = new WaitingStudent();
        waiter.setPartners(conn, studId);
        Set<Integer> prospPartners = waiter.getPossiblePartners();
        requesters.put(studId, waiter);  // save the WaitingStudent object for this studID
        for(Integer helperId : prospPartners){
            if(requestees_requesters.containsKey(helperId)){
                requestees_requesters.get(helperId).add(studId);  // the helper has this student added to its list of requestors
            }
            else{
                requestees_requesters.put(helperId, new HashSet<Integer>());
                requestees_requesters.get(helperId).add(studId);
            }
        }
    }

    public synchronized static Integer checkForRequestingPartner(int id){
        if(requestees_requesters.containsKey(id)){
            Integer partner = requestees_requesters.get(id).iterator().next();
            requesters.get(partner).setPartner(id);
            current_matches.put(id, partner);
            Set<Integer> toRemove = requesters.get(partner).getPossiblePartners();
            for(Integer rem : toRemove){
                requestees_requesters.get(rem).remove(partner);
                if(requestees_requesters.get(rem).isEmpty()){
                    requestees_requesters.remove(rem);
                }
            }
            return partner;
        }
        else{
            return null;
        }
    }

    public synchronized static Integer getRequestedPartner(int id){
        return requesters.get(id).getPartner();
    }

    public synchronized static void removeRequest(int id){
        requesters.remove(id);
        current_matches.values().remove(id);
    }

    public synchronized static boolean requestExists(int id) {
        return requesters.containsKey(id);
    }

    public synchronized static boolean isPartner(int id){
        if(current_matches.containsKey(id)){
            return true;
        }
        return false;
    }

    public synchronized static Integer getRequestingPartner(int id) {
        if(current_matches.containsKey(id)){
            return current_matches.get(id);
        }
        return null;
    }

    public synchronized static String getPartnerName(Connection conn, int id) throws SQLException{
        User u = DbUser.getStudent(conn,id);
        String name = u.getFname();
        if (name == null || name.equals(""))
            return u.getUname();
        else return name;
    }

    //TODO update partners method

    public static boolean hasEligiblePartners(Connection conn, int id) throws SQLException{
        WaitingStudent waiter = new WaitingStudent();
        waiter.setPartners(conn, id);
        return !waiter.getPossiblePartners().isEmpty();
    }

    public static void clearOldData(int id){
        removeSelfFromLists(id);
        collaborationStates.remove(id);
        requesters.remove(id);
        //TODO change the next two removals to only remove if prospective partner is inactive?
        if(requestees_requesters.containsKey(id)){
            requestees_requesters.remove(id);
        }
        current_matches.remove(id);
        current_matches.values().remove(id);
    }

    public synchronized static void removeSelfFromLists(int id){
        Set<Integer> toRemove = null;
        WaitingStudent requester = requesters.get(id);
        if(requester != null){
            toRemove = requester.getPossiblePartners();
        }
        if(toRemove != null){
            for(Integer rem : toRemove){
                HashSet<Integer> requestee =  requestees_requesters.get(rem);
                if(requestee != null){
                    requestee.remove(id);
                    if(requestees_requesters.get(rem).isEmpty()){
                        requestees_requesters.remove(rem);
                    }
                }
            }
        }
    }

    public synchronized static void decline(int id) {
        removeSelfFromLists(id);
        removeRequest(id);
    }

    /**
     * Gets the CollaborationState for a student (as determined by the session manager).
     * If the student does not have a CollaborationState yet, it creates a new one.
     * @param smgr The session manager for the student.
     * @return The CollaborationState for the student.
     * @throws SQLException
     */
    public synchronized static CollaborationState getCollaborationState(SessionManager smgr) throws SQLException {
        if(!collaborationStates.containsKey(smgr.getStudentId()))
            collaborationStates.put(smgr.getStudentId(), new CollaborationState(smgr));
        else
            collaborationStates.get(smgr.getStudentId()).reloadSession(smgr);
        return collaborationStates.get(smgr.getStudentId());
    }

    /**
     * Determines whether the student can collaborate, taking into account:
     *  - Problem mode (must be practice mode)
     *  - Collaboration cooldown (time between collaborations)
     *  - Number of problems between collaborations (normally disabled)
     * @param smgr The session manager for the student.
     * @return Whether this student can collaborate.
     * @throws SQLException
     */
    public synchronized static boolean canCollaborate(SessionManager smgr) throws SQLException {
        int id = smgr.getStudentId();
        CollaborationState state = getCollaborationState(smgr);
        return hasEligiblePartners(smgr.getConnection(), id)
                && Problem.PRACTICE.equals(smgr.getStudentState().getLessonState().getNextProblemMode())
                && (state.isTimeToCollab() || state.hasSeenEnoughProblemsForCollab());
    }

    private static class WaitingStudent{
        private Set<Integer> possiblePartners;
        private Integer partner = null;

        private void setPartner(Integer partner){
            this.partner = partner;
        }

        private Integer getPartner(){
            return partner;
        }

        private Set<Integer> getPossiblePartners(){
            return possiblePartners;
        }

        private void setPartners(Connection conn, int id) throws SQLException {
            possiblePartners = getNeighbors(conn, id);
        }

        private Set<Integer> getNeighbors(Connection conn, int id) throws SQLException {
            HashSet<Integer> partners = new HashSet<Integer>();
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                String q = "select leftStudId, rightStudId from flankinguser where studId=?";
                ps = conn.prepareStatement(q);
                ps.setInt(1,id);
                rs = ps.executeQuery();
                if (rs.next() ) {
                    Integer left = rs.getInt("leftStudId");
                    Integer right = rs.getInt("rightStudId");
                    //This will be a problem if a  studIds is ever 0.
                    if(left != 0){
                        partners.add(left);
                    }
                    if(right != 0){
                        partners.add(right);
                    }
                }
            } finally {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();

            }
            return partners;
        }
    }
}