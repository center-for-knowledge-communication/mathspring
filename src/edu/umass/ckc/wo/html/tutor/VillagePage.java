package edu.umass.ckc.wo.html.tutor;

import edu.umass.ckc.wo.tutormeta.LearningCompanion;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 6/7/12
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class VillagePage {

    private LearningCompanion lc;
    private int sessNum;

    public VillagePage(LearningCompanion lc, int sessNum) {
        this.lc =lc;
        this.sessNum = sessNum;
    }

    public String getPage (String flashClient) {
        String name="";
        if (lc != null)
            name=lc.getCharactersName();
        String x = "<META HTTP-EQUIV=\"Refresh\" Content=\"0; URL=http://cadmium.cs.umass.edu/wayang2/flash/client/" +flashClient+ ".swf?sessnum="
                    +sessNum+ "&learningHutChoice=false&learningCompanion=" +name+ "\"/>";
        String y = "<META HTTP-EQUIV=\"Refresh\" Content=\"0; URL=http://localhost/wayang/flash/client/" + flashClient + ".swf?sessnum="
                    +sessNum+ "&learningHutChoice=false&learningCompanion=" +name+ "\"/>";
            return y;
    }

}
