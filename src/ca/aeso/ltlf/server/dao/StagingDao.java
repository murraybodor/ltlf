package ca.aeso.ltlf.server.dao;

import java.util.Date;
import java.util.List;

import ca.aeso.ltlf.model.GapAnalysis;
import ca.aeso.ltlf.model.GapSummaryValue;
import ca.aeso.ltlf.rpc.LtlfServiceException;

public interface StagingDao {

	public Integer loadStaging(Date startDate, Date endDate) throws LtlfServiceException ;
	public void gapAnalysis(String mpName) throws LtlfServiceException ;
	public GapAnalysis fetchGapAnalysis(boolean filterResults) throws LtlfServiceException ;
	public void copyFromStaging(String mpName) throws LtlfServiceException ;
	public void saveGapAnalysis(List<GapSummaryValue> aValue) throws LtlfServiceException ;
	public GapSummaryValue getGapSummaryValue(Long mpOid) throws LtlfServiceException ;
}
