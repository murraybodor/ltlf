package ca.aeso.ltlf.client.allocation.system;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfGlobal;
import ca.aeso.ltlf.client.common.LtlfStyle;
import ca.aeso.ltlf.client.event.LtlfCompositeEvent;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListener;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListenerAdapter;
import ca.aeso.ltlf.client.util.Utils;
import ca.aeso.ltlf.model.Allocation;
import ca.aeso.ltlf.model.AllocationForecastYear;
import ca.aeso.ltlf.model.AllocationForecastYearComparer;
import ca.aeso.ltlf.model.AllocationSector;
import ca.aeso.ltlf.model.AllocationSectorComparer;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListener;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

public class SystemAllocationEditor extends LtlfComposite
{
	private List<Allocation> allocations = null;
    private GridPanel gpVersionSummary = null;
    private ExtElement versionSummaryGridElement = null;
    private ArrayReader versionSummaryReader = null;
    private ColumnModel versionSummaryTableColumnModel = null;
    private Panel versionDetailPanel = null;
    private SystemAllocationGrid gridVersionDetail = null;
	
	private final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final DateTimeFormat datetimeFormater = DateTimeFormat.getFormat(DATE_TIME_FORMAT);
	private static final String YMD_FORMAT = "yyyy-MM-dd";
	private static final DateTimeFormat ymdFormater = DateTimeFormat.getFormat(YMD_FORMAT);
	private final String YEAR_FORMAT = "yyyy";
	private final DateTimeFormat yearFormater = DateTimeFormat.getFormat(YEAR_FORMAT);

	private Integer baseYear = LtlfGlobal.getBaseYear();
	private String selectedVersionOid = "-1";
	private String defaultVersionDetailTitle = "Energy Allocation";
	private Allocation currentAllocation;
	private boolean loadingVersionDetail = false;
	
	public SystemAllocationEditor()
	{
		this.createJavascriptFunctions(this);
		
	  	VerticalPanel layoutPanel = new VerticalPanel();

	  	this.gpVersionSummary = createVersionSummaryGrid();
		this.versionSummaryGridElement = new ExtElement(this.gpVersionSummary.getElement());
		updateVersionSummaryGrid();
	  	layoutPanel.add(this.gpVersionSummary);
		
		Utils.addHtml(layoutPanel, "<br>");
	  	
	  	this.gridVersionDetail = new SystemAllocationGrid();
	  	
	  	this.versionDetailPanel = new Panel();
	  	this.versionDetailPanel.setWidth(610);
	  	this.versionDetailPanel.setTitle(this.defaultVersionDetailTitle);
	  	
        ToolbarButton btnSaveAllocation = new ToolbarButton("Save", new ButtonListenerAdapter() {   
            public void onClick(com.gwtext.client.widgets.Button button, EventObject e) {
            	saveAllocation();
            }
        });
        btnSaveAllocation.setCls(LtlfStyle.normalText);
	  	
        ToolbarButton btnAllocByArea = new ToolbarButton("Allocate By Area", new ButtonListenerAdapter() {   
            public void onClick(com.gwtext.client.widgets.Button button, EventObject e) {
            	if (!selectedVersionOid.equals("-1")) {
            		fireLtlfCompositeEvent(new LtlfCompositeEvent(this, LtlfCompositeEvent.EventCode.EditAreaAllocation, new Object[]{new Long(selectedVersionOid), new Integer(gridVersionDetail.getStartYear()), new Integer(gridVersionDetail.getEndYear())}));
            	}
            }
        });
        btnAllocByArea.setCls(LtlfStyle.normalText);
	  	
        Toolbar versionToolbar = new Toolbar();
        versionToolbar.addButton(btnSaveAllocation);
        versionToolbar.addFill();
        versionToolbar.addButton(btnAllocByArea);
        
        this.versionDetailPanel.setTopToolbar(versionToolbar);
	  	
	  	this.versionDetailPanel.add(this.gridVersionDetail);
	  	
	  	layoutPanel.add(this.versionDetailPanel);
		
		SimplePanel wrapperPanel = new SimplePanel();
		wrapperPanel.setStyleName(LtlfStyle.tabContentPanel);
		wrapperPanel.add(layoutPanel);

		initWidget(wrapperPanel);
	}

