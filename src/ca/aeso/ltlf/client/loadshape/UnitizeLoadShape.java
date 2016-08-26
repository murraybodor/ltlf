package ca.aeso.ltlf.client.loadshape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.HTMLPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ProgressBar;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.FitLayout;

import ca.aeso.ltlf.client.common.LtlfBorderlessPanel;
import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfGlobal;
import ca.aeso.ltlf.client.common.WaitButton;
import ca.aeso.ltlf.client.event.LtlfCompositeEvent;
import ca.aeso.ltlf.client.util.Utils;
import ca.aeso.ltlf.model.Area;
import ca.aeso.ltlf.model.LoadShape;
import ca.aeso.ltlf.model.LoadShapeSummary;

public class UnitizeLoadShape extends LtlfComposite {
	private final String purgeWarning = "All previous data will be purged";
	private WaitButton unitizeButton = new WaitButton();
	private int baseYear;
	private Label baseYearLabel = new Label();
	private Label lastUnitizeDate = new Label();
    private GridPanel gpUnitizeSummary = null;
	private ExtElement gpUnitizeSummaryElement = null;
    private ArrayReader unitizeSummaryReader = null;
    private ColumnModel unitizeSummaryTableColumnModel = null;
    private LoadShape shapeSummary = null;
    private ArrayList<Object[]> unitizedSummaries = null;
	private ComboBox planningAreaList;
    private LtlfBorderlessPanel progressBarPanel = null;
    private ProgressBar progressBar = null;   
    private Timer timer = null;
    private boolean unitizeInProgress = false;
    
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
	private final int REFRESH_INTERVAL = LtlfGlobal.getRefreshInterval();
	
