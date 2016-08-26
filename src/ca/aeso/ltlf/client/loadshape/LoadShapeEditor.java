package ca.aeso.ltlf.client.loadshape;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ca.aeso.ltlf.client.loadshape.*;
import ca.aeso.ltlf.client.common.*;
import ca.aeso.ltlf.client.event.*;
import ca.aeso.ltlf.model.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * LoadShapeEditor
 * This UI class displays 2 charts on separate tabs, graphing a load shape by magnitude and time scale.
 *
 * @author mbodor
 */
public class LoadShapeEditor extends LtlfComposite {

	LoadShapeEditor _instance;
	private List<MeasurementPoint> measurementPoints;
	private Map mpMap;
	private Map mpOidMap;
	private UnitizeLoadShape caller = null;
	LoadShapeSummary summ = null;

	final LoadShapeHourlyChart hourlyChart = new LoadShapeHourlyChart();
	final LoadShapeChart loadShapeChart = new LoadShapeChart();;

	Long mpOid;
	Long comparisonMpOid = null;
	int baseYear;
	Integer versionId = null;
	Label mpIdLabel = new Label();
	Label mpDescrLabel = new Label();
	Label mpAreaLabel = new Label();
	Label mpEffectiveLabel = new Label();
	Label mpExpiryLabel = new Label();

	CheckBox graphOriginalCb = new CheckBox("Graph original");
	Button loadHistoryButton = new Button("Edit Load History");

	StartsWithSuggestOracle mpOracle = new StartsWithSuggestOracle();
	SuggestBox comparisonMpBox = new SuggestBox(mpOracle);
	TabPanel loadShapeTabPanel = new TabPanel();
	TextBox lowerRangeTb = new TextBox();
	TextBox upperRangeTb = new TextBox();

	HorizontalPanel graphControlPanel = new HorizontalPanel();

	RadioButton rbGe = new RadioButton("operGroup", ">=");
	RadioButton rbLe = new RadioButton("operGroup", "<=");
    TextBox overrideValueTb = new TextBox();

	RadioButton rbDays = new RadioButton("offsetGroup", "Days");
	RadioButton rbHours = new RadioButton("offsetGroup", "Hours");
	TextBox offsetValueTb = new TextBox();

	SuggestBox sourceMpBox = new SuggestBox(mpOracle);
	TextBox fromTb = new TextBox();
	TextBox toTb = new TextBox();
	TextArea commentsTextArea = new TextArea();
	FlexTable commentsTable = new FlexTable();

	private DateTimeFormat dateFmtSlash = DateTimeFormat.getFormat("yyyy/MM/dd");
	private DateTimeFormat dateFmtDash = DateTimeFormat.getFormat("yyyy-MM-dd");
	private DateTimeFormat dateFmtNopunc = DateTimeFormat.getFormat("yyyyMMdd");

