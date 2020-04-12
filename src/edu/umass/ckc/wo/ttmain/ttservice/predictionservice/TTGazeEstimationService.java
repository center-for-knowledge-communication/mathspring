package edu.umass.ckc.wo.ttmain.ttservice.predictionservice;

import java.util.List;

import edu.umass.ckc.wo.ttmain.ttmodel.GazeEstimation;

public interface TTGazeEstimationService {

	public boolean save(GazeEstimation gazeEstimation);
	public List<GazeEstimation> listAll();
	public GazeEstimation get(Long id);
	public void delete(Long id);
}
