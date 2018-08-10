package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutormeta.Intervention;

abstract public class SimpleBaseIntervention implements Intervention {

    protected int id;
    protected String resource;
    protected String name;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getResource() { return resource; }


    @Override
    public String logEventName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public SimpleBaseIntervention() {
    }

}
