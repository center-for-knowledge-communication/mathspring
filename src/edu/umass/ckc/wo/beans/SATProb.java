package edu.umass.ckc.wo.beans;

import edu.umass.ckc.wo.content.Problem;

import java.text.DecimalFormat;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 23, 2008
 * Time: 5:55:25 PM
 */
public class SATProb {
    private Problem problem;
    private boolean activated;
    private boolean externalURL ;
    private boolean formality ;

    public SATProb (Problem prob, boolean isActivated) {
        this.problem = prob;
        this.activated = isActivated;
        this.externalURL = false ;
        formality = false ;

        if ( prob.getResource()!= null && prob.getResource().startsWith("http")) {
            this.externalURL = true ;
        }
        if ( prob.getResource()!= null && prob.getName().startsWith("formality")) {
            this.formality = true ;
            this.externalURL = true ;
        }
    }

    public int getId () {
        return problem.getId();
    }

    public String getName () {
        return problem.getName();
    }

     public String getNickname () {
        return problem.getNickname();
    }

    public boolean isActivated () {
        return activated;
    }

    public boolean isFormality () {
        return formality;
    }

    public boolean isExternalURL () {
        return externalURL;
    }

    public String toString () {
        return "ID="+getId()+ " name="+ getName() + " activated=" + isActivated()
                + " externalURL=" + isExternalURL() + " resource=" + getResource() ;
    }

    public String getDifficulty() {
        DecimalFormat twoPlaces = new DecimalFormat("0.00");
        return twoPlaces.format(problem.getDifficulty());
    }


    public String getResource () {
        return problem.getResource() ;
    }

    public Problem getProblem() {
        return this.problem;
    }

    public String getQuestNum () {
        String[] name = problem.getName().split("_");
        if (name.length > 1)
            return name[1];
        else return null;
    }

    public String getType () {
        return this.problem.getType();
    }
}