	ClickListener prevMpBtnClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			setChartsLoading();
			scrollMp(false);
		}
	};

	ClickListener nextMpBtnClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			setChartsLoading();
			scrollMp(true);
		}
	};

	ClickListener editLoadHistoryButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
	    	fireLtlfCompositeEvent(new LtlfCompositeEvent(this, LtlfCompositeEvent.EventCode.EditLoadHistory, new Object[]{mpOid.toString()}));
		}
	};

	ClickListener graphViewClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			setChartsLoading();
			regenerateShapeGraphs();
		}
	};

	ClickListener graphOriginalClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			graphViewClickListener.onClick(sender);
		}
	};

	ClickListener batchOverrideButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			applyBatchOverride();
		}
	};

	ClickListener offsetButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			applyOffset();
		}
	};

	ClickListener copyButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			copy();
		}
	};

	ClickListener revertToOriginalButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			revertToOriginal();
		}
	};

	ClickListener undoButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			undo();
		}
	};

	ClickListener commitButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			commit();
		}
	};

	public LoadShapeEditor(String mpOid, String versionId, UnitizeLoadShape caller)
	{
		_instance = this;
		this.mpOid = new Long(mpOid);
		this.versionId = new Integer(versionId);
		this.caller = caller;

		this.baseYear = caller.getBaseYear();

		// prePopulate static data
		loadMpList();

		lowerRangeTb.setText("0");
		upperRangeTb.setText("1");

		VerticalPanel loadShapeMainPanel = new VerticalPanel();

		loadShapeMainPanel.setStyleName("editLoadShapeMainPanel");


		// header panel
		loadShapeMainPanel.add(drawHeaderPanel());

		// body
		loadShapeMainPanel.add(drawTabPanel());

		// button panel
		Widget buttons = drawButtonPanel();

		loadShapeMainPanel.add(buttons);
		loadShapeMainPanel.setCellHorizontalAlignment(buttons, HasHorizontalAlignment.ALIGN_RIGHT);

		regenerateShapeGraphs();

		initWidget(loadShapeMainPanel);


	}


	private void scrollMp(boolean forward) {
		// get previous or next mpid

		int currIdx = 0;
		int newIdx = 0;

		int j = -1;

		List summaries = caller.getUnitizedSummaries();

		for (int i = 0; i < summaries.size(); i++) {
			Object[] entry = (Object[])summaries.get(i);
			Long summaryMpOid = (Long)entry[0];
			Integer summaryVersionId = (Integer)entry[1];

			if (summaryMpOid.equals(this.mpOid) & summaryVersionId.equals(this.versionId)) {
				// found the current MP in the list - now scroll forward or back by 1
				if (forward) {
					j = i+1;
					if (j > summaries.size() - 1)
						j = 0;
				} else {
					j = i-1;
					if (j < 0)
						j = summaries.size()-1;
				}

				break;
			}


		}

		if (j > -1) {
			Object[] entry = (Object[])summaries.get(j);
			Long summaryMpOid = (Long)entry[0];
			Integer summaryVersionId = (Integer)entry[1];
			this.mpOid = summaryMpOid;
			this.versionId = summaryVersionId;
			decodeMp(mpOid);
			getShapeSummary();
			regenerateShapeGraphs();
		}
	}

	private String getMpSummaryStatus() {
		
		List summaries = caller.getUnitizedSummaries();

		for (int i = 0; i < summaries.size(); i++) {
			Object[] entry = (Object[])summaries.get(i);
			String summaryMpId = (String)entry[0];
			Long summaryVersionId = (Long)entry[1];
			
			if (summaryMpId.equals(this.mpOid) & summaryVersionId.equals(this.versionId)) {
				return (String)entry[2];
			}
		}
		
		return null;
		
	}
	
	private void decodeMp(Long mpOid) {

		if (mpMap!=null) {
			MeasurementPoint measP = (MeasurementPoint)mpOidMap.get(mpOid);

			mpDescrLabel.setText(measP.getDescription());
			mpAreaLabel.setText(measP.getZoneCode());
			mpEffectiveLabel.setText(dateFmtSlash.format(measP.getMPEffectiveDate()));
			mpExpiryLabel.setText(dateFmtSlash.format(measP.getMPExpireDate()));
			mpIdLabel.setText(measP.getName());

			if (getMpSummaryStatus().equals("No")) {
				loadHistoryButton.setEnabled(false);
			} else {
				loadHistoryButton.setEnabled(true);
			}
			
		}

	}

	private void loadMpList() {
		AsyncCallback callback = new AsyncCallback() {

			public void onSuccess(Object result) {
				measurementPoints = (List) result;
				if (measurementPoints != null) {
					mpMap = new HashMap();
					mpOidMap = new HashMap();
					for (Iterator i = measurementPoints.iterator(); i.hasNext();) {
						MeasurementPoint mp = (MeasurementPoint)i.next();
						mpMap.put(mp.getName(), mp);
						mpOidMap.put(mp.getOid(), mp);
						mpOracle.add(mp.getName(), mp.getName() + " - " + mp.getDescription());
					}
					decodeMp(mpOid);
				}
			}

			public void onFailure(Throwable caught) {
				measurementPoints = null;
			}
		};

		rpcProxy.getLtlfService().getMeasurementPoints(callback);
	}

	private Widget drawTabPanel() {

		loadShapeTabPanel.add(drawLoadShapeTab(), "Load Shape");
		loadShapeTabPanel.add(drawHourlyShapeTab(), "Hourly Shape");

		setChartsLoading();

		loadShapeTabPanel.selectTab(0);

		return loadShapeTabPanel;
	}

	private VerticalPanel drawLoadShapeTab() {

		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("editLoadShapeLoadShapeChartPanel");

		vp.add(drawGraphControlPanel());
		vp.setCellHorizontalAlignment(graphControlPanel, HasHorizontalAlignment.ALIGN_CENTER);

		HorizontalPanel hp = new HorizontalPanel();

		hp.add(loadShapeChart);
		hp.add(drawBatchOverridePanel());

		vp.add(hp);

		return vp;
	}

	private VerticalPanel drawHourlyShapeTab() {

		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("editLoadShapeHourlyShapeChartPanel");

		HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		hp.add(hourlyChart);

		VerticalPanel controlPanel = new VerticalPanel();
		controlPanel.setStyleName("editLoadShapeHourlyShapeControlPanel");

		controlPanel.add(drawCopyPanel());
		controlPanel.add(drawOffsetPanel());
		controlPanel.add(drawCommentsPanel());

		hp.add(controlPanel);

		vp.add(hp);

		return vp;
	}

	private HorizontalPanel drawHeaderPanel() {
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.setStyleName("editLoadShapeHeaderPanel");
		headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Label analysisMpLabel = new Label("Analysis MP: ");
		analysisMpLabel.setWordWrap(false);
		analysisMpLabel.setStyleName("ltlfNormalBoldText");

		mpIdLabel.setWordWrap(false);
		mpIdLabel.setStyleName("ltlfNormalText");

		mpDescrLabel.setWordWrap(false);
		mpDescrLabel.setStyleName("ltlfNormalText");

		Label comparisonMpLabel = new Label("Comparison MP: ");
		comparisonMpLabel.setWordWrap(false);
		comparisonMpLabel.setStyleName("ltlfNormalBoldText");

		comparisonMpBox.setWidth("100px");

		Label areaLabel = new Label("Area:");
		areaLabel.setStyleName("ltlfNormalBoldText");

		mpAreaLabel.setWordWrap(false);
		mpAreaLabel.setStyleName("ltlfNormalText");

		Label effectiveLabel = new Label("Effective:");
		effectiveLabel.setStyleName("ltlfNormalBoldText");

		mpEffectiveLabel.setWordWrap(false);
		mpEffectiveLabel.setStyleName("ltlfNormalText");

		Label expiryLabel = new Label("Expiry:");
		expiryLabel.setStyleName("ltlfNormalBoldText");

		mpExpiryLabel.setWordWrap(false);
		mpExpiryLabel.setStyleName("ltlfNormalText");

		loadHistoryButton.setStyleName("button");
		loadHistoryButton.addClickListener(editLoadHistoryButtonClickListener);
		
		if (getMpSummaryStatus().equals("No")) {
			loadHistoryButton.setEnabled(false);
		}
		
		Button prevMpButton = new Button("<- Previous MP");
		prevMpButton.setStyleName("button");
		prevMpButton.addClickListener(prevMpBtnClickListener);

		Button nextMpButton = new Button("Next MP ->");
		nextMpButton.setStyleName("button");
		nextMpButton.addClickListener(nextMpBtnClickListener);

		headerPanel.add(analysisMpLabel);
		headerPanel.add(mpIdLabel);
		headerPanel.add(mpDescrLabel);
		headerPanel.add(areaLabel);
		headerPanel.add(mpAreaLabel);
		headerPanel.add(effectiveLabel);
		headerPanel.add(mpEffectiveLabel);
		headerPanel.add(expiryLabel);
		headerPanel.add(mpExpiryLabel);

		headerPanel.add(comparisonMpLabel);
		headerPanel.add(comparisonMpBox);
		
		headerPanel.add(loadHistoryButton);

		if (caller!=null) {
			headerPanel.add(prevMpButton);
			headerPanel.add(nextMpButton);
		}


		return headerPanel;
	}

	private Widget drawGraphControlPanel() {

		graphControlPanel.setStyleName("editLoadShapeGraphControlPanel");
		graphControlPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Label selectDateLabel = new Label("Unit range to analyze: ");
		selectDateLabel.setWordWrap(false);
		selectDateLabel.setStyleName("ltlfNormalBoldText");

		Label lowerLabel = new Label("Lower:");
		lowerLabel.setWordWrap(false);
		lowerLabel.setStyleName("ltlfNormalText");

		lowerRangeTb.setWidth("35px");

		Label toLabel = new Label(" To ");
		toLabel.setWordWrap(false);
		toLabel.setStyleName("ltlfNormalBoldText");

		Label upperLabel = new Label("Upper:");
		upperLabel.setWordWrap(false);
		upperLabel.setStyleName("ltlfNormalText");

		upperRangeTb.setWidth("35px");

		Label comparisonMpLabel = new Label("Comparison MP: ");
		comparisonMpLabel.setWordWrap(false);
		comparisonMpLabel.setStyleName("ltlfNormalBoldText");

		comparisonMpBox.setWidth("100px");

		graphOriginalCb.setStyleName("ltlfNormalText");
		graphOriginalCb.addClickListener(graphOriginalClickListener);


		Button regraphButton = new Button("Regraph");
		regraphButton.setStyleName("button");
		regraphButton.addClickListener(graphViewClickListener);

		graphControlPanel.add(selectDateLabel);
		graphControlPanel.add(lowerLabel);
		graphControlPanel.add(lowerRangeTb);
		graphControlPanel.add(toLabel);
		graphControlPanel.add(upperLabel);
		graphControlPanel.add(upperRangeTb);
		graphControlPanel.add(comparisonMpLabel);
		graphControlPanel.add(comparisonMpBox);
		graphControlPanel.add(graphOriginalCb);
		graphControlPanel.add(regraphButton);

		return graphControlPanel;
	}


	private Widget drawBatchOverridePanel () {
		HorizontalPanel batchOverridePanel = new HorizontalPanel();

		batchOverridePanel.setStyleName("editLoadShapeBatchOverridePanel");

		Label selectLabel = new Label("Select values for batch override:");
		selectLabel.setWordWrap(false);
		selectLabel.setStyleName("ltlfNormalBoldText");

		Label operLabel = new Label("Operand:");
		operLabel.setStyleName("ltlfNormalText");


		Label valueLabel = new Label("Value:");
		valueLabel.setStyleName("ltlfNormalText");

		rbGe.setStyleName("ltlfNormalText");
		rbLe.setStyleName("ltlfNormalText");
		rbGe.setChecked(true);

		overrideValueTb.setWidth("50px");

		Button updateButton = new Button("Update");
		updateButton.addClickListener(batchOverrideButtonClickListener);


		FlexTable batchOverrideTable = new FlexTable();
		FlexCellFormatter fcf = batchOverrideTable.getFlexCellFormatter();

		fcf.setColSpan(0, 0, 4);
		batchOverrideTable.setWidget(0, 0, selectLabel);

		fcf.setColSpan(1, 0, 4);
		batchOverrideTable.setHTML(1, 0, "&nbsp;");

		batchOverrideTable.setHTML(2, 0, "&nbsp;");

		fcf.setWidth(2, 1, "100px");
		fcf.setWidth(2, 2, "100px");

		batchOverrideTable.setWidget(2, 1, operLabel);
		batchOverrideTable.setWidget(2, 2, rbGe);

		batchOverrideTable.setHTML(3, 0, "&nbsp;");
		batchOverrideTable.setHTML(3, 1, "&nbsp;");
		batchOverrideTable.setWidget(3, 2, rbLe);

		batchOverrideTable.setHTML(4, 0, "&nbsp;");
		batchOverrideTable.setWidget(4, 1, valueLabel);
		batchOverrideTable.setWidget(4, 2, overrideValueTb);

		fcf.setHorizontalAlignment(4, 3, HasHorizontalAlignment.ALIGN_RIGHT);
		batchOverrideTable.setWidget(4, 3, updateButton);


		batchOverridePanel.add(batchOverrideTable);

		return batchOverridePanel;

	}

	private Widget drawOffsetPanel () {
		
		VerticalPanel offsetPanel = new VerticalPanel();
		offsetPanel.setStyleName("editLoadShapeOffsetPanel");
		
		Label offsetLabel = new Label("Select values to offset: ");
		offsetLabel.setWordWrap(false);
		offsetLabel.setStyleName("ltlfNormalBoldText");
		
		
		FlexTable offsetTable = new FlexTable();
		FlexCellFormatter fcf = offsetTable.getFlexCellFormatter();

		fcf.setColSpan(0, 0, 4);
		offsetTable.setWidget(0, 0, offsetLabel);

		Label offsetUnitLabel = new Label("Offset Unit:");
		offsetUnitLabel.setWordWrap(false);
		offsetUnitLabel.setStyleName("ltlfNormalText");
		
		offsetTable.setHTML(1, 0, "&nbsp;");

		fcf.setWidth(1, 1, "100px");
		fcf.setWidth(1, 2, "100px");

		offsetTable.setWidget(1, 1, offsetUnitLabel);
		
		rbDays.setStyleName("ltlfNormalText");
		rbHours.setStyleName("ltlfNormalText");
		rbDays.setChecked(true);
		
		offsetTable.setWidget(1, 2, rbDays);
		offsetTable.setWidget(2, 2, rbHours);

		Label offsetValueLabel = new Label("Offset Value:");
		offsetValueLabel.setWordWrap(false);
		offsetValueLabel.setStyleName("ltlfNormalText");

		offsetTable.setWidget(3, 1, offsetValueLabel);

		offsetValueTb.setWidth("60px");
		
		offsetTable.setWidget(3, 2, offsetValueTb);
		
		Button updateButton = new Button("Update");
		updateButton.addClickListener(offsetButtonClickListener);
		
		fcf.setHorizontalAlignment(3, 3, HasHorizontalAlignment.ALIGN_RIGHT);
		offsetTable.setWidget(3, 3, updateButton);
		
		offsetPanel.add(offsetTable);
		
		return offsetPanel;
		
	}

	
	private Widget drawCopyPanel() {
		
		VerticalPanel copyPanel = new VerticalPanel();
		copyPanel.setStyleName("editLoadShapeCopyPanel");

		Label copyLabel = new Label("Select values to copy: ");
		copyLabel.setWordWrap(false);
		copyLabel.setStyleName("ltlfNormalBoldText");
		

		FlexTable copyTable = new FlexTable();
		FlexCellFormatter fcf = copyTable.getFlexCellFormatter();

		fcf.setColSpan(0, 0, 3);
		copyTable.setWidget(0, 0, copyLabel);

		copyTable.setHTML(1, 0, "&nbsp;");

		Label sourceMpLabel = new Label("Source MP:");
		sourceMpLabel.setWordWrap(false);
		sourceMpLabel.setStyleName("ltlfNormalText");

		sourceMpBox.setWidth("100px");
		
		fromTb.setText("1");
		toTb.setText("365");
		
		copyTable.setWidget(1, 1, sourceMpLabel);
		copyTable.setWidget(1, 2, sourceMpBox);
		
		Button updateButton = new Button("Update");
		updateButton.addClickListener(copyButtonClickListener);
		
		copyTable.setWidget(1, 3, updateButton);
		
		copyPanel.add(copyTable);
		
		return copyPanel;
	}
	
	private Widget drawCommentsPanel() {
		
		VerticalPanel commentsPanel = new VerticalPanel();
		commentsPanel.setStyleName("editLoadShapeNotesPanel");

		Label commentsLabel = new Label("Load shape comments: ");
		commentsLabel.setWordWrap(false);
		commentsLabel.setStyleName("ltlfNormalBoldText");
		

		FlexCellFormatter fcf = commentsTable.getFlexCellFormatter();

		fcf.setColSpan(0, 0, 2);
		commentsTable.setWidget(0, 0, commentsLabel);

		commentsTable.setHTML(1, 0, "&nbsp;");
		commentsTextArea.setHeight("100px");
		commentsTextArea.setWidth("220px");

		commentsTable.setWidget(1, 1, commentsTextArea);

		commentsPanel.add(commentsTable);
		
		return commentsPanel;
	}



	private Widget drawButtonPanel () {

		HorizontalPanel buttonPanel = new HorizontalPanel();

		buttonPanel.setStyleName("editLoadShapeButtonPanel");
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		// buttons
		Button revertButton = new Button("Revert to Original");
		revertButton.setStyleName("button");
		revertButton.setWidth("125px");
		revertButton.addClickListener(revertToOriginalButtonClickListener);

		Button undoButton = new Button("Undo");
		undoButton.setStyleName("button");
		undoButton.setWidth("90px");
		undoButton.addClickListener(undoButtonClickListener);

		Button commitButton = new Button("Commit");
		commitButton.setStyleName("button");
		commitButton.setWidth("90px");
		commitButton.addClickListener(commitButtonClickListener);


		buttonPanel.add(revertButton);
		buttonPanel.add(undoButton);
		buttonPanel.add(commitButton);

		return buttonPanel;
	}

	private Date parseDate(String dateStr) {
		Date aDate = null;

		try {
			aDate = dateFmtSlash.parse(dateStr);
		} catch (IllegalArgumentException iae) {
			try {
				aDate = dateFmtDash.parse(dateStr);
			} catch (IllegalArgumentException iae2) {
				try {
					aDate = dateFmtNopunc.parse(dateStr);
				} catch (IllegalArgumentException iae3) {}

			}
		}
		return aDate;
	}

	private void applyBatchOverride() {

		double overrideValue = 0;
		String overrideStr = overrideValueTb.getText();
		boolean overrideTop = true;
		if (rbLe.isChecked()) {
			overrideTop = false;
		}

		try {
			overrideValue = Double.parseDouble(overrideStr);

			AsyncCallback callback = new AsyncCallback() {
				public void onSuccess(Object s) {
					regenerateShapeGraphs();
				}

				public void onFailure(Throwable ex) {
					Window.alert("Failed to apply batch override: " + ex.getMessage());
				}
			};

			setChartsLoading();
			rpcProxy.getLtlfService().applyBatchOverride(baseYear, mpOid, versionId, overrideTop, overrideValue, callback);

		} catch (NumberFormatException nfe) {
			Window.alert("Invalid override value");
		}

	}

	private void applyOffset() {

		int offsetValue = 0;
		String offsetStr = offsetValueTb.getText();
		String offsetUnit = "";

		if (rbDays.isChecked()) {
			offsetUnit = "DAYS";
		}

		try {
			offsetValue = Integer.parseInt(offsetStr);

			AsyncCallback callback = new AsyncCallback() {
				public void onSuccess(Object s) {
					regenerateShapeGraphs();
				}

				public void onFailure(Throwable ex) {
					Window.alert(ex.getMessage());
				}
			};

			setChartsLoading();
			rpcProxy.getLtlfService().applyOffset(baseYear, mpOid, versionId, offsetUnit, offsetValue, callback);

		} catch (NumberFormatException nfe) {
			Window.alert("Invalid offset value");
		}

	}

	private void copy() {

		String sourceMp = sourceMpBox.getText();
		String baseDayStartStr = fromTb.getText();
		String baseDayEndStr = toTb.getText();

		int baseDayStart = 1;
		int baseDayEnd = 365;

		try {

			if (sourceMp==null || sourceMp.length()==0) {
				Window.alert("Source MP is required");
				return;
			}

			baseDayStart = Integer.parseInt(baseDayStartStr);
			baseDayEnd = Integer.parseInt(baseDayEndStr);

			if (baseDayStart>baseDayEnd) {
				Window.alert("Base Day End must be after Base Day Start");
				return;
			}

			Long sourceMpOid = null;
			Object sourceMpObj = mpMap.get(sourceMp);
			if (sourceMpObj!=null) {
				sourceMpOid = ((MeasurementPoint)sourceMpObj).getOid();
			}


			AsyncCallback callback = new AsyncCallback() {
				public void onSuccess(Object s) {
					regenerateShapeGraphs();
				}

				public void onFailure(Throwable ex) {
					Window.alert(ex.getMessage());
				}
			};

			setChartsLoading();
			rpcProxy.getLtlfService().copyLoadShape(baseYear, mpOid, versionId, sourceMpOid, baseDayStart, baseDayEnd, callback);

		} catch (NumberFormatException nfe) {
			Window.alert("Invalid From Base Day or To Base Day value");
		}

	}

    private void revertToOriginal() {

    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			regenerateShapeGraphs();
    		}

    		public void onFailure(Throwable ex) {
    			// do nothing for now
    			Window.alert("Failed to revert to original");
    		}
    	};

		setChartsLoading();
    	rpcProxy.getLtlfService().revertToOriginalLoadShape(baseYear, mpOid, versionId, callback);
    }

    private void undo() {

    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			regenerateShapeGraphs();
    		}

    		public void onFailure(Throwable ex) {
    			// do nothing for now
    			Window.alert("Failed to undo: " + ex.getMessage());
    		}
    	};

		setChartsLoading();
    	rpcProxy.getLtlfService().undoLoadShapeFixes(baseYear, mpOid, versionId, callback);

    }

    private void commit() {

    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			regenerateShapeGraphs();
				overrideValueTb.setText("");
				rbGe.setChecked(true);
				offsetValueTb.setText("");
				rbDays.setChecked(true);
				sourceMpBox.setText("");
				fromTb.setText("1");
				toTb.setText("365");
    		}

    		public void onFailure(Throwable ex) {
    			// do nothing for now
    			Window.alert("Failed to commit: " + ex.getMessage());
    		}
    	};

    	String comments = commentsTextArea.getText();
    	summ.setComments(comments);
    	rpcProxy.getLtlfService().commitLoadShapeFixes(baseYear, mpOid, versionId, summ, callback);

    }

	private void setChartsLoading() {
		loadShapeChart.setLoadingUrl(GWT.getModuleBaseURL() + "images/graph-loading.gif");
		hourlyChart.setLoadingUrl(GWT.getModuleBaseURL() + "images/graph-loading.gif");
	}

	private void getShapeSummary() {
		
		AsyncCallback callback = new AsyncCallback() {
			public void onSuccess(Object s) {
				summ = (LoadShapeSummary)s;
				commentsTextArea.setText(summ.getComments());
				
			}

			public void onFailure(Throwable ex) {
				Window.alert("Failed to get shape summary: " + ex.getMessage());
			}
		};
		
		rpcProxy.getLtlfService().getShapeSummary(mpOid, callback);
	}
	
