package ca.aeso.ltlf.client.calendar;

import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.TabPanel;

import ca.aeso.ltlf.client.common.LtlfBorderlessPanel;
import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.loadshape.UnitizeLoadShape;

public class CalendarMain extends LtlfComposite {
	private TabPanel tabPanel = null;

	public CalendarMain() {
		tabPanel = new TabPanel();
		tabPanel.setBodyBorder(false);
		tabPanel.setBorder(false);
		tabPanel.setTabPosition(Position.TOP);
		tabPanel.setActiveTab(0);

		LtlfBorderlessPanel calendarPanel = new LtlfBorderlessPanel("Calendar");
		calendarPanel.setAutoScroll(true);
		LoadCalendar loadCalendarView = new LoadCalendar();
//		loadCalendarView.addEditLoadHistoryEventListener(editLoadHistoryEventListener);
		calendarPanel.add(loadCalendarView);

		tabPanel.add(calendarPanel);

		initWidget(tabPanel);
	}
}
