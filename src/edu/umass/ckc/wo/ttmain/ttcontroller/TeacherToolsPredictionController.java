package edu.umass.ckc.wo.ttmain.ttcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.AuPrediction;
import edu.umass.ckc.wo.ttmain.ttservice.predictionservice.TTAuPredictionService;

@Controller
public class TeacherToolsPredictionController {

	
	 @Autowired
	 private TTAuPredictionService auPredictionService;
	 
	 
	 	@RequestMapping(value = "/tt/getAuPredictions", method = RequestMethod.POST)
	    public @ResponseBody List<AuPrediction> getAllAuPredictions(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("classId") String studentId, @RequestParam("lang") String lang) throws TTCustomException {
	    	//System.out.println("TeacherToolsReportController filter = " + filter);
	    	return auPredictionService.listAll(teacherId, studentId, lang);

	    }

	    

	 
}