	public UnitizeLoadShape()
	{
		this.createJavascriptFunctions(this);
		
		String normalTextStyle = "ltlfNormalText";
		String normalBoldTextStyle = "ltlfNormalBoldText";
		String warningTextStyle = "ltlfWarningText";
		
	    this.timer = new Timer() {   
	    	public void run() {
	    		updateUnitizeSummaryDisplay(planningAreaList.getValue());
	    	}   
	    };   

		VerticalPanel layoutPanel = new VerticalPanel();

		Panel unitizePanel = new Panel();
		unitizePanel.setWidth(600);
		unitizePanel.setPaddings(5);
		unitizePanel.setFrame(true);
		
		Label label = new Label();
		label.setStyleName(normalBoldTextStyle);
		label.setText("Unitize Load Shapes from Load History");
		unitizePanel.add(label);
		
		Utils.addHtml(unitizePanel, "<br>");
		
		Utils.addHtml(unitizePanel, "<span class=\"" + normalBoldTextStyle + "\">WARNING:</span>&nbsp;<span class=\"" + warningTextStyle + "\">" + this.purgeWarning + "</span>");

		Grid grid = new Grid(1, 2);
		grid.setCellPadding(5);

		this.unitizeButton.setStyleName(normalTextStyle);
		this.unitizeButton.setText("Unitize");
		this.unitizeButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				if (Window.confirm(purgeWarning))
				{
					unitizeButton.setWaiting(true);
					unitize();
				}
			}
		});
		grid.setWidget(0, 0, this.unitizeButton);

		// progress bar
        this.progressBar = new ProgressBar();   
        this.progressBar.setWidth(450);
        this.progressBar.reset();
        this.progressBar.setText("0.0%");
  
        this.progressBarPanel = new LtlfBorderlessPanel();   
        this.progressBarPanel.setPaddings(10);   
  
        this.progressBarPanel.add(new HTMLPanel("<span class=\"" + normalTextStyle + "\">Unitize in progress...</span>", 0, 0, 0, 5));
        this.progressBarPanel.add(this.progressBar);
        grid.setWidget(0, 1, this.progressBarPanel);   
		
        unitizePanel.add(grid);

		grid = new Grid(1, 2);
		grid.setCellPadding(5);

		label = new Label();
		label.setStyleName(normalTextStyle);
		label.setText("Base Year: ");
		grid.setWidget(0, 0, label);
		
		this.baseYearLabel.setStyleName(normalTextStyle);
		grid.setWidget(0, 1, this.baseYearLabel);
		
		unitizePanel.add(grid);

		grid = new Grid(1, 2);
		grid.setCellPadding(5);

		label = new Label();
		label.setStyleName(normalTextStyle);
		label.setText("Last Unitize Date: ");
		grid.setWidget(0, 0, label);
		
		this.lastUnitizeDate.setStyleName(normalTextStyle);

		grid.setWidget(0, 1, this.lastUnitizeDate);
		
		unitizePanel.add(grid);

		layoutPanel.add(unitizePanel);

		Utils.addHtml(layoutPanel, "<br>");

		this.gpUnitizeSummary = new GridPanel();
		this.gpUnitizeSummary.setLayout(new FitLayout());
		this.gpUnitizeSummary.setAutoScroll(true);
		this.gpUnitizeSummary.setFooter(false);
        this.gpUnitizeSummary.setHeight(450);
        this.gpUnitizeSummary.setWidth(600);
        this.gpUnitizeSummary.setTitle("Unitize Summary");
        this.gpUnitizeSummary.setDisableSelection(true);
        this.gpUnitizeSummary.setEnableHdMenu(false);
        this.gpUnitizeSummary.setFrame(false);
        this.gpUnitizeSummary.setStripeRows(true);

        RecordDef unitizeSummaryRecordDef = new RecordDef(
				new FieldDef[]{
						new StringFieldDef("mpName", 1),
						new StringFieldDef("description", 2),
						new StringFieldDef("status", 3),
						new StringFieldDef("edit", 4),
						new StringFieldDef("reunitize", 5)
				}
		);
        this.unitizeSummaryReader = new ArrayReader(0, unitizeSummaryRecordDef);
        Renderer editColumnRenderer = new Renderer() {   
            public String render(Object value, CellMetadata cellMetadata, Record record,   
                                 int rowIndex, int colNum, Store store) {
            	String mpOid = record.getId();
            	if (mpOid == null || mpOid.trim().length() == 0 || mpOid.equals("-1"))
            		return "&nbsp;";
                return "<a class=\"ltlfNormalText\" href=\"javascript:editLoadShape('" + mpOid + "', '1')\">Edit</a>";   
            }
        };
        Renderer reunitizeColumnRenderer = new Renderer() {   
            public String render(Object value, CellMetadata cellMetadata, Record record,   
                                 int rowIndex, int colNum, Store store) {
            	String mpOid = record.getId();
            	if (mpOid == null || mpOid.trim().length() == 0 || mpOid.equals("-1"))
            		return "&nbsp;";
                return "<a class=\"ltlfNormalText\" href=\"javascript:reunitize('" + mpOid + "')\">Reunitize</a>";   
            }
        };   
        ColumnConfig mpidColumnConfig = new ColumnConfig("MP Name", "mpName", 100, true, null, "mpName");
        mpidColumnConfig.setAlign(TextAlign.CENTER);
        ColumnConfig descriptionColumnConfig = new ColumnConfig("<div style=\"text-align:center\">Description</div>", "description", 200, true, null, "description");
        descriptionColumnConfig.setAlign(TextAlign.LEFT);
        ColumnConfig statusColumnConfig = new ColumnConfig("Load History", "status", 100, true, null, "status");
        statusColumnConfig.setAlign(TextAlign.CENTER);
        ColumnConfig editColumnConfig = new ColumnConfig("&nbsp;", "edit", 100, true, editColumnRenderer, "edit");
        editColumnConfig.setAlign(TextAlign.CENTER);
        editColumnConfig.setSortable(false);
        ColumnConfig reunitizeColumnConfig = new ColumnConfig("&nbsp;", "reunitize", 100, true, reunitizeColumnRenderer, "reunitize");
        reunitizeColumnConfig.setAlign(TextAlign.CENTER);
        reunitizeColumnConfig.setSortable(false);
        ColumnConfig[] unitizeSummaryTableColumns = new ColumnConfig[]{
        		mpidColumnConfig,
        		descriptionColumnConfig,
        		statusColumnConfig,
        		editColumnConfig,
        		reunitizeColumnConfig
        };
        this.unitizeSummaryTableColumnModel = new ColumnModel(unitizeSummaryTableColumns);
        Toolbar topToolbar = new Toolbar();
        topToolbar.addFill();
        ToolbarTextItem text = new ToolbarTextItem("Planning Area: ");
        topToolbar.addItem(text);

        Object[][] areas = new Object[][]{  
                new Object[]{new Integer(1), "blank"}
        };
		Store popupStore = new SimpleStore(new String[]{"code", "name"}, areas);
		popupStore.load();
        planningAreaList = new ComboBox();
		planningAreaList.setStore(popupStore);
        planningAreaList.setCls(normalTextStyle);
        planningAreaList.setReadOnly(true);
        planningAreaList.setDisplayField("name");
        planningAreaList.setValueField("code"); 
		loadAreas();

		planningAreaList.addListener(new ComboBoxListenerAdapter() {  
        	  
            public void onSelect(ComboBox comboBox, Record record, int index) {
            	if (unitizeInProgress)
            		return;
            	
            	String areaCode = comboBox.getValue();
            	if (areaCode.equals("All")){
            		areaCode = null;
            	}
            	updateUnitizeSummaryDisplay(areaCode);
            }  
        });  
        topToolbar.addField(planningAreaList);
        
        this.gpUnitizeSummary.setTopToolbar(topToolbar);

        // init the grid panel with one empty row

	    Object[][] unitizeSummaries = getUnitizeSummaryData();
	    showUnitizeSummary(unitizeSummaries);
	    

        updateUnitizeSummaryDisplay(null);
        
		layoutPanel.add(this.gpUnitizeSummary);

		SimplePanel wrapperPanel = new SimplePanel();
		wrapperPanel.setStyleName("ltlfTabContentPanel");
		wrapperPanel.add(layoutPanel);

		initWidget(wrapperPanel);
		
		this.progressBarPanel.setVisible(false);
	}
   
    private native void createJavascriptFunctions(UnitizeLoadShape unitizeLoadShape)
    /*-{
		$wnd.editLoadShape = function(mpOid, versionId) {
			unitizeLoadShape.@ca.aeso.ltlf.client.loadshape.UnitizeLoadShape::editLoadShape(Ljava/lang/String;Ljava/lang/String;)(mpOid, versionId);
		};
		$wnd.reunitize = function(mpOid) {
			unitizeLoadShape.@ca.aeso.ltlf.client.loadshape.UnitizeLoadShape::reunitize(Ljava/lang/String;)(mpOid);
		};
    }-*/;
 
    public void editLoadShape(String mpOid, String versionId)
    {
    	if (unitizeInProgress)
    		return;
    	
    	fireLtlfCompositeEvent(new LtlfCompositeEvent(this, LtlfCompositeEvent.EventCode.EditLoadShape, new Object[] {mpOid, versionId, this}));
    }
    
    private void reunitize(String mpOid)
    {
		try
		{
			if (this.unitizeInProgress)
				return;
			
			AsyncCallback<Object> callback = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					if (gpUnitizeSummaryElement != null)
						gpUnitizeSummaryElement.unmask();
	            	String areaCode = planningAreaList.getValue();
	            	if (areaCode.equals("All"))
	            		areaCode = null;
	            	updateUnitizeSummaryDisplay(areaCode);
				}
				
				public void onFailure(Throwable caught) {
					if (gpUnitizeSummaryElement != null)
						gpUnitizeSummaryElement.unmask();
					LtlfGlobal.showDebugText("Failed to reunitize load shape: " + caught);
				}
			};
			
			if (gpUnitizeSummaryElement == null)
				gpUnitizeSummaryElement = new ExtElement(gpUnitizeSummary.getElement());
			gpUnitizeSummaryElement.mask("Reunitizing ... ", true);
	    	rpcProxy.getLtlfService().unitizeLoadShape(mpOid, callback);
		}
		catch (Exception ex)
		{
			if (gpUnitizeSummaryElement != null)
				gpUnitizeSummaryElement.unmask();
			LtlfGlobal.showDebugText(ex.toString());
		}
    }
    
    private void unitize()
    {
		try
		{
			AsyncCallback<Object> callback = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					Integer returnCode = (Integer) result;
					if (returnCode != null && returnCode.intValue() == 0)	// unitize started successfully
					{
						unitizeInProgress = true;

						// reset Planning Area list
						planningAreaList.reset();
						planningAreaList.disable();
						// set analysis to null to clear the summary table
						shapeSummary = null;
						// clear unitize summary table
					    Object[][] unitizeSummaries = getUnitizeSummaryData();
					    showUnitizeSummary(unitizeSummaries);

				        // reset progress bar
				        progressBar.setText("0.0%");
				        progressBar.reset();
				        progressBarPanel.setVisible(unitizeInProgress);
				        
				        // activate timer
						timer.schedule(REFRESH_INTERVAL);
					}
					else
					{
						unitizeButton.setWaiting(false);
						LtlfGlobal.showDebugText("Failed to start unitizing load shape");
					}
				}
				public void onFailure(Throwable caught) {
					unitizeButton.setWaiting(false);
					System.out.println(caught);
					LtlfGlobal.showDebugText("Failed to unitize load shape: " + caught);
				}
			};
			
	    	rpcProxy.getLtlfService().unitizeLoadShape(null, callback);
		}
		catch (Exception ex)
		{
			LtlfGlobal.showDebugText(ex.toString());
		}
    }
    
    private void loadAreas()
    {
		try
		{
			AsyncCallback<Object> callback = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					Object [][] arData = getAreaEntries((ArrayList)result);
					Store popupStore = new SimpleStore(new String[]{"code", "name"}, arData);
					popupStore.load();
					planningAreaList.setStore(popupStore);
					System.out.println("success in areas");
					//System.out.println(result);
				}
				public void onFailure(Throwable caught) {
					System.out.println(caught);
				}
			};
			
	    	rpcProxy.getLtlfService().getAreas(callback);
		}
		catch (Exception ex)
		{
			LtlfGlobal.showDebugText(ex.toString());
		}
    }
	
    private Object[][] getAreaEntries(ArrayList areas){
    	if (areas == null || areas.size() == 0)
        	return new Object[][]{  
                new Object[]{new Integer(1), "blank"}
        };
        
    	Object[][] areaEntries = new Object[areas.size() + 1][];
    	
    	int index = 0;
    	String[] allEntry = new String[] {
				"All",
				"All" };
    	areaEntries[index++] = allEntry;

    	Iterator<Area> itr = areas.iterator();
    	while (itr.hasNext()) {
    		Area anArea = itr.next();
    		String[] areaData = new String[] {
    				anArea.getCode(),
    				anArea.getName()
    		};
    		areaEntries[index++] = areaData;
    	}
    	
		Comparator<Object> comparator = new Comparator<Object>() {
			public int compare(Object obj1, Object obj2) {
				String[] ary1 = (String[])obj1;
				String[] ary2 = (String[])obj2;
				
				if (ary2 == null)
					return 1;
				
				if (ary1 == null)
					return -1;
				
				String v1 = ary1[1];
				String v2 = ary2[1];
				
				if (v2 == null)
					return 1;
				
				if (v1 == null)
					return -1;
				
				return v1.compareTo(v2);
			}
		};
    	Arrays.sort(areaEntries, 1, index, comparator);

    	return areaEntries;
    }
    private Object[][] getUnitizeSummaryData()
    {
		if (this.shapeSummary == null || this.shapeSummary.getSummaries().isEmpty())
	        return new Object[][]{
                new Object[]{-1, "", "", "", "", ""}
			};

		Object[][] unitizeSummaries = new Object[this.shapeSummary.getSummaries().size()][];
		Iterator<LoadShapeSummary> itr = this.shapeSummary.getSummaries().iterator();
		int index = 0;
		while (itr.hasNext())
		{
			LoadShapeSummary lsuSummary = itr.next();
			Object[] summaryData = new Object[] {
				lsuSummary.getMpOid(),
				lsuSummary.getMpName(),
				lsuSummary.getDescription(),
				lsuSummary.getStatus(),
				"",
				""
			};
			unitizeSummaries[index++] = summaryData;
		}
		
    	return unitizeSummaries;
    }
    
    private void showUnitizeSummary(Object[][] unitizeSummaries)
    {
        MemoryProxy proxy = new MemoryProxy(unitizeSummaries);

        Store store = new Store(proxy, this.unitizeSummaryReader);
        store.load();

        if (!this.gpUnitizeSummary.isRendered())
        {
	        for (int i = 0; i < this.unitizeSummaryTableColumnModel.getColumnConfigs().length; i++)
	        {
	        	ColumnConfig columnConfig = (ColumnConfig)this.unitizeSummaryTableColumnModel.getColumnConfigs()[i];
	        	this.gpUnitizeSummary.setAutoExpandColumn(columnConfig.getId());
	        }
        }
        
        if (this.gpUnitizeSummary.getStore() == null)
        {
        	this.gpUnitizeSummary.setStore(store);
            this.gpUnitizeSummary.setColumnModel(this.unitizeSummaryTableColumnModel);
        }
        else
        	this.gpUnitizeSummary.reconfigure(store, this.unitizeSummaryTableColumnModel);
    }

	private void updateUnitizeSummaryDisplay(String planningArea)
	{
		try
		{
			AsyncCallback<Object> callback = new AsyncCallback<Object>()
			{
				public void onSuccess(Object result) {
					if (gpUnitizeSummaryElement != null)
						gpUnitizeSummaryElement.unmask();
					shapeSummary = (LoadShape)result;
					unitizedSummaries = new ArrayList<Object[]>();
					if (shapeSummary == null)
					{
						unitizeInProgress = false;
						progressBarPanel.setVisible(unitizeInProgress);
						unitizeButton.setWaiting(false);
						baseYearLabel.setText("");
						lastUnitizeDate.setText("");
						// disable timer
						timer.cancel();
					}
					else
					{
						Iterator<LoadShapeSummary> itr = shapeSummary.getSummaries().iterator();
						while (itr.hasNext())
						{
							LoadShapeSummary unitizeSummary = itr.next();
							unitizedSummaries.add(new Object[] {unitizeSummary.getMeasurementPoint().getOid(), unitizeSummary.getVersionNumber()});
						}
					
						baseYear = shapeSummary.getBaseYear().intValue();
						baseYearLabel.setText(Integer.toString(baseYear));
						DateTimeFormat dateFormater = DateTimeFormat.getFormat(DATE_FORMAT);
						String unitizeDate = dateFormater.format(shapeSummary.getUnitizeDate());
						lastUnitizeDate.setText(unitizeDate);
						
						if (shapeSummary.getStatus().equals("READY"))
						{
							unitizeInProgress = false;
							progressBarPanel.setVisible(unitizeInProgress);
							planningAreaList.enable();
							unitizeButton.setWaiting(false);
							// disable timer
							timer.cancel();
						}
						else if (shapeSummary.getStatus().equals("INPROGRESS"))
						{
							unitizeInProgress = true;
							progressBarPanel.setVisible(unitizeInProgress);
							planningAreaList.disable();
							progressBar.updateProgress(shapeSummary.getProgress() / 100, shapeSummary.getProgress() + "%");
							unitizeButton.setWaiting(true);
							// activate timer
							timer.schedule(REFRESH_INTERVAL);
						}
						else
						{
							unitizeInProgress = false;
							progressBarPanel.setVisible(unitizeInProgress);
							planningAreaList.enable();
							unitizeButton.setWaiting(false);
							lastUnitizeDate.setText("Failed at " + unitizeDate);
							// disable timer
							timer.cancel();
						}
					}
					
				    Object[][] unitizeSummaries2 = getUnitizeSummaryData();
				    showUnitizeSummary(unitizeSummaries2);
				}
				
				public void onFailure(Throwable caught) {
					if (gpUnitizeSummaryElement != null)
						gpUnitizeSummaryElement.unmask();
					Window.alert(caught.getMessage());
					LtlfGlobal.showDebugText("Failed to fetch unitize summary: " + caught);
				}
			};

			if (gpUnitizeSummaryElement == null)
				gpUnitizeSummaryElement = new ExtElement(gpUnitizeSummary.getElement());
			gpUnitizeSummaryElement.mask("Loading ... ", true);
			
			rpcProxy.getLtlfService().fetchLoadShapeUnitize(planningArea,callback);
		}
		catch (Exception ex)
		{
			LtlfGlobal.showDebugText(ex.toString());
			if (gpUnitizeSummaryElement != null)
				gpUnitizeSummaryElement.unmask();
		}
	}
	
	public List getUnitizedSummaries() {
		return unitizedSummaries;
	}

	public int getBaseYear() {
		return baseYear;
	}
}
