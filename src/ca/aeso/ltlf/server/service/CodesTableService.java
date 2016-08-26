package ca.aeso.ltlf.server.service;

import java.util.List;

import ca.aeso.ltlf.model.CodesTable;
import ca.aeso.ltlf.rpc.LtlfServiceException;

public interface CodesTableService {

	public Integer getRefreshInterval() throws LtlfServiceException;
	public Integer getBaseYear() throws LtlfServiceException;
	public String getReleaseId() throws LtlfServiceException;
	public List<CodesTable> getAllocationSectorTypes() throws LtlfServiceException;
	public CodesTable getCodeValue(String key) throws LtlfServiceException;
	
}
