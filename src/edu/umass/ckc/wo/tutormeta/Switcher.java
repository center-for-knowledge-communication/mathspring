package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.DynamicPedagogy;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.model.InterventionGroup;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 10/2/15
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Switcher {

    protected SessionManager smgr;
    protected DynamicPedagogy ped;
    protected InterventionGroup intervs;

    public Switcher(SessionManager smgr, DynamicPedagogy pedagogy, InterventionGroup intervs){
        this.smgr = smgr;
        ped = pedagogy;
        this.intervs = intervs;
    }

    public abstract boolean checkConditions();

    public abstract void doChange();

    public abstract void doRelatedTasks();


    //TODO coordinate whole class for time-based events


}
