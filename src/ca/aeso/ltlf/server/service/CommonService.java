package ca.aeso.ltlf.server.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ca.aeso.ltlf.model.Area;
import ca.aeso.ltlf.model.Calendar;
import ca.aeso.ltlf.model.MeasurementPoint;
import ca.aeso.ltlf.model.Sector;
import ca.aeso.ltlf.rpc.LtlfServiceException;

public interface CommonService {

	public List<MeasurementPoint> getMeasurementPoints() throws LtlfServiceException;
	public MeasurementPoint getMeasurementPoint(String mpId) throws LtlfServiceException;
	public List<Area> getAreas() throws LtlfServiceException;
	public Area getArea(String areaCode) throws LtlfServiceException;
	public List<Calendar> fetchCalendars() throws LtlfServiceException;
	public Date fetchCalendarLoadDate() throws LtlfServiceException;
	public List<Sector> getSectors() throws LtlfServiceException;
	public Map<String, List<MeasurementPoint>> getAreaToMpMap() throws LtlfServiceException;
}
