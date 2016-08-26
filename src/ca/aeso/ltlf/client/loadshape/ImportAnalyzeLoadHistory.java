package ca.aeso.ltlf.client.loadshape;

import java.util.Date;

import ca.aeso.ltlf.client.common.*;
import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.event.LtlfCompositeEvent;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListener;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListenerAdapter;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.TabPanel;

public class ImportAnalyzeLoadHistory extends LtlfComposite {
	private TabPanel tabPanel = null;
	private LtlfBorderlessPanel editPanel = null;
	static final String DATE_FORMAT = "yyyy-MM-dd";
	private ImportLoadHistory importLoadHistory;
	
	public ImportAnalyzeLoadHistory(final AppFrame frame)
	{
		tabPanel = new TabPanel();
		tabPanel.setBodyBorder(false);
		tabPanel.setBorder(false);
		tabPanel.setTabPosition(Position.TOP);
		tabPanel.setActiveTab(0);

		LtlfCompositeEventListener editLoadHistoryEventListener = new LtlfCompositeEventListenerAdapter() {
			public void onEvent(LtlfCompositeEvent event) {
				
				if (event.getEventCode() == LtlfCompositeEvent.EventCode.EditLoadHistory) {
					
					Object[] eventParams = (Object[])event.getEventArgs();
					if (eventParams == null || eventParams.length < 3)
						return;
					Long mpOid = (Long)eventParams[0];
					if (mpOid == null)
						return;
					Date startDate = (Date)eventParams[1];
					Date endDate = (Date)eventParams[2];
					ImportLoadHistory hist = (ImportLoadHistory)eventParams[3];
					DateTimeFormat dateFormater = DateTimeFormat.getFormat(DATE_FORMAT);
					String startDateText = dateFormater.format(startDate);
					String endDateText = dateFormater.format(endDate);

					String panelId = "pnlEditLoadHistory";
					if (editPanel == null)
					{
						editPanel = new LtlfBorderlessPanel("Edit Load History");
						editPanel.setId(panelId);
						editPanel.setClosable(true);
						editPanel.setAutoScroll(true);
						tabPanel.add(editPanel);
					}
					else if (!tabPanel.hasItem(panelId))
						tabPanel.add(editPanel);
					
					LoadHistoryEditor analysisEditor = new LoadHistoryEditor(mpOid, startDateText, endDateText, hist); 
					editPanel.removeAll(true);	// clear any existing content
					editPanel.add(analysisEditor);
					getTabPanel().add(editPanel);
					getTabPanel().activate(panelId);
					
				} else if (event.getEventCode() == LtlfCompositeEvent.EventCode.EditLoadShape) {
					
					Object[] eventParams = (Object[])event.getEventArgs();
					if (eventParams == null || eventParams.length < 2)
						return;
					String mpOid = (String)eventParams[0];
					if (mpOid == null || mpOid.trim().length() == 0)
						return;
					String versionId = (String)eventParams[1];
					
					frame.getUnitizeEditLoadShape().getUnitizeLoadShape().editLoadShape(mpOid, versionId);
					
				}
				
			}
		};
		
		LtlfBorderlessPanel summPanel = new LtlfBorderlessPanel("Load History Summary");
		summPanel.setAutoScroll(true);
		ImportLoadHistory importLoadHistory = new ImportLoadHistory();
		importLoadHistory.addLtlfCompositeEventListener(editLoadHistoryEventListener);
		summPanel.add(importLoadHistory);

		initWidget(summPanel);
	}

	public ImportLoadHistory getImportLoadHistory() {
		return importLoadHistory;
	}

	public void setImportLoadHistory(ImportLoadHistory importLoadHistory) {
		this.importLoadHistory = importLoadHistory;
	}
}
