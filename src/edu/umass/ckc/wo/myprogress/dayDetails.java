package edu.umass.ckc.wo.myprogress;

import edu.umass.ckc.wo.content.CCStandard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dovanrai
 * Date: 9/10/12
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class dayDetails {


    private int dayIndex;
    private String day;
    private int problemsSolved;
    private int topicsDone;

    List<String> topicsList= new ArrayList();
    List<Float> topicMasteryList= new ArrayList();


    public dayDetails(int day_Index, String dateDay, int problems_Solved, int topics_Done,  List<String> topics_List, List<Float> topicMastery_List){

        dayIndex=day_Index;
        day=dateDay;
        problemsSolved=problems_Solved;
        topicsDone=topics_Done;
        topicsList=topics_List;
        topicMasteryList=topicMastery_List;

    }

    public int getDayIndex() {
        return dayIndex;
    }

    public String getDay() {
        return day;
    }

    public int getProblemsSolved() {
        return problemsSolved;
    }

    public int getTopicsDone() {
        return topicsDone;
    }

    public List<String> getTopicsList() {
        return topicsList;
    }

    public List<Float> getTopicMasteryList() {
        return topicMasteryList;
    }


}