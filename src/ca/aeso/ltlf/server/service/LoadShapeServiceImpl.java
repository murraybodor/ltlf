package ca.aeso.ltlf.server.service;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.hibernate4gwt.gwt.HibernateRemoteService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ca.aeso.ltlf.model.ShapePointWrapper;
import ca.aeso.ltlf.model.LoadShapeDetail;
import ca.aeso.ltlf.model.*;
import ca.aeso.ltlf.model.ShapeComparer;
import ca.aeso.ltlf.model.util.Constants;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.dao.LoadShapeDao;
import ca.aeso.ltlf.server.dao.StagingDao;
import ca.aeso.ltlf.server.util.GraphUtil;

/**
 * Service class to support load shape operations
 *
 * @author mbodor
 */
public class LoadShapeServiceImpl extends HibernateRemoteService implements LoadShapeService {

	LoadShapeDao loadShapeDao;
	StagingDao stagingDao;

	protected static Log logger = LogFactory.getLog(LoadShapeServiceImpl.class);
	private static String SESSION_KEY = "SHAPE";

	public void setLoadShapeDao(LoadShapeDao lsDao) {
		loadShapeDao = lsDao;
	}

	public void setStagingDao(StagingDao aValue) {
		stagingDao = aValue;
	}

	/**
	 * Unitize a load shape
	 */
	public Integer unitizeLoadShape(String mpOid, HttpSession sess) throws LtlfServiceException {
		logger.debug("LoadShapeServiceImpl.unitizeLoadShape() starting");

		clearCachedShape(sess, mpOid);

		Integer result;

		try {
			result = loadShapeDao.unitizeLoadShape(mpOid);
		} catch (Exception e) {
			logger.error("Exception unitizing load shape: " + e.getMessage());
			e.printStackTrace();
			throw new LtlfServiceException("Exception unitizing load shape: " + e.getMessage());
		}

		logger.debug("LoadShapeServiceImpl.unitizeLoadShape() starting");
		return result;
	}

	private void clearCachedShape(HttpSession session, String mpOid) {

		Enumeration sessEnum = session.getAttributeNames();

		while (sessEnum.hasMoreElements()) {
			String elem = (String) sessEnum.nextElement();
			if (elem.equals(SESSION_KEY + mpOid)) {
				session.removeAttribute(elem);
				break;
			}
		}

	}


	public LoadShape fetchLoadShapeUnitize(String areaCode) throws LtlfServiceException{
		logger.debug("LoadShapeServiceImpl.fetchLoadShapeUnitize() starting");

		LoadShape returnValue = null;

		try {
			returnValue = loadShapeDao.fetchLoadShapeUnitize(areaCode);
		} catch (Exception e) {
			logger.error("Exception fetching load shape unitize: " + e.getMessage());
			e.printStackTrace();
			throw new LtlfServiceException("Exception fetching load shape unitize: " + e.getMessage());
		}

		logger.debug("LoadShapeServiceImpl.fetchLoadShapeUnitize() starting");
		return returnValue;
	}

	private ShapePointWrapper getShape(int baseYear, Long mpOid, Integer versionNumber, HttpSession session) throws LtlfServiceException {
		logger.debug("LoadShapeServiceImpl.getShape() starting");

		ShapePointWrapper shape = getCachedShape(mpOid, session);

		if (shape==null) {

			logger.debug("LoadShapeServiceImpl.getShape() no shape data in session with key=" + SESSION_KEY + mpOid + ", loading");

			List<LoadShapeDetail> shapePoints = getShapeValues(baseYear, mpOid, versionNumber);

			if (shapePoints!=null && shapePoints.size()>0) {
				shape = new ShapePointWrapper(mpOid, shapePoints);
				setCachedShape(shape, session);
			}
		}

		logger.debug("LoadShapeServiceImpl.getShape() done");

		return shape;
	}

	private ShapePointWrapper getCachedShape(Long mpOid, HttpSession session) {

		logger.debug("LoadShapeServiceImpl.getCachedShape() checking for cache with key=" + SESSION_KEY + mpOid);

		Object shapeObj = session.getAttribute(SESSION_KEY + mpOid);

		if (shapeObj!=null) {
			return (ShapePointWrapper)shapeObj;
		}
		else
			return null;
	}

