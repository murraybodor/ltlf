package ca.aeso.ltlf.server.service;

import java.util.List;

import net.sf.hibernate4gwt.gwt.HibernateRemoteService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.aeso.ltlf.model.CodesTable;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.dao.CodesTableDao;

/**
 * Service class for getting values from CODES table
 * 
 * @author schen
 */
public class CodesTableServiceImpl extends HibernateRemoteService implements CodesTableService {

	CodesTableDao _dao;
	protected static Log logger = LogFactory.getLog(CodesTableServiceImpl.class);

	public void setDao(CodesTableDao dao) {
		_dao = dao;
	}

	public Integer getRefreshInterval() throws LtlfServiceException {
		logger.debug("CodesTableServiceImpl.getRefreshInterval() starting");

		try
		{
			return (int)Math.round(getCodeValue("INTERVAL").getNumericValue());
		}
		catch (Exception e)
		{
			logger.error("CodesTableServiceImpl caught exception in getRefreshInterval: " + e);
			e.printStackTrace();
			throw new LtlfServiceException("Error getting refresh interval: " + e.getMessage());
		}
	}

	public Integer getBaseYear() throws LtlfServiceException {
		logger.debug("CodesTableServiceImpl.getBaseYear() starting");

		try
		{
			return (int)Math.round(getCodeValue("BASE_YEAR").getNumericValue());
		}
		catch (Exception e)
		{
			logger.error("CodesTableServiceImpl caught exception in getBaseYear: " + e);
			e.printStackTrace();
			throw new LtlfServiceException("Error getting base year: " + e.getMessage());
		}
	}

	public String getReleaseId() throws LtlfServiceException {
		logger.debug("CodesTableServiceImpl.getReleaseId() starting");

		try
		{
			return getCodeValue("RELEASE_ID").getStringValue();
		}
		catch (Exception e)
		{
			logger.error("CodesTableServiceImpl caught exception in getReleaseId: " + e);
			e.printStackTrace();
			throw new LtlfServiceException("Error getting release id: " + e.getMessage());
		}
	}

	public List<CodesTable> getAllocationSectorTypes() throws LtlfServiceException {
		logger.debug("CodesTableServiceImpl.getAllocationSectorTypes() starting");

		try
		{
			return _dao.getCodeValues("ALLOC_SECTOR");
		}
		catch (Exception e)
		{
			logger.error("CodesTableServiceImpl caught exception in getAllocationSectorTypes: " + e);
			e.printStackTrace();
			throw new LtlfServiceException("Error getting allocation sector types: " + e.getMessage());
		}
	}
	
	public CodesTable getCodeValue(String key) throws LtlfServiceException {
		logger.debug("CodesTableServiceImpl.getCodeValue() starting");

		try
		{
			return _dao.getCodeValue(key);
		}
		catch (Exception e)
		{
			logger.error("CodesTableServiceImpl caught exception in getCodeValue: " + e);
			e.printStackTrace();
			throw new LtlfServiceException(e.getMessage());
		}
		
	}
	
}
