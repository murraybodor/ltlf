package ca.aeso.ltlf.server.dao;

import java.util.*;
import java.util.Iterator;
import java.util.List;

import ca.aeso.ltlf.model.*;
import ca.aeso.ltlf.server.junit.AbstractDataAccessTest;

public class MpAnalysisDaoTest extends AbstractDataAccessTest {

	MpAnalysisDao dao;

	public void setDao(MpAnalysisDao lsDao) {
		dao = lsDao;
	}
	
	public void testGetLoadValues() {
		logger.debug("MpAnalysisDaoTest.testGetLoadValues starting");

//		Calendar start = Calendar.getInstance();
//		start.set(Calendar.YEAR, 2008);
//		start.set(Calendar.MONTH, 0);
//		start.set(Calendar.DATE, 12);
//		start.set(Calendar.HOUR, 0);
//		start.set(Calendar.MINUTE, 0);
//		start.set(Calendar.SECOND, 0);
//		start.set(Calendar.MILLISECOND, 0);
//
//		Calendar end = Calendar.getInstance();
//		end.set(Calendar.YEAR, 2008);
//		end.set(Calendar.MONTH, 0);
//		end.set(Calendar.DATE, 13);
//		end.set(Calendar.HOUR, 0);
//		end.set(Calendar.MINUTE, 0);
//		end.set(Calendar.SECOND, 0);
//		end.set(Calendar.MILLISECOND, 0);
		

//		logger.debug("MpAnalysisDaoTest.testGetLoadValues start=" + start.toString());
//		logger.debug("MpAnalysisDaoTest.testGetLoadValues end=" + end.toString());
//		
//		List<MpLoadValue> histList = dao.getLoadValues("RL1", start.getTime(), 1, end.getTime(), 24);
//
//		for (Iterator<MpLoadValue> iterator = histList.iterator(); iterator.hasNext();) {
//			MpLoadValue hist = (MpLoadValue) iterator.next();
//			logger.debug("load=" + hist.getLoadDate() + " / " + hist.getLoadHourEnd() + " = " + hist.getLoadMw());
//		}
		
//		assertTrue(histList.size()==48);
		
	}

	public void testGetMeasurementPoints() {

//		List<MeasurementPoint> mps = dao.getMeasurementPoints();
//
//		logger.debug("MpAnalysisDaoTest.testGetMeasurementPoints size=" + mps.size());
		
		
		
		
	}
	
}
