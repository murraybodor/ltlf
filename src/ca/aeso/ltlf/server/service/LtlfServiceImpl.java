package ca.aeso.ltlf.server.service;

import java.util.*;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import net.sf.hibernate4gwt.gwt.HibernateRemoteService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ca.aeso.ltlf.model.Allocation;
import ca.aeso.ltlf.model.AllocationArea;
import ca.aeso.ltlf.model.AllocationComment;
import ca.aeso.ltlf.model.AllocationForecastYear;
import ca.aeso.ltlf.model.AnalysisDetail;
import ca.aeso.ltlf.model.Area;
import ca.aeso.ltlf.model.Calendar;
import ca.aeso.ltlf.model.CodesTable;
import ca.aeso.ltlf.model.GapAnalysis;
import ca.aeso.ltlf.model.GapSummaryValue;
import ca.aeso.ltlf.model.LoadShape;
import ca.aeso.ltlf.model.LoadShapeSummary;
import ca.aeso.ltlf.model.LtlfGlobalSettings;
import ca.aeso.ltlf.model.MeasurementPoint;
import ca.aeso.ltlf.rpc.LtlfService;
import ca.aeso.ltlf.rpc.LtlfServiceException;

/**
 * Application service facade
 * @author mbodor
 */
public class LtlfServiceImpl extends HibernateRemoteService implements LtlfService {

	protected Log logger = LogFactory.getLog(LtlfServiceImpl.class);
	private WebApplicationContext appCtx;
	private MpAnalysisService mpAnalysisService;
	private StagingService stagingService;
	private LoadShapeService loadShapeService;
	private CodesTableService codesTableService;
	private CommonService commonService;
	private AllocationService allocationService;

	static LtlfServiceImpl instance;
	
	static public LtlfServiceImpl globalInstance() {
		return instance;
	}
	/**
	 * Initialize the Spring WebApplicationContext
	 */
	public void init() throws ServletException {
		super.init();
		ServletContext ser = this.getServletContext();
		appCtx = WebApplicationContextUtils.getRequiredWebApplicationContext(ser) ;
		mpAnalysisService = (MpAnalysisService)appCtx.getBean("mpAnalysisService");
		stagingService = (StagingService)appCtx.getBean("stagingService");
		loadShapeService = (LoadShapeService)appCtx.getBean("loadShapeService");
		codesTableService = (CodesTableService)appCtx.getBean("codesTableService");
		commonService = (CommonService)appCtx.getBean("commonService");
		allocationService = (AllocationService)appCtx.getBean("allocationService");
		instance = this;
	} 

	/**
	 * Gets the current user (DEV in development or authenticated userid from the browser)
	 * @return the userId
	 */
	public String getUser() throws LtlfServiceException {

		HttpSession sess = getThreadLocalRequest().getSession();
		String userId = (String)sess.getAttribute("USERID");

		if (userId==null) {
    		logger.debug("LtlfServiceImpl.getUser() session USERID is null, discovering user");
			userId = discoverUser();
		} else {
    		logger.debug("LtlfServiceImpl.getUser() got existing user=" + userId);
		}

		return userId;
	}

	private String discoverUser() throws LtlfServiceException {

		String user = null;

    	CodesTable envCode = codesTableService.getCodeValue("ENVIRONMENT");

    	if (envCode!=null) {
    		logger.debug("LtlfServiceImpl.discoverUser() envcode not null: " + envCode.getCode());
    		if (envCode.getStringValue().equals("DEV")) {
        		logger.debug("LtlfServiceImpl.discoverUser() using dev user");
        		user = envCode.getStringValue();
    		} else {
        		logger.debug("LtlfServiceImpl.discoverUser() using remote user");
            	user = getThreadLocalRequest().getRemoteUser();
    		}
    	} else {
    		logger.debug("LtlfServiceImpl.discoverUser() envcode is null, assuming production");
        	user = getThreadLocalRequest().getRemoteUser();
    	}

    	if (user!=null) {
    		logger.debug("LtlfServiceImpl.discoverUser() remote user=" + user);
    		setUser(user);
    	}
    	else {
    		logger.error("LtlfServiceImpl.discoverUser() remote user is null!");
    		throw new LtlfServiceException("User not authenticated!");
    	}

    	return user;
	}


