package ca.aeso.ltlf.server.service;

import java.util.Date;
import java.util.List;

import ca.aeso.ltlf.model.GapAnalysis;
import ca.aeso.ltlf.model.GapSummaryValue;
import ca.aeso.ltlf.rpc.LtlfServiceException;

public interface StagingService {
	public Integer loadStaging(Date startDate, Date endDate) throws LtlfServiceException;
	public void gapAnalysis(String mpName) throws LtlfServiceException;
	public GapAnalysis fetchGapAnalysis(boolean filteredResults) throws LtlfServiceException;
	public void saveGapAnalysis(List<GapSummaryValue> aValue) throws LtlfServiceException;
	public GapSummaryValue getGapSummaryValue(Long mpOid) throws LtlfServiceException;

}
