package ca.aeso.ltlf.server.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ca.aeso.ltlf.model.LoadShapeDetail;
import ca.aeso.ltlf.model.LoadShape;
import ca.aeso.ltlf.model.LoadShapeSummary;
import ca.aeso.ltlf.server.model.CallLoadShapeUnitize;
import ca.aeso.ltlf.server.model.CallLoadStaging;

/**
 * DAO methods to support load shape operations
 * 
 * @author mbodor
 */
public class LoadShapeDaoImpl implements LoadShapeDao {


    private SessionFactory sessionFactory;
    private DataSource dataSource;

	protected static Log logger = LogFactory.getLog(LoadShapeDaoImpl.class);

	public void setDataSource(DataSource dataSource) {
	    this.dataSource = dataSource;
	}
	   
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		logger.debug("LoadShapeDaoImpl.setSessionFactory() starting");
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Retrieve load shape values for the MP
	 */
	public List<LoadShapeDetail> getShapeValues(int baseYear, Long mpOid, Integer versionNumber) throws HibernateException  {
		logger.debug("LoadShapeDaoImpl.getShapeValues() starting baseyear=" + baseYear + " mpoid=" + mpOid + " version=" + versionNumber);

		Session session = sessionFactory.getCurrentSession();
		
		Transaction tx = session.beginTransaction();

		Criteria queryCrit = session.createCriteria(LoadShapeDetail.class);
		
		Criterion yearCrit = Restrictions.eq("baseYear", baseYear);
		queryCrit.add(yearCrit);
		
		Criterion mpCrit = Restrictions.eq("mpOid", mpOid);
		queryCrit.add(mpCrit);
		
		queryCrit.addOrder(Order.asc("baseDay"));
		queryCrit.addOrder(Order.asc("baseHourEnding"));
		

		Criteria vers = queryCrit.createCriteria("loadShapeSummary");
		vers.add(Restrictions.eq("versionNumber", versionNumber));
		
		List<LoadShapeDetail> resultSet = vers.list();
		tx.commit();
		
		logger.debug("LoadShapeDaoImpl.getShapeValues() done, size=" + resultSet.size());

		return resultSet;
		
	}
	
	/**
	 * Save supplied load shape values
	 */
	public int saveShapeValues(List<LoadShapeDetail> shapeValues) throws HibernateException  {
		logger.debug("LoadShapeDaoImpl.saveShapeValues() starting");

		int i = 0;

		Session session = sessionFactory.getCurrentSession();

		Transaction tx = session.beginTransaction();

		for (Iterator iterator = shapeValues.iterator(); iterator.hasNext();) {
			LoadShapeDetail value = (LoadShapeDetail) iterator.next();
			session.saveOrUpdate(value);
			i++;
		}

		tx.commit();

		logger.debug("LoadShapeDaoImpl.saveShapeValues() done");

		return i;
	}
	
	/**
	 * Save supplied load shape summary
	 */
	public void saveShapeSummary(LoadShapeSummary summ) throws HibernateException  {
		logger.debug("LoadShapeDaoImpl.saveShapeSummary() starting");

		try {
			
			Session session = sessionFactory.getCurrentSession();
			
			Transaction tx = session.beginTransaction();
			
			session.saveOrUpdate(summ);
			
			tx.commit();
		
		} catch (Exception ex) {
			logger.error("Exception saving load shape summary: " + ex.getMessage());
			ex.printStackTrace();
			throw new HibernateException("Exception saving load shape summary: " + ex.getMessage());
		}
		
		logger.debug("LoadShapeDaoImpl.saveShapeSummary() done");
	}
	
	/**
	 * Get a shape
	 */
	public LoadShapeSummary getShape(Long mpOid) throws HibernateException  {
		logger.debug("LoadShapeDaoImpl.getShape() starting");

		LoadShapeSummary result = null;

		try {
			
			Session session = sessionFactory.getCurrentSession();
			
			Transaction tx = session.beginTransaction();
			
			Criteria lsusCrit = session.createCriteria(LoadShapeSummary.class);
			Criterion mpIdCrit = Restrictions.eq("OID", mpOid);
			lsusCrit.add(mpIdCrit);
			
			result = (LoadShapeSummary)lsusCrit.uniqueResult();
			
			tx.commit();
		
		} catch (Exception ex) {
			logger.error("Exception getting load shape : " + ex.getMessage());
			ex.printStackTrace();
			throw new HibernateException("Exception getting load shape: " + ex.getMessage());
		}
		
		logger.debug("LoadShapeDaoImpl.getShape() done");
		
		return result;
	}
	
	public Integer unitizeLoadShape(String mpName) throws HibernateException {
		logger.debug("LoadShapeDaoImpl.unitizeLoadShape() starting");

		Integer returnCode = null;
		HashMap results = null;
		
		CallLoadShapeUnitize proc = new CallLoadShapeUnitize(dataSource);
		results = proc.execute(mpName);
		
		returnCode = (Integer)results.get(CallLoadStaging.ReturnCodeKey);
		
		if (results.get(CallLoadShapeUnitize.ErrorKey) != null) {
			String errorMsg = (String)results.get(CallLoadShapeUnitize.ErrorKey);
			logger.debug("CallLoadShapeUnitize error " + errorMsg);
			throw new HibernateException(errorMsg);
		}
		
		logger.debug("LoadShapeDaoImpl.unitizeLoadShape() done");
		return returnCode;
	}
	
	public LoadShape fetchLoadShapeUnitize(String zoneCode)
			throws HibernateException {
		logger.debug("LoadShapeDaoImpl.fetchLoadShapeUnitize() starting");

		LoadShape resultValue = null;

		Session session = sessionFactory.getCurrentSession();
		
		Transaction tx = session.beginTransaction();
		
		Criteria queryCrit = session.createCriteria(LoadShape.class);
		
		List results = queryCrit.list();

		if (results != null && results.size() > 0) {
			resultValue = (LoadShape) results.get(0);

			if (zoneCode != null) {
				// filter records
				int size = resultValue.getSummaries().size();
				for (int i = size - 1; i >= 0; i--)
				{
					LoadShapeSummary summary = resultValue.getSummaries().get(i);
					if (!summary.getZoneCode().equals(zoneCode))
						resultValue.getSummaries().remove(i);
				}
			}

			// simulate eagerly fetch
			Iterator<LoadShapeSummary> itr = resultValue.getSummaries().iterator();
			while (itr.hasNext())
				itr.next().getMeasurementPoint().getCurrentDetail().getOid();
		}
		tx.commit();

		logger.debug("LoadShapeDaoImpl.fetchLoadShapeUnitize() done");

		return resultValue;
	}
	

}