	private void updateVersionSummaryGrid()
	{
		AsyncCallback<Object> callback = new AsyncCallback<Object>() {
			public void onSuccess(Object result) {
				versionSummaryGridElement.unmask();
				allocations = (List<Allocation>) result;
				Object[][] versionSummaryData = getVersionSummaryData();
				Utils.displayDataInGrid(gpVersionSummary, versionSummaryTableColumnModel, versionSummaryData, versionSummaryReader);
				gpVersionSummary.getStore().sort("updatedDate", SortDir.DESC);

				if (allocations != null && allocations.size() > 0)
					updateVersionDetailGrid(Long.valueOf(gpVersionSummary.getStore().getRecordAt(0).getId()));
			}
			public void onFailure(Throwable caught) {
				versionSummaryGridElement.unmask();
				Window.alert(caught.getMessage());
				LtlfGlobal.showDebugText("Failed to fetch allocation: " + caught);
			}
		};

		this.versionSummaryGridElement.mask("Loading...");
		this.rpcProxy.getLtlfService().fetchAllocations(this.baseYear.intValue(), callback);
	}

	private GridPanel createVersionSummaryGrid()
	{
		GridPanel gridPanel = new GridPanel();
		gridPanel.setLayout(new FitLayout());
		gridPanel.setAutoScroll(true);
		gridPanel.setFooter(false);
        gridPanel.setHeight(160);
        gridPanel.setWidth(832);
        gridPanel.setTitle("Energy Allocation Versions");
        gridPanel.setDisableSelection(true);
        gridPanel.setEnableHdMenu(false);
        gridPanel.setFrame(false);
        gridPanel.setStripeRows(true);
        GridRowListener gridRowListener = new GridRowListenerAdapter() {
        	public void onRowClick(GridPanel grid, int rowIndex, EventObject e)
        	{
        		selectedVersionOid = grid.getStore().getRecordAt(rowIndex).getId();
        		if (selectedVersionOid.equals("-1"))
        			return;
        		
        		updateVersionDetailGrid(Long.valueOf(selectedVersionOid));
        	}
        };
        gridPanel.addGridRowListener(gridRowListener);
        
        RecordDef versionSummaryRecordDef = new RecordDef(
        	new FieldDef[]{
				new StringFieldDef("version", 1),
				new StringFieldDef("description", 2),
				new StringFieldDef("baseYear", 3),
				new StringFieldDef("startYear", 4),
				new StringFieldDef("endYear", 5),
				new DateFieldDef("createdDate", 6, "Y-m-d H:i:s"),
				new DateFieldDef("updatedDate", 7, "Y-m-d H:i:s"),
				new StringFieldDef("status", 8),
				new StringFieldDef("comments", 9)
        	}
        );

        this.versionSummaryReader = new ArrayReader(0, versionSummaryRecordDef);
        Renderer createdDateColumnRenderer = new Renderer() {
        	public String render(Object value, CellMetadata cellMetadata, Record record,   
        		int rowIndex, int colNum, Store store) {
        		Date createdDate = record.getAsDate("createdDate");
        		if (createdDate == null)
        			return "&nbsp;";
    	
        		return SystemAllocationEditor.ymdFormater.format(createdDate);   
        	}
        };
        
        Renderer updatedDateColumnRenderer = new Renderer() {
        	public String render(Object value, CellMetadata cellMetadata, Record record,   
        		int rowIndex, int colNum, Store store) {
        		Date updatedDate = record.getAsDate("updatedDate");
        		if (updatedDate == null)
        			return "&nbsp;";
    	
        		return SystemAllocationEditor.ymdFormater.format(updatedDate);   
        	}
        };
        
        Renderer copyColumnRenderer = new Renderer() {
        	public String render(Object value, CellMetadata cellMetadata, Record record,   
        		int rowIndex, int colNum, Store store) {
        		String versionOid = record.getId();
        		if (versionOid == null || versionOid.trim().length() == 0 || versionOid.equals("-1"))
        			return "&nbsp;";
    	
        		return "<a class=\"ltlfNormalText\" href=\"javascript:copyVersion('" + versionOid + "')\">Copy</a>";   
        	}
        };
        
        ColumnConfig versionColumnConfig = new ColumnConfig("Version", "version", 60, true, null, "version");
        versionColumnConfig.setAlign(TextAlign.CENTER);
		ColumnConfig descriptionColumnConfig = new ColumnConfig("Description", "description", 200, true, null, "description");
		descriptionColumnConfig.setAlign(TextAlign.LEFT);
		ColumnConfig baseYearColumnConfig = new ColumnConfig("Base", "baseYear", 60, true, null, "baseYear");
		baseYearColumnConfig.setAlign(TextAlign.CENTER);
		ColumnConfig startYearColumnConfig = new ColumnConfig("Start", "startYear", 60, true, null, "startYear");
		startYearColumnConfig.setAlign(TextAlign.CENTER);
		ColumnConfig endYearColumnConfig = new ColumnConfig("End", "endYear", 60, true, null, "endYear");
		endYearColumnConfig.setAlign(TextAlign.CENTER);
		ColumnConfig createdDateColumnConfig = new ColumnConfig("Created", "createdDate", 90, true, createdDateColumnRenderer, "createdDate");
		createdDateColumnConfig.setAlign(TextAlign.CENTER);
		ColumnConfig updatedDateColumnConfig = new ColumnConfig("Updated", "updatedDate", 90, true, updatedDateColumnRenderer, "updatedDate");
		updatedDateColumnConfig.setAlign(TextAlign.CENTER);
		ColumnConfig statusColumnConfig = new ColumnConfig("Status", "status", 50, true, null, "status");
		statusColumnConfig.setAlign(TextAlign.CENTER);
		ColumnConfig commentsColumnConfig = new ColumnConfig("Comments", "comments", 100, true, null, "comments");
		commentsColumnConfig.setAlign(TextAlign.CENTER);
		ColumnConfig copyColumnConfig = new ColumnConfig("&nbsp;", "copy", 40, true, copyColumnRenderer, "copy");
		copyColumnConfig.setAlign(TextAlign.CENTER);
		copyColumnConfig.setSortable(false);

		ColumnConfig[] versionSummaryTableColumns = new ColumnConfig[]{
			versionColumnConfig,
			descriptionColumnConfig,
			baseYearColumnConfig,
			startYearColumnConfig,
			endYearColumnConfig,
			createdDateColumnConfig,
			updatedDateColumnConfig,
			statusColumnConfig,
			commentsColumnConfig,
			copyColumnConfig
		};
		
		this.versionSummaryTableColumnModel = new ColumnModel(versionSummaryTableColumns);

		Toolbar bottomToolbar = new Toolbar();
		
		ButtonListener buttonListener = new ButtonListenerAdapter() {
			public void onClick(com.gwtext.client.widgets.Button button, EventObject e)
			{
				showCreateNewVersionWin();
			}
		};
		ToolbarButton btnNewVersion = new ToolbarButton("Create New Version", buttonListener);
		btnNewVersion.setIconCls("add-icon");
		bottomToolbar.addButton(btnNewVersion);
		
		gridPanel.setBottomToolbar(bottomToolbar);
      
        // init the grid panel with one empty row
		Object[][] versionSummaryData = this.getVersionSummaryData();
		Utils.displayDataInGrid(gridPanel, this.versionSummaryTableColumnModel, versionSummaryData, this.versionSummaryReader);

        return gridPanel;
	}

