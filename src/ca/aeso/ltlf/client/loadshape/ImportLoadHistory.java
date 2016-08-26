package ca.aeso.ltlf.client.loadshape;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ca.aeso.ltlf.client.common.LtlfBorderlessPanel;
import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfGlobal;
import ca.aeso.ltlf.client.common.WaitButton;
import ca.aeso.ltlf.client.event.LtlfCompositeEvent;
import ca.aeso.ltlf.client.util.Utils;
import ca.aeso.ltlf.model.GapAnalysis;
import ca.aeso.ltlf.model.GapSummaryValue;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.DateUtil;
import com.gwtext.client.widgets.HTMLPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ProgressBar;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.FitLayout;

public class ImportLoadHistory extends LtlfComposite
{
	private TextBox tbStartDate = new TextBox();
	private TextBox tbEndDate = new TextBox();
	private WaitButton importButton = new WaitButton();
	private Label recordCount = new Label();
	private Label lblLastImportDate = new Label();
	private final String purgeWarning = "All previous data will be purged";
	private GapAnalysis analysis = null;
    private GridPanel gpAnalysisSummary = null;
	private ExtElement gpAnalysisSummaryElement = null;
    private ArrayReader analysisSummaryReader = null;
    private ColumnModel analysisSummaryTableColumnModel = null;
    private TextField mpNameSearchField;
    private Checkbox chkDisplayAllMpids = null;
    private int rowHeight = -1;
    private ExtElement _scrollableContainer;	// scrollable panel in the summary grid
    private LtlfBorderlessPanel progressBarPanel = null;
    private ProgressBar progressBar = null;   
    private Timer timer = null;
    private boolean importInProgress = false;
    private boolean validateInProgress = false;
	
	private final String DATE_FORMAT = "yyyy-MM-dd";
	private final DateTimeFormat dateFormater = DateTimeFormat.getFormat(DATE_FORMAT);
	private final int REFRESH_INTERVAL = LtlfGlobal.getRefreshInterval();

