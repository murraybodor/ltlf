package ca.aeso.ltlf.server.dao;

import java.util.List;

import ca.aeso.ltlf.model.LoadShape;
import ca.aeso.ltlf.model.LoadShapeDetail;
import ca.aeso.ltlf.model.LoadShapeSummary;

public interface LoadShapeDao {

	public LoadShapeSummary getShape(Long mpOid) throws Exception;
	public List<LoadShapeDetail> getShapeValues(int baseYear, Long mpOid, Integer versionNumber) throws Exception ;
	public int saveShapeValues(List<LoadShapeDetail> shapeVals) throws Exception ;
	public void saveShapeSummary(LoadShapeSummary summ) throws Exception;
	public Integer unitizeLoadShape(String mpName) throws Exception ;
	public LoadShape fetchLoadShapeUnitize(String zoneCode) throws Exception ;
}