//	private void regenGraphs() {
//		boolean graphOriginal = false;
//		if (graphOriginalCb.isChecked())
//			graphOriginal = true;
//
//		regenHourlyShapeGraph(graphOriginal);
//	}

	private void regenerateShapeGraphs() {

		boolean graphOriginal = false;
		if (graphOriginalCb.isChecked())
			graphOriginal = true;
		
		String lowerRangeStr = lowerRangeTb.getText();
		String upperRangeStr = upperRangeTb.getText();

		try {
			final double lowerUnitRange = Double.parseDouble(lowerRangeStr);
			final double upperUnitRange = Double.parseDouble(upperRangeStr);


			if (lowerUnitRange<0 || upperUnitRange<0 || upperUnitRange < lowerUnitRange) {
				Window.alert("Invalid lower or upper range, or upper range not greater than lower range");
			} else {

				String comparisonMp = comparisonMpBox.getText();

				if (comparisonMp!=null && comparisonMp.length()>0) {
					Object compMpObj = mpMap.get(comparisonMp);
					if (compMpObj!=null) {
						comparisonMpOid = ((MeasurementPoint)compMpObj).getOid();
					} else
						comparisonMpOid=null;
				} else
					comparisonMpOid=null;

				AsyncCallback callback = new AsyncCallback() {
					public void onSuccess(Object s) {
						String[] chartNames = (String[])s;
						String hourlyImageUrl = GWT.getModuleBaseURL() + "displayChart?filename=" + chartNames[0];
						hourlyChart.setChartUrl(hourlyImageUrl);
						String shapeImageUrl = GWT.getModuleBaseURL() + "displayChart?filename=" + chartNames[1];
						loadShapeChart.setChartUrl(shapeImageUrl);
					}

					public void onFailure(Throwable ex) {
						Window.alert("Failed to generate shape graphs: " + ex.getMessage());
					}
				};

				rpcProxy.getLtlfService().generateShapeGraphs(baseYear, mpOid, versionId, comparisonMpOid, graphOriginal, lowerUnitRange, upperUnitRange, callback);
			}
		} catch (NumberFormatException ne) {
			Window.alert("Invalid lower or upper unit range");
		}
	}
	
