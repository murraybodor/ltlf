package ca.aeso.ltlf.client.common;

import ca.aeso.ltlf.client.allocation.area.AreaAllocationEditor;
import ca.aeso.ltlf.client.allocation.EnergyAllocationMain;
import ca.aeso.ltlf.client.event.LtlfCompositeEvent;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListener;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListenerAdapter;
import ca.aeso.ltlf.client.loadshape.ImportAnalyzeLoadHistory;
import ca.aeso.ltlf.client.loadshape.UnitizeEditLoadShapeMain;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

import ca.aeso.ltlf.client.calendar.CalendarMain;

public class AppFrame extends LtlfComposite {
	private AppFrame _instance;
	private LtlfHeader header = null;
	private LtlfMenu menu = null;
	private LtlfWorkspace workspace = null;
	
	private ImportAnalyzeLoadHistory importAnalyzeLoadHistory = null;
	private UnitizeEditLoadShapeMain unitizeEditLoadShape = null;
	private AreaAllocationEditor areaAlloc = null;
	private CalendarMain calendarMain = null;
	private EnergyAllocationMain energyAllocationMain = null;
	
	private String appTitle = "Long Term Load Forecast";

	public AppFrame() {
		
		_instance = this;

		LtlfBorderlessPanel mainPanel = new LtlfBorderlessPanel();
		mainPanel.setLayout(new BorderLayout());

		header = new LtlfHeader();
		menu = new LtlfMenu();
		workspace = new LtlfWorkspace();
		
		LtlfCompositeEventListener menuListener = new LtlfCompositeEventListenerAdapter()
		{
			public void onEvent(LtlfCompositeEvent event) {
				if (event.getEventCode() != LtlfCompositeEvent.EventCode.MenuItemSelected)
					return;
				
				String menuText = (String)event.getEventArgs();
				if (menuText == null)
					return;
				setWindowTitle(appTitle + " - " + menuText);
				if (menuText.equals(LtlfMenuItems.LOAD_HISTORY_AND_SHAPES))
				{
					if (importAnalyzeLoadHistory == null) {
						importAnalyzeLoadHistory = new ImportAnalyzeLoadHistory(_instance);
						workspace.addTab(importAnalyzeLoadHistory);
						workspace.setActiveTab(0);
						
						DeferredCommand.addCommand(new Command() {
							public void execute() {
								unitizeEditLoadShape = new UnitizeEditLoadShapeMain(_instance);
								workspace.addTab(unitizeEditLoadShape);
							}
						});
						
					}
				}
				else if (menuText.equals(LtlfMenuItems.CALENDAR_DATE_MAPPING))
				{
					if (calendarMain == null)
						calendarMain = new CalendarMain();
					workspace.loadWidget(calendarMain);
				}
				else if (menuText.equals(LtlfMenuItems.ENERGY_ALLOCATE_ENERGY))
				{
					if (energyAllocationMain == null)
						energyAllocationMain = new EnergyAllocationMain();
					workspace.loadWidget(energyAllocationMain);
				}
				else
				{
					workspace.loadWidget(null);
					setWindowTitle(appTitle);
				}
			}
		};
		
		menu.addLtlfCompositeEventListener(menuListener);
		
		LtlfBorderlessPanel northWrapperPanel = new LtlfBorderlessPanel();
//		northWrapperPanel.setHeight(113);
		northWrapperPanel.add(header);
		northWrapperPanel.add(menu);
		mainPanel.add(northWrapperPanel, new BorderLayoutData(RegionPosition.NORTH));

		LtlfBorderlessPanel centerWrapperPanel = new LtlfBorderlessPanel();
		centerWrapperPanel.setLayout(new FitLayout());
		centerWrapperPanel.add(workspace);
		
		BorderLayoutData centerLayoutData = new BorderLayoutData(RegionPosition.CENTER);
		centerLayoutData.setMargins(new Margins(3, 3, 3, 3));
		mainPanel.add(centerWrapperPanel, centerLayoutData);

		initWidget(mainPanel);
	}
	
	private native void setWindowTitle(String title)
	/*-{
		$doc.title = title;
	}-*/;
	
	public ImportAnalyzeLoadHistory getImportAnalyzeLoadHistory() {
		return importAnalyzeLoadHistory;
	}

	public UnitizeEditLoadShapeMain getUnitizeEditLoadShape() {
		return unitizeEditLoadShape;
	}
}
