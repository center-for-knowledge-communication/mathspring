package edu.umass.ckc.wo.tutor;
import java.sql.Connection;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class ResponseSelector {
    private Connection conn_;

    public ResponseSelector(Connection conn) {
        conn_ = conn;
    }

//    public Response selectResponse (boolean isStudentCorrect, Hint h) {
//        Emotion em ;
//        if (isStudentCorrect)
//            em = new Emotion(EmotionEnum.HAPPY);
//        else em = new Emotion(EmotionEnum.SAD);
//        Response r = new Response(isStudentCorrect,h,em);
//        return r;
//    }
}