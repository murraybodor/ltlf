package ca.aeso.ltlf.client.loadshape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ca.aeso.ltlf.client.common.LtlfBorderlessPanel;
import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.WaitButton;
import ca.aeso.ltlf.client.util.Utils;
import ca.aeso.ltlf.model.GapAnalysis;
import ca.aeso.ltlf.model.GapSummaryValue;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.FitLayout;

public class AnalyzeLoadHistory extends LtlfComposite
{
	private TextBox tbVersionName = null;
	private TextBox tbVersionDescription = null;
	private WaitButton btnNewAnalysis = null;
	private Label lblServerMessage = null;
    private GridPanel gpAnalysisVersions = null;
    private ArrayReader analysisVersionReader = null;
    private ColumnModel versionTableColumnModel = null;
    private LtlfBorderlessPanel pnlAnalysisResult = null;
    private GridPanel gpAnalysisResult = null;
    private ArrayReader analysisResultReader = null;
    private ColumnModel resultTableColumnModel = null;
    
	public AnalyzeLoadHistory()
	{
		this.createJavascriptFunctions(this);
		
		String normalTextStyle = "ltlfNormalText";
		String normalBoldTextStyle = "ltlfNormalBoldText";
		String warningTextStyle = "ltlfWarningText";

		VerticalPanel layoutPanel = new VerticalPanel();

		Label label = new Label();
		label.setStyleName(normalBoldTextStyle);
		label.setText("Run New Gap Analysis");
		layoutPanel.add(label);

		Utils.addHtml(layoutPanel, "<br>");

		Grid grid = new Grid(1, 5);
		label = new Label();
		label.setStyleName(normalTextStyle);
		label.setText("Name:");
		grid.setWidget(0, 0, label);

		tbVersionName = new TextBox();
		tbVersionName.setStyleName(normalTextStyle);
		grid.setWidget(0, 1, tbVersionName);

		label = new Label();
		label.setStyleName(normalTextStyle);
		label.setText("Description:");
		grid.setWidget(0, 2, label);

		tbVersionDescription = new TextBox();
		tbVersionDescription.setStyleName(normalTextStyle);
		grid.setWidget(0, 3, tbVersionDescription);

		btnNewAnalysis = new WaitButton("Run New Gap Analysis");
		btnNewAnalysis.setStyleName(normalTextStyle);
		btnNewAnalysis.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				btnNewAnalysis.setWaiting(true);
				performAnalysis(tbVersionName.getText(), tbVersionDescription.getText());
			}
		});
		grid.setWidget(0, 4, btnNewAnalysis);

		layoutPanel.add(grid);
		
		this.lblServerMessage = new Label();
		this.lblServerMessage.setStyleName(warningTextStyle);
		layoutPanel.add(this.lblServerMessage);

		Utils.addHtml(layoutPanel, "<br>");

		label = new Label();
		label.setStyleName(normalBoldTextStyle);
		label.setText("Analysis Imported Data");
		layoutPanel.add(label);

		Utils.addHtml(layoutPanel, "<br>");

		this.gpAnalysisVersions = new GridPanel();
		this.gpAnalysisVersions.setLayout(new FitLayout());
		this.gpAnalysisVersions.setAutoScroll(true);
		this.gpAnalysisVersions.setFooter(false);
		this.gpAnalysisVersions.setLoadMask(true);
		this.gpAnalysisVersions.setLoadMask("Loading ...", normalTextStyle);
        this.gpAnalysisVersions.setHeight(150);
        this.gpAnalysisVersions.setWidth(600);
        this.gpAnalysisVersions.setTitle("Analysis Versions");
        this.gpAnalysisVersions.setDisableSelection(true);
        this.gpAnalysisVersions.setEnableHdMenu(false);
        this.gpAnalysisVersions.setFrame(false);
        this.gpAnalysisVersions.setStripeRows(true);

        RecordDef versionRecordDef = new RecordDef(
				new FieldDef[]{
						new StringFieldDef("name"),
						new StringFieldDef("description"),
						new StringFieldDef("created"),
						new StringFieldDef("updated"),
						new StringFieldDef("status"),
						new StringFieldDef("view")
				}
		);
        this.analysisVersionReader = new ArrayReader(versionRecordDef);
        Renderer viewColumnRenderer = new Renderer() {   
            public String render(Object value, CellMetadata cellMetadata, Record record,   
                                 int rowIndex, int colNum, Store store) {
            	String versionName = record.getAsString("name");
            	if (versionName == null || versionName.trim().length() == 0)
            		return "&nbsp;";
                return "<a class=\"ltlfNormalText\" href=\"javascript:viewAnalysisResult('" + versionName + "')\">View</a>";   
            }
        };   
        ColumnConfig[] versionTableColumns = new ColumnConfig[]{
                new ColumnConfig("Name", "name", 100, true, null, "name"),
                new ColumnConfig("Description", "description", 100, true, null, "description"),
                new ColumnConfig("Created", "created", 100, true, null, "created"),
                new ColumnConfig("Updated", "updated", 100, true, null, "updated"),
                new ColumnConfig("Status", "status", 100, true, null, "status"),
                new ColumnConfig("&nbsp;", "view", 100, true, viewColumnRenderer, "view")
        };
        this.versionTableColumnModel = new ColumnModel(versionTableColumns);

		// display empty list
		Object[][] versions = getVersionData(null);
		showAnalysisVersions(versions);
		layoutPanel.add(this.gpAnalysisVersions);
		// update version list
		update();

		Utils.addHtml(layoutPanel, "<br>");

