package edu.umass.ckc.wo.tutor.pedModel;

import edu.umass.ckc.wo.event.tutorhut.EndProblemEvent;
import edu.umass.ckc.wo.log.TutorLogger;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.DynamicPedagogy;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Switcher;
import org.apache.log4j.Logger;
import org.jdom.Element;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 8/25/15
 * Time: 8:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicPedagogicalModel extends CollabPedagogicalModel {

    private static Logger logger = Logger.getLogger(BasePedagogicalModel.class);
    private List<Switcher> switchers = new ArrayList<Switcher>();

    public DynamicPedagogicalModel(){
        super();
    }

    public DynamicPedagogicalModel(SessionManager smgr, Pedagogy pedagogy) throws SQLException {
        super(smgr, pedagogy);
    }

    @Override
    protected void buildComponents(SessionManager smgr, Pedagogy pedagogy){
        super.buildComponents(smgr, pedagogy);
        try{
            buildSwitchersFromPedagogy(pedagogy);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildSwitchersFromPedagogy(Pedagogy pedagogy) throws Exception {
       DynamicPedagogy ped = (DynamicPedagogy) pedagogy;
        List<Element> switcherElts = ped.getSwitchers().getChildren("switcher");
        for (Element elt : switcherElts) {

            Switcher switcher = (Switcher)Class.forName(Pedagogy.getFullyQualifiedClassname(Pedagogy.defaultClasspath+".switcher",elt.getAttributeValue("class"))).getConstructor(SessionManager.class).newInstance(smgr);
            switchers.add(switcher);
        }
    }

    @Override
    public Response processEndProblemEvent(EndProblemEvent e) throws Exception {
        Response r = new Response();

        // at the end of a problem the emotional state of the student model is updated
//        this.studentModel.updateEmotionalState(this.smgr,e.getProbElapsedTime(),e.getElapsedTime());
        this.studentModel.endProblem(smgr, smgr.getStudentId(),e.getProbElapsedTime(),e.getElapsedTime());
        for(Switcher potential : switchers){
            if(potential.checkConditions()){
                potential.doChange();
                potential.doRelatedTasks();
                new TutorLogger(smgr).logDynamicChange(e, potential.toString());
            }
        }
        r.setEffort(this.studentModel.getEffort());
        new TutorLogger(smgr).logEndProblem(e, r);

        if (learningCompanion != null )
            learningCompanion.processUncategorizedEvent(e,r);
        return r;
    }

}