	private void setCachedShape(ShapePointWrapper shape, HttpSession session) {
		logger.debug("LoadShapeServiceImpl.setCachedShape() caching with key=" + SESSION_KEY + shape.getMpOid());

		session.setAttribute(SESSION_KEY + shape.getMpOid(), shape);

		Enumeration enumS = session.getAttributeNames();
		while (enumS.hasMoreElements()) {
			String elem = (String) enumS.nextElement();
			logger.debug("LoadShapeServiceImpl.setCachedShape existing session elements: " + elem);
		}
	}

	public void clearCachedShapes(HttpSession session) {

		Enumeration sessEnum = session.getAttributeNames();

		while (sessEnum.hasMoreElements()) {
			String elem = (String) sessEnum.nextElement();
			if (elem.startsWith(SESSION_KEY)) {
				session.removeAttribute(elem);
			}
		}

	}

	private List<LoadShapeDetail> getShapeValues(int baseYear, Long mpOid, Integer versionNumber) throws LtlfServiceException {
		logger.debug("LoadShapeServiceImpl.getShapeValues starting");
		List<LoadShapeDetail> values = null;

		try {
			values = loadShapeDao.getShapeValues(baseYear, mpOid, versionNumber);
		} catch (Exception e) {
			logger.error("Exception getting load shape values: " + e.getMessage());
			e.printStackTrace();
			throw new LtlfServiceException("Exception getting load shape values: " + e.getMessage());
		}

		logger.debug("LoadShapeServiceImpl.getShapeValues done");
		return values;
	}

	public LoadShapeSummary getShapeSummary(Long mpOid) throws LtlfServiceException {
		
		try {
			return loadShapeDao.getShape(mpOid);	
		} catch (Exception e) {
			logger.error("Exception getting load shape summary: " + e.getMessage());
			e.printStackTrace();
			throw new LtlfServiceException("Exception getting load shape summary: " + e.getMessage());
		}
	}
	