	private void showCreateNewVersionWin()
	{
		com.gwtext.client.widgets.Window inputWin = new com.gwtext.client.widgets.Window("Create New Version", true, false);
		inputWin.setWidth(220);
		inputWin.setHeight(180);
		LtlfCompositeEventListener newVersionCreatedListener = new LtlfCompositeEventListenerAdapter() {
			public void onEvent(LtlfCompositeEvent event)
			{
				if (event.getEventCode() != LtlfCompositeEvent.EventCode.SystemAllocationVersionCreated)
					return;
				
				Long versionOid = (Long)event.getEventArgs();
				updateVersionSummaryGrid();
			}
		};
		NewAllocationVersionForm form = new NewAllocationVersionForm(inputWin, this.baseYear);
		form.addLtlfCompositeEventListener(newVersionCreatedListener);
		inputWin.add(form);
		inputWin.show();
		inputWin.center();
	}

	private Object[][] getVersionSummaryData()
    {
		if (this.allocations == null || this.allocations.isEmpty())
	        return new Object[][]{
                new Object[]{"-1", "", "", "", "", "", "", "", "", ""}
			};

		Object[][] versionSummaryData = new Object[this.allocations.size()][];
		Iterator<Allocation> itr = this.allocations.iterator();
		int index = 0;
		
		while (itr.hasNext())
		{
			Allocation allocation = itr.next();
			
			Object[] summaryData = new Object[] {
				allocation.getOid(),
				allocation.getVersionNumber(),
				allocation.getDescription(),
				allocation.getBaseYear(),
				(allocation.getStartDate() != null ? this.yearFormater.format(allocation.getStartDate()) : ""),
				allocation.getEndYear(),
				"",		// created
				(allocation.getAuditDateTime() != null ? this.datetimeFormater.format(allocation.getAuditDateTime()) : ""),
				allocation.getStatus(),
				""		// comment
			};
			versionSummaryData[index++] = summaryData;

		}
		
    	return versionSummaryData;
    }
    
    private native void createJavascriptFunctions(SystemAllocationEditor systemAllocationEditor)
    /*-{
		$wnd.copyVersion = function(versionOid) {
			systemAllocationEditor.@ca.aeso.ltlf.client.allocation.system.SystemAllocationEditor::copyVersion(Ljava/lang/String;)(versionOid);
		};
    }-*/;
    
