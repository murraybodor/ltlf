package ca.aeso.ltlf.server.dao;

import java.util.List;
import ca.aeso.ltlf.model.Calendar;

public interface CalendarDao {

	public void saveCalendars(List<Calendar> entries);
	public void deleteAllCalendars();
	public List<Calendar> fetchCalendars();
}
