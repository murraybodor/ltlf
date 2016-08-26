package ca.aeso.ltlf.server.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import ca.aeso.ltlf.model.CodesTable;

/**
 * DAO methods to support Codes table operations
 * 
 * @author schen
 */
public class CodesTableDaoImpl implements CodesTableDao {


    private SessionFactory sessionFactory;
	protected static Log logger = LogFactory.getLog(CodesTableDaoImpl.class);

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		logger.debug("CodesTableDaoImpl.setSessionFactory() starting");
		this.sessionFactory = sessionFactory;
	}

	public CodesTable getCodeValue(String key) throws HibernateException {
		logger.debug("CodesTableDaoImpl.getCodeValue() starting");
		
		sessionFactory.getCurrentSession().beginTransaction();
		
		Query query = sessionFactory.getCurrentSession().createQuery("from CodesTable where code = '" + key + "'");
		
		CodesTable code = (CodesTable)query.uniqueResult();
		
		logger.debug("CodesTableDaoImpl.getCodeValue() got result");
		
		return code;
	}

	public List<CodesTable> getCodeValues(String key) throws HibernateException {
		logger.debug("CodesTableDaoImpl.getCodeValues() starting");
		
		sessionFactory.getCurrentSession().beginTransaction();
		
		Query query = sessionFactory.getCurrentSession().createQuery("from CodesTable where code = '" + key + "' order by orderInfo");
		
		List<CodesTable> codes = query.list();
		
		logger.debug("CodesTableDaoImpl.getCodeValues() got result");
		
		return codes;
	}
	
}
