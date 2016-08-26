package ca.aeso.ltlf.server.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import ca.aeso.ltlf.model.GapAnalysis;
import ca.aeso.ltlf.model.GapSummaryValue;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.model.CallCopyFromStaging;
import ca.aeso.ltlf.server.model.CallGapAnalysis;
import ca.aeso.ltlf.server.model.CallLoadStaging;

/**
 * DAO methods to support staging operations
 *
 */
public class StagingDaoImpl implements StagingDao {
	public static final String versionId = "$Id: StagingDaoImpl.java,v 1.8 2008/06/24 20:18:56 mbodor Exp $ by $Author: mbodor $, $DateTime:  $";
    private static final Log logger = LogFactory.getLog(StagingDaoImpl.class);

    private DataSource dataSource;
    private SessionFactory _sessionFactory;
    
    public void setDataSource(DataSource dataSource) {
    	logger.debug("StagingDaoImpl.setDataSource(): executing");
        this.dataSource = dataSource;
    }

    public SessionFactory getSessionFactory()
    {
        return _sessionFactory;
    }

    /**
     * Sets the associated Hibernate session facgtory
     * @param factory
     */
   public void setSessionFactory(SessionFactory factory)
    {
    	_sessionFactory = factory;
    }

	public Integer loadStaging(Date startDate, Date endDate) throws LtlfServiceException {
		Integer returnCode = null;
		HashMap results = null;
		CallLoadStaging proc = new CallLoadStaging(dataSource);
		logger.debug("StagingDaoImpl.loadStaging() executing stored proc ");
		results = proc.execute(startDate,  endDate);
		returnCode = (Integer)results.get(CallLoadStaging.ReturnCodeKey);
		if (results.get(CallLoadStaging.ErrorKey) != null) {
			String errorMsg = (String)results.get(CallLoadStaging.ErrorKey);
			logger.error("StagingDaoImpl.loadStaging() error: " + errorMsg);
			throw new LtlfServiceException(errorMsg);
		}
		return returnCode;
	}
	
	public void gapAnalysis(String mpName)  throws LtlfServiceException{
		logger.debug("StagingDaoImpl.gapAnalysis() starting");
		HashMap results = null;
		CallGapAnalysis proc = new CallGapAnalysis(dataSource);
		results = proc.execute(mpName);
		if (results.get(CallGapAnalysis.ErrorKey) != null) {
			String errorMsg = (String)results.get(CallGapAnalysis.ErrorKey);
			logger.error("StagingDaoImpl.gapAnalysis() error: " + errorMsg);
			throw new LtlfServiceException(errorMsg);
		}
		logger.debug("StagingDaoImpl.gapAnalysis() done");
	}
	
	public void copyFromStaging(String mpName)  throws LtlfServiceException{
		logger.debug("StagingDaoImpl.copyFromStaging() starting");
		HashMap results = null;
		CallCopyFromStaging proc = new CallCopyFromStaging(dataSource);
		results = proc.execute(mpName);
		if (results.get(CallCopyFromStaging.ErrorKey) != null) {
			String errorMsg = (String)results.get(CallCopyFromStaging.ErrorKey);
			logger.error("StagingDaoImpl.copyFromStaging error " + errorMsg);
			throw new LtlfServiceException(errorMsg);
		}
		logger.debug("StagingDaoImpl.copyFromStaging() done");
	}

	public GapAnalysis fetchGapAnalysis(boolean filterResults) {
		logger.debug("StagingDaoImpl.fetchGapAnalysis() starting");
		GapAnalysis resultValue = null;
		try {
			Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();

			if (filterResults) {
				_sessionFactory.getCurrentSession().enableFilter("FilteredResults");
			}
			else {
				_sessionFactory.getCurrentSession().disableFilter("FilteredResults");
			}
			Query query = (Query) _sessionFactory.getCurrentSession().createQuery("from GapAnalysis");
			List records = query.list();
			if (records == null || records.size() == 0)
				return null;
			resultValue = (GapAnalysis)records.get(0);
			Iterator<GapSummaryValue> itr = resultValue.getSummaryValues().iterator();
			while (itr.hasNext())
				itr.next().getMeasurementPoint().getCurrentDetail().getOid();
			tx.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		logger.debug("StagingDaoImpl.fetchGapAnalysis() done");
		return resultValue;
	}
	
	public void saveGapAnalysis(List<GapSummaryValue> aValue){
		logger.debug("StagingDaoImpl.saveGapAnalysis() starting");
		try {
			Transaction tx1 = _sessionFactory.getCurrentSession().beginTransaction();
			Iterator<GapSummaryValue> itr = aValue.iterator();
			while (itr.hasNext())
				_sessionFactory.getCurrentSession().update(itr.next());
			tx1.commit();
		}
		catch (Exception ex)
		{
			logger.error("Exception saving gap analysis: " + ex);
			ex.printStackTrace();
		}
		logger.debug("StagingDaoImpl.saveGapAnalysis() done");
	}
	
	public GapSummaryValue getGapSummaryValue(Long mpOid) throws LtlfServiceException {
		logger.debug("StagingDaoImpl.getGapSummaryValue starting, oid=" + mpOid);
		
		GapSummaryValue resultValue = null;
		
		try {
			Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();

			Criteria queryCrit = _sessionFactory.getCurrentSession().createCriteria(GapSummaryValue.class);
			Criterion mpCrit = Restrictions.eq("mpOid", mpOid);
			queryCrit.add(mpCrit);
			
			List summValues = queryCrit.list();
			if (summValues.size()>0)
				resultValue = (GapSummaryValue)summValues.get(0);
			
			tx.commit();

		} catch (Exception ex) {
			logger.error("Exception getting GapSummaryValue: " + ex);
			ex.printStackTrace();
			throw new LtlfServiceException("Exception getting GapSummaryValue: " + ex);
		}

		logger.debug("StagingDaoImpl.getGapSummaryValue done");
		return resultValue;
	}

}