	private void setUser(String userId) {
		logger.info("LtlfServiceImpl.setUser() setting session USERID=" + userId);
		HttpSession sess = getThreadLocalRequest().getSession();
		sess.setAttribute("USERID", userId);

		Enumeration enumS = sess.getAttributeNames();
		while (enumS.hasMoreElements()) {
			String elem = (String) enumS.nextElement();
			logger.debug("LtlfServiceImpl.setUser existing session elements: " + elem);
		}

	}

    /**
     * Logs out and invalidates a user session
     */
	public void logoutUser() throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.logoutUser() logging out user=" + getUser());
		HttpSession session = getThreadLocalRequest().getSession();
		session.invalidate();
	}

	public Integer getBaseYear() throws LtlfServiceException {
    	return codesTableService.getBaseYear();
	}

	public String getApplicationReleaseId() throws LtlfServiceException {
    	return codesTableService.getReleaseId();
	}

	public Integer loadStaging(Date startDate, Date endDate) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.loadStaging invoked");
		return stagingService.loadStaging(startDate,endDate);
	}
	public void gapAnalysis(String mpName) throws LtlfServiceException{
		logger.debug("LtlfServiceImpl.gapAnalysis invoked");
		stagingService.gapAnalysis(mpName);
	}

	public List<MeasurementPoint> getMeasurementPoints() throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.getMeasurementPoints invoked");
		return commonService.getMeasurementPoints();
	}

	public Map<String, List<MeasurementPoint>> getAreaToMpMap() throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.getAreaToMpMap invoked");
		return commonService.getAreaToMpMap();
	}
	
	public String graphLoads(Long mpOid, Long comparisonMpOid, Date analysisStart, Date analysisEnd, boolean graphOriginal, Date graphStart, int startHe, Date graphEnd, int endHe) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.viewMpGraph invoked");
		String userId = getUser();
		logger.debug("LtlfServiceImpl.graphLoads user=" + userId);
		HttpSession session = getThreadLocalRequest().getSession();
		return mpAnalysisService.graphLoads(mpOid, comparisonMpOid, analysisStart, analysisEnd, graphOriginal, graphStart, startHe, graphEnd, endHe, session);
	}
	public void addCoarseFix(Long mpOid, Date fixStartDate, int fixStartHe, Date fixEndDate, int fixEndHe, Long sourceMpOid, Date sourceStartDate, int sourceStartHe, Date sourceEndDate, int sourceEndHe) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.addCoarseFix invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		mpAnalysisService.addCoarseFix(mpOid, fixStartDate, fixStartHe, fixEndDate, fixEndHe, sourceMpOid, sourceStartDate, sourceStartHe, sourceEndDate, sourceEndHe, session);
	}
	public List<AnalysisDetail> getAnalysisValues(Long mpOid, Date startDate, int startHe, Date endDate, int endHe) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.getAnalysisValues invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		List<AnalysisDetail> vals = mpAnalysisService.getAnalysisValues(mpOid, startDate, startHe, endDate, endHe, session);
		return vals;
	}
	public void addFineFix(Long mpOid, List<AnalysisDetail> fineFixes) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.addFineFix invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		mpAnalysisService.addFineFix(mpOid, fineFixes, session);
	}
	public void smoothOutliers(Long mpOid, Date fixStartDate, Date fixEndDate, String type, Double max, Double min, int tolerancePercent, int trendWindowSize) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.smoothOutliers invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		mpAnalysisService.smoothOutliers(mpOid, fixStartDate, fixEndDate, type, max, min, tolerancePercent, trendWindowSize, session);
	}
	public GapSummaryValue revertToOriginalMpAnalysis(Long mpOid) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.revertToOriginalMpAnalysis invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		// undo any fixes
		mpAnalysisService.undoMpAnalysisFixes(mpOid, session);
		// null out any overridden values
		mpAnalysisService.removeMpAnalysisOverrides(mpOid, session);
		// commit
		return commitMpAnalysisFixes(mpOid);
	}
	public void undoMpAnalysisFixes(Long mpOid) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.undoMpAnalysisFixes invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		mpAnalysisService.undoMpAnalysisFixes(mpOid, session);
	}
	public GapSummaryValue commitMpAnalysisFixes(Long mpOid) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.commitMpAnalysisFixes invoked");
		
		HttpSession session = getThreadLocalRequest().getSession();
		mpAnalysisService.commitMpAnalysisFixes(mpOid, session);
//		stagingService.gapAnalysis(mpOid); // this is  broken! fix it!
		GapSummaryValue summ = stagingService.getGapSummaryValue(mpOid);
		saveAnalysisSummary(summ);
		
		return summ;
	}
	public void saveAnalysisSummary(GapSummaryValue aValue)throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.saveAnalysisSummary invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		mpAnalysisService.saveAnalysisSummary(aValue, session);
	}
	public GapAnalysis fetchGapAnalysis(boolean filteredResults) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.fetchGapAnalysis invoked");
		return stagingService.fetchGapAnalysis(filteredResults);
	}

	public void saveGapAnalysis(List<GapSummaryValue> aValue)throws LtlfServiceException {
		stagingService.saveGapAnalysis(aValue);
	}

	public Integer unitizeLoadShape(String mpName) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.unitizeLoadShape invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		return loadShapeService.unitizeLoadShape(mpName, session);
	}

	public LoadShape fetchLoadShapeUnitize(String areaCode) throws LtlfServiceException {
		return loadShapeService.fetchLoadShapeUnitize(areaCode);
	}

	public LtlfGlobalSettings getGlobalSettings() throws LtlfServiceException
	{
		LtlfGlobalSettings globalSettings = new LtlfGlobalSettings();
		String isDebug = getInitParameter("debug");
		globalSettings.setDebug((isDebug != null && isDebug.equalsIgnoreCase("true")));
		globalSettings.setRefreshInterval(codesTableService.getRefreshInterval());
		globalSettings.setBaseYear(codesTableService.getBaseYear());
		return globalSettings;
	}

	// load shape
	public String[] generateShapeGraphs(int baseYear, Long mpOid, Integer versionNumber, Long comparisonMpOid, boolean graphOriginal, double lowerUnitRange, double upperUnitRange) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.generateShapeGraphs invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		return loadShapeService.generateShapeGraphs(baseYear, mpOid, versionNumber, comparisonMpOid, graphOriginal, lowerUnitRange, upperUnitRange, session);
	}
	public void applyBatchOverride(int baseYear, Long mpOid, Integer versionNumber, boolean overrideTop, double overrideValue) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.graphLoadShape invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		loadShapeService.applyBatchOverride(baseYear, mpOid, versionNumber, overrideTop, overrideValue, session);
	}
	public void applyOffset(int baseYear, Long mpOid, Integer versionNumber, String offsetUnit, int offsetValue) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.applyOffset invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		loadShapeService.applyOffset(baseYear, mpOid, versionNumber, offsetUnit, offsetValue, session);
	}
	public void copyLoadShape(int baseYear, Long mpOid, Integer versionNumber, Long sourceMpOid, int baseDayStart, int baseDayEnd) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.copyLoadShape invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		loadShapeService.copyLoadShape(baseYear, mpOid, versionNumber, sourceMpOid, baseDayStart, baseDayEnd, session);
	}
	public void revertToOriginalLoadShape(int baseYear, Long mpOid, Integer versionNumber) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.revertToOriginalLoadShape invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		// undo any fixes
		loadShapeService.undoLoadShapeFixes(baseYear, mpOid, versionNumber, session);
		// null out any overridden values
		loadShapeService.removeLoadShapeOverrides(baseYear, mpOid, versionNumber, session);
		// commit
		commitLoadShapeFixes(baseYear, mpOid, versionNumber, getShapeSummary(mpOid));
	}
	public void undoLoadShapeFixes(int baseYear, Long mpOid, Integer versionNumber) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.undoLoadShapeFixes invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		loadShapeService.undoLoadShapeFixes(baseYear, mpOid, versionNumber, session);
	}
	public void commitLoadShapeFixes(int baseYear, Long mpOid, Integer versionNumber, LoadShapeSummary summ) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.commitLoadShapeFixes invoked");
		HttpSession session = getThreadLocalRequest().getSession();
		loadShapeService.commitLoadShapeFixes(baseYear, mpOid, versionNumber, summ, session);
	}
	public LoadShapeSummary getShapeSummary(Long mpOid) throws LtlfServiceException {
		logger.debug("LtlfServiceImpl.getShapeSummary invoked");
		return loadShapeService.getShapeSummary(mpOid);
	}
	
	public List<Area> getAreas() throws LtlfServiceException {
		return commonService.getAreas();
	}
	public List<Allocation> fetchAllocations(int baseYear)
			throws LtlfServiceException {
		return allocationService.fetchAllocations(baseYear);
	}
	public Allocation fetchAllocation(Long allocationVersionOid)
			throws LtlfServiceException {
		return allocationService.fetchAllocation(allocationVersionOid);
	}
	public Allocation fetchAllocationAreasBySector(Long allocationVersionOid, String sectorType) throws LtlfServiceException {
		return allocationService.fetchAllocationAreasBySector(allocationVersionOid, sectorType);
	}
	public String[][] fetchAllocationSectorsBySector(Long allocationVersionOid, String sectorType) throws LtlfServiceException {
		return allocationService.fetchAllocationSectorsBySector(allocationVersionOid, sectorType);
	}
	public Long saveAllocationAreaSectors(String[][] changes) throws LtlfServiceException {
		return allocationService.saveAllocationAreaSectors(changes);
	}
	
	public Long saveAllocation(Allocation aValue) throws LtlfServiceException {
		allocationService.saveAllocation(aValue);
		return Long.valueOf(1);
	}
	public Long saveAllocationComments(List<AllocationComment> comments) throws LtlfServiceException {
		return allocationService.saveAllocationComments(comments);
	}
	public String[][] fetchAllocationMpsByArea(Long allocationVersionOid, String areaCode) throws LtlfServiceException {
		return allocationService.fetchAllocationMpsByArea(allocationVersionOid, areaCode);
	}
	
	public List<CodesTable> getAllocationSectorTypes() throws LtlfServiceException {
    	return codesTableService.getAllocationSectorTypes();
	}
	public List<Calendar> fetchCalendars() throws LtlfServiceException {
		return commonService.fetchCalendars();
	}
	public Date fetchCalendarLoadDate() throws LtlfServiceException {
		return commonService.fetchCalendarLoadDate();
	}

	public Long createAllocation(Integer baseYear, Date startDate, Integer endYear, String description) throws LtlfServiceException {
		
		Allocation allocation = allocationService.createAllocation(baseYear, startDate, endYear, description);
		if (allocation == null)
			return Long.valueOf(0);
		
		allocationService.saveAllocation(allocation);
		return allocation.getOid();
	}
	
	public Long deepCopyAllocation(Allocation aValue) throws LtlfServiceException {
		Allocation allocation = allocationService.deepCopyAllocation(aValue);
		if (allocation == null)
			return Long.valueOf(0);
		
		allocationService.saveAllocation(allocation);
		return allocation.getOid();
	}
	
	public List<AllocationArea> fetchAllocationAreas(AllocationForecastYear aValue) throws LtlfServiceException {
		return allocationService.fetchAllocationAreas(aValue);
	}
} 

