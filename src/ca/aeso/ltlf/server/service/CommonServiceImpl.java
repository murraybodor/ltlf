package ca.aeso.ltlf.server.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.*;
import java.util.Map;

import net.sf.hibernate4gwt.gwt.HibernateRemoteService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.aeso.ltlf.model.*;
import ca.aeso.ltlf.model.Calendar;
import ca.aeso.ltlf.model.MeasurementPoint;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.dao.CalendarDao;
import ca.aeso.ltlf.server.dao.CommonDao;

/**
 * Service class for getting commonly used values
 * 
 * @author mbodor
 */
public class CommonServiceImpl extends HibernateRemoteService implements CommonService {

	CommonDao _dao;
	CalendarDao _calDao;
	
	protected static Log logger = LogFactory.getLog(CommonServiceImpl.class);
	private static List<MeasurementPoint> mpList = null;
	private static Map<String, MeasurementPoint> mpMap = new HashMap<String, MeasurementPoint>();
	private static List<Area> areaList = null;
	private static Map<String, Area> areaMap = new HashMap<String, Area>();
	private static List<Sector> sectorList = null;
	private static Map<String, List<MeasurementPoint>> areaToMpMap = new HashMap<String, List<MeasurementPoint>>();
	
	public void setDao(CommonDao dao) {
		_dao = dao;
	}

	public void setCalendarDao(CalendarDao aValue) {
		_calDao = aValue;
	}
	
	public List<Calendar> fetchCalendars() throws LtlfServiceException {
		return _calDao.fetchCalendars();
	}
	
	public Date fetchCalendarLoadDate() throws LtlfServiceException {
		Date returnValue = null;
		
		List allCalendars = _calDao.fetchCalendars();
		returnValue = ((Calendar) allCalendars.get(0)).getAuditDateTime();
		return returnValue;
	}
	
	public List<MeasurementPoint> getMeasurementPoints() throws LtlfServiceException {
		if (mpList==null) {
			mpList = _dao.getMeasurementPoints();
			for (Iterator iterator = mpList.iterator(); iterator.hasNext();) {
				MeasurementPoint mp = (MeasurementPoint) iterator.next();
				mpMap.put(mp.getName(), mp);
				
				List<MeasurementPoint> mps = areaToMpMap.get(mp.getZoneCode());
				if (mps==null) {
					mps = new ArrayList<MeasurementPoint>();
					areaToMpMap.put(mp.getZoneCode(), mps);
				}
				mps.add(mp);
				
			}
		}
			
		return mpList;
	}
	
	public MeasurementPoint getMeasurementPoint(String name) throws LtlfServiceException {
		getMeasurementPoints();
		return (MeasurementPoint)mpMap.get(name);
	}

	public List<Area> getAreas() throws LtlfServiceException {
		if (areaList==null) {
			areaList = _dao.getAreas();
			for (Iterator iterator = areaList.iterator(); iterator.hasNext();) {
				Area anArea = (Area) iterator.next();
				areaMap.put(anArea.getCode(), anArea);
			}
		}
			
		return areaList;
	}
	
	public Area getArea(String areaCode) throws LtlfServiceException {
		getAreas();
		return (Area)areaMap.get(areaCode);
	}
	
	public List<Sector> getSectors() throws LtlfServiceException {
		if (sectorList==null) {
			sectorList = _dao.getSectors();
//			areaMap = new HashMap<String, Area>();
//			for (Iterator iterator = areaList.iterator(); iterator.hasNext();) {
//				Area anArea = (Area) iterator.next();
//				areaMap.put(anArea.getCode(), anArea);
//			}
		}
			
		return sectorList;
	}

	public Map<String, List<MeasurementPoint>> getAreaToMpMap() throws LtlfServiceException  {
		if (areaToMpMap.size()==0) {
			getMeasurementPoints();
		}
		return areaToMpMap;
	}
	
}
