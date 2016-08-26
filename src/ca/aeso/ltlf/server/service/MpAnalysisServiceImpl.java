package ca.aeso.ltlf.server.service;

import java.util.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.hibernate4gwt.gwt.HibernateRemoteService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import ca.aeso.ltlf.model.GapSummaryValue;
import ca.aeso.ltlf.model.MpAnalysis;
import ca.aeso.ltlf.model.AnalysisDetail;
import ca.aeso.ltlf.model.util.Constants;
import ca.aeso.ltlf.model.util.DateUtil;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.dao.MpAnalysisDao;
import ca.aeso.ltlf.server.util.GraphUtil;

/**
 * Service class to support MP Analysis operations
 *
 * @author mbodor
 */
public class MpAnalysisServiceImpl extends HibernateRemoteService implements MpAnalysisService {

	MpAnalysisDao dao;
	protected static Log logger = LogFactory.getLog(MpAnalysisServiceImpl.class);
	private static String SESSION_KEY = "ANALYSIS";

	public void setDao(MpAnalysisDao lsDao) {
		dao = lsDao;
	}

	private MpAnalysis getAnalysis(Long mpOid, Date startDate, int startHe, Date endDate, int endHe, HttpSession session) {
		logger.debug("MpAnalysisServiceImpl.getAnalysis() starting");

		Date t1 = new Date();

		MpAnalysis analysis = getCachedAnalysis(mpOid, session);

		if (analysis==null) {

			logger.debug("MpAnalysisServiceImpl.getAnalysis() no data in session with key=" + SESSION_KEY + mpOid + ", loading");

			List<AnalysisDetail> mpData = getLoadHistory(mpOid, startDate, startHe, endDate, endHe);

			if (mpData!=null && mpData.size()>0) {

				Double maxLoad = 0.0;
				for (Iterator iterator = mpData.iterator(); iterator.hasNext();) {
					AnalysisDetail entry = (AnalysisDetail) iterator.next();
					if (entry.getValue(AnalysisDetail.CORRECTED_TYPE) > maxLoad) {
						maxLoad = entry.getValue(AnalysisDetail.CORRECTED_TYPE);
					}
				}

				analysis = new MpAnalysis(mpOid, maxLoad, mpData);
				setCachedAnalysis(analysis, session);
			}
		}

		Date t2 = new Date();
		long diff = t2.getTime() - t1.getTime();

		logger.debug("MpAnalysisServiceImpl.getAnalysis() done, took " + diff);

		return analysis;
	}

	private MpAnalysis getCachedAnalysis(Long mpOid, HttpSession session) {

		logger.debug("MpAnalysisServiceImpl.getCachedAnalysis() checking for cache with key=" + SESSION_KEY + mpOid);
		Object analysisObj = session.getAttribute(SESSION_KEY + mpOid);

		if (analysisObj!=null) {
			return (MpAnalysis)analysisObj;
		}
		else
			return null;
	}

	private void setCachedAnalysis(MpAnalysis analysis, HttpSession session) {
		logger.debug("MpAnalysisServiceImpl.setCachedAnalysis() caching with key=" + SESSION_KEY + analysis.getMpOid());
		session.setAttribute(SESSION_KEY + analysis.getMpOid(), analysis);

		Enumeration enumS = session.getAttributeNames();
		while (enumS.hasMoreElements()) {
			String elem = (String) enumS.nextElement();
			logger.debug("MpAnalysisServiceImpl.setCachedAnalysis existing session elements: " + elem);
		}

	}

	public void clearCachedAnalyses(HttpSession session) {

		Enumeration sessEnum = session.getAttributeNames();

		while (sessEnum.hasMoreElements()) {
			String elem = (String) sessEnum.nextElement();
			if (elem.startsWith(SESSION_KEY)) {
				session.removeAttribute(elem);
			}
		}

	}

	private List<AnalysisDetail> getLoadHistory(Long mpOid, Date startDate, int startHe, Date endDate, int endHe) {
		return dao.getLoadValues(mpOid, startDate, startHe, endDate, endHe);
	}