//		this.pnlAnalysisResult.setWidth("100%");
		this.pnlAnalysisResult = new LtlfBorderlessPanel();
		this.pnlAnalysisResult.setLayout(new FitLayout());
//		this.pnlAnalysisResult.setAutoWidth(true);
//		this.pnlAnalysisResult.setMonitorResize(true);
		this.gpAnalysisResult = new GridPanel();
		this.gpAnalysisResult.setLayout(new FitLayout());
		this.gpAnalysisResult.setAutoScroll(true);
		this.gpAnalysisResult.setFooter(false);
		this.gpAnalysisResult.setLoadMask(true);
		this.gpAnalysisResult.setLoadMask("Loading ...", normalTextStyle);
        this.gpAnalysisResult.setHeight(150);
        this.gpAnalysisResult.setWidth(600);
        this.gpAnalysisResult.setTitle("Analysis Versions");
        this.gpAnalysisResult.setDisableSelection(true);
        this.gpAnalysisResult.setEnableHdMenu(false);
        this.gpAnalysisResult.setFrame(false);
        this.gpAnalysisResult.setStripeRows(true);

        RecordDef resultRecordDef = new RecordDef(
				new FieldDef[]{
						new StringFieldDef("name"),
						new StringFieldDef("description"),
						new StringFieldDef("created"),
						new StringFieldDef("updated"),
						new StringFieldDef("status"),
						new StringFieldDef("view")
				}
		);
        this.analysisResultReader = new ArrayReader(resultRecordDef);
        Renderer analyzeColumnRenderer = new Renderer() {   
            public String render(Object value, CellMetadata cellMetadata, Record record,   
                                 int rowIndex, int colNum, Store store) {
            	String versionName = record.getAsString("name");
            	if (versionName == null || versionName.trim().length() == 0)
            		return "&nbsp;";
                return "<a class=\"ltlfNormalText\" href=\"javascript:viewAnalysisResult('" + versionName + "')\">View</a>";   
            }
        };   
        ColumnConfig[] resultTableColumns = new ColumnConfig[]{
                new ColumnConfig("MP ID", "mpid", 120, true, null, "mpid"),
                new ColumnConfig("Number of Gaps", "gapcount", 120, true, null, "gapcount"),
                new ColumnConfig("Number of outliers", "outliercount", 120, true, null, "outliercount"),
                new ColumnConfig("Status", "status", 120, true, null, "status"),
                new ColumnConfig("&nbsp;", "analyze", 120, true, analyzeColumnRenderer, "analyze")
        };
        this.resultTableColumnModel = new ColumnModel(resultTableColumns);
        Checkbox chkDisplayAllMpids = new Checkbox("Display all MP IDs");
        Toolbar toolbar = new Toolbar();
        toolbar.addField(chkDisplayAllMpids);
        this.gpAnalysisResult.setTopToolbar(toolbar);
        Object[][] analysisResults = getAnalysisResultData(null);
        showAnalysisResult(analysisResults);
		this.pnlAnalysisResult.add(this.gpAnalysisResult);
		this.pnlAnalysisResult.setVisible(false);
		layoutPanel.add(this.pnlAnalysisResult);

		SimplePanel wrapperPanel = new SimplePanel();
		wrapperPanel.setStyleName("ltlfTabContentPanel");
		wrapperPanel.add(layoutPanel);

		initWidget(wrapperPanel);
	}
	
	private void performAnalysis(String analysisName, String analysisDesc)
	{
		try
		{
			AsyncCallback<Object> callback = new AsyncCallback<Object>()
			{
				public void onSuccess(Object result)
				{
					btnNewAnalysis.setWaiting(false);
					String serverMsg = "";
					if (((HashMap)result).get("errorString") != null)
						serverMsg = (String)((HashMap)result).get("errorString");
					lblServerMessage.setText(serverMsg);
					//update();
				}
				
				public void onFailure(Throwable caught)
				{
					btnNewAnalysis.setWaiting(false);
					System.out.println("something not working");
				}
			};

			rpcProxy.getLtlfService().gapAnalysis(null,callback);
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}
	}
	
	private void update()
	{
		try
		{
			AsyncCallback<Object> callback = new AsyncCallback<Object>()
			{
				public void onSuccess(Object result) {
					GapAnalysis analysis = (GapAnalysis) result;
					//Object[][] versions = getVersionData(analysis);
					//showAnalysisVersions(versions);
					System.out.println(analysis.getSummaryValues());
				}
				public void onFailure(Throwable caught) {
					System.out.println("something not working " + caught);
				}
			};
			System.out.println("**Calling fetch service");
			rpcProxy.getLtlfService().fetchGapAnalysis(false, callback);
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}
	}

    private Object[][] getVersionData(ArrayList<GapAnalysis> analysis)
    {
		if (analysis == null || analysis.isEmpty())
	        return new Object[][]{
                new Object[]{"", "", "", "", "", ""}
			};

		Object[][] versions = new Object[analysis.size()][];
		Iterator<GapAnalysis> itr = analysis.iterator();
		int index = 0;
		final String datePattern = "yyyy-MM-dd";
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(datePattern);
		while (itr.hasNext())
		{
			GapAnalysis ga = itr.next();
			String[] versionData = new String[] {
				dateFormat.format(ga.getCreatedDate()),
				dateFormat.format(ga.getImportStartDate()),
				dateFormat.format(ga.getImportEndDate()),
				ga.getStatus()
			};
			versions[index++] = versionData;
			Iterator itr2 = ga.getSummaryValues().iterator();
			while (itr.hasNext()){
				GapSummaryValue gv = (GapSummaryValue)itr2.next();
				System.out.println(gv);
			}
		}
		
    	return versions;
    }
    
    private void showAnalysisVersions(Object[][] versions)
    {
        MemoryProxy proxy = new MemoryProxy(versions);

        Store store = new Store(proxy, this.analysisVersionReader);
        store.load();
        
        if (this.gpAnalysisVersions.getStore() == null)
        {
        	this.gpAnalysisVersions.setStore(store);
            this.gpAnalysisVersions.setColumnModel(this.versionTableColumnModel);
        }
        else
        	this.gpAnalysisVersions.reconfigure(store, this.versionTableColumnModel);

        for (int i = 0; i < this.versionTableColumnModel.getColumnConfigs().length; i++)
        {
        	ColumnConfig columnConfig = (ColumnConfig)this.versionTableColumnModel.getColumnConfigs()[i];
        	this.gpAnalysisVersions.setAutoExpandColumn(columnConfig.getId());
        }
    }
    
    private void viewAnalysisVersion(String versionName)
    {
    	this.pnlAnalysisResult.setVisible(true);
    	this.gpAnalysisResult.setTitle(versionName + " Analysis");
    	Window.alert(versionName);
    }
    
    private native void createJavascriptFunctions(AnalyzeLoadHistory analyzeLoadHistory)
    /*-{
		$wnd.viewAnalysisResult = function(versionName) {
			analyzeLoadHistory.@ca.aeso.ltlf.client.AnalyzeLoadHistory::viewAnalysisVersion(Ljava/lang/String;)(versionName);
		};
    }-*/;

    private Object[][] getAnalysisResultData(ArrayList<GapAnalysis> analysis)
    {
		if (analysis == null || analysis.isEmpty())
	        return new Object[][]{
                new Object[]{"", "", "", "", ""}
			};

		Object[][] versions = new Object[analysis.size()][];
		Iterator<GapAnalysis> itr = analysis.iterator();
		int index = 0;
		final String datePattern = "yyyy-MM-dd";
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(datePattern);
		while (itr.hasNext())
		{
			GapAnalysis ga = itr.next();
			String[] versionData = new String[] {
				dateFormat.format(ga.getCreatedDate()),
				ga.getStatus()
			};
			versions[index++] = versionData;
		}
		
    	return versions;
    }
    
    private void showAnalysisResult(Object[][] analysisResults)
    {
        MemoryProxy proxy = new MemoryProxy(analysisResults);

        Store store = new Store(proxy, this.analysisResultReader);
        store.load();
        
        if (this.gpAnalysisResult.getStore() == null)
        {
        	this.gpAnalysisResult.setStore(store);
            this.gpAnalysisResult.setColumnModel(this.resultTableColumnModel);
        }
        else
        	this.gpAnalysisResult.reconfigure(store, this.resultTableColumnModel);

        for (int i = 0; i < this.resultTableColumnModel.getColumnConfigs().length; i++)
        {
        	ColumnConfig columnConfig = (ColumnConfig)this.resultTableColumnModel.getColumnConfigs()[i];
        	this.gpAnalysisResult.setAutoExpandColumn(columnConfig.getId());
        }
    }
}
