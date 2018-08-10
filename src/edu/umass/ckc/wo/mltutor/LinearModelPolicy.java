package edu.umass.ckc.wo.mltutor;

import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.StreamTokenizer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Connection;


/**
 * Copyright (c) University of Massachusetts
 * Written by: Alicia P. Wolfe
 * Date: Sep 26, 2005
 * Time: 11:30:46 AM
 */
class ProblemDifficultyAction implements WayangAction {
    int id;
    double low, high;
    public ProblemDifficultyAction(int id, double low, double high) {
	this.id = id;
	this.low = low;
	this.high = high;
    }
    public double high() { return high; }
    public double low() { return low; }
    public String name() {return "PROBLEM DIFFICULTY";}
    public int id() { return id; }

    public String toString() {
	return "" + id + ":" + low + ", " + high;
    }
}




class Utilities {
    static final char QUOTE_CHAR = '\"';



    /**
       Reads in a 1-D array of strings, on one line of the input file, formatted as follows:

       <double><sep><double><sep>...<newline>

       Where "sep" is any of the standard separators reconized by java.
       
     */

    static String[] readStringArray(StreamTokenizer tokens) throws IOException {
	ArrayList tempArray = new ArrayList();
	tokens.nextToken();
	while (tokens.ttype == tokens.TT_EOL) {
	    tokens.nextToken();
	}

	while ((!(tokens.ttype == tokens.TT_EOL)) & (!(tokens.ttype == tokens.TT_EOF))) {
	    // these tokens should all be strings!
	    if((tokens.ttype != tokens.TT_WORD) & (tokens.ttype != QUOTE_CHAR)) {
		//System.out.println((char) tokens.ttype);
		throw new RuntimeException("Cannot parse policy file: non-string value where string expected <" + tokens.ttype + "> on line " + tokens.lineno());
	    }
	    //System.out.println(tokens.sval);
	    tempArray.add(tokens.sval);
	    tokens.nextToken();
	}
	String[] retVal = new String[tempArray.size()];

	for (int i = 0; i < tempArray.size(); i++) {
	    retVal[i] = (String) (tempArray.get(i));
	}
	return retVal;

    }



    /**
       Reads in a 1-D array of doubles, on one line of the input file, formatted as follows:

       <double><sep><double><sep>...<newline>

       Where "sep" is any of the standard separators recognized by java. The doubles must be in a format parsable by the Double constructor (they will first be read in as strings, then parsed).
       
     */

    static double[] readDoubleArray(StreamTokenizer tokens) throws IOException {
	ArrayList tempArray = new ArrayList();
	tokens.nextToken();
	while (tokens.ttype == tokens.TT_EOL) {
	    tokens.nextToken();
	}

	// switch to not take numbers into account

	while ((!(tokens.ttype == tokens.TT_EOL)) & (!(tokens.ttype == tokens.TT_EOF))) {
	    // these tokens should all be numbers!
	    // change to use Double.parseDouble to get the exponents

	    if (tokens.ttype == tokens.TT_NUMBER) {
		tempArray.add(new Double(tokens.nval));
		tokens.nextToken();
	    } else if ((tokens.ttype == tokens.TT_WORD) || (tokens.ttype == QUOTE_CHAR)) {
		//System.out.println(tokens.sval);
		tempArray.add(new Double(tokens.sval));
		tokens.nextToken();
	    } else {
		throw new RuntimeException("Cannot parse policy file: non-numeric value where number expected <" + tokens.ttype + "> on line " + tokens.lineno());
	    }	
	}

	// convert from an ArrayList to an array
	double[] retVal = new double[tempArray.size()];

	for (int i = 0; i < tempArray.size(); i++) {
	    retVal[i] = ((Double) tempArray.get(i)).doubleValue();
	}
	
	return retVal;
		
    }


