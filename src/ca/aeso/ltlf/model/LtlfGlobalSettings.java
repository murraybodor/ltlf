package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LtlfGlobalSettings implements Serializable {
	private static final long serialVersionUID = 1100158671571691094L;
	private final Integer DEFAULT_INTERVAL = Integer.valueOf(30 * 1000);	// default to 30 seconds
	
	private boolean _isDebug = false;
	private Integer _refresh_interval;	// in millisecond
	private Integer _baseYear;
	private List<Area> areaList = new ArrayList<Area>();
	private Map<String,Area> areaMap = new HashMap<String,Area>();
	private Map<String,List<MeasurementPoint>> areaToMpMap = new HashMap<String,List<MeasurementPoint>>();
	
	public void setDebug(boolean isDebug)
	{
		this._isDebug = isDebug;
	}
	
	public boolean isDebug()
	{
		return this._isDebug;
	}
	
	public Integer getRefreshInterval()
	{
		return (this._refresh_interval == null ? this.DEFAULT_INTERVAL : this._refresh_interval);
	}
	
	public void setRefreshInterval(Integer interval)
	{
		this._refresh_interval = (interval == null ? this.DEFAULT_INTERVAL : Integer.valueOf(interval.intValue() * 1000));
	}
	
	public Integer getBaseYear()
	{
		return this._baseYear;
	}
	
	public void setBaseYear(Integer baseYear)
	{
		this._baseYear = baseYear;
	}

	public List<Area> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<Area> areaList) {
		this.areaList = areaList;
		for (Iterator<Area> iterator = areaList.iterator(); iterator.hasNext();) {
			Area area = (Area) iterator.next();
			areaMap.put(area.getCode(), area);
		}
	}

	public Map<String,Area> getAreaMap() {
		return areaMap;
	}

	public void setAreaMap(Map<String,Area> areaMap) {
		this.areaMap = areaMap;
	}

	public Map<String, List<MeasurementPoint>> getAreaToMpMap() {
		return areaToMpMap;
	}

	public void setAreaToMpMap(Map<String, List<MeasurementPoint>> areaToMpMap) {
		this.areaToMpMap = areaToMpMap;
	}
}