	/**
	 * Generate the chart for the MP Analysis Editor
	 */
	public String graphLoads(Long mpOid, Long comparisonMpOid, Date analysisStart, Date analysisEnd, boolean graphOriginal, Date graphStartDate, int graphStartHe, Date graphEndDate, int graphEndHe, HttpSession session) {
		logger.debug("MpAnalysisServiceImpl.graphLoads() starting");

		Date t1 = new Date();

		TimeSeriesCollection collection = new TimeSeriesCollection();

		// process analysis
		logger.debug("MpAnalysisServiceImpl.graphLoads() graphing original");
		MpAnalysis analysis = getAnalysis(mpOid, analysisStart, 1, analysisEnd, 24, session);
		if (analysis!=null && analysis.getSize()>0) {

			collection.addSeries(createTimeAnalysisSeries(analysis.getAllLoadValues(), AnalysisDetail.CORRECTED_TYPE, graphStartDate, graphStartHe, graphEndDate, graphEndHe));
			if (graphOriginal) {
				collection.addSeries(createTimeAnalysisSeries(analysis.getAllLoadValues(), AnalysisDetail.ORIGINAL_TYPE, graphStartDate, graphStartHe, graphEndDate, graphEndHe));
			}
		}

		// process comparison
		if (comparisonMpOid!=null) {
			MpAnalysis comparison = getAnalysis(comparisonMpOid, analysisStart, 1, analysisEnd, 24, session);
			if (comparison!=null && comparison.getSize()>0) {
				collection.addSeries(createTimeAnalysisSeries(comparison.getAllLoadValues(), AnalysisDetail.COMPARISON_TYPE, graphStartDate, graphStartHe, graphEndDate, graphEndHe));
			}
		}

		if (analysis!=null && analysis.getSize()>0) {
			for (int i = 0; i < analysis.getNumFixes(); i++) {
				String fixNumber = "Fix " + (i+1);
				collection.addSeries(createTimeAnalysisSeries(analysis.getAllLoadValues(), fixNumber, graphStartDate, graphStartHe, graphEndDate, graphEndHe));
			}
		}
		String chartName = GraphUtil.getInstance().generateTimeChart(collection, analysis.getMaxValue(), Constants.ANALYSIS_CHART_WIDTH, Constants.ANALYSIS_CHART_HEIGHT);

		Date t2 = new Date();
		long diff = t2.getTime() - t1.getTime();

		logger.debug("MpAnalysisServiceImpl.graphLoads() done, took " + diff);

		return chartName;

	}

	/**
	 * Add a coarse fix to the analysis
	 */
	public void addCoarseFix(Long mpOid, Date fixStartDate, int fixStartHe, Date fixEndDate, int fixEndHe, Long sourceMpOid, Date sourceStartDate, int sourceStartHe, Date sourceEndDate, int sourceEndHe, HttpSession session) throws LtlfServiceException {
		logger.debug("MpAnalysisServiceImpl.addCoarseFix() starting");

		Date sourceStartCalDate = correctDst(sourceStartDate);
		Date sourceEndCalDate = correctDst(sourceEndDate);

		List<AnalysisDetail> sourceLoads = getLoadHistory(sourceMpOid, sourceStartCalDate, sourceStartHe, sourceEndCalDate, sourceEndHe);
		if (sourceLoads.size()==0) {
			throw new LtlfServiceException("Invalid source MP, or no loads found between start and end dates specified.");
		}

		addFix(mpOid, fixStartDate, fixStartHe, fixEndDate, fixEndHe, session, sourceLoads);

		logger.debug("MpAnalysisServiceImpl.addCoarseFix() done");
	}

	/**
	 * Add a fine fix to the analysis
	 */
	public void addFineFix(Long mpOid, List<AnalysisDetail> fixData, HttpSession session) throws LtlfServiceException {
		logger.debug("MpAnalysisServiceImpl.addFineFix() starting");

		addFix(mpOid, fixData.get(0).getLoadDate(), 1, fixData.get(fixData.size()-1).getLoadDate(), 24, session, fixData);

		logger.debug("MpAnalysisServiceImpl.addFineFix() done");
	}

