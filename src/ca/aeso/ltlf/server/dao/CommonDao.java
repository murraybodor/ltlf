package ca.aeso.ltlf.server.dao;

import java.util.List;

import ca.aeso.ltlf.model.Area;
import ca.aeso.ltlf.model.MeasurementPoint;
import ca.aeso.ltlf.model.Sector;

/**
 * Common DAO Interface
 * @author mbodor
 */
public interface CommonDao {
	
	public List<MeasurementPoint> getMeasurementPoints();
	public List<Area> getAreas();
	public List<Sector> getSectors();
	
}
