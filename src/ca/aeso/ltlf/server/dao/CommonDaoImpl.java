package ca.aeso.ltlf.server.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ca.aeso.ltlf.model.Area;
import ca.aeso.ltlf.model.MeasurementPoint;
import ca.aeso.ltlf.model.Sector;

/**
 * DAO methods to support commonly used objects
 * @author mbodor
 *
 */
public class CommonDaoImpl implements CommonDao {
	
	protected static Log logger = LogFactory.getLog(CommonDaoImpl.class);

	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		logger.debug("CommonDaoImpl.setSessionFactory() starting");
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Get a list of all measurement points, ordered by MP ID
	 */
	public List<MeasurementPoint> getMeasurementPoints() throws HibernateException {
		logger.debug("CommonDaoImpl.getMeasurementPoints() starting");

		Session session = sessionFactory.getCurrentSession();
		
		session.beginTransaction();

		// select measurement_point
		Criteria mpCrit = session.createCriteria(MeasurementPoint.class);
		
		// join to measurement_point_details
		Criteria detailCrit = mpCrit.createCriteria("details");

		// WHERE MEASUREMENT_POINT_TYPE_CODE=DEM AND INCL_IN_POD_LSB
		Criterion mptc = Restrictions.eq("measurementPointTypeCode", "DEM");
		Criterion inclInPod = Restrictions.eq("inclInPodLsb", "Y");
		LogicalExpression mpExpression = Restrictions.and(mptc, inclInPod);
		detailCrit.add(mpExpression);

		
		// AND CREATE_DATE <= today
		Date today = new Date();
		Criterion effCrit = Restrictions.le("creationDate", today);
		detailCrit.add(effCrit);
		
		// AND EXPIRY_DATE is null or >= today
		Criterion expCrit = Restrictions.isNull("expiryDate");
		Criterion exp2Crit = Restrictions.ge("expiryDate", today);
		LogicalExpression expiryExpression = Restrictions.or(expCrit, exp2Crit);
		detailCrit.add(expiryExpression);

		// sort results
		detailCrit.addOrder(Order.asc("name"));
		
		
//		mpCrit.createCriteria("currentDetail").addOrder(Order.asc("name"));
		
		List<MeasurementPoint> resultSet = detailCrit.list(); 

//		List<MeasurementPoint> mpList = new ArrayList();
		
//		for (Iterator iterator = resultSet.iterator(); iterator.hasNext();) {
//			MeasurementPoint mp = (MeasurementPoint) iterator.next();
//			mp.getCurrentDetail();
//		}
		
		logger.debug("CommonDaoImpl.getMeasurementPoints() done, size=" + resultSet.size());
		
		return resultSet;
		
	}
	
	/**
	 * Get a list of areas
	 */
	public List<Area> getAreas() {
		logger.debug("CommonDaoImpl.getAreas() starting");
		
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();

		Query query = (Query) sessionFactory.getCurrentSession().createQuery("from Area");

		List<Area> resultValue = query.list();
		
		tx.commit();
		
		logger.debug("CommonDaoImpl.getAreas() done, size=" + resultValue.size());
		
		return resultValue;
		
	}
	
	public List<Sector> getSectors() {
		List fetchedValues = null;
		
		Transaction tx = null;
		
		try {
			tx = sessionFactory.getCurrentSession().beginTransaction();
			Query query = (Query) sessionFactory.getCurrentSession().createQuery("from Sector");
			fetchedValues = query.list();
			tx.commit();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			tx.rollback();
		} 
		
		return fetchedValues;
	}
	
}