//	private void regenHourlyShapeGraph(final boolean graphOriginal) {
//
//		String comparisonMp = comparisonMpBox.getText();
//
//		if (comparisonMp!=null && comparisonMp.length()>0) {
//			Object compMpObj = mpMap.get(comparisonMp);
//			if (compMpObj!=null) {
//				comparisonMpOid = ((MeasurementPoint)compMpObj).getOid();
//			} else
//				comparisonMpOid=null;
//		} else
//			comparisonMpOid=null;
//
//		AsyncCallback callback = new AsyncCallback() {
//			public void onSuccess(Object s) {
//				String chartName = (String)s;
//				String imageUrl = GWT.getModuleBaseURL() + "displayChart?filename=" + chartName;
//				hourlyChart.setChartUrl(imageUrl);
//				regenLoadShapeGraph(graphOriginal);
//			}
//
//			public void onFailure(Throwable ex) {
//				Window.alert("Failed to generate Hourly Shape graph: " + ex.getMessage());
//			}
//		};
//
//		rpcProxy.getLtlfService().graphHourlyShape(baseYear, mpOid, versionId, comparisonMpOid, graphOriginal, callback);
//		
//	}
//
//	private void regenLoadShapeGraph(boolean graphOriginal) {
//
//		String lowerRangeStr = lowerRangeTb.getText();
//		String upperRangeStr = upperRangeTb.getText();
//
//		try {
//			final double lowerUnitRange = Double.parseDouble(lowerRangeStr);
//			final double upperUnitRange = Double.parseDouble(upperRangeStr);
//
//
//			if (lowerUnitRange<0 || upperUnitRange<0 || upperUnitRange < lowerUnitRange) {
//				Window.alert("Invalid lower or upper range, or upper range not greater than lower range");
//			} else {
//
//				AsyncCallback callback = new AsyncCallback() {
//					public void onSuccess(Object s) {
//						String chartName = (String)s;
//						String imageUrl = GWT.getModuleBaseURL() + "displayChart?filename=" + chartName;
//						loadShapeChart.setChartUrl(imageUrl);
//					}
//
//					public void onFailure(Throwable ex) {
//						Window.alert("Failed to generate Load Shape graph: " + ex.getMessage());
//					}
//				};
//
//				rpcProxy.getLtlfService().graphLoadShape(baseYear, mpOid, versionId, comparisonMpOid, graphOriginal, lowerUnitRange, upperUnitRange, callback);
//			}
//		} catch (NumberFormatException ne) {
//			Window.alert("Invalid lower or upper unit range");
//		}
//	}


}