	private void addFix(Long mpOid, Date fixStartDate, int fixStartHe, Date fixEndDate, int fixEndHe,
			HttpSession session, List<AnalysisDetail> sourceLoads) throws LtlfServiceException {

		MpAnalysis analysis = getCachedAnalysis(mpOid, session);

		int fixNum = analysis.getNumFixes() + 1;
		String fixNumStr = "Fix " + fixNum;

		Date fixStartCalDate = correctDst(fixStartDate);
		Date fixEndCalDate = correctDst(fixEndDate);

		logger.debug("MpAnalysisServiceImpl.addFix() applying fix: " + fixNumStr);

		List<AnalysisDetail> analysisData = analysis.getAllLoadValues();

		int sourceIndex = 0;

		try {

			// iterate through analysis data and add fix values
			for (Iterator<AnalysisDetail> iterator = analysisData.iterator(); iterator.hasNext();) {
				AnalysisDetail analysisLoad = (AnalysisDetail) iterator.next();

				if (analysisLoad.getLoadDate().before(fixStartCalDate)) {
					// skip
					continue;
				}

				if (analysisLoad.getLoadDate().after(fixEndCalDate)) {
					// stop
					break;
				}

				// dates are in the right range - now check hour ends
				// start hour end
				if (analysisLoad.getLoadDate().compareTo(fixStartCalDate)==0) {
					if (analysisLoad.getLoadHourEnd() < fixStartHe) {
						// skip
						continue;
					}
				}

				// end hour end
				if (analysisLoad.getLoadDate().compareTo(fixEndCalDate)==0) {
					if (analysisLoad.getLoadHourEnd() > fixEndHe) {
						// stop
						break;
					}
				}

				// ok now we know that this is a record we want to fix

				if (sourceIndex > sourceLoads.size()-1) {
					// reset source index if we have used up the source range (source starts over again)
					sourceIndex = 0;
				}

				// get the source load value
				AnalysisDetail sourceLoad = (AnalysisDetail) sourceLoads.get(sourceIndex);
				analysisLoad.addFix(fixNumStr, sourceLoad.getValue(AnalysisDetail.CORRECTED_TYPE));

				sourceIndex++;
			}
		} catch (NullPointerException npe) {
			logger.error("pAnalysisServiceImpl.addFix: Error iterating loadwrappers", npe);
			throw new LtlfServiceException("Null pointer exception in MpAnalysisServiceImpl.addFix: iterating loadwrappers " + npe.getMessage());
		}

		analysis.setNumFixes(fixNum);
		setCachedAnalysis(analysis, session);

		logger.debug("MpAnalysisServiceImpl.addFix() done");
	}

