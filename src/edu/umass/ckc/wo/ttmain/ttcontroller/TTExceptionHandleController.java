package edu.umass.ckc.wo.ttmain.ttcontroller;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by nsmenon on 6/16/2017.
 */
@ControllerAdvice
public class TTExceptionHandleController {

    @ExceptionHandler(TTCustomException.class)
    public @ResponseBody String handleTeacherToolException(TTCustomException customException){
        return customException.getErrorCode() + "*** " + customException.getErrorMessage();
    }
}


