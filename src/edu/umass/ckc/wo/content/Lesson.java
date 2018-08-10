package edu.umass.ckc.wo.content;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 8/1/14
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class Lesson {
    private int id;
    private String name;
    private String notes;
    private Timestamp dueDate;
    private Timestamp assignedDate;
    private List<LessonOmit> omits;


    private List<CurricUnit> curricUnits;


    public Lesson() {
        this.curricUnits = new ArrayList<CurricUnit>();
    }

    public Lesson(int id, String name, String notes, Timestamp due, Timestamp assigned) {
        this();
        this.name=name;
        this.id=id;
        this.notes=notes;
    }

    public List<CurricUnit> getCurricUnits() {
        return curricUnits;
    }

    public int getId() {
        return id;
    }


    /**
     * Insert the cu into position given (positions are 1-based.   We are storing in an array and
     * have to convert to 0 based.
     * @param cu
     * @param position
     */
    public void insertCU(CurricUnit cu, int position) {
        curricUnits.add(position-1,cu);
    }

    /**
     * Given a 1-based position, get the CU (convert to 0 based to fetch from array)
     * @param pos
     * @return
     */
    public CurricUnit getCUByPosition (int pos) {
        return curricUnits.get(pos-1);
    }

    public List<LessonOmit> getOmits() {
        return omits;
    }

    public void setOmits(List<LessonOmit> omits) {
        this.omits = omits;
    }
}
