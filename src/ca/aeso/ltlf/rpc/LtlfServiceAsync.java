package ca.aeso.ltlf.rpc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ca.aeso.ltlf.model.*;

import com.google.gwt.user.client.rpc.AsyncCallback;
/**
 * LtlfService
 * RemoteService Interface for RPCs
 * 
 * @author mbodor
 */
public interface LtlfServiceAsync {

	// common
	public void getMeasurementPoints(AsyncCallback callback);
	public void getAreas(AsyncCallback callback);
	public void getUser(AsyncCallback callback);
	public void logoutUser(AsyncCallback callback);
	public void getBaseYear(AsyncCallback callback);
	public void getApplicationReleaseId(AsyncCallback callback);
	public void fetchCalendars(AsyncCallback callback);
	public void fetchCalendarLoadDate(AsyncCallback callback);
	public void getAreaToMpMap(AsyncCallback callback);
	
	// analysis
	public void loadStaging(Date startDate, Date endDate, AsyncCallback callback);
	public void gapAnalysis(String mpId,AsyncCallback callback);
	public void fetchGapAnalysis(boolean filteredResults, AsyncCallback callback);
	public void graphLoads(Long mpOid, Long comparisonMpOid, Date analysisStart, Date analysisEnd, boolean graphOriginal, Date startDate, int startHe, Date endDate, int endHe, AsyncCallback callback);
	public void addCoarseFix(Long mp, Date fixStartDate, int fixStartHe, Date fixEndDate, int fixEndHe, Long sourceMpOid, Date sourceStartDate, int sourceStartHe, Date sourceEndDate, int sourceEndHe, AsyncCallback callback);
	public void getAnalysisValues(Long mpOid, Date startDate, int startHe, Date endDate, int endHe, AsyncCallback callback);
	public void addFineFix(Long mpOid, List<AnalysisDetail> fineFixes, AsyncCallback callback);
	public void smoothOutliers(Long mpOid, Date fixStartDate, Date fixEndDate, String type, Double max, Double min, int tolerancePercent, int trendWindowSize, AsyncCallback callback);
	public void revertToOriginalMpAnalysis(Long mpOid, AsyncCallback callback);
	public void undoMpAnalysisFixes(Long mpOid, AsyncCallback callback);
	public void commitMpAnalysisFixes(Long mpOid, AsyncCallback callback);
	public void getGlobalSettings(AsyncCallback callback);
	public void saveGapAnalysis(List<GapSummaryValue> aValue, AsyncCallback callback);
	public void saveAnalysisSummary(GapSummaryValue aValue, AsyncCallback callback);
	
	// load shape
	public void generateShapeGraphs(int baseYear, Long mpOid, Integer versionNumber, Long comparisonMpOid, boolean graphOriginal, double lowerUnitRange, double upperUnitRange, AsyncCallback callback);
	public void applyBatchOverride(int baseYear, Long mpOid, Integer versionNumber, boolean overrideTop, double overrideValue, AsyncCallback callback);
	public void unitizeLoadShape(String mpId, AsyncCallback callback);
	public void fetchLoadShapeUnitize(String areaCode, AsyncCallback callback);
	public void applyOffset(int baseYear, Long mpOid, Integer versionNumber, String offsetUnit, int offsetValue, AsyncCallback callback);
	public void copyLoadShape(int baseYear, Long mpOid, Integer versionNumber, Long sourceMpOid, int baseDayStart, int baseDayEnd, AsyncCallback callback);
	public void revertToOriginalLoadShape(int baseYear, Long mpOid, Integer versionNumber, AsyncCallback callback);
	public void undoLoadShapeFixes(int baseYear, Long mpOid, Integer versionNumber, AsyncCallback callback);
	public void commitLoadShapeFixes(int baseYear, Long mpOid, Integer versionNumber, LoadShapeSummary summ, AsyncCallback callback);
	public void getShapeSummary(Long mpOid, AsyncCallback callback);
	
	//allocation
	public void fetchAllocation(Long allocationVersionOid, AsyncCallback callback);
	public void saveAllocation(Allocation aValue, AsyncCallback callback);
	public void getAllocationSectorTypes(AsyncCallback callback);
	public void createAllocation(Integer baseYear, Date startDate, Integer endYear,String description, AsyncCallback callback);
	public void deepCopyAllocation(Allocation aValue,AsyncCallback callback);
	public void fetchAllocations(int baseYear, AsyncCallback callback);
	public void fetchAllocationAreas(AllocationForecastYear aValue, AsyncCallback callback);
	public void fetchAllocationSectorsBySector(Long allocationVersionOid, String sectorType, AsyncCallback callback);
	public void saveAllocationAreaSectors(String[][] changes, AsyncCallback callback);
	public void saveAllocationComments(List<AllocationComment> comments, AsyncCallback callback);
	public void fetchAllocationMpsByArea(Long allocationVersionOid, String areaCode, AsyncCallback callback);

} 