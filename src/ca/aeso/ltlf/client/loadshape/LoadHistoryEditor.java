package ca.aeso.ltlf.client.loadshape;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ca.aeso.ltlf.client.event.*;
import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.StartsWithSuggestOracle;
import ca.aeso.ltlf.model.GapSummaryValue;
import ca.aeso.ltlf.model.MeasurementPoint;
import ca.aeso.ltlf.model.AnalysisDetail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.util.DateUtil;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.form.DateField;

/**
 * LoadHistoryEditor
 * This UI allows editing of historical DSM data for a measurement point
 *
 * @author mbodor
 */
public class LoadHistoryEditor extends LtlfComposite {

	private final static String DATE_FIELD_FORMAT = "Y-m-d";

	LoadHistoryEditor _instance;
	ImportLoadHistory caller;
	GapSummaryValue summary;
	private Map mpMap;
	private Map mpOidMap;
	LoadHistoryChart chart = new LoadHistoryChart();
	String mpName = "";
	Long mpOid;
	String analysisStartStr;
	String analysisEndStr;
	Date analysisStart;
	Date analysisEnd;
	Label mpIdLabel = new Label();
	Label mpDescription = new Label();
	Label mpArea = new Label();
	Label mpEffective = new Label();
	Label mpExpiry = new Label();
	Label prevLoadFactor = new Label("");
	Label baseLoadFactor = new Label("");

	StartsWithSuggestOracle mpOracle = new StartsWithSuggestOracle();
	SuggestBox comparisonMpBox = new SuggestBox(mpOracle);
	private List<MeasurementPoint> measurementPoints;

	HorizontalPanel graphControlPanel = new HorizontalPanel();
	CheckBox graphOriginalCb = new CheckBox("Graph original");

	private DateField graphStartDatePicker;
	private DateField graphEndDatePicker;
	private DateField fixStartDatePicker;
	private DateField fixEndDatePicker;
	private DateField sourceStartDatePicker;
	private DateField sourceEndDatePicker;
	private DateField fineFixStartDatePicker;
	private DateField smoothStartDatePicker;
	private DateField smoothEndDatePicker;

	ListBox fixStartHe = new ListBox();
	ListBox fixEndHe = new ListBox();

	SuggestBox sourceMpBox = new SuggestBox(mpOracle);
	ListBox sourceStartHe = new ListBox();
	ListBox sourceEndHe = new ListBox();

	LoadHistoryFineFix fineFix = new LoadHistoryFineFix();

	RadioButton smoothMaxMinRb = new RadioButton("smoothGroup", "Absolute");
	TextBox smoothMaxTextBox = new TextBox();
	TextBox smoothMinTextBox = new TextBox();
	RadioButton smoothToleranceRb = new RadioButton("smoothGroup", "Tolerance");
	ListBox smoothToleranceListBox = new ListBox();
	ListBox smoothTrendListBox = new ListBox();

	CheckBox readyCb = new CheckBox("Ready");
	Button saveReadyButton = new Button("Save");

	private static final String LISTBOX_SIZE = "150px";
	protected static final String[] HOURS = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };

	private DateTimeFormat dateFmtSlash = DateTimeFormat.getFormat("yyyy/MM/dd");
	private DateTimeFormat dateFmtDash = DateTimeFormat.getFormat("yyyy-MM-dd");
	private DateTimeFormat dateFmtNopunc = DateTimeFormat.getFormat("yyyyMMdd");

	ClickListener editLoadShapeBtnClickListener = new ClickListener() {
		public void onClick(Widget sender) {
	    	fireLtlfCompositeEvent(new LtlfCompositeEvent(this, LtlfCompositeEvent.EventCode.EditLoadShape, new Object[]{mpOid.toString(), "1"}));
		}
	};

	ClickListener prevMpBtnClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			scrollMp(false);
		}
	};

	ClickListener nextMpBtnClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			scrollMp(true);
		}
	};


	ClickListener readyCbClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			saveReadyButton.setEnabled(true);
		}
	};

	ClickListener graphOriginalClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			regraphClickListener.onClick(sender);
		}
	};

	ClickListener regraphClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			regenGraph(true);
		}
	};
	ClickListener zoomInClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			zoom(true, false);
		}
	};

	ClickListener zoomOutClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			zoom(false, false);
		}
	};

	ClickListener zoomOutFullClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			zoom(false, true);
		}
	};

	ClickListener scrollLeftClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			scroll(true);
		}
	};

	ClickListener scrollRightClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			scroll(false);
		}
	};

	ClickListener coarseUpdateButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			addCoarseFix();
		}
	};

	ClickListener smoothOutlierButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			smoothOutliers();
		}
	};

	ClickListener fineUpdateButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			addFineFix();
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

	ClickListener saveReadyBtnClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			saveReadyStatus();
		}
	};

	private DateField createDateField()
	{
		DateField dateField = new DateField();
		dateField.setFormat(LoadHistoryEditor.DATE_FIELD_FORMAT);
		dateField.setMinValue(DateUtil.parseDate("2000-01-01", LoadHistoryEditor.DATE_FIELD_FORMAT));
		dateField.setValidationEvent(false);
		dateField.setFieldMsgTarget("qtip");
		return dateField;
	}

	public LoadHistoryEditor(Long mpOid, String analysisStartStr, String analysisEndStr, ImportLoadHistory caller)
	{
		_instance = this;
		this.caller = caller;
		this.mpOid = mpOid;
		this.summary = caller.getSummary(mpOid);

		this.analysisStartStr = analysisStartStr;
		this.analysisEndStr = analysisEndStr;
		analysisStart = parseDate(analysisStartStr);
		analysisEnd = parseDate(analysisEndStr);

		graphStartDatePicker = createDateField();
		graphStartDatePicker.setValue(analysisStartStr);
		graphEndDatePicker = createDateField();
		graphEndDatePicker.setValue(analysisEndStr);
		fixStartDatePicker = createDateField();
		fixStartDatePicker.setValue(analysisStartStr);
		fixEndDatePicker = createDateField();
		fixEndDatePicker.setValue(analysisEndStr);
		sourceStartDatePicker = createDateField();
		sourceStartDatePicker.setValue(analysisStartStr);
		sourceEndDatePicker = createDateField();
		sourceEndDatePicker.setValue(analysisEndStr);
		fineFixStartDatePicker = createDateField();
		fineFixStartDatePicker.setValue(analysisStartStr);
		fineFixStartDatePicker.addKeyListener(13/*Enter*/, new KeyListener() {
			public void onKey(int keyCode, EventObject e)
			{
				if (fineFixStartDatePicker.isValid())
					loadFineFixTable();
			}
		});
		smoothStartDatePicker = createDateField();
		smoothStartDatePicker.setValue(analysisStartStr);
		smoothEndDatePicker = createDateField();
		smoothEndDatePicker.setValue(analysisEndStr);

		VerticalPanel mpAnalysisPanel = new VerticalPanel();

		mpAnalysisPanel.setStyleName("mpAnalysisPanel");

		// prePopulate static data
		prePopulate();

		// graph panel
		mpAnalysisPanel.add(drawHeaderPanel());

		mpAnalysisPanel.add(drawBodyPanel());

		mpAnalysisPanel.add(drawFooterPanel());

		setChartLoading();
		regenGraph(true);

		initWidget(mpAnalysisPanel);
	}

	private void prePopulate() {
		loadMpList();
	}

	private void loadMpList() {
		GWT.log("loadMpList()", null);

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
					updateMpFields();

				}
			}

			public void onFailure(Throwable caught) {
				measurementPoints = null;
			}
		};

		rpcProxy.getLtlfService().getMeasurementPoints(callback);
	}

	private void updateMpFields() {

		if (mpOidMap!=null) {
			MeasurementPoint measP = (MeasurementPoint)mpOidMap.get(mpOid);

			mpDescription.setText(measP.getDescription());
			mpArea.setText(measP.getZoneCode());
			mpEffective.setText(dateFmtSlash.format(measP.getMPEffectiveDate()));
			mpExpiry.setText(dateFmtSlash.format(measP.getMPExpireDate()));

			mpName = measP.getName();
			sourceMpBox.setText(mpName);
			mpIdLabel.setText(mpName);

		}

		if (summary!=null) {
			Double prevLf = summary.getPrevYearLoadFactor();
			prevLoadFactor.setText((prevLf==null?"":prevLf.toString()));
			Double baseLf = this.summary.getBaseYearLoadFactor();
			baseLoadFactor.setText((baseLf==null?"":baseLf.toString()));

			String readyString = summary.getReadyString();
			if (readyString!=null && readyString.equals("T")) {
				readyCb.setChecked(true);
			} else {
				readyCb.setChecked(false);
			}
		}

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

		mpDescription.setWordWrap(false);
		mpDescription.setStyleName("ltlfNormalText");

		Label areaLabel = new Label("Area:");
		areaLabel.setStyleName("ltlfNormalBoldText");

		mpArea.setWordWrap(false);
		mpArea.setStyleName("ltlfNormalText");

		Label effectiveLabel = new Label("Effective:");
		effectiveLabel.setStyleName("ltlfNormalBoldText");

		mpEffective.setWordWrap(false);
		mpEffective.setStyleName("ltlfNormalText");

		Label expiryLabel = new Label("Expiry:");
		expiryLabel.setStyleName("ltlfNormalBoldText");

		mpExpiry.setWordWrap(false);
		mpExpiry.setStyleName("ltlfNormalText");


		Label prevLoadFactorLabel = new Label("Prev LF:");
		prevLoadFactorLabel.setWordWrap(false);
		prevLoadFactorLabel.setStyleName("ltlfNormalBoldText");

		prevLoadFactor.setWordWrap(false);
		prevLoadFactor.setStyleName("ltlfNormalText");

		Label baseLoadFactorLabel = new Label("Base LF:");
		baseLoadFactorLabel.setWordWrap(false);
		baseLoadFactorLabel.setStyleName("ltlfNormalBoldText");

		baseLoadFactor.setWordWrap(false);
		baseLoadFactor.setStyleName("ltlfNormalText");

		Button prevMpButton = new Button("<- Prev MP");
		prevMpButton.setStyleName("button");
		prevMpButton.addClickListener(prevMpBtnClickListener);

		Button nextMpButton = new Button("Next MP ->");
		nextMpButton.setStyleName("button");
		nextMpButton.addClickListener(nextMpBtnClickListener);

		readyCb.setStyleName("ltlfNormalText");
		readyCb.addClickListener(readyCbClickListener);

		saveReadyButton.setStyleName("button");
		saveReadyButton.addClickListener(saveReadyBtnClickListener);
		saveReadyButton.setEnabled(false);

		Button loadShapeButton = new Button("Edit Load Shape");
		loadShapeButton.setStyleName("button");
		loadShapeButton.addClickListener(editLoadShapeBtnClickListener);
		
		headerPanel.add(analysisMpLabel);
		headerPanel.add(mpIdLabel);
		headerPanel.add(mpDescription);
		headerPanel.add(areaLabel);
		headerPanel.add(mpArea);
		headerPanel.add(effectiveLabel);
		headerPanel.add(mpEffective);
		headerPanel.add(expiryLabel);
		headerPanel.add(mpExpiry);
		headerPanel.add(prevLoadFactorLabel);
		headerPanel.add(prevLoadFactor);
		headerPanel.add(baseLoadFactorLabel);
		headerPanel.add(baseLoadFactor);
		headerPanel.add(readyCb);
		headerPanel.add(saveReadyButton);
		headerPanel.add(loadShapeButton);

		if (caller!=null) {
			headerPanel.add(prevMpButton);
			headerPanel.add(nextMpButton);
		}


		return headerPanel;
	}

	private Widget drawBodyPanel () {

		VerticalPanel bodyPanel = new VerticalPanel();
		bodyPanel.setStyleName("mpAnalysisBodyPanel");
		bodyPanel.add(drawGraphHeaderPanel());
		bodyPanel.setCellHorizontalAlignment(graphControlPanel, HasHorizontalAlignment.ALIGN_LEFT);

		HorizontalPanel hp = new HorizontalPanel();

		VerticalPanel vp = new VerticalPanel();

		vp.add(chart);
		vp.add(drawBottomFixPanel());

		hp.add(vp);
		hp.add(drawFineFixPanel());

		bodyPanel.add(hp);
		return bodyPanel;
	}

	private HorizontalPanel drawGraphHeaderPanel() {

		graphControlPanel.setStyleName("mpAnalysisGraphControlPanel");
		graphControlPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		graphOriginalCb.setStyleName("ltlfNormalText");
		graphOriginalCb.addClickListener(graphOriginalClickListener);

		Label selectDateLabel = new Label("Graph range: ");
		selectDateLabel.setWordWrap(false);
		selectDateLabel.setStyleName("ltlfNormalBoldText");

		Label startDateLabel = new Label("Start:");
		startDateLabel.setStyleName("ltlfNormalText");

		Label endDateLabel = new Label("End:");
		endDateLabel.setStyleName("ltlfNormalText");

		Label comparisonMpLabel = new Label("Comparison MP: ");
		comparisonMpLabel.setWordWrap(false);
		comparisonMpLabel.setStyleName("ltlfNormalBoldText");

		comparisonMpBox.setWidth("100px");

		Button regraphButton = new Button("Regraph");
		regraphButton.setStyleName("button");
		regraphButton.addClickListener(regraphClickListener);

		Button zoomInButton = new Button("+");
		zoomInButton.setStyleName("button");
		zoomInButton.setTitle("Zoom in");
		zoomInButton.addClickListener(zoomInClickListener);

		Button zoomOutButton = new Button("-");
		zoomOutButton.setStyleName("button");
		zoomOutButton.setTitle("Zoom out");
		zoomOutButton.addClickListener(zoomOutClickListener);

		Button scrollLeftButton = new Button("<-");
		scrollLeftButton.setStyleName("button");
		scrollLeftButton.addClickListener(scrollLeftClickListener);

		Button scrollRightButton = new Button("->");
		scrollRightButton.setStyleName("button");
		scrollRightButton.addClickListener(scrollRightClickListener);

		Button zoomOutFullButton = new Button("Zoom out full");
		zoomOutFullButton.setStyleName("button");
		zoomOutFullButton.addClickListener(zoomOutFullClickListener);

		graphControlPanel.add(selectDateLabel);
		graphControlPanel.add(startDateLabel);
		graphControlPanel.add(graphStartDatePicker);
		graphControlPanel.add(endDateLabel);
		graphControlPanel.add(graphEndDatePicker);
		graphControlPanel.add(comparisonMpLabel);
		graphControlPanel.add(comparisonMpBox);
		graphControlPanel.add(graphOriginalCb);
		graphControlPanel.add(regraphButton);
		graphControlPanel.add(zoomInButton);
		graphControlPanel.add(zoomOutButton);
		graphControlPanel.add(scrollLeftButton);
		graphControlPanel.add(scrollRightButton);
		graphControlPanel.add(zoomOutFullButton);

		return graphControlPanel;
	}

	private Widget drawBottomFixPanel () {

		FlexTable fixTable = new FlexTable();
		fixTable.setWidth("1050px");
		FlexCellFormatter cf = fixTable.getFlexCellFormatter();
		cf.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cf.setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT);

		fixTable.setWidget(0, 0, drawCoarseFixPanel());
		fixTable.setHTML(0, 1, "&nbsp;");
		fixTable.setWidget(0, 2, drawSmoothOutlierPanel());

		return fixTable;
	}

	private Widget drawCoarseFixPanel () {
		HorizontalPanel coarseFixPanel = new HorizontalPanel();
		coarseFixPanel.setStyleName("coarseFixPanel");

		Label selectLabel = new Label("Select coarse-grained range to fix: ");
		selectLabel.setWordWrap(false);
		selectLabel.setStyleName("ltlfNormalBoldText");

		Label startLabel = new Label("Start: ");
		startLabel.setStyleName("ltlfNormalText");

		Label endLabel = new Label("End: ");
		endLabel.setStyleName("ltlfNormalText");

		Label startHeLabel = new Label("HE: ");
		startHeLabel.setStyleName("ltlfNormalText");
		fixStartHe.setVisibleItemCount(1);

		Label endHeLabel = new Label("HE: ");
		endHeLabel.setStyleName("ltlfNormalText");
		fixEndHe.setVisibleItemCount(1);

		FlexTable rangeTable = new FlexTable();
		FlexCellFormatter rcf = rangeTable.getFlexCellFormatter();
		rcf.setColSpan(0, 0, 5);

		rangeTable.setWidget(0, 0, selectLabel);

		rangeTable.setWidget(1, 0, startLabel);
		rangeTable.setWidget(1, 1, fixStartDatePicker);

		rangeTable.setWidget(1, 2, startHeLabel);
		rangeTable.setWidget(1, 3, fixStartHe);

		rangeTable.setWidget(2, 0, endLabel);
		rangeTable.setWidget(2, 1, fixEndDatePicker);

		rangeTable.setWidget(2, 2, endHeLabel);
		rangeTable.setWidget(2, 3, fixEndHe);

		SimplePanel leftPanel = new SimplePanel();
		leftPanel.add(rangeTable);
		coarseFixPanel.add(leftPanel);

		Label sourceLabel = new Label("Select Source: ");
		sourceLabel.setStyleName("ltlfNormalBoldText");

		Label mpLabel = new Label("MP: ");
		mpLabel.setStyleName("ltlfNormalText");

		sourceMpBox.setWidth(LISTBOX_SIZE);

		Label sourceStartLabel = new Label("Start: ");
		sourceStartLabel.setStyleName("ltlfNormalText");

		Label sourceStartHeLabel = new Label("HE: ");
		sourceStartHeLabel.setStyleName("ltlfNormalText");

		sourceStartHe.setVisibleItemCount(1);

		Label sourceEndLabel = new Label("End: ");
		sourceEndLabel.setStyleName("ltlfNormalText");

		Label sourceEndHeLabel = new Label("HE: ");
		sourceEndHeLabel.setStyleName("ltlfNormalText");
		sourceEndHe.setVisibleItemCount(1);

		FlexTable sourceTable = new FlexTable();
		FlexCellFormatter scf = sourceTable.getFlexCellFormatter();
		scf.setColSpan(0, 0, 6);
		sourceTable.setWidget(0, 0, sourceLabel);

		sourceTable.setWidget(1, 0, mpLabel);
		scf.setColSpan(1, 1, 5);
		sourceTable.setWidget(1, 1, sourceMpBox);

		sourceTable.setWidget(2, 0, sourceStartLabel);
		sourceTable.setWidget(2, 1, sourceStartDatePicker);

		sourceTable.setWidget(2, 2, sourceStartHeLabel);
		sourceTable.setWidget(2, 3, sourceStartHe);

		sourceTable.setWidget(3, 0, sourceEndLabel);
		sourceTable.setWidget(3, 1, sourceEndDatePicker);

		sourceTable.setWidget(3, 2, sourceEndHeLabel);
		sourceTable.setWidget(3, 3, sourceEndHe);

		Button updateButton = new Button("Update");
		updateButton.addClickListener(coarseUpdateButtonClickListener);

		sourceTable.setWidget(3, 5, updateButton);

		SimplePanel rightPanel = new SimplePanel();
		rightPanel.add(sourceTable);
		coarseFixPanel.add(rightPanel);

		for (int i = 0; i < HOURS.length; i++) {
			fixStartHe.addItem(HOURS[i]);
			fixEndHe.addItem(HOURS[i]);
			sourceStartHe.addItem(HOURS[i]);
			sourceEndHe.addItem(HOURS[i]);
		}

		fixEndHe.setItemSelected(HOURS.length-1, true);
		sourceEndHe.setItemSelected(HOURS.length-1, true);

		return coarseFixPanel;
	}

	private Widget drawFineFixPanel () {

		VerticalPanel fineFixPanel = new VerticalPanel();
		fineFixPanel.setStyleName("fineFixPanel");

		FlexTable fineFixTable = new FlexTable();
		fineFixTable.setStyleName("fineFixTable");

		FlexCellFormatter cf = fineFixTable.getFlexCellFormatter();

		Label fixLabel = new Label("Select date for fine-grained fixes: ");
		fixLabel.setWordWrap(false);
		fixLabel.setStyleName("ltlfNormalBoldText");

		cf.setColSpan(0, 0, 3);

		fineFixTable.setWidget(0, 0, fixLabel);

		cf.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);

		fineFixTable.setWidget(1, 0, fineFixStartDatePicker);
		cf.setColSpan(1, 0, 3);
		cf.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);

		ScrollPanel ffScroll = new ScrollPanel();
		ffScroll.setStyleName("fineFixScrollPanel");
		ffScroll.add(fineFix);

		fineFixTable.setWidget(2, 0, ffScroll);
		cf.setColSpan(2, 0, 3);

		Button updateButton = new Button("Update");
		updateButton.setStyleName("button");
		updateButton.addClickListener(fineUpdateButtonClickListener);

		cf.setVerticalAlignment(3, 2, HasVerticalAlignment.ALIGN_BOTTOM);
		fineFixTable.setWidget(3, 2, updateButton);

		fineFixPanel.add(fineFixTable);

		return fineFixPanel;

	}

	private Widget drawSmoothOutlierPanel () {
		HorizontalPanel smoothPanel = new HorizontalPanel();
		smoothPanel.setStyleName("smoothPanel");

		Label selectLabel = new Label("Select parameters to smooth outliers: ");
		selectLabel.setWordWrap(false);
		selectLabel.setStyleName("ltlfNormalBoldText");


		Label smoothStartLabel = new Label("Start: ");
		smoothStartLabel.setStyleName("ltlfNormalText");

		Label smoothEndLabel = new Label("End:");
		smoothEndLabel.setStyleName("ltlfNormalText");

		smoothMaxMinRb.setStyleName("ltlfNormalBoldText");
		smoothToleranceRb.setStyleName("ltlfNormalBoldText");

		smoothMaxMinRb.setChecked(true);
		Label smoothMaxLabel = new Label("Max:");
		smoothMaxLabel.setStyleName("ltlfNormalText");
		Label smoothMinLabel = new Label("Min:");
		smoothMinLabel.setStyleName("ltlfNormalText");

		smoothMaxTextBox.setWidth("50px");
		smoothMinTextBox.setWidth("50px");

		Label toleranceLabel = new Label("%:");
		toleranceLabel.setWordWrap(false);
		toleranceLabel.setStyleName("ltlfNormalText");

		smoothToleranceListBox.setWidth("50px");
		smoothToleranceListBox.setVisibleItemCount(1);
		smoothToleranceListBox.addItem("50");
		smoothToleranceListBox.addItem("40");
		smoothToleranceListBox.addItem("30");
		smoothToleranceListBox.addItem("20");
		smoothToleranceListBox.addItem("10");
		smoothToleranceListBox.addItem("5");

		Label trendlabel = new Label("Trend:");
		trendlabel.setWordWrap(false);
		trendlabel.setStyleName("ltlfNormalText");

		smoothTrendListBox.setWidth("50px");
		smoothTrendListBox.setVisibleItemCount(1);
		smoothTrendListBox.addItem("1");
		smoothTrendListBox.addItem("4");
		smoothTrendListBox.addItem("7");
		smoothTrendListBox.addItem("14");
		smoothTrendListBox.addItem("21");

		FlexTable smoothTable = new FlexTable();
		FlexCellFormatter scf = smoothTable.getFlexCellFormatter();
		scf.setColSpan(0, 0, 8);
		smoothTable.setWidget(0, 0, selectLabel);

		smoothTable.setWidget(1, 0, smoothStartLabel);
		smoothTable.setWidget(1, 1, smoothStartDatePicker);

		scf.setVerticalAlignment(1, 2, HasVerticalAlignment.ALIGN_MIDDLE);
		smoothTable.setWidget(1, 2, smoothMaxMinRb);
		smoothTable.setWidget(1, 3, smoothMaxLabel);
		smoothTable.setWidget(1, 4, smoothMaxTextBox);
		scf.setAlignment(1, 5, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		smoothTable.setWidget(1, 5, smoothMinLabel);
		smoothTable.setWidget(1, 6, smoothMinTextBox);

		smoothTable.setWidget(2, 0, smoothEndLabel);
		smoothTable.setWidget(2, 1, smoothEndDatePicker);

		scf.setVerticalAlignment(2, 2, HasVerticalAlignment.ALIGN_MIDDLE);
		smoothTable.setWidget(2, 2, smoothToleranceRb);
		scf.setAlignment(2, 3, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		smoothTable.setWidget(2, 3, toleranceLabel);
		smoothTable.setWidget(2, 4, smoothToleranceListBox);
		smoothTable.setWidget(2, 5, trendlabel);
		smoothTable.setWidget(2, 6, smoothTrendListBox);

		Label notelabel = new Label("NOTE: Fixes limited to maximum of 39 per commit");
		notelabel.setStyleName("ltlfNormalText");

		scf.setColSpan(3, 0, 7);
		smoothTable.setWidget(3, 0, notelabel);

		Button updateButton = new Button("Update");
		updateButton.addClickListener(smoothOutlierButtonClickListener);
		scf.setAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_BOTTOM);
		smoothTable.setWidget(3, 1, updateButton);
		smoothPanel.add(smoothTable);

		return smoothPanel;
	}

	private Widget drawFooterPanel () {

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

		FlexTable footerTable = new FlexTable();
		FlexCellFormatter cf = footerTable.getFlexCellFormatter();

		cf.setWidth(0, 0, "450px");
		cf.setWidth(0, 1, "125px");
		cf.setWidth(0, 2, "90px");
		cf.setWidth(0, 3, "90px");
		footerTable.setHTML(0, 0, "&nbsp;");
		footerTable.setWidget(0, 1, revertButton);
		footerTable.setWidget(0, 2, undoButton);
		footerTable.setWidget(0, 3, commitButton);

		return footerTable;
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
				} catch (IllegalArgumentException iae3) {
					return null;
				}

			}
		}
		return aDate;
	}

	private void addCoarseFix() {

		Date fixStart = fixStartDatePicker.getValue();
		int fsHe = fixStartHe.getSelectedIndex()+1;
		Date fixEnd = fixEndDatePicker.getValue();
		int feHe = fixEndHe.getSelectedIndex()+1;

		String sourceMp = sourceMpBox.getText();
		Date sourceStart = sourceStartDatePicker.getValue();
		int ssHe = sourceStartHe.getSelectedIndex()+1;
		Date sourceEnd = sourceEndDatePicker.getValue();
		int seHe = sourceEndHe.getSelectedIndex()+1;

		if (fixStart!=null && fixEnd!=null && sourceStart!=null && sourceEnd!=null && sourceMp!=null && sourceEnd.compareTo(sourceStart)>=0 && fixEnd.compareTo(fixStart)>=0) {

			Long sourceMpOid = ((MeasurementPoint)mpMap.get(sourceMp)).getOid();

			AsyncCallback callback = new AsyncCallback() {
				public void onSuccess(Object s) {
					regenGraph(false);
				}

				public void onFailure(Throwable ex) {
					// do nothing for now
					Window.alert("Failed to add coarse fix: " + ex.getMessage());
				}
			};

			setChartLoading();
			rpcProxy.getLtlfService().addCoarseFix(mpOid, fixStart, fsHe, fixEnd, feHe, sourceMpOid, sourceStart, ssHe, sourceEnd, seHe, callback);

		} else {
			Window.alert("Invalid coarse fix entry");
		}
	}

	private void addFineFix() {

		List<AnalysisDetail> fineFixes = fineFix.getData();

		AsyncCallback callback = new AsyncCallback() {
			public void onSuccess(Object s) {
				regenGraph(false);
			}

			public void onFailure(Throwable ex) {
				// do nothing for now
				Window.alert("Failed to add fine fix: " + ex.getMessage());
			}
		};

		setChartLoading();
		rpcProxy.getLtlfService().addFineFix(mpOid, fineFixes, callback);
	}

    private void loadFineFixTable()
    {
    	Date fineFixStart = fineFixStartDatePicker.getValue();

    	if (fineFixStart!=null && (fineFixStart.compareTo(analysisStart)>=0) && (fineFixStart.compareTo(analysisEnd)<=0)) {

    		Long startTime = fineFixStart.getTime();
    		Long endTime = startTime + (1000*60*60*24);
    		Date fineFixEnd = new Date();
    		fineFixEnd.setTime(endTime);

    		AsyncCallback callback = new AsyncCallback() {
    			public void onSuccess(Object s) {
    				List<AnalysisDetail> loads = (List)s;
    				fineFix.loadData(loads);
    			}

    			public void onFailure(Throwable ex) {
    	    		Window.alert("Unable to load data into fine fix table: " + ex.getMessage());
    			}
    		};

    		rpcProxy.getLtlfService().getAnalysisValues(mpOid, fineFixStart, 1, fineFixEnd, 24, callback);
    	} else {
    		Window.alert("Fine fix date is invalid");
    	}
    }

	private void smoothOutliers() {

		Date smoothStart = smoothStartDatePicker.getValue();
		Date smoothEnd = smoothEndDatePicker.getValue();

		if (smoothStart!=null && smoothEnd!=null && smoothEnd.compareTo(smoothStart)>=0) {

			if (smoothMaxMinRb.isChecked()) {
				String maxStr = smoothMaxTextBox.getText();
				String minStr = smoothMinTextBox.getText();
				Double max=null;
				Double min=null;

				try {
					if (maxStr!=null && maxStr.length()>0)
						max = Double.parseDouble(maxStr);

					if (minStr!=null && minStr.length()>0)
						min = Double.parseDouble(minStr);

					AsyncCallback callback = new AsyncCallback() {
						public void onSuccess(Object s) {
							regenGraph(false);
						}

						public void onFailure(Throwable ex) {
							Window.alert("Failed to smooth outliers: " + ex.getMessage());
						}
					};

					setChartLoading();
					rpcProxy.getLtlfService().smoothOutliers(mpOid, smoothStart, smoothEnd, "Absolute", max, min, 0, 0, callback);


				} catch (NumberFormatException nfe) {
					Window.alert("Invalid max or min entry");
				}

			} else {
				String tolStr = smoothToleranceListBox.getItemText(smoothToleranceListBox.getSelectedIndex());
				String trendStr = smoothTrendListBox.getItemText(smoothTrendListBox.getSelectedIndex());


				int tolerance = 0;
				int trendDays = 0;

				try {
					tolerance = Integer.parseInt(tolStr);
					if (tolerance<1 || tolerance>100) {
						throw new NumberFormatException();
					}

					trendDays = Integer.parseInt(trendStr);
					if (trendDays<1 || trendDays>21) {
						throw new NumberFormatException();
					}

					AsyncCallback callback = new AsyncCallback() {
						public void onSuccess(Object s) {
							regenGraph(false);
						}

						public void onFailure(Throwable ex) {
							Window.alert("Failed to smooth outliers: " + ex.getMessage());
						}
					};

					setChartLoading();
					rpcProxy.getLtlfService().smoothOutliers(mpOid, smoothStart, smoothEnd, "Tolerance", null, null, tolerance, trendDays, callback);


				} catch (NumberFormatException nfe) {
					Window.alert("Invalid tolerance % or trend size entry");
				}



			}
		} else {
			Window.alert("Invalid start or end date entry, or start date is not less than end date");
		}
	}

    private void revertToOriginal() {

    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			summary = (GapSummaryValue)s;
    			caller.updateDisplay();
				updateMpFields();
				regenGraph(true);
    		}

    		public void onFailure(Throwable ex) {
    			// do nothing for now
    			Window.alert("Failed to revert to original");
    		}
    	};

		setChartLoading();
    	rpcProxy.getLtlfService().revertToOriginalMpAnalysis(mpOid, callback);
    }

    private void undo() {

    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
				regenGraph(false);
				loadFineFixTable();
    		}

    		public void onFailure(Throwable ex) {
    			// do nothing for now
    			Window.alert("Failed to undo");
    		}
    	};

		setChartLoading();
    	rpcProxy.getLtlfService().undoMpAnalysisFixes(mpOid, callback);
    }

    private void commit() {

    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			summary = (GapSummaryValue)s;
    			caller.updateDisplay();
				updateMpFields();
				regenGraph(true);
    		}

    		public void onFailure(Throwable ex) {
    			Window.alert("Failed to commit: " + ex.getMessage());
    		}
    	};

		setChartLoading();
    	rpcProxy.getLtlfService().commitMpAnalysisFixes(mpOid, callback);
    }

    private void saveReadyStatus() {

    	String ready = "F";

    	if (readyCb.isChecked()) {
    		ready = "T";
    	}

    	summary.setReadyString(ready);

    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			saveReadyButton.setEnabled(false);
    			caller.updateDisplay();
    		}

    		public void onFailure(Throwable ex) {
    			Window.alert("Failed to save ready status");
    		}
    	};

    	rpcProxy.getLtlfService().saveAnalysisSummary(summary, callback);
    }

    private void zoom(boolean zoomingIn, boolean zoomFull) {

		Date newStartDate = new Date();
		Date newEndDate = new Date();

    	if (zoomFull) {
    		newStartDate = analysisStart;
    		newEndDate = analysisEnd;
    	} else {
    		Date graphStart = graphStartDatePicker.getValue();
    		Date graphEnd = graphEndDatePicker.getValue();

    		long startTime = graphStart.getTime();
    		long endTime = graphEnd.getTime();
    		long diff = endTime - startTime;
    		long oneDay = 24*60*60*1000;

    		if (diff<oneDay) {
    			diff=oneDay;
    		}

    		long middle = startTime + (diff/2);
    		long newDiff = 0;

    		if (zoomingIn) {
    			newDiff = diff/4;
    		} else {
    			newDiff = diff * 4;
    		}

    		long newStartTime = middle - (newDiff/2);
    		long newEndTime = middle + (newDiff/2);

    		newStartDate.setTime(newStartTime);

    		if (newStartDate.compareTo(analysisStart)<0) {
    			newStartDate = analysisStart;
    		}

    		newEndDate.setTime(newEndTime);

    		if (newEndDate.compareTo(analysisEnd)>0) {
    			newEndDate = analysisEnd;
    		}

    	}

		graphStartDatePicker.setValue(newStartDate);
		graphEndDatePicker.setValue(newEndDate);

		regenGraph(true);
    }

    private void scroll(boolean scrollLeft) {

		Date graphStart = graphStartDatePicker.getValue();
		Date graphEnd = graphEndDatePicker.getValue();

		long startTime = graphStart.getTime();
		long endTime = graphEnd.getTime();
		long diff = endTime - startTime;
		long oneDay = 24*60*60*1000;

		if (diff<oneDay) {
			diff=oneDay;
		}

		long newStartTime = 0;
		long newEndTime = 0;

		if (scrollLeft) {
			newStartTime = startTime - diff;
			newEndTime = startTime;
		} else {
			newStartTime = endTime;
			newEndTime = endTime + diff;
		}

		Date newStartDate = new Date();
		newStartDate.setTime(newStartTime);

		if (newStartDate.compareTo(analysisStart)<0) {
			newStartDate = analysisStart;
			newEndTime = endTime;
		}

		Date newEndDate = new Date();
		newEndDate.setTime(newEndTime);

		if (newEndDate.compareTo(analysisEnd)>0) {
			newEndDate = analysisEnd;
			newStartTime = startTime;
			newStartDate.setTime(newStartTime);
		}

		graphStartDatePicker.setValue(newStartDate);
		graphEndDatePicker.setValue(newEndDate);

		regenGraph(true);
    }

    // scroll to previous or next MP
    private void scrollMp(boolean forward) {
    	int j = -1;
    	List summaries = caller.getLoadHistories();

    	if (summaries!=null) {
    		setChartLoading();
    		for (int i = 0; i < summaries.size(); i++) {
    			Long summaryMpOId = (Long)summaries.get(i);

    			if (summaryMpOId.equals(this.mpOid)) {
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

    		Long summaryMpOId = (Long)summaries.get(j);
    		this.mpOid = summaryMpOId;
    		updateMpFields();
    		graphStartDatePicker.setValue(analysisStartStr);
    		graphEndDatePicker.setValue(analysisEndStr);
    		regenGraph(true);
    	} else {
    		Window.alert("Could not retrieve next/previous MP");
    	}

    }

	private void setChartLoading() {
		chart.setImageUrl(GWT.getModuleBaseURL() + "images/graph-loading.gif");
	}

	private void regenGraph(final boolean resetInputFields) {

		setChartLoading();

		boolean graphOriginal = false;
		if (graphOriginalCb.isChecked())
			graphOriginal = true;

		Date graphStart = graphStartDatePicker.getValue();
		Date graphEnd = graphEndDatePicker.getValue();
		String comparisonMp = comparisonMpBox.getText();
		Long comparisonMpOid = null;

		if (graphStart!=null && graphEnd!=null && !graphStart.after(graphEnd)) {

			if (comparisonMp!=null && comparisonMp.length()>0) {
				Object compMpObj = mpMap.get(comparisonMp);
				if (compMpObj!=null) {
					comparisonMpOid = ((MeasurementPoint)compMpObj).getOid();
				}

			}

			AsyncCallback callback = new AsyncCallback() {
				public void onSuccess(Object s) {
					String chartName = (String)s;
					String imageUrl = GWT.getModuleBaseURL() + "displayChart?filename=" + chartName;
					chart.setImageUrl(imageUrl);
					if (resetInputFields) {
						fixStartDatePicker.setValue(graphStartDatePicker.getValue());
						fixEndDatePicker.setValue(graphEndDatePicker.getValue());
						fixStartHe.setItemSelected(0, true);
						fixEndHe.setItemSelected(fixEndHe.getItemCount()-1, true);
						sourceStartDatePicker.setValue(graphStartDatePicker.getValue());
						sourceEndDatePicker.setValue(graphEndDatePicker.getValue());
						sourceStartHe.setItemSelected(0, true);
						sourceEndHe.setItemSelected(sourceEndHe.getItemCount()-1, true);
						smoothStartDatePicker.setValue(graphStartDatePicker.getValue());
						smoothEndDatePicker.setValue(graphEndDatePicker.getValue());
						smoothMaxTextBox.setText("");
						smoothMinTextBox.setText("");
						smoothToleranceListBox.setSelectedIndex(0);
						smoothTrendListBox.setSelectedIndex(0);
						smoothMaxMinRb.setChecked(true);
			    		fineFixStartDatePicker.setValue(graphStartDatePicker.getValue());
						loadFineFixTable();
					}
				}

				public void onFailure(Throwable ex) {
					Window.alert("Failed to generate Load Shape graph: " + ex.getMessage());
				}
			};

			rpcProxy.getLtlfService().graphLoads(mpOid, comparisonMpOid, analysisStart, analysisEnd, graphOriginal, graphStart, 1, graphEnd, 24, callback);

		} else {
			Window.alert("Invalid start or end date, or end date is not later than start date");
		}
	}
}
