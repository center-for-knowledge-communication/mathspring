package edu.umass.ckc.wo.beans;
import edu.umass.ckc.wo.content.CCStandard;
import edu.umass.ckc.wo.content.Problem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 17, 2008
 * Time: 10:30:17 AM
 * 
 * Frank	12-26-20	Issue #329 Added mlName element for multi-lingual support
 * Frank	01-03-21	Issue #329R2 Added mlDescription and fixed setters to parse JSON formats
 */


public class Topic implements Comparable<Topic> {
    private int id;
    private String name;
    private String mlName;
    private String summary;
    private String mlDescription;
    private int seqPos;
    private int oldSeqPos;
    private Set<CCStandard> ccStandards;
    private int numProbs;
    private int[] problemsByGrade;
    private Map<String,Integer> gradewiseProblemDistribution;
    private Map<String, String> mlNameMap;
    private Map<String, String> mlDescMap;
    
    public static final String ID = "id";
    public static final String INTRO = "intro";
    public static final String SUMMARY = "summary";
    public static final String DESCRIPTION = "description";
    public static final String TYPE = "type";
    public static final String ACTIVE = "active";
   
    public Topic () {
    }

    public Topic (int id, String name) {
        this();
        this.name = name;
        this.id = id;
    }

    public Topic (int id, String name, String summary) {
        this(id,name);
        setSummary(summary);
    }

    public Topic (int id, String name, String summary, String mlName) {
        this(id,name);
        setSummary(summary);
        setMlName(mlName);
    }

    public Topic (int id, String name, String summary, String mlName, String mlDescription) {
        this(id,name);
        setSummary(summary);
        setMlName(mlName);
        setMlDescription(mlDescription);
    }
    
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public void setName (String n) {
        this.name = n;
    }

    public String getMlDescription() {
        return mlDescription;
    }

    public String getMlDescription(String lang) {
    	
    	String result = mlNameMap.get(lang);
        return result;
    }


    
    public void setMlDescription (String d) {
    	// Store JSON-formatted string
        mlDescription = d;

    	// Convert JSON-formatted string to HashMap
        String tmpDesc =  d;
    	try {
		    tmpDesc = tmpDesc.replace("{","");
		    tmpDesc = tmpDesc.replace("}","");
		    tmpDesc = tmpDesc.replaceAll("\"","");
		    tmpDesc = tmpDesc.replaceFirst(",","~");
		    tmpDesc = tmpDesc.trim();
		    String jsonArr[] = tmpDesc.split("~");
		    mlDescMap = new HashMap<String, String>();
		    for( int i=0; i< jsonArr.length; i++ ) {
		    	String jsonObj = jsonArr[i];
		    	int index = jsonObj.indexOf(":");
		    	if (index > 1) {
			    	String lang = jsonObj.substring(0,index);
			    	String text = jsonObj.substring(index+1,jsonObj.length());
		        	mlDescMap.put(lang.trim(),text.trim());
		    	}
		    	else {
		    		System.out.println("Malformed topic description " + mlName);
		    	}
		   }
	    
    	}
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
     }


    
    public String getMlName() {
        
    	return mlName;
    }

    public String getMlName(String lang) {
    	
    	String result = mlNameMap.get(lang);
        return result;
    }

    public void setMlName (String n) {
    	// Store JSON-formatted string
        mlName = n;
    	// Convert JSON-formatted string to HashMap
        String tmpName =  n;
    	try {
		    tmpName = tmpName.replace("{","");
		    tmpName = tmpName.replace("}","");
		    tmpName = tmpName.replaceAll("\"","");
		    tmpName = tmpName.replaceFirst(",","~");
		    tmpName = tmpName.trim();
		    String jsonArr[] = tmpName.split("~");
		    mlNameMap = new HashMap<String, String>();
		    for( int i=0; i< jsonArr.length; i++ ) {
		    	String jsonObj = jsonArr[i];
		    	int index = jsonObj.indexOf(":");
		    	if (index > 1) {
		    		String lang = jsonObj.substring(0,index);
		    		String text = jsonObj.substring(index+1,jsonObj.length());
		    		mlNameMap.put(lang.trim(),text.trim());
		    	}
		    	else {
		    		System.out.println("Malformed topic name " + mlName);
		    	}
		    }
    	}
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeqPos() {
        return seqPos;
    }

    public void setSeqPos(int seqPos) {
        this.seqPos = seqPos;
    }

    public int getOldSeqPos() {
        return oldSeqPos;
    }

    public void setOldSeqPos(int oldSeqPos) {
        this.oldSeqPos = oldSeqPos;
    }

    public Set<CCStandard> getCcStandards() {
        return ccStandards;
    }

    public void setCcStandards(Set<CCStandard> ccStandards) {
        this.ccStandards = ccStandards;
    }

    public Map<String, Integer> getGradewiseProblemDistribution() {
        return gradewiseProblemDistribution;
    }

    public void setGradewiseProblemDistribution(Map<String, Integer> gradewiseProblemDistribution) {
        this.gradewiseProblemDistribution = gradewiseProblemDistribution;
    }

    /**
     * @return The Common Core Standards associated with this topic, as a comma-separated string.
     */
    public String getStandards () {
        if (this.ccStandards == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (CCStandard std : this.ccStandards) {
            sb.append(std.getCode() + ",");
        }
        if (sb.length()>0)
            sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public int getNumProbs() {
        return numProbs;
    }

    public void setNumProbs(int numProbs) {
        this.numProbs = numProbs;
    }

    public int[] getProblemsByGrade() { return problemsByGrade; }

    public void setNumProbsByGrade(int[] problemsByGrade) {
        this.problemsByGrade = problemsByGrade;
    }

    // sort based on the previous indices set on the topics
    public int compareTo (Topic other) {
        int diff =  this.getOldSeqPos() - other.getOldSeqPos();
        return diff;
    }

    public String toString () {
        return " id: " + this.id + " curPos: " + this.getSeqPos() + " prevPos: " + this.getOldSeqPos() +  " name: " + this.name ;
    }
}