    /**
       Parses a single double values from the stream.
     */
    static double readDouble(StreamTokenizer tokens) throws IOException {
	tokens.nextToken();
	while (tokens.ttype == tokens.TT_EOL)
	    tokens.nextToken();

	if (tokens.ttype != tokens.TT_NUMBER) {
	    throw new RuntimeException("Cannot parse policy file: non-numeric value where number expected <" + tokens.ttype + "> on line " + tokens.lineno());
	}
	return tokens.nval;
    }
    static int readInteger(StreamTokenizer tokens) throws IOException {
	tokens.nextToken();
	while (tokens.ttype == tokens.TT_EOL)
	    tokens.nextToken();

	if (tokens.ttype != tokens.TT_NUMBER) {
	    throw new RuntimeException("Cannot parse policy file: non-numeric value where number expected <" + tokens.ttype + "> on line " + tokens.lineno());
	}
	return (int) tokens.nval;
    }
}


/**
   Policy for a single time step.
 */
class OneStepPolicy {

    public static final Logger logger = Logger.getLogger(OneStepPolicy.class);
    static final private char QUOTE_CHAR = '"';

    HashMap featureNames = new HashMap();
    HashMap featureWeights = new HashMap();;
    
    WayangAction[] actions;

    /**
       Initialize the policy at a single time step. The
       StreamTokenizer must be advanced to the beginning of the line
       with the names of the features for this time step.
     */
    OneStepPolicy(StreamTokenizer tokens, WayangAction[] actions) throws IOException {


	this.actions = actions;

	tokens.eolIsSignificant(true);

	for (int i = 0; i < actions.length; i++) {
	    int actionId = Utilities.readInteger(tokens);
	    //System.out.println("Action: " + actionId);
	    featureNames.put(new Integer(actionId), Utilities.readStringArray(tokens));
	    featureWeights.put(new Integer(actionId), Utilities.readDoubleArray(tokens));
	}

	
    }

    // mon tues thurs fri

    /**
       Calculates the linear function of features and weights which
       determines the value of the action in this state.
     */

    private double lookupValue(HashMap featureValues, String featureString, double weight) {

	
	if (featureString.equals("(Intercept)")) {
	    return weight;
	} else if (featureString.indexOf(":") > -1) {
	    String temp1 = featureString.substring(0, featureString.indexOf(":"));
	    String temp2 = featureString.substring(featureString.indexOf(":")+1);
	    //System.out.println("multiples: " + temp1 + ", " + temp2);
	    return mapLookup(featureValues, temp1) * mapLookup(featureValues, temp1) * weight;

	} else if (featureString.indexOf("^2") > -1) {
	    // form: "I(<name>^2)
	    String temp = featureString.substring(featureString.indexOf("(")+1, featureString.indexOf("^"));
	    //System.out.println("square: " + temp);
	    return Math.pow(mapLookup(featureValues, temp), 2) * weight;
	}
	return (mapLookup(featureValues, featureString) * weight);

    }
    
    /**
       
     */
    private double mapLookup(HashMap featureValues, String featureName) {
	// handle values not in the map by failing silently (note that this breaks the algorithm! but not the server)
	Double value = (Double) featureValues.get(featureName);
	if (value != null) {
	    return value.doubleValue();
	} else {
	    logger.info("MLPolicy:Could not find feature for feature name " + featureName + " returning 0");
	    return 0;
	}
    }




    private double getActionValue(HashMap featureValues, String[] featureNames, double[] featureWeights) {
	double sum = 0;
	for(int i = 0; i < featureNames.length; i++) {
	    sum += lookupValue(featureValues, featureNames[i], featureWeights[i]);
	}
	return sum;
    }



    /**
     */
    WayangAction getAction(HashMap featureValues) {

	double maxValue = -1.0/0.0; // -Infinity
	int maxAct = -1;
	//System.out.println(featureNames.size());
	for (int i = 1; i < featureNames.size()+1; i++) {
	    //System.out.println(featureNames.get(new Integer(i)));
	    double value = getActionValue(featureValues, (String[])featureNames.get(new Integer(i)), (double[])featureWeights.get(new Integer(i)));
	    if (value > maxValue) {
		maxValue = value;
		maxAct = i;
	    }
	}
	return actions[maxAct-1];
    }
}

/**
   The main class which reads in a policy file and parses it into an actual
   policy.
 */
public class LinearModelPolicy implements Policy {
    public static final Logger logger = Logger.getLogger(LinearModelPolicy.class);

    String[] featureNames = null;;
    WayangAction[] actions;
    ArrayList policies = new ArrayList();

