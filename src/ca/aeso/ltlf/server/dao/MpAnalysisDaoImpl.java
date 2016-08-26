package ca.aeso.ltlf.server.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ca.aeso.ltlf.model.GapSummaryValue;
import ca.aeso.ltlf.model.AnalysisDetail;

/**
 * DAO methods to support MP Analysis operations
 * 
 * @author mbodor
 */
public class MpAnalysisDaoImpl implements MpAnalysisDao {


    private SessionFactory sessionFactory;
	protected static Log logger = LogFactory.getLog(MpAnalysisDaoImpl.class);

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		logger.debug("MpAnalysisDaoImpl.setSessionFactory() starting");
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Retrieve load values for an MP the supplied period
	 */
	public List<AnalysisDetail> getLoadValues(Long mpOid, Date startDate, int startHe, Date endDate, int endHe) throws HibernateException  {
		logger.debug("MpAnalysisDaoImpl.getLoadValues() starting for " + startDate + " to " + endDate);

		Date t1 = new Date();
		
		Session session = sessionFactory.getCurrentSession();
		
		Transaction tx = session.beginTransaction();

		// select load values
		Criteria mpLoadValues = session.createCriteria(AnalysisDetail.class);
		
		if (startDate.compareTo(endDate)==0) {
			Criterion dateCriteria = Restrictions.eq("loadDate", startDate);
			Criterion heCrit1a = Restrictions.between("loadHourEnd", startHe, endHe);
			mpLoadValues.add(dateCriteria);
			mpLoadValues.add(heCrit1a);
		} else {
			LogicalExpression eqStartDate = Restrictions.and(Restrictions.eq("loadDate", startDate), Restrictions.ge("loadHourEnd", startHe));
			LogicalExpression btwnStartAndEndDate = Restrictions.and(Restrictions.gt("loadDate", startDate), Restrictions.lt("loadDate", endDate));
			LogicalExpression eqEndDate = Restrictions.and(Restrictions.eq("loadDate", endDate), Restrictions.le("loadHourEnd", endHe));
			LogicalExpression eqStartOrEndDate = Restrictions.or(eqStartDate, eqEndDate);
			LogicalExpression dateExpression = Restrictions.or(eqStartOrEndDate, btwnStartAndEndDate);

			mpLoadValues.add(dateExpression);
		}

		mpLoadValues.addOrder(Order.asc("loadDate"));
		mpLoadValues.addOrder(Order.asc("loadHourEnd"));

		// join to measurement point
		Criteria mp = mpLoadValues.createCriteria("measurementPoint");
		mp.add(Restrictions.eq("oid", mpOid));
		
		List<AnalysisDetail> resultSet = mp.list();
		
		tx.commit();
		
		logger.debug("MpAnalysisDaoImpl.getLoadValues() done for " + startDate + " to " + endDate + ", size=" + resultSet.size());
		Date t2 = new Date();
		
		long diff = t2.getTime() - t1.getTime();
		logger.debug("MpAnalysisDaoImpl.getLoadValues() took " + diff);

		return resultSet;
	}

	/**
	 * Save supplied MP load values
	 */
	public int saveLoadValues(List<AnalysisDetail> loadValues) throws HibernateException  {
		logger.debug("MpAnalysisDaoImpl.saveLoadValues() starting");

		Date t1 = new Date();

		Session session = sessionFactory.getCurrentSession();
		
		Transaction tx = session.beginTransaction();

		int i = 0;
		for (Iterator iterator = loadValues.iterator(); iterator.hasNext();) {
			AnalysisDetail value = (AnalysisDetail) iterator.next();
			session.saveOrUpdate(value);
			i++;
		}

		tx.commit();
		
		Date t2 = new Date();
		long diff = t2.getTime() - t1.getTime();
		
		logger.debug("MpAnalysisDaoImpl.saveLoadValues() done, took " + diff);
		
		return i;
	}
	
	public void saveAnalysisSummary(GapSummaryValue summary) throws HibernateException  {
		logger.debug("MpAnalysisDaoImpl.saveAnalysisSummary() starting");

		Date t1 = new Date();

		Session session = sessionFactory.getCurrentSession();
		
		Transaction tx = session.beginTransaction();

		int i = 0;
		session.saveOrUpdate(summary);

		tx.commit();
		
		Date t2 = new Date();
		long diff = t2.getTime() - t1.getTime();
		
		logger.debug("MpAnalysisDaoImpl.saveAnalysisSummary() done, took " + diff);
		
	}
}