	public ImportLoadHistory()
	{
		this.createJavascriptFunctions(this);
		
		String normalTextStyle = "ltlfNormalText";
		String normalBoldTextStyle = "ltlfNormalBoldText";
		String warningTextStyle = "ltlfWarningText";
		
	    this.timer = new Timer() {   
	    	public void run() {
	    		updateDisplay(chkDisplayAllMpids.getValue());
	    	}   
	    };   

	  	VerticalPanel layoutPanel = new VerticalPanel();

		Panel importPanel = new Panel();
		importPanel.setWidth(600);
		importPanel.setPaddings(5);
		importPanel.setFrame(true);
		
		Label label = new Label();
		label.setStyleName(normalBoldTextStyle);
		label.setText("Import data from data warehouse into staging area");
		importPanel.add(label);
		
		Utils.addHtml(importPanel, "<br>");
		
		Grid grid = new Grid(1, 5);
		grid.setCellPadding(5);
		
		label = new Label();
		label.setStyleName(normalTextStyle);
		label.setText("Start Date:");
		grid.setWidget(0, 0, label);
		
		tbStartDate.setStyleName(normalTextStyle);
		grid.setWidget(0, 1, tbStartDate);

		grid.setHTML(0, 2, "&nbsp;");
		
		label = new Label();
		label.setStyleName(normalTextStyle);
		label.setText("End Date:");
		grid.setWidget(0, 3, label);
		
		tbEndDate.setStyleName(normalTextStyle);
		grid.setWidget(0, 4, tbEndDate);

		Date now = new Date();
		Date startDate = DateUtil.add(now, DateUtil.YEAR, - 2);
		String startDateText = DateTimeFormat.getFormat("yyyy").format(startDate) + "-01-01";
		String endDateText = this.dateFormater.format(now);
		tbStartDate.setText(startDateText);
		tbEndDate.setText(endDateText);

		importPanel.add(grid);
		
		Utils.addHtml(importPanel, "<br>");
		
		Utils.addHtml(importPanel, "<span class=\"" + normalBoldTextStyle + "\">WARNING:</span>&nbsp;<span class=\"" + warningTextStyle + "\">" + purgeWarning + "</span>");

		grid = new Grid(1, 2);
		grid.setCellPadding(5);

		this.importButton.setStyleName(normalTextStyle);
		this.importButton.setText("Import");
		this.importButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				if (Window.confirm(purgeWarning))
				{
					importButton.setWaiting(true);
					loadStaging(tbStartDate.getText(), tbEndDate.getText());
				}
			}
		});
		grid.setWidget(0, 0, this.importButton);

		// progress bar
        this.progressBar = new ProgressBar();   
        this.progressBar.setWidth(450);
        this.progressBar.reset();
        this.progressBar.setText("0.0%");
  
        this.progressBarPanel = new LtlfBorderlessPanel();   
        this.progressBarPanel.setPaddings(10);   
  
        this.progressBarPanel.add(new HTMLPanel("<span class=\"" + normalTextStyle + "\">Import in progress...</span>", 0, 0, 0, 5));
        this.progressBarPanel.add(this.progressBar);
        grid.setWidget(0, 1, this.progressBarPanel);   
		
		importPanel.add(grid);
		
		Utils.addHtml(importPanel, "<br>");

		grid = new Grid(1, 2);
		grid.setCellPadding(5);

		label = new Label();
		label.setStyleName(normalTextStyle);
		label.setText("Last Import Date: ");
		grid.setWidget(0, 0, label);
		
		this.lblLastImportDate.setStyleName(normalTextStyle);
		this.lblLastImportDate.setText("");
		grid.setWidget(0, 1, this.lblLastImportDate);
		
		importPanel.add(grid);
		
		grid = new Grid(1, 2);
		grid.setCellPadding(5);

		label = new Label();
		label.setStyleName(normalTextStyle);
		label.setText("Number of Records Imported: ");
		grid.setWidget(0, 0, label);
		
		this.recordCount.setStyleName(normalTextStyle);
		grid.setWidget(0, 1, this.recordCount);
		
		importPanel.add(grid);
		
		layoutPanel.add(importPanel);

		Utils.addHtml(layoutPanel, "<br>");

		this.gpAnalysisSummary = new GridPanel();
		this.gpAnalysisSummary.setLayout(new FitLayout());
		this.gpAnalysisSummary.setAutoScroll(true);
		this.gpAnalysisSummary.setFooter(false);
        this.gpAnalysisSummary.setHeight(420);
        this.gpAnalysisSummary.setWidth(1030);
        this.gpAnalysisSummary.setTitle("Load History Summary");
        this.gpAnalysisSummary.setDisableSelection(true);
        this.gpAnalysisSummary.setEnableHdMenu(false);
        this.gpAnalysisSummary.setFrame(false);
        this.gpAnalysisSummary.setStripeRows(true);

        RecordDef analysisSummaryRecordDef = new RecordDef(
				new FieldDef[]{
						new StringFieldDef("mpName", 1),
						new StringFieldDef("description", 2),
						new IntegerFieldDef("anomalies", 3),
						new StringFieldDef("status", 4),
						new StringFieldDef("lastUpdated", 5),
						new StringFieldDef("updatedBy", 6),
						new StringFieldDef("effectiveDate", 7),
						new StringFieldDef("expiryDate", 8),
						new FloatFieldDef("prevYearLoadFactor", 9),
						new FloatFieldDef("baseYearLoadFactor", 10),
						new StringFieldDef("ready", 11),
						new StringFieldDef("edit", 12)
				}
		);
        this.analysisSummaryReader = new ArrayReader(0, analysisSummaryRecordDef);
        Renderer readyColumnRenderer = new Renderer() {   
            public String render(Object value, CellMetadata cellMetadata, Record record,   
                                 int rowIndex, int colNum, Store store) {
            	String mpOid = record.getId();
            	if (mpOid == null || mpOid.trim().length() == 0 || mpOid.equals("-1"))
            		return "&nbsp;";
            	String ready = record.getAsString("ready");
                return "<input type=\"checkbox\" class=\"ltlfNormalText\" onclick=\"updateReadyFlag('" + mpOid + "', this.checked)\"" + ((ready != null && ready.equalsIgnoreCase("T")) ? " checked" : "") + ">";   
            }
        };   
        Renderer editColumnRenderer = new Renderer() {   
            public String render(Object value, CellMetadata cellMetadata, Record record,   
                                 int rowIndex, int colNum, Store store) {
            	String mpOid = record.getId();
            	if (mpOid == null || mpOid.trim().length() == 0 || mpOid.equals("-1"))
            		return "&nbsp;";
            	
            	return "<a class=\"ltlfNormalText\" href=\"javascript:editLoadHistory('" + mpOid + "')\">Edit</a>";   
            		
            }
        };
        ColumnConfig mpidColumnConfig = new ColumnConfig("MP Name", "mpName", 80, true, null, "mpName");
        mpidColumnConfig.setAlign(TextAlign.CENTER);
        ColumnConfig descriptionColumnConfig = new ColumnConfig("Description", "description", 200, true, null, "description");
        descriptionColumnConfig.setAlign(TextAlign.LEFT);
        ColumnConfig anomaliesColumnConfig = new ColumnConfig("Number of Anomalies", "anomalies", 120, true, null, "anomalies");
        anomaliesColumnConfig.setAlign(TextAlign.CENTER);
        ColumnConfig statusColumnConfig = new ColumnConfig("Status", "status", 50, true, null, "status");
        statusColumnConfig.setAlign(TextAlign.CENTER);
        ColumnConfig lastUpdatedColumnConfig = new ColumnConfig("Last Updated", "lastUpdated", 80, true, null, "lastUpdated");
        lastUpdatedColumnConfig.setAlign(TextAlign.CENTER);
        ColumnConfig updatedByColumnConfig = new ColumnConfig("Updated By", "updatedBy", 80, true, null, "updatedBy");
        updatedByColumnConfig.setAlign(TextAlign.CENTER);
        ColumnConfig effectiveDateColumnConfig = new ColumnConfig("Effective Date", "effectiveDate", 80, true, null, "effectiveDate");
        effectiveDateColumnConfig.setAlign(TextAlign.CENTER);
        ColumnConfig expiryDateColumnConfig = new ColumnConfig("Expiry Date", "expiryDate", 80, true, null, "expiryDate");
        expiryDateColumnConfig.setAlign(TextAlign.CENTER);
        ColumnConfig prevYearLoadFactorColumnConfig = new ColumnConfig("Prev LF", "prevYearLoadFactor", 50, true, null, "prevYearLoadFactor");
        prevYearLoadFactorColumnConfig.setAlign(TextAlign.CENTER);
        ColumnConfig baseYearLoadFactorColumnConfig = new ColumnConfig("Base LF", "baseYearLoadFactor", 50, true, null, "baseYearLoadFactor");
        baseYearLoadFactorColumnConfig.setAlign(TextAlign.CENTER);
        ColumnConfig readyColumnConfig = new ColumnConfig("Ready <input type='button' value='Save' title=\"Save Ready Flags\" onclick=\"saveReadyFlags(event);return false;\" style='font-size:80%' class='" + normalTextStyle + "'>", "ready", 80, true, readyColumnRenderer, "ready");
        readyColumnConfig.setAlign(TextAlign.CENTER);
        ColumnConfig editColumnConfig = new ColumnConfig("&nbsp;", "edit", 40, true, editColumnRenderer, "edit");
        editColumnConfig.setAlign(TextAlign.CENTER);
        editColumnConfig.setSortable(false);
        ColumnConfig[] analysisSummaryTableColumns = new ColumnConfig[]{
        		mpidColumnConfig,
        		descriptionColumnConfig,
        		anomaliesColumnConfig,
        		statusColumnConfig,
        		lastUpdatedColumnConfig,
        		updatedByColumnConfig,
        		effectiveDateColumnConfig,
        		expiryDateColumnConfig,
        		prevYearLoadFactorColumnConfig,
        		baseYearLoadFactorColumnConfig,
        		readyColumnConfig,
        		editColumnConfig
        };
        this.analysisSummaryTableColumnModel = new ColumnModel(analysisSummaryTableColumns);
        
        mpNameSearchField = new TextField();
        mpNameSearchField.setWidth(80);
        mpNameSearchField.setCls(normalTextStyle);
        mpNameSearchField.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int keyCode, EventObject e)
			{
				Store store = gpAnalysisSummary.getStore();
				if (store.getCount() < 2)
					return;
				
				if (rowHeight < 0)	// get scrollable container and rowHeight only at the first time
				{
					rowHeight = 0;
					GridView gridView = gpAnalysisSummary.getView();
					ExtElement elt1 = new ExtElement(gridView.getRow(0));
					ExtElement elt2 = new ExtElement(gridView.getRow(1));
					if (elt1 != null && elt2 != null)
					{
						rowHeight = elt2.getTop() - elt1.getTop();
						_scrollableContainer = getScrollableContainer(elt1);
					}
				}
				String searchValue = mpNameSearchField.getText();
        		if (searchValue == null)
        			return;

        		searchValue = searchValue.toUpperCase();
        		int rowIndex = -1;
        		for (int i = 0; i < store.getCount(); i++)
        		{
        			Record record = store.getRecordAt(i);
        			if (record.getAsString("mpName").toUpperCase().startsWith(searchValue))
        			{
        				rowIndex = i;
        				break;
        			}
        		}
        		if (rowIndex != -1)
        			_scrollableContainer.scrollTo("top", rowIndex * rowHeight, false);
        	}
		});
        this.chkDisplayAllMpids = new Checkbox("Display all MP_IDs");
        this.chkDisplayAllMpids.addListener(new CheckboxListenerAdapter() {
        	public void onCheck(Checkbox field, boolean checked)
        	{
        		// if import is in progress, do NOT do anything
        		if (importInProgress || validateInProgress)
        			return;
        		filterAnalysisSummaryDisplay(checked);
        	}
        });
        Toolbar topToolbar = new Toolbar();
        topToolbar.addItem(new ToolbarTextItem("Find MP: "));
        topToolbar.addField(mpNameSearchField);
        topToolbar.addFill();
        topToolbar.addField(this.chkDisplayAllMpids);
        
        this.gpAnalysisSummary.setTopToolbar(topToolbar);

        ToolbarButton btnClearSort = new ToolbarButton("Clear Sort", new ButtonListenerAdapter() {   
            public void onClick(com.gwtext.client.widgets.Button button, EventObject e) {
                gpAnalysisSummary.clearSortState(true);
            }
        });
        btnClearSort.setCls(normalTextStyle);
        ToolbarButton btnRevalidateAll = new ToolbarButton("Revalidate All", new ButtonListenerAdapter() {   
            public void onClick(com.gwtext.client.widgets.Button button, EventObject e) {
            	if (!importInProgress)
            		performAnalysis(null);
            }
        });
        btnRevalidateAll.setCls(normalTextStyle);

        Toolbar bottomToolbar = new Toolbar();
        bottomToolbar.addButton(btnClearSort);
        bottomToolbar.addFill();
        bottomToolbar.addButton(btnRevalidateAll);
        this.gpAnalysisSummary.setBottomToolbar(bottomToolbar);   

        // init the grid panel with one empty row
        Object[][] analysisSummaries = getAnalysisSummaryData();
        showAnalysisSummary(analysisSummaries);
		updateDisplay(false);
        
		layoutPanel.add(this.gpAnalysisSummary);
		
		SimplePanel wrapperPanel = new SimplePanel();
		wrapperPanel.setStyleName("ltlfTabContentPanel");
		wrapperPanel.add(layoutPanel);

		initWidget(wrapperPanel);
		
		this.progressBarPanel.setVisible(false);
	}

	private ExtElement getScrollableContainer(ExtElement row)
	{
		ExtElement container = row.up("");
		while (container != null)
		{
			if (container.isScrollable())
				return container;
			container = container.up("");
		}
		
		return null;
	}
	
	private void loadStaging(String startDate, String endDate)
	{
		try
		{
			recordCount.setText("");
			Date dStartDate = DateTimeFormat.getFormat(DATE_FORMAT).parse(startDate);
			Date dEndDate = DateTimeFormat.getFormat(DATE_FORMAT).parse(endDate);
			AsyncCallback<Object> callback = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					Integer returnCode = (Integer) result;
					if (returnCode.intValue() == 0)	// import started successfully
					{
						importInProgress = true;
						
						// restore "Display All" to false
						chkDisplayAllMpids.setValue(false);
						// set analysis to null to clear the summary table
						analysis = null;
						// clear analysis summary table
				        Object[][] analysisSummaries = getAnalysisSummaryData();
				        showAnalysisSummary(analysisSummaries);

				        // reset progress bar
				        progressBar.setText("0.0%");
				        progressBar.reset();
				        progressBarPanel.setVisible(importInProgress);
				        
				        // activate timer
						timer.schedule(REFRESH_INTERVAL);
					}
				}
				public void onFailure(Throwable caught) {
					importButton.setWaiting(false);
					Window.alert(caught.getMessage());
					LtlfGlobal.showDebugText("Failed to import load history: " + caught);
				}
			};
			
			rpcProxy.getLtlfService().loadStaging(dStartDate, dEndDate, callback);
		}
		catch (Exception ex)
		{
			LtlfGlobal.showDebugText(ex.toString());
		}
	}

	private void updateDisplay(boolean allMpIds)
	{
		try
		{
			AsyncCallback<Object> callback = new AsyncCallback<Object>()
			{
				public void onSuccess(Object result) {
					if (gpAnalysisSummaryElement != null)
						gpAnalysisSummaryElement.unmask();
					analysis = (GapAnalysis) result;
					if (analysis == null)
					{
						importInProgress = false;
						progressBarPanel.setVisible(importInProgress);
						importButton.setWaiting(false);
						recordCount.setText("");
						// disable timer
						timer.cancel();
					}
					else
					{
						DateTimeFormat dateTimeFormater = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm");
						lblLastImportDate.setText(dateTimeFormater.format(analysis.getCreatedDate()));
						if (analysis.getStatus().equals("READY"))
						{
							importInProgress = false;
							progressBarPanel.setVisible(importInProgress);
							importButton.setWaiting(false);
							recordCount.setText("" + analysis.getTotalCount());
							Object[][] analysisSummaries = getAnalysisSummaryData();
							showAnalysisSummary(analysisSummaries);
							// disable timer
							timer.cancel();
						}
						else if (analysis.getStatus().equals("INPROGRESS"))
						{
							importInProgress = true;
							progressBarPanel.setVisible(importInProgress);
							progressBar.updateProgress(analysis.getProgress() / 100, analysis.getProgress() + "%");
							importButton.setWaiting(true);
							recordCount.setText("" + analysis.getTotalCount());
							// activate timer
							timer.schedule(REFRESH_INTERVAL);
						}
						else
						{
							importInProgress = false;
							progressBarPanel.setVisible(importInProgress);
							importButton.setWaiting(false);
							recordCount.setText("Last import failed");
							// disable timer
							timer.cancel();
						}
					}
				}
				public void onFailure(Throwable caught) {
					if (gpAnalysisSummaryElement != null)
						gpAnalysisSummaryElement.unmask();
					Window.alert(caught.getMessage());
					LtlfGlobal.showDebugText("Failed to fetch analysis summary: " + caught);
				}
			};

			if (this.gpAnalysisSummaryElement == null)
				this.gpAnalysisSummaryElement = new ExtElement(this.gpAnalysisSummary.getElement());
		
			if (!this.importInProgress)
				this.gpAnalysisSummaryElement.mask("Loading ... ", true);
			rpcProxy.getLtlfService().fetchGapAnalysis(!allMpIds, callback);
		}
		catch (Exception ex)
		{
			LtlfGlobal.showDebugText(ex.toString());
			if (this.gpAnalysisSummaryElement != null)
				this.gpAnalysisSummaryElement.unmask();
		}
	}
	
    private Object[][] getAnalysisSummaryData()
    {
    	
		if (this.analysis == null || this.analysis.getSummaryValues().isEmpty())
	        return new Object[][]{
                new Object[]{"-1", "", "", "", "", "", "", "", "", "", "", "", ""}
			};

		Object[][] analysisSummaries = new Object[this.analysis.getSummaryValues().size()][];
		Iterator<GapSummaryValue> itr = this.analysis.getSummaryValues().iterator();
		int index = 0;
		
		while (itr.hasNext())
		{
			GapSummaryValue ga = itr.next();
			
			Object[] summaryData = new Object[] {
				ga.getMpOid(),
				ga.getMpName(),
				ga.getDescription(),
				ga.getGapCount(),
				ga.getStatus(),
				(ga.getAuditDateTime() != null ? this.dateFormater.format(ga.getAuditDateTime()) : ""),		// last updated
				ga.getAuditUserId(),		// updated by
				(ga.getEffectiveDate() != null ? this.dateFormater.format(ga.getEffectiveDate()) : ""),
				(ga.getExpiryDate() != null ? this.dateFormater.format(ga.getExpiryDate()) : ""),
				(ga.getPrevYearLoadFactor() == null ? "" : ga.getPrevYearLoadFactor().toString()),
				(ga.getBaseYearLoadFactor() == null ? "" : ga.getBaseYearLoadFactor().toString()),
				ga.getReadyString(),
				""		// edit
			};
			analysisSummaries[index++] = summaryData;

		}
		
    	return analysisSummaries;
    }
    
    private void showAnalysisSummary(Object[][] analysisSummaries)
    {
        MemoryProxy proxy = new MemoryProxy(analysisSummaries);

        Store store = new Store(proxy, this.analysisSummaryReader);
        store.load();

        if (!this.gpAnalysisSummary.isRendered())
        {
	        for (int i = 0; i < this.analysisSummaryTableColumnModel.getColumnConfigs().length; i++)
	        {
	        	ColumnConfig columnConfig = (ColumnConfig)this.analysisSummaryTableColumnModel.getColumnConfigs()[i];
	        	this.gpAnalysisSummary.setAutoExpandColumn(columnConfig.getId());
	        }
        }
        
        if (this.gpAnalysisSummary.getStore() == null)
        {
        	this.gpAnalysisSummary.setStore(store);
            this.gpAnalysisSummary.setColumnModel(this.analysisSummaryTableColumnModel);
        }
        else
        	this.gpAnalysisSummary.reconfigure(store, this.analysisSummaryTableColumnModel);
    }
    
    private native void createJavascriptFunctions(ImportLoadHistory importLoadHistory)
    /*-{
		$wnd.editLoadHistory = function(mpOid) {
			importLoadHistory.@ca.aeso.ltlf.client.loadshape.ImportLoadHistory::editLoadHistory(Ljava/lang/String;)(mpOid);
		};
		$wnd.updateReadyFlag = function(mpOid, isReady) {
			importLoadHistory.@ca.aeso.ltlf.client.loadshape.ImportLoadHistory::updateReadyFlag(Ljava/lang/String;Z)(mpOid, isReady);
		};
		$wnd.saveReadyFlags = function(event) {
			if (event.stopPropagation)
				event.stopPropagation();
			else
				event.cancelBubble = true;
			importLoadHistory.@ca.aeso.ltlf.client.loadshape.ImportLoadHistory::saveReadyFlags()();
		};
    }-*/;
    
    public void editLoadHistory(String mpOid)
    {
    	fireLtlfCompositeEvent(new LtlfCompositeEvent(this, LtlfCompositeEvent.EventCode.EditLoadHistory, new Object[]{new Long(mpOid), this.analysis.getImportStartDate(), this.analysis.getImportEndDate(), this}));
    }
	
	private void filterAnalysisSummaryDisplay(boolean allMpIds)
	{
		updateDisplay(allMpIds);
	}
	
	private void performAnalysis(String mpOid)
	{
		try
		{
			AsyncCallback<Object> callback = new AsyncCallback<Object>()
			{
				public void onSuccess(Object result)
				{
					validateInProgress = false;
					if (gpAnalysisSummaryElement != null)
						gpAnalysisSummaryElement.unmask();
					updateDisplay(chkDisplayAllMpids.getValue());
				}
				
				public void onFailure(Throwable caught)
				{
					validateInProgress = false;
					if (gpAnalysisSummaryElement != null)
						gpAnalysisSummaryElement.unmask();
					Window.alert(caught.getMessage());
					LtlfGlobal.showDebugText("Gap analysis failed: " + caught.toString());
				}
			};

			if (gpAnalysisSummaryElement == null)
				gpAnalysisSummaryElement = new ExtElement(gpAnalysisSummary.getElement());
			gpAnalysisSummaryElement.mask("Loading ... ", true);
			this.validateInProgress = true;
			if (mpOid == null)	// revalidate all
			{
				// restore "Display All" to false
				chkDisplayAllMpids.setValue(false);
			}
			rpcProxy.getLtlfService().gapAnalysis(mpOid, callback);
		}
		catch (Exception ex)
		{
			this.validateInProgress = false;
			LtlfGlobal.showDebugText(ex.toString());
		}
	}

	private void updateReadyFlag(String mpOid, boolean isReady)
	{
		if (this.importInProgress || this.analysis == null)
			return;
		
		Store dataStore = this.gpAnalysisSummary.getStore();
		if (dataStore == null)
			return;

		dataStore.commitChanges();
		
		Record record = dataStore.getById(mpOid);
		if (record != null)
		{
			record.beginEdit();
			record.set("ready", isReady ? "T" : "F");
			record.endEdit();
		}
	}
	
	private void saveReadyFlags()
	{
		try
		{
			if (this.importInProgress || this.analysis == null)
				return;

			Store dataStore = this.gpAnalysisSummary.getStore();
			if (dataStore == null)
				return;

			dataStore.commitChanges();
			
			List<GapSummaryValue> modifications = new ArrayList<GapSummaryValue>();
			Iterator<GapSummaryValue> itr = this.analysis.getSummaryValues().iterator();
			while (itr.hasNext())
			{
				GapSummaryValue ga = itr.next();
				Record record = dataStore.getById(ga.getMpOid().toString());
				if (record == null)
					continue;
				
				String oldValue = ga.getReadyString();
				if (oldValue == null)
					oldValue = "F";

				String newValue = record.getAsString("ready");
				if (newValue == null)
					newValue = "F";

				if (!oldValue.equals(newValue))
				{
					ga.setReadyString(newValue);
					modifications.add(ga);
				}
			}
			
			if (modifications.size() < 1)
				return;

			AsyncCallback<Object> callback = new AsyncCallback<Object>() {
				public void onSuccess(Object result) {
					if (gpAnalysisSummaryElement != null)
						gpAnalysisSummaryElement.unmask();
					//updateDisplay(chkDisplayAllMpids.getValue());
				}
				public void onFailure(Throwable caught) {
					if (gpAnalysisSummaryElement != null)
						gpAnalysisSummaryElement.unmask();
					Window.alert(caught.getMessage());
					LtlfGlobal.showDebugText("Failed to save ready flags: " + caught);
				}
			};

			if (gpAnalysisSummaryElement == null)
				gpAnalysisSummaryElement = new ExtElement(gpAnalysisSummary.getElement());
			gpAnalysisSummaryElement.mask("Saving Ready flags ... ", true);
			rpcProxy.getLtlfService().saveGapAnalysis(modifications, callback);
		}
		catch (Exception ex)
		{
			LtlfGlobal.showDebugText(ex.toString());
		}
	}
	
	public List getLoadHistories() {

		List histories = new ArrayList();
		
		Iterator<GapSummaryValue> itr = this.analysis.getSummaryValues().iterator();
		while (itr.hasNext())
		{
			GapSummaryValue ga = itr.next();
			histories.add(ga.getMpOid());
		}
		
		return histories;
	}
	
	public GapSummaryValue getSummary(Long mpOid) {
		
		Iterator<GapSummaryValue> itr = this.analysis.getSummaryValues().iterator();
		while (itr.hasNext())
		{
			GapSummaryValue ga = itr.next();
			if (ga.getMpOid().equals(mpOid)) {
				return ga;
			}
		}

		return null;
	}
	
	public void updateDisplay() {
		updateDisplay(chkDisplayAllMpids.getValue());
	}
}