	/**
	 * Generate shape graphs for the supplied MP
	 */
	public String[] generateShapeGraphs(int baseYear, Long mpOid, Integer versionNumber, Long comparisonMpOid, boolean graphOriginal, double lowerUnitRange, double upperUnitRange, HttpSession session) throws LtlfServiceException {

		logger.debug("LoadShapeServiceImpl.generateShapeGraphs() starting mp=" + mpOid + " version=" + versionNumber);
		String hourlyChartName = null;

		try {

			XYSeriesCollection collection = new XYSeriesCollection();

			// process shape
			ShapePointWrapper shape = getShape(baseYear, mpOid, versionNumber, session);

			if (shape!=null) {
				List<LoadShapeDetail> shapeVals = shape.getAllLoadValues();
				logger.debug("LoadShapeServiceImpl.generateShapeGraphs() graphing original, size=" + shapeVals.size());
				collection.addSeries(createTimeShapeSeries(shapeVals, LoadShapeDetail.CORRECTED_TYPE));
				if (graphOriginal)
					collection.addSeries(createTimeShapeSeries(shapeVals, LoadShapeDetail.ORIGINAL_TYPE));

				for (int i = 0; i < shape.getNumFixes(); i++) {
					String fixNumber = "Fix " + (i+1);
					logger.debug("LoadShapeServiceImpl.generateShapeGraphs() graphing " + fixNumber);
					collection.addSeries(createTimeShapeSeries(shapeVals, fixNumber));
				}
			}

			// process comparison
			if (comparisonMpOid!=null) {
				ShapePointWrapper comparison = getShape(baseYear, comparisonMpOid, new Integer(1), session);

				if (comparison!=null) {
					collection.addSeries(createTimeShapeSeries(comparison.getAllLoadValues(), LoadShapeDetail.COMPARISON_TYPE));
				}
			}

			hourlyChartName = GraphUtil.getInstance().generateShapeChart(collection, Constants.LOAD_SHAPE_HOURLY_CHART_WIDTH, Constants.LOAD_SHAPE_HOURLY_CHART_HEIGHT, 0, 1);

			logger.debug("LoadShapeServiceImpl.generateShapeGraphs() done");

		} catch (Exception e) {
			e.printStackTrace();
			throw new LtlfServiceException("Error graphing hourly shape: " + e.getMessage());
		}

		logger.debug("LoadShapeServiceImpl.generateShapeGraphs() starting mp=" + mpOid + " version=" + versionNumber + " lower= " + lowerUnitRange + " upper= " + upperUnitRange);

		String loadChartName = null;

		try {

			XYSeriesCollection collection = new XYSeriesCollection();
			ShapePointWrapper shape = getShape(baseYear, mpOid, versionNumber, session);

			// process shape
			if (shape!=null) {
				List<LoadShapeDetail> shapeVals = shape.getAllLoadValues();
				logger.debug("LoadShapeServiceImpl.generateShapeGraphs() shape size=" + shapeVals.size());
				collection.addSeries(createLoadShapeSeries(shapeVals, LoadShapeDetail.CORRECTED_TYPE, lowerUnitRange, upperUnitRange));
				if (graphOriginal)
					collection.addSeries(createLoadShapeSeries(shapeVals, LoadShapeDetail.ORIGINAL_TYPE, lowerUnitRange, upperUnitRange));

				for (int i = 0; i < shape.getNumFixes(); i++) {
					String fixNumber = "Fix " + (i+1);
					collection.addSeries(createLoadShapeSeries(shapeVals, fixNumber, lowerUnitRange, upperUnitRange));
				}
			}

			// process comparison
			if (comparisonMpOid!=null) {
				ShapePointWrapper comparison = getShape(baseYear, comparisonMpOid, new Integer(1), session);

				if (comparison!=null) {
					collection.addSeries(createLoadShapeSeries(comparison.getAllLoadValues(), LoadShapeDetail.COMPARISON_TYPE, lowerUnitRange, upperUnitRange));
				}
			}

			loadChartName = GraphUtil.getInstance().generateShapeChart(collection, Constants.LOAD_SHAPE_CHART_WIDTH, Constants.LOAD_SHAPE_CHART_HEIGHT, lowerUnitRange, upperUnitRange);

			logger.debug("LoadShapeServiceImpl.generateShapeGraphs() done");
		} catch (Exception e) {
			e.printStackTrace();
			throw new LtlfServiceException("Error graphing load shape: " + e.getMessage());
		}

		return new String[]{hourlyChartName, loadChartName};
	}
	
//	/**
//	 * Generate an hourly shape chart for the supplied MP
//	 */
//	public String graphHourlyShape(int baseYear, Long mpOid, Integer versionNumber, Long comparisonMpOid, boolean graphOriginal, HttpSession session) throws LtlfServiceException {
//
//		logger.debug("LoadShapeServiceImpl.graphHourlyShape() starting mp=" + mpOid + " version=" + versionNumber);
//		String chartName = null;
//
//		try {
//
//			XYSeriesCollection collection = new XYSeriesCollection();
//
//			// process shape
//			ShapePointWrapper shape = getShape(baseYear, mpOid, versionNumber, session);
//
//			if (shape!=null) {
//				List<LoadShapeDetail> shapeVals = shape.getAllLoadValues();
//				logger.debug("LoadShapeServiceImpl.graphHourlyShape() graphing original, size=" + shapeVals.size());
//				collection.addSeries(createTimeShapeSeries(shapeVals, LoadShapeDetail.CORRECTED_TYPE));
//				if (graphOriginal)
//					collection.addSeries(createTimeShapeSeries(shapeVals, LoadShapeDetail.ORIGINAL_TYPE));
//
//				for (int i = 0; i < shape.getNumFixes(); i++) {
//					String fixNumber = "Fix " + (i+1);
//					logger.debug("LoadShapeServiceImpl.graphHourlyShape() graphing " + fixNumber);
//					collection.addSeries(createTimeShapeSeries(shapeVals, fixNumber));
//				}
//			}
//
//			// process comparison
//			if (comparisonMpOid!=null) {
//				ShapePointWrapper comparison = getShape(baseYear, comparisonMpOid, new Integer(1), session);
//
//				if (comparison!=null) {
//					collection.addSeries(createTimeShapeSeries(comparison.getAllLoadValues(), LoadShapeDetail.COMPARISON_TYPE));
//				}
//			}
//
//			chartName = GraphUtil.getInstance().generateShapeChart(collection, Constants.LOAD_SHAPE_HOURLY_CHART_WIDTH, Constants.LOAD_SHAPE_HOURLY_CHART_HEIGHT, 0, 1);
//
//			logger.debug("LoadShapeServiceImpl.graphHourlyShape() done");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new LtlfServiceException("Error graphing hourly shape: " + e.getMessage());
//		}
//
//		return chartName;
//
//	}
//
//	/**
//	 * Generate a Load Shape chart for the supplied MP
//	 */
//	public String graphLoadShape(int baseYear, Long mpOid, Integer versionNumber, Long comparisonMpOid, boolean graphOriginal, double lowerUnitRange, double upperUnitRange, HttpSession session) throws LtlfServiceException {
//
//		logger.debug("LoadShapeServiceImpl.graphLoadShape() starting mp=" + mpOid + " version=" + versionNumber + " lower= " + lowerUnitRange + " upper= " + upperUnitRange);
//
//		String chartName = null;
//
//		try {
//
//			XYSeriesCollection collection = new XYSeriesCollection();
//			ShapePointWrapper shape = getShape(baseYear, mpOid, versionNumber, session);
//
//			// process shape
//			if (shape!=null) {
//				List<LoadShapeDetail> shapeVals = shape.getAllLoadValues();
//				logger.debug("LoadShapeServiceImpl.graphLoadShape() shape size=" + shapeVals.size());
//				collection.addSeries(createLoadShapeSeries(shapeVals, LoadShapeDetail.CORRECTED_TYPE, lowerUnitRange, upperUnitRange));
//				if (graphOriginal)
//					collection.addSeries(createLoadShapeSeries(shapeVals, LoadShapeDetail.ORIGINAL_TYPE, lowerUnitRange, upperUnitRange));
//
//				for (int i = 0; i < shape.getNumFixes(); i++) {
//					String fixNumber = "Fix " + (i+1);
//					collection.addSeries(createLoadShapeSeries(shapeVals, fixNumber, lowerUnitRange, upperUnitRange));
//				}
//			}
//
//			// process comparison
//			if (comparisonMpOid!=null) {
//				ShapePointWrapper comparison = getShape(baseYear, comparisonMpOid, new Integer(1), session);
//
//				if (comparison!=null) {
//					collection.addSeries(createLoadShapeSeries(comparison.getAllLoadValues(), LoadShapeDetail.COMPARISON_TYPE, lowerUnitRange, upperUnitRange));
//				}
//			}
//
//			chartName = GraphUtil.getInstance().generateShapeChart(collection, Constants.LOAD_SHAPE_CHART_WIDTH, Constants.LOAD_SHAPE_CHART_HEIGHT, lowerUnitRange, upperUnitRange);
//
//			logger.debug("LoadShapeServiceImpl.graphLoadShape() done");
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new LtlfServiceException("Error graphing load shape: " + e.getMessage());
//		}
//
//		return chartName;
//	}

