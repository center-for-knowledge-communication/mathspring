package edu.umass.ckc.wo.ttmain.ttservice.predictionservice.impl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umass.ckc.wo.ttmain.ttconfiguration.TTConfiguration;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.ErrorCodeMessageConstants;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.AuPrediction;
import edu.umass.ckc.wo.ttmain.ttmodel.PerProblemReportBean;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.impl.TTReportServiceImpl;
import edu.umass.ckc.wo.ttmain.ttservice.predictionservice.TTAuPredictionService;
import edu.umass.ckc.wo.ttmain.ttservice.util.TTUtil;

@Service
@Transactional
public class AuPredictionServiceImpl implements TTAuPredictionService{


    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static Logger logger = Logger.getLogger(TTReportServiceImpl.class);
	private ResourceBundle rb = null;
	
	@Override
	public boolean save(AuPrediction auprediction) {
		boolean success = true;
		
		try {
		AuPrediction au = null;//repo.save(auprediction);
		if(au==null) {
			success = false;
		}
		}catch(Exception e) {
			success = false;
			System.out.println(e.getStackTrace());
		}
		return success;
	}
	
	@Override
	public List<AuPrediction> listAll(String teacherId,String studentId,String lang) {
		//return (List<AuPrediction>) repo.findAll();
		
		Map<String, String> selectParams = new LinkedHashMap<String, String>();
		Map<String, String> problemDescriptionMap = new LinkedHashMap<String, String>();
		//selectParams.put("classId", studen);
		//selectParams.put("clusterID", clusterId);
		List<AuPrediction> auPredictionList = namedParameterJdbcTemplate.query(TTUtil.ALL_AU_PREDS, selectParams, new RowMapper<AuPrediction>() {
		    @Override
		    public AuPrediction mapRow(ResultSet rs, int rowNum) throws SQLException {
		    	AuPrediction employee = new AuPrediction();
		 
		        employee.setId(rs.getLong("ID"));
		        employee.setAu1(rs.getString("au1"));
		        employee.setAu2(rs.getString("au2"));
		        employee.setAu3(rs.getString("au3"));
		        employee.setSaveTime(Timestamp.valueOf(rs.getString("saveTime")));
		 
		        return employee;
		    }
		});
		//Map<String, PerProblemReportBean> resultObjectPerCluster = generatePerProblemReportForGivenProblemID(classId, problemIdsList, problemDescriptionMap);
		//ObjectMapper perStudentPerProblemClusterReportMapper = new ObjectMapper();
		return auPredictionList;//perStudentPerProblemClusterReportMapper.writeValueAsString(resultObjectPerCluster);
		
	}
	
	public List<AuPrediction> listAllWithinDates(String teacherId,String studentId,String lang) {
		
		return null;// (List<AuPrediction>) repo.findBySaveTimeBetween(fromTime, toTime);
	}
	
	@Override
	public AuPrediction get(Long id) {
		return null;//repo.findById(id).get();
	}
	
	@Override
	public void delete(Long id) {
		//repo.deleteById(id);
	}

	
	
}