	/**
	 * This method smoothes outlying data in a load curve by:
	 * - calculating a trend average over the last N periods
	 * - calculating a tolerance percentage against the trend average
	 * - adding a fix if the point value is outside the tolerance
	 *
	 * This could be a real pig for performance!
	 *
	 * @param mpOid
	 * @param tolerancePercent - tolerance level (e.g. 25 = +/- 25% of the trend average)
	 * @param trendSizeDays - number of periods (days) to calculate a trend average for
	 * @param session
	 */
	public void smoothOutliers(Long mpOid, Date fixStartDate, Date fixEndDate, String smoothMethod, Double max, Double min, int tolerancePercent, int trendSizeDays, HttpSession session) {

		Date fixStartCalDate = correctDst(fixStartDate);
		Date fixEndCalDate = correctDst(fixEndDate);

		logger.debug("MpAnalysisServiceImpl.smoothOutliers() starting, start=" + fixStartCalDate + ", end=" + fixEndCalDate);

		Double loadMax=null;
		Double loadMin=null;

		MpAnalysis analysis = getCachedAnalysis(mpOid, session);

		int curFixNum = analysis.getNumFixes();
		boolean prevPointFixed = false;
		int newFixNum = curFixNum;

		List<AnalysisDetail> analysisData = analysis.getAllLoadValues();

		int sourceIndex = 0;

		// go through all of the values
		for (Iterator<AnalysisDetail> iterator = analysisData.iterator(); iterator.hasNext();) {
			AnalysisDetail analysisLoad = (AnalysisDetail) iterator.next();

			Date loadDate = analysisLoad.getLoadDate();
			if (loadDate.compareTo(fixStartCalDate) < 0) {
				// skip
				sourceIndex++;
				continue;
			}

			if (loadDate.compareTo(fixEndCalDate) > 0) {
				// stop
				break;
			}

			// get the latest value for this point (could have already been fixed)
			double loadValue = analysisLoad.getValue(AnalysisDetail.LATEST_TYPE);

			if (smoothMethod.equals("Tolerance") && (sourceIndex < (trendSizeDays*24)) ) {
				// skip
				sourceIndex++;
				continue;
			}

			if (smoothMethod.equals("Tolerance")) {

				double trendTotal = 0;


				// look back and average the loads over the trend window days (x24 hours/day)
				for (int i = (sourceIndex-(trendSizeDays*24)); i < sourceIndex; i++) {
					double val = analysisData.get(i).getValue(AnalysisDetail.LATEST_TYPE);
					trendTotal = trendTotal + val;

				}
				Double trendAverage = trendTotal/(trendSizeDays*24);
				long trendAverageL = Math.round(trendAverage * 1000000);
				trendAverage = new Long(trendAverageL).doubleValue() / 1000000;

				// calculate the min and max allowed based on the tolerance percent
				double toleranceAmt = trendAverage * tolerancePercent/100;

				loadMax = trendAverage + toleranceAmt;
				loadMin = trendAverage - toleranceAmt;

			} else {
				if (max!=null)
					loadMax = max;
				if (min!=null)
					loadMin = min;
			}

			loadMax = round(loadMax);
			loadMin = round(loadMin);

			// if outside the tolerance, add a fix
			if (loadMin!=null && loadValue < loadMin) {
				if (!prevPointFixed)
					newFixNum++;
				String fixNumStr = "Fix " + newFixNum;
				analysisLoad.addFix(fixNumStr, loadMin);
				prevPointFixed = true;
			} else if (loadMax!=null && loadValue > loadMax) {
				if (!prevPointFixed)
					newFixNum++;
				String fixNumStr = "Fix " + newFixNum;
				analysisLoad.addFix(fixNumStr, loadMax);
				prevPointFixed = true;
			} else {
				// load is within the boundaries, no fix needed
				prevPointFixed = false;
			}

			if (newFixNum>38) // stop at 39 fixes
				break;

			sourceIndex++;
		}

		if (newFixNum>curFixNum) {
			analysis.setNumFixes(newFixNum);
			setCachedAnalysis(analysis, session);
		}

		logger.debug("MpAnalysisServiceImpl.smoothOutliers() done, #fixes=" + newFixNum);

	}

	private Double round(Double value) {
		if (value!=null) {
			long maxL = Math.round(value * 1000000);
			return new Long(maxL).doubleValue() / 1000000;
		} else
			return null;
	}


	/**
	 * Get a list of specific analysis values for the MP Analysis fine fix editor
	 */
	public List<AnalysisDetail> getAnalysisValues(Long mpOid, Date startDate, int startHe, Date endDate, int endHe, HttpSession session) {
		logger.debug("MpAnalysisServiceImpl.getAnalysisValues() starting, start=" + startDate + " end=" + endDate);

		Date startCalDate = correctDst(startDate);
		Date endCalDate = correctDst(endDate);
		logger.debug("MpAnalysisServiceImpl.getAnalysisValues() corrected start date: " + startCalDate + " corrected end date: " + endCalDate);

		MpAnalysis base = getAnalysis(mpOid, startCalDate, startHe, endCalDate, endHe, session);
		logger.debug("MpAnalysisServiceImpl.getAnalysisValues() done");
		return base.getLoadValues(startCalDate, startHe, endCalDate, endHe);
	}

	private Date correctDst(Date startDate) {

		// correct the date
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);

		int year = startCal.get(Calendar.YEAR);
		int month = startCal.get(Calendar.MONTH);
		int day = startCal.get(Calendar.DATE);
		int hour = startCal.get(Calendar.HOUR_OF_DAY);

		logger.debug("MpAnalysisServiceImpl.correctDst() uncorrected: Y" + year + " M" + month + " D" + day + "H" + hour);

		if (hour < 2) {
			// do nothing
		} else if (hour>22) {
			// the hour is off, add 1 to date
			startCal.add(Calendar.DATE, 1);
			startCal.set(Calendar.HOUR_OF_DAY, 0);
		}