	/**
	 * Apply a global override to a load shape
	 */
	public void applyBatchOverride(int baseYear, Long mpOid, Integer versionNumber, boolean overrideTop, double overrideValue, HttpSession session) throws LtlfServiceException {
		logger.debug("LoadShapeServiceImpl.applyBatchOverride() starting, overriding " + overrideValue);

		try {

			double maxVal = 1;
			ShapePointWrapper shape = getShape(baseYear, mpOid, versionNumber, session);

			if (shape!=null) {
				List<LoadShapeDetail> shapeValues = shape.getAllLoadValues();

				int fixNum = shape.getNumFixes() + 1;
				String fixNumStr = "Fix " + fixNum;

				// iterate through shape values
				logger.debug("LoadShapeServiceImpl.applyBatchOverride() applying override");
				for (Iterator iterator = shapeValues.iterator(); iterator.hasNext();) {
					LoadShapeDetail shapeDetail = (LoadShapeDetail) iterator.next();
					Double value = shapeDetail.getValue(LoadShapeDetail.CORRECTED_TYPE);

					if (overrideTop) {
						if (value >= overrideValue) {
							shapeDetail.addFix(fixNumStr, maxVal);
						} else {
							double newVal = value/overrideValue;
							shapeDetail.addFix(fixNumStr, newVal);
						}
					} else {
						if (value <= overrideValue) {
							shapeDetail.addFix(fixNumStr, overrideValue);
						}
					}
				}

				logger.debug("LoadShapeServiceImpl.applyBatchOverride() incrementing fix #");
				shape.setNumFixes(fixNum);
				setCachedShape(shape, session);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LtlfServiceException("Error applying batch override: " + e.getMessage());
		}

		logger.debug("LoadShapeServiceImpl.applyBatchOverride() done");
	}

    /**
     * Apply a global offset to a load shape
     */
	public void applyOffset(int baseYear, Long mpOid, Integer versionNumber, String offsetUnit, int offsetValue, HttpSession session) throws LtlfServiceException {
		logger.debug("LoadShapeServiceImpl.applyOffset() starting, offsetting " + offsetValue + " " + offsetUnit);

		try {

			ShapePointWrapper shape = getShape(baseYear, mpOid, versionNumber, session);

			if (shape!=null) {
				List<LoadShapeDetail> shapeValues = shape.getAllLoadValues();
				java.util.Collections.<LoadShapeDetail>sort(shapeValues, new ShapeComparer("time"));

				int fixNum = shape.getNumFixes() + 1;
				String fixNumStr = "Fix " + fixNum;

				int offsetHours = offsetValue;

				if (offsetUnit.equals("DAYS")) {
					offsetHours = (offsetValue * 24);
				}

				logger.debug("LoadShapeServiceImpl.applyOffset() offset hours= " + offsetHours);

				int size = shapeValues.size();
				logger.debug("LoadShapeServiceImpl.applyOffset() size= " + size);

				for (int i = 0; i < size; i++) {
					LoadShapeDetail entry = shapeValues.get(i);

					int offsetIndex = i - offsetHours; // offset

					if (offsetIndex > (size-1)) {
						offsetIndex = offsetIndex - size;
					} else if (offsetIndex < 0) {
						offsetIndex = size + offsetIndex;
					}


					LoadShapeDetail offsetEntry = shapeValues.get(offsetIndex);

					Double offsetEntryValue = offsetEntry.getValue(LoadShapeDetail.CORRECTED_TYPE);
					if (offsetEntryValue==1) {
						logger.debug("LoadShapeServiceImpl.applyOffset() found 1 at offset index= " + offsetIndex);
					}

					entry.addFix(fixNumStr, offsetEntryValue);

				}

				logger.debug("LoadShapeServiceImpl.applyOffset() incrementing fix #");
				shape.setNumFixes(fixNum);
				setCachedShape(shape, session);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LtlfServiceException("Error applying load shape offset: " + e.getMessage());
		}

		logger.debug("LoadShapeServiceImpl.applyOffset() done");
	}

	/**
	 * Copy in unit values from another load shape
	 */
	public void copyLoadShape(int baseYear, Long mpOid, Integer versionNumber, Long sourceMpOid, int baseDayStart, int baseDayEnd, HttpSession session) throws LtlfServiceException {
		logger.debug("LoadShapeServiceImpl.copyLoadShape starting");

		try {
			ShapePointWrapper destShape = getShape(baseYear, mpOid, versionNumber, session);
			List<LoadShapeDetail> destShapeVals = destShape.getAllLoadValues();
			int numDestShapeHours = destShapeVals.size();

			int fixNum = destShape.getNumFixes() + 1;
			String fixNumStr = "Fix " + fixNum;

			ShapePointWrapper sourceShape = getShape(baseYear, sourceMpOid, new Integer(1), session);

			if (sourceShape==null) {
				// no source shape
				throw new LtlfServiceException("No load shape found for source MP");
			}

			List<LoadShapeDetail> sourceShapeVals = sourceShape.getAllLoadValues();

			int baseDayStartHour = ((baseDayStart-1) * 24) + 1;
			int baseDayEndHour = ((baseDayEnd) * 24);

			if (sourceShapeVals.size() < baseDayEndHour) {
				// not enough values to copy
				throw new LtlfServiceException("Source MP does not have values for base days specified");
			}


			// loop through all source shape hours and copy to dest shape
			int copyIndex = 0;
			for (int i = baseDayStartHour; i <= baseDayEndHour; i++) {
				copyIndex = i-1;
				LoadShapeDetail sourceShapeDetail = sourceShapeVals.get(copyIndex);
				LoadShapeDetail shapeDetail = null;

//				logger.debug("LoadShapeServiceImpl.copyLoadShape copyIndex=" + copyIndex + ", numdestshapehours=" + numDestShapeHours);

				// check if we have anything to overwrite
				if (copyIndex < numDestShapeHours) {
					shapeDetail = destShapeVals.get(copyIndex);
				} else {
					shapeDetail = sourceShapeDetail.clone();
					shapeDetail.setMpOid(mpOid);
					destShapeVals.add(shapeDetail);
				}

				if (sourceShapeDetail!=null && shapeDetail!=null) {
					Double sourceVal = sourceShapeDetail.getValue(LoadShapeDetail.CORRECTED_TYPE);
					shapeDetail.addFix(fixNumStr, sourceVal);
				}
			}

			destShape.setNumFixes(fixNum);
			setCachedShape(destShape, session);

		} catch (Exception e) {
			e.printStackTrace();
			throw new LtlfServiceException("Error copying load shape: " + e.getMessage());
		}

		logger.debug("LoadShapeServiceImpl.copyLoadShape done");
	}

	/**
	 * Take the load shape back to it's original state
	 */
	public void removeLoadShapeOverrides(int baseYear, Long mpOid, Integer versionNumber, HttpSession session) throws LtlfServiceException {
		logger.debug("LoadShapeServiceImpl.removeLoadShapeOverrides starting");

		try {
			ShapePointWrapper shape = getShape(baseYear, mpOid, versionNumber, session);
			shape.removeOverrides();
			setCachedShape(shape, session);
		} catch (Exception e) {
			throw new LtlfServiceException("Error removing load shape overrides: " + e.getMessage());
		}

		logger.debug("LoadShapeServiceImpl.removeLoadShapeOverrides done");
	}

	/**
	 * Undo any uncommitted load shape changes
	 */
	public void undoLoadShapeFixes(int baseYear, Long mpOid, Integer versionNumber, HttpSession session) throws LtlfServiceException {
		logger.debug("LoadShapeServiceImpl.undoLoadShapeFixes starting");

		try {
			ShapePointWrapper shape = getShape(baseYear, mpOid, versionNumber, session);
			shape.undoFixes();
			setCachedShape(shape, session);
		} catch (Exception e) {
			throw new LtlfServiceException("Error undoing load shape fixes: " + e.getMessage());
		}

		logger.debug("LoadShapeServiceImpl.undoLoadShapeFixes done");
	}

	/**
	 * Commit all load shape changes
	 */
	public void commitLoadShapeFixes(int baseYear, Long mpOid, Integer versionNumber, LoadShapeSummary summ, HttpSession session) throws LtlfServiceException {
		logger.debug("LoadShapeServiceImpl.commitLoadShapeFixes starting");

		try {
			String userId = (String)session.getAttribute("USERID");
			logger.debug("LoadShapeServiceImpl.commitLoadShapeFixes user=" + userId);

			ShapePointWrapper shape = getShape(baseYear, mpOid, versionNumber, session);

			shape.applyFixes(userId);

			loadShapeDao.saveShapeValues(shape.getChangedLoadValues());

			loadShapeDao.saveShapeSummary(summ);
			
		} catch (Exception e) {
			throw new LtlfServiceException("Error saving load shape fixes: " + e.getMessage());
		}

		logger.debug("LoadShapeServiceImpl.commitLoadShapeFixes done");
	}

    private XYSeries createTimeShapeSeries(List<LoadShapeDetail> shapeVals, String seriesName) {

		logger.debug("GraphUtil.createTimeShapeSeries() starting, series name= " + seriesName);

		java.util.Collections.<LoadShapeDetail>sort(shapeVals, new ShapeComparer("time"));

        XYSeries series = new XYSeries(seriesName);

		int x = 0;

		for (Iterator<LoadShapeDetail> iterator = shapeVals.iterator(); iterator.hasNext();) {
			LoadShapeDetail point = (LoadShapeDetail) iterator.next();

    		if (point.hasValue(seriesName)) {
        		int day = point.getBaseDay();
        		int hour = point.getBaseHourEnding();
        		x =  ((day-1) * 24) + hour;
    			double y = point.getValue(seriesName).doubleValue();
    			series.addOrUpdate(x, y);
    		}
		}

		logger.debug("GraphUtil.createTimeShapeSeries() done");

        return series;
    }


    private XYSeries createLoadShapeSeries(List<LoadShapeDetail> shapeVals, String seriesName, double lowerUnitRange, double upperUnitRange) {

		logger.debug("GraphUtil.createLoadShapeSeries() starting, series name= " + seriesName + ", lower=" + lowerUnitRange + ", upper=" + upperUnitRange);

		java.util.Collections.<LoadShapeDetail>sort(shapeVals, new ShapeComparer(seriesName));

        XYSeries series = new XYSeries(seriesName);
		int x = 0;

		for (Iterator<LoadShapeDetail> iterator = shapeVals.iterator(); iterator.hasNext();) {
			LoadShapeDetail point = (LoadShapeDetail) iterator.next();
			x++;

			if (point.hasValue(seriesName)) {
				double y = point.getValue(seriesName).doubleValue();

				if (y > upperUnitRange) {
					// skip
					continue;
				}

				if (y < lowerUnitRange) {
					// stop
					break;
				}

				series.addOrUpdate(x, y);
			}
		}

		logger.debug("GraphUtil.createLoadShapeSeries() done");
        return series;
    }

}
