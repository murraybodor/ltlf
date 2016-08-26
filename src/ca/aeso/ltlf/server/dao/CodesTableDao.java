package ca.aeso.ltlf.server.dao;

import java.util.List;

import ca.aeso.ltlf.model.CodesTable;

public interface CodesTableDao {

	public CodesTable getCodeValue(String key);
	public List<CodesTable> getCodeValues(String key);

}
