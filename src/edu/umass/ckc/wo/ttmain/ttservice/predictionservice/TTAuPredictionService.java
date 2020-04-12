package edu.umass.ckc.wo.ttmain.ttservice.predictionservice;

import java.util.List;

import edu.umass.ckc.wo.ttmain.ttmodel.AuPrediction;

public interface TTAuPredictionService {

	public boolean save(AuPrediction auprediction);
	public List<AuPrediction> listAll(String teacherId,String studentId,String lang);
	public List<AuPrediction> listAllWithinDates(String teacherId, String studentId, String lang);
	public AuPrediction get(Long id);
	public void delete(Long id);
}
