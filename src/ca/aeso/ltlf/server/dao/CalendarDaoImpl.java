package ca.aeso.ltlf.server.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import ca.aeso.ltlf.model.Calendar;

public class CalendarDaoImpl implements CalendarDao {

	private SessionFactory _sessionFactory;

	public SessionFactory getSessionFactory()
	{
		return _sessionFactory;
	}

	public void setSessionFactory(SessionFactory factory)
	{
		_sessionFactory = factory;
	}

	public void deleteAllCalendars() {
		Session session = _sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		session.createQuery("delete Calendar").executeUpdate();
		tx.commit();
	}
	public void saveCalendars(List<Calendar> entries) {

		Session session = _sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		Iterator calIterator = entries.iterator();
		while (calIterator.hasNext()){
			Calendar aCal = (Calendar) calIterator.next();
			session.saveOrUpdate(aCal);
		}

		tx.commit();
	}
	
	public List<Calendar> fetchCalendars() {
		List fetchedValues = null;
		
		try {
			Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();
			Query query = (Query) _sessionFactory.getCurrentSession().createQuery("from Calendar");
			fetchedValues = query.list();
			if (fetchedValues == null || fetchedValues.size() == 0)
				return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return fetchedValues;
	}
}