		day = startCal.get(Calendar.DATE);
		hour = startCal.get(Calendar.HOUR_OF_DAY);

		logger.debug("MpAnalysisServiceImpl.correctDst() corrected: Y" + year + " M" + month + " D" + day + "H" + hour);

		return startCal.getTime();
	}

	/**
	 * Undo any uncommitted analysis fixes
	 */
	public void undoMpAnalysisFixes(Long mpOid, HttpSession session) {
		logger.debug("MpAnalysisServiceImpl.undoMpAnalysisFixes starting");

		MpAnalysis analysis = getCachedAnalysis(mpOid, session);
		analysis.undoFixes();
		setCachedAnalysis(analysis, session);

		logger.debug("MpAnalysisServiceImpl.undoMpAnalysisFixes done");
	}

	/**
	 * Take the analysis back to it's original state
	 */
	public void removeMpAnalysisOverrides(Long mpOid, HttpSession session) {
		logger.debug("MpAnalysisServiceImpl.removeMpAnalysisOverrides starting");

		MpAnalysis analysis = getCachedAnalysis(mpOid, session);
		analysis.removeOverrides();
		setCachedAnalysis(analysis, session);

		logger.debug("MpAnalysisServiceImpl.removeMpAnalysisOverrides done");
	}

	/**
	 * Commit any pending fixes to the analysis
	 */
	public void commitMpAnalysisFixes(Long mpOid, HttpSession session) throws LtlfServiceException {
		logger.debug("MpAnalysisServiceImpl.commitMpAnalysisFixes starting");

		Date t1 = new Date();

		try {
			String userId = (String)session.getAttribute("USERID");

			MpAnalysis analysis = getCachedAnalysis(mpOid, session);
			analysis.applyFixes(userId);

			dao.saveLoadValues(analysis.getChangedLoadValues());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LtlfServiceException("Error saving analysis changes: " + e.getMessage());
		}

		Date t2 = new Date();
		long diff = t2.getTime() - t1.getTime();

		logger.debug("MpAnalysisServiceImpl.commitMpAnalysisFixes() done, took " + diff);

	}

    private TimeSeries createTimeAnalysisSeries(List<AnalysisDetail> loads, String seriesName, Date graphStartDate, int graphStartHe, Date graphEndDate, int graphEndHe) {

//		logger.debug("MpAnalysisServiceImpl.createTimeAnalysisSeries() starting, series name= " + seriesName);

        TimeSeries series = new TimeSeries(seriesName, Hour.class);

        for (Iterator<AnalysisDetail> iterator = loads.iterator(); iterator.hasNext();) {
        	AnalysisDetail hist = (AnalysisDetail) iterator.next();

			if (hist.getLoadDate().before(graphStartDate)) {
				// skip
				continue;
			}

			if (hist.getLoadDate().after(graphEndDate)) {
				// stop
				break;
			}

			if (DateUtil.checkDateRange(hist.getLoadDate(), hist.getLoadHourEnd(), graphStartDate, graphStartHe, graphEndDate, graphEndHe)) {
				if (hist.hasValue(seriesName)) {
					Day d = new Day(hist.getLoadDate());
		        	Hour h = new Hour(hist.getLoadHourEnd(), d);
		        	series.addOrUpdate(h, hist.getValue(seriesName));
				}
        	}
		}

//		logger.debug("MpAnalysisServiceImpl.createTimeAnalysisSeries() done");

        return series;
    }

	public void saveAnalysisSummary(GapSummaryValue summary, HttpSession session) throws LtlfServiceException {
		logger.debug("MpAnalysisServiceImpl.saveAnalysisSummary starting");

		Date t1 = new Date();

		try {
			String userId = (String)session.getAttribute("USERID");
			summary.setAuditDateTime(new Date());
			summary.setAuditUserId(userId);
			dao.saveAnalysisSummary(summary);
		} catch (Exception e) {
			logger.error("Error saving analysis summary: " + e.toString());
			throw new LtlfServiceException("Error saving analysis summary: " + e.getMessage());
		}

		Date t2 = new Date();
		long diff = t2.getTime() - t1.getTime();

		logger.debug("MpAnalysisServiceImpl.saveAnalysisSummary() done, took " + diff);
	}
}
