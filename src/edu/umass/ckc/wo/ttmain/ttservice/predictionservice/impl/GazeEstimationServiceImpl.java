package edu.umass.ckc.wo.ttmain.ttservice.predictionservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umass.ckc.wo.ttmain.ttmodel.GazeEstimation;
import edu.umass.ckc.wo.ttmain.ttservice.predictionservice.TTGazeEstimationService;

@Service
@Transactional
public class GazeEstimationServiceImpl implements TTGazeEstimationService{

	
	
	@Override
	public boolean save(GazeEstimation gazeEstimation) {
		boolean success = true;
		
		try {
			GazeEstimation ge = null;//repo.save(gazeEstimation);
		if(ge==null) {
			success = false;
		}
		}catch(Exception e) {
			success = false;
			System.out.println(e.getStackTrace());
		}
		return success;
	}
	
	@Override
	public List<GazeEstimation> listAll() {
		return null;//(List<GazeEstimation>) repo.findAll();
	}
	
	@Override
	public GazeEstimation get(Long id) {
		return null;//repo.findById(id).get();
	}
	
	@Override
	public void delete(Long id) {
		//repo.deleteById(id);
	}

	

	
}
