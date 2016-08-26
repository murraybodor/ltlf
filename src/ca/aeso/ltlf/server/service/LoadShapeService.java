package ca.aeso.ltlf.server.service;

import javax.servlet.http.HttpSession;

import ca.aeso.ltlf.model.LoadShape;
import ca.aeso.ltlf.model.LoadShapeSummary;
import ca.aeso.ltlf.rpc.LtlfServiceException;

public interface LoadShapeService {

	public String[] generateShapeGraphs(int baseYear, Long mpOid, Integer versionNumber, Long comparisonMpOid, boolean graphOriginal, double lowerUnitRange, double upperUnitRange, HttpSession session) throws LtlfServiceException;
	public Integer unitizeLoadShape(String mpId, HttpSession sess) throws LtlfServiceException;
	public LoadShape fetchLoadShapeUnitize(String areaCode) throws LtlfServiceException;
	public void applyBatchOverride(int baseYear, Long mpOid, Integer versionNumber, boolean overrideTop, double overrideValue, HttpSession session) throws LtlfServiceException;
	public void applyOffset(int baseYear, Long mpOid, Integer versionNumber, String offsetUnit, int offsetValue, HttpSession session) throws LtlfServiceException;
	public void copyLoadShape(int baseYear, Long mpOid, Integer versionNumber, Long sourceMpOid, int baseDayStart, int baseDayEnd, HttpSession session) throws LtlfServiceException;
	public void removeLoadShapeOverrides(int baseYear, Long mpOid, Integer versionNumber, HttpSession session) throws LtlfServiceException;
	public void undoLoadShapeFixes(int baseYear, Long mpOid, Integer versionNumber, HttpSession session) throws LtlfServiceException;
	public void commitLoadShapeFixes(int baseYear, Long mpOid, Integer versionNumber, LoadShapeSummary summ, HttpSession session) throws LtlfServiceException;
	public void clearCachedShapes(HttpSession session) throws LtlfServiceException;
	public LoadShapeSummary getShapeSummary(Long mpOid) throws LtlfServiceException;


}