    private void copyVersion(String versionOid)
    {
    	Allocation sourceAllocation = null;
    	Long oid = Long.valueOf(versionOid);
    	Iterator<Allocation> itr = this.allocations.iterator();
    	while (itr.hasNext())
    	{
    		Allocation allocation = itr.next();
    		if (allocation.getOid().equals(oid))
    		{
    			sourceAllocation = allocation;
    			break;
    		}
    	}
    	if (sourceAllocation == null)
    		return;
    	
		AsyncCallback<Object> callback = new AsyncCallback<Object>() {
			public void onSuccess(Object result) {
				versionSummaryGridElement.unmask();
				Long versionOid = (Long) result;
				if (versionOid == null || versionOid.longValue() < 1)
				{
					Window.alert("Failed to copy allocation version: Unknown error");
					return;
				}
				
				updateVersionSummaryGrid();
			}
			public void onFailure(Throwable caught) {
				versionSummaryGridElement.unmask();
				Window.alert(caught.getMessage());
				LtlfGlobal.showDebugText("Failed to copy allocation: " + caught);
			}
		};

		versionSummaryGridElement.mask("Copying...");
		this.rpcProxy.getLtlfService().deepCopyAllocation(sourceAllocation, callback);
    }
	
	private void updateVersionDetailGrid(final Long versionOid)
	{
		if (this.currentAllocation != null && this.currentAllocation.getOid().compareTo(versionOid) == 0)
			return;
		
	  	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object result) {
    	    	loadingVersionDetail = false;
    			currentAllocation = (Allocation)result;
    			SystemAllocationGrid newGridVersionDetail = new SystemAllocationGrid(currentAllocation);
    			versionDetailPanel.remove(gridVersionDetail);
    		  	gridVersionDetail = newGridVersionDetail;
    		  	versionDetailPanel.add(gridVersionDetail);
    		  	versionDetailPanel.doLayout();
    		  	String title = currentAllocation.getDescription();
    		  	if (title == null || title.trim().length() == 0)
    		  		title = defaultVersionDetailTitle;
    		  	versionDetailPanel.setTitle(title);
        		selectedVersionOid = versionOid.toString();
    		  	
    		}

    		public void onFailure(Throwable ex) {
    	    	loadingVersionDetail = false;
    			Window.alert("Failed to fetch allocation: " + ex.getMessage());
				LtlfGlobal.showDebugText("Failed to fetch allocation: " + ex);
    		}
    	}; 

    	if (this.loadingVersionDetail)
    		return;
    	
    	this.loadingVersionDetail = true;
    	rpcProxy.getLtlfService().fetchAllocation(versionOid, callback);
	}
    
    private void saveAllocation()
    {
    	if (this.currentAllocation == null
    			|| this.currentAllocation.getAllocationForecastYears() == null
    			|| this.currentAllocation.getAllocationForecastYears().isEmpty())
    		return;
    	
	  	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object result) {
    	    	loadingVersionDetail = false;
    			Long returnCode = (Long)result;
    			if (returnCode == null || returnCode.longValue() != 1)
    				Window.alert("Failed to save allocation: Unknown error");
    			else
    				Window.alert("Allocation saved");
    		}

    		public void onFailure(Throwable ex) {
    	    	loadingVersionDetail = false;
    			Window.alert("Failed to save allocation: " + ex.getMessage());
				LtlfGlobal.showDebugText("Failed to save allocation: " + ex);
    		}
    	}; 

    	if (this.loadingVersionDetail)
    		return;
    	
    	// update currentAllocation object
    	SortedSet<AllocationForecastYear> AllocationForecastYearSet = currentAllocation.getAllocationForecastYears(new AllocationForecastYearComparer());
		AllocationForecastYear[] forecastYearList = (AllocationForecastYear[])AllocationForecastYearSet.toArray(new AllocationForecastYear[AllocationForecastYearSet.size()]);
		
    	for (int i = 0; i < this.gridVersionDetail.getRowCount(); i++)
    	{
	    	SortedSet<AllocationSector> AllocationSectorSet = forecastYearList[i].getAllocationSectors(new AllocationSectorComparer());
	    	AllocationSector[] sectorList = (AllocationSector[])AllocationSectorSet.toArray(new AllocationSector[AllocationSectorSet.size()]);
	    	
    		for (int j = 0; j < this.gridVersionDetail.getColumnCount(); j++)
    		{
    			SystemAllocationGridCell allocationCell = this.gridVersionDetail.getCell(i, j);
    	    	sectorList[j].setAllocEnergy(allocationCell.getEnergyValue());
    		}
    	}
    	
    	this.loadingVersionDetail = true;
    	rpcProxy.getLtlfService().saveAllocation(currentAllocation, callback);
    }
}
