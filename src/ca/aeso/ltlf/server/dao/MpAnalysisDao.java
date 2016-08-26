package ca.aeso.ltlf.server.dao;

import java.util.Date;
import java.util.List;

import ca.aeso.ltlf.model.GapSummaryValue;
import ca.aeso.ltlf.model.AnalysisDetail;

public interface MpAnalysisDao {

	public List<AnalysisDetail> getLoadValues(Long mpOid, Date startDate, int startHe, Date endDate, int endHe);
	public int saveLoadValues(List<AnalysisDetail> loadValues);
	public void saveAnalysisSummary(GapSummaryValue summary);
}