    int startStep = 0;


    public LinearModelPolicy() {}
	
    /**
       Read the parameters of the policy in from a file.
     */
    public String[] init(String fileName) {
	try {
	    
	    // read in the names of the variables used
	    
	    BufferedReader file = new BufferedReader(new FileReader(fileName));
	    StreamTokenizer tokens = new StreamTokenizer(file);
	    tokens.eolIsSignificant(true);
	    tokens.commentChar('#');
	    
	    featureNames = Utilities.readStringArray(tokens);
	    
	    int numModels = Utilities.readInteger(tokens);
	    startStep = Utilities.readInteger(tokens);
	    
	    // read in the action types and values
	    // feature name(s)
	    int numActions = Utilities.readInteger(tokens);
	    
	    // ranges
	    double[] actionRanges = Utilities.readDoubleArray(tokens);
	    
	    
	    actions = new WayangAction[numActions];
	    for (int i = 0; i < numActions; i++) {
		actions[i] = new ProblemDifficultyAction(i, actionRanges[i], actionRanges[i+1]);
		//System.out.println(actionRanges[i] + ", " + actionRanges[i+1]);
	    }
	    
	    // TODO: these are in reverse order in the file!
	    // make it an array and fill in from the end?
	    
	    // read in the policy for the last step
	    for (int i = 0; i < (numModels-startStep); i++) {
		OneStepPolicy policy = new OneStepPolicy(tokens, actions);
		// insert the policies into the start of the arraylist, pushing
		// the last-read policy one further along. The policies are 
		// listed in reverse order in the file, so this results in
		// a list of policies in ascending order of time, rather than
		// descending.
		policies.add(0, policy);
	    }
	} catch (Exception exp) {
	    logger.info("MLPolicy: Could not read policy file: " + fileName + ". Error: " +exp);
	    throw(new RuntimeException("MLPolicy: Could not read policy file: " + fileName + ". Error: " +exp));
	}

	return featureNames;
    }

    /**
       Selects an action for the given time step (using the model for
       that timestep and the current features.
     */
    protected WayangAction getAction(int timeStep, HashMap stateFeatures) {
	int numPolicies = policies.size();
	timeStep = timeStep-startStep;
	timeStep = (int) Math.max(timeStep, 0);
	timeStep = (int) Math.min(timeStep, policies.size()-1);
	WayangAction action = ((OneStepPolicy) policies.get(timeStep)).getAction(stateFeatures);
	logger.info("MLPolicy chose action: " + action + " at time step " + timeStep);
	return action;
    }

    /**
       Get the problem difficulty based on the current features
     */
    public WayangAction selectProblemDifficulty(Connection conn, HashMap vals, int timeStep) {

	WayangAction act;
	try {
	    act = getAction(timeStep, vals);
	} catch (Exception exp) {
	    logger.info("MLPolicy: unable to select action at time step " + timeStep + " choosing random action. Error string: " + exp);
	    act = new ProblemDifficultyAction(-1, 0.0, 1.0);
	    throw new RuntimeException("MLPolicy: unable to select action at time step " + timeStep + " choosing random action. Error string: " + exp);
	}
	return act;
    }

    protected WayangAction[] getActionSet() {
	return actions;
    }

    public WayangAction[] getAlternativeActions(WayangAction action) {
	int id = action.id();
	// actions are ordered by their difficulty
	int maxAct = actions.length;


	WayangAction[] retAct = new WayangAction[maxAct-1];

	// do a sensible permutation: next easier, next harder, next easier...

	int index = 0;
	// first split into easier and harder, then take from each list
	for (int i = 1; i <= maxAct; i++) {
	    // get all problems at this distance
	    if ((id + i) < maxAct) {
		retAct[index] = actions[id+i];
		//System.out.println(id+i + ":" + index);
		index++;
	    }
	    if ((id - i) >= 0) {
		retAct[index] = actions[id-i];
		//System.out.println(id-i + ":" + index);
		index++;
	    }
	}
	// check that all actions are filled in?

	return retAct;
    }


    /**
       The names of the features used in this policy representation.
     */
    public String[] getFeatureNames() {
	return featureNames;
    }

}


