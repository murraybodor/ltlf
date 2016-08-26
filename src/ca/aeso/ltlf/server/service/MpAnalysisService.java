package ca.aeso.ltlf.server.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import ca.aeso.ltlf.model.GapSummaryValue;
import ca.aeso.ltlf.model.AnalysisDetail;
import ca.aeso.ltlf.rpc.LtlfServiceException;

public interface MpAnalysisService {

	public String graphLoads(Long mpOid, Long comparisonMpOid, Date analysisStart, Date analysisEnd, boolean graphOriginal, Date graphStart, int startHe, Date graphEnd, int endHe, HttpSession session) throws LtlfServiceException;
	public void addCoarseFix(Long mpOid, Date fixStartDate, int fixStartHe, Date fixEndDate, int fixEndHe, Long sourceMpOid, Date sourceStartDate, int sourceStartHe, Date sourceEndDate, int sourceEndHe, HttpSession session) throws LtlfServiceException;
	public List<AnalysisDetail> getAnalysisValues(Long mpOid, Date startDate, int startHe, Date endDate, int endHe, HttpSession session) throws LtlfServiceException;
	public void addFineFix(Long mpOid, List<AnalysisDetail> fineFixes, HttpSession session) throws LtlfServiceException;
	public void smoothOutliers(Long mpOid, Date fixStartDate, Date fixEndDate, String method, Double max, Double min, int tolerancePercent, int trendWindowSize, HttpSession session) throws LtlfServiceException;
	public void undoMpAnalysisFixes(Long mpOid, HttpSession session) throws LtlfServiceException;
	public void removeMpAnalysisOverrides(Long mpOid, HttpSession session) throws LtlfServiceException;
	public void commitMpAnalysisFixes(Long mpOid, HttpSession session) throws LtlfServiceException;
	public void clearCachedAnalyses(HttpSession session);
	public void saveAnalysisSummary(GapSummaryValue summary, HttpSession session) throws LtlfServiceException;
	
}
