package ca.aeso.ltlf.rpc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ca.aeso.ltlf.model.*;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
/**
 * LtlfService
 * RemoteService Interface for RPCs
 * 
 * @author mbodor
 */
public interface LtlfService extends RemoteService {

	// common
	public List<MeasurementPoint> getMeasurementPoints() throws LtlfServiceException;
	public List<Area> getAreas() throws LtlfServiceException;
	public String getUser() throws LtlfServiceException;
	public void logoutUser() throws LtlfServiceException;
	public Integer getBaseYear() throws LtlfServiceException;
	public String getApplicationReleaseId() throws LtlfServiceException;
	public List<Calendar> fetchCalendars() throws LtlfServiceException;
	public Date fetchCalendarLoadDate() throws LtlfServiceException;
	public Map<String, List<MeasurementPoint>> getAreaToMpMap() throws LtlfServiceException;
	
	// analysis
	public Integer loadStaging(Date startDate, Date endDate) throws LtlfServiceException;
	public void gapAnalysis(String mpId) throws LtlfServiceException;
	public GapAnalysis fetchGapAnalysis(boolean filteredResults) throws LtlfServiceException;
	public String graphLoads(Long mpOid, Long comparisonMpOid, Date analysisStart, Date analysisEnd, boolean graphOriginal, Date startDate, int startHe, Date endDate, int endHe) throws LtlfServiceException;
	public void addCoarseFix(Long mpOid, Date fixStartDate, int fixStartHe, Date fixEndDate, int fixEndHe, Long sourceMpOid, Date sourceStartDate, int sourceStartHe, Date sourceEndDate, int sourceEndHe) throws LtlfServiceException;
	public List<AnalysisDetail> getAnalysisValues(Long mpOid, Date startDate, int startHe, Date endDate, int endHe)  throws LtlfServiceException;
	public void addFineFix(Long mpOid, List<AnalysisDetail> fineFixes) throws LtlfServiceException;
	public void smoothOutliers(Long mpOid, Date fixStartDate, Date fixEndDate, String type, Double max, Double min, int tolerancePercent, int trendWindowSize) throws LtlfServiceException;
	public GapSummaryValue revertToOriginalMpAnalysis(Long mpOid) throws LtlfServiceException;
	public void undoMpAnalysisFixes(Long mpOid) throws LtlfServiceException;
	public GapSummaryValue commitMpAnalysisFixes(Long mpOid) throws LtlfServiceException;
	public LtlfGlobalSettings getGlobalSettings() throws LtlfServiceException;
	public void saveGapAnalysis(List<GapSummaryValue> aValue)throws LtlfServiceException;
	public void saveAnalysisSummary(GapSummaryValue aValue) throws LtlfServiceException;
	
	// load shape
	public String[] generateShapeGraphs(int baseYear, Long mpOid, Integer versionNumber, Long comparisonMpOid, boolean graphOriginal, double lowerUnitRange, double upperUnitRange) throws LtlfServiceException;
	public void applyBatchOverride(int baseYear, Long mpOid, Integer versionNumber, boolean overrideTop, double overrideValue) throws LtlfServiceException;
	public Integer unitizeLoadShape(String mpName) throws LtlfServiceException;
	public LoadShape fetchLoadShapeUnitize(String areaCode) throws LtlfServiceException;
	public void applyOffset(int baseYear, Long mpOid, Integer versionNumber, String offsetUnit, int offsetValue) throws LtlfServiceException;
	public void copyLoadShape(int baseYear, Long mpOid, Integer versionNumber, Long sourceMpOid, int baseDayStart, int baseDayEnd) throws LtlfServiceException;
	public void revertToOriginalLoadShape(int baseYear, Long mpOid, Integer versionNumber) throws LtlfServiceException;
	public void undoLoadShapeFixes(int baseYear, Long mpOid, Integer versionNumber) throws LtlfServiceException;
	public void commitLoadShapeFixes(int baseYear, Long mpOid, Integer versionNumber, LoadShapeSummary summ) throws LtlfServiceException;
	public LoadShapeSummary getShapeSummary(Long mpOid) throws LtlfServiceException;

	//allocation
	public Allocation fetchAllocation(Long allocationVersionOid) throws LtlfServiceException;
	public List<Allocation> fetchAllocations(int baseYear) throws LtlfServiceException;
	public Long saveAllocation(Allocation aValue) throws LtlfServiceException;
	public List<CodesTable> getAllocationSectorTypes()  throws LtlfServiceException;
	public Long createAllocation(Integer baseYear, Date startDate, Integer endYear, String description) throws LtlfServiceException;
	public Long deepCopyAllocation(Allocation aValue) throws LtlfServiceException;
	public List<AllocationArea> fetchAllocationAreas(AllocationForecastYear aValue) throws LtlfServiceException;
	public String[][] fetchAllocationSectorsBySector(Long allocationVersionOid, String sectorType) throws LtlfServiceException;
	public Long saveAllocationAreaSectors(String[][] changes) throws LtlfServiceException;
	public Long saveAllocationComments(List<AllocationComment> comments) throws LtlfServiceException;
	public String[][] fetchAllocationMpsByArea(Long allocationVersionOid, String areaCode) throws LtlfServiceException;
	
	
} 