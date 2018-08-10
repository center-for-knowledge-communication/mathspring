package edu.umass.ckc.wo.tutor.agent;

import edu.umass.ckc.wo.tutormeta.StudentModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ivon
 * Date: Jul 24, 2008
 * Time: 11:04:18 AM
 * This is a BASIC EMOTIONAL LEARNING COMPANION. It shows some positive clips only,
 * to make the characters more dynamic and change postures.
 */
public abstract class BasicEmotionalLC extends EmotionalLC {


     public List<String> selectEmotions(StudentModel sm)
    {
       List<String> r = new ArrayList<String>();

       String[] emotions = new String[] {
               "high-interest",
            //   "low-interest",
               "high-excitement",
               "low-excitement",
            //   "high-frustration",
               "low-frustration",
               "high-confidence",
               "low-confidence" } ;

       //Every 3 problems it shows a random emotion
       if ( java.lang.Math.random() < 0.34  ) {
           List emotionsList = Arrays.asList(emotions);
           Collections.shuffle(emotionsList);

           r.add( (String) emotionsList.get(0)) ;
       }

        //Every 5 problems, it trains attributions
       if ( java.lang.Math.random() < 0.10 ) {
           List genAttrList = Arrays.asList(generalAttribution);
           Collections.shuffle(genAttrList);

           r.add( (String) genAttrList.get(0)) ;
       }

       return r ;
    }


}
