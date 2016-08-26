package ca.aeso.ltlf.server.service;

import java.util.Date;
import java.util.List;

import net.sf.hibernate4gwt.gwt.HibernateRemoteService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.aeso.ltlf.model.GapAnalysis;
import ca.aeso.ltlf.model.GapSummaryValue;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.dao.StagingDao;

/**
 * StagingServiceImpl
 *
 */
public class StagingServiceImpl extends  HibernateRemoteService implements StagingService {
	
	StagingDao dao;
	protected static Log logger = LogFactory.getLog(MpAnalysisServiceImpl.class);

	public StagingServiceImpl() {
		super();		
	}
	public void setDao(StagingDao lsDao) {
		dao = lsDao;
	}
	public Integer loadStaging(Date startDate, Date endDate) throws LtlfServiceException{
		Integer returnCode = dao.loadStaging(startDate, endDate);
		return returnCode;
	}

	public void gapAnalysis(String mpName) throws LtlfServiceException{
		dao.gapAnalysis(mpName);
	}
	
	public GapAnalysis fetchGapAnalysis(boolean filteredResults) throws LtlfServiceException{
		GapAnalysis returnValue = dao.fetchGapAnalysis(filteredResults);
		
		return returnValue;
	}
	
	public void saveGapAnalysis(List<GapSummaryValue> aValue) throws LtlfServiceException {
		dao.saveGapAnalysis(aValue);
	}

	public GapSummaryValue getGapSummaryValue(Long mpOid) throws LtlfServiceException{
		logger.debug("StagingServiceImpl.getGapSummaryValue starting");
		GapSummaryValue returnValue = dao.getGapSummaryValue(mpOid);
		logger.debug("StagingServiceImpl.getGapSummaryValue done, got " + (returnValue!=null?1:0) );
		return returnValue;
	}
}
