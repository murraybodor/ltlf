package ca.aeso.ltlf.client.allocation.system;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.util.DateUtil;
import com.gwtext.client.widgets.form.DateField;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfGlobal;
import ca.aeso.ltlf.client.common.LtlfStyle;
import ca.aeso.ltlf.client.common.WaitButton;
import ca.aeso.ltlf.client.event.LtlfCompositeEvent;
import ca.aeso.ltlf.model.Allocation;

public class NewAllocationVersionForm extends LtlfComposite
{
	private final static String DATE_FIELD_FORMAT = "Y-m-d";
	private final static String END_DATE_FIELD_FORMAT = "Y";

	private NewAllocationVersionForm _instance;
	
	private com.gwtext.client.widgets.Window container;
	private Integer baseYear;
	private TextBox tbDescription;
	private DateField dtStartDate;
	private DateField dtEndDate;
	private WaitButton createButton;
	
	public NewAllocationVersionForm(com.gwtext.client.widgets.Window container, Integer baseYear)
	{
		_instance = this;
		
		this.container = container;
		this.baseYear = baseYear;
		
		FlexTable layoutGrid = new FlexTable();
		layoutGrid.setCellPadding(15);
		
		Label label = new Label();
		label.setStyleName(LtlfStyle.normalText);
		label.setText("Base Year:");
		layoutGrid.setWidget(0, 0, label);

		label = new Label();
		label.setStyleName(LtlfStyle.normalText);
		label.setText(String.valueOf(this.baseYear));
		layoutGrid.setWidget(0, 1, label);

		label = new Label();
		label.setStyleName(LtlfStyle.normalText);
		label.setText("Description:");
		layoutGrid.setWidget(1, 0, label);

		tbDescription = new TextBox();
		tbDescription.setStyleName(LtlfStyle.normalText);
		layoutGrid.setWidget(1, 1, tbDescription);

		Date now = new Date();
		Date endDate = DateUtil.add(now, DateUtil.YEAR, 20);
		String endDateText = DateTimeFormat.getFormat("yyyy").format(endDate);

		label = new Label();
		label.setStyleName(LtlfStyle.normalText);
		label.setText("Start Date:");
		layoutGrid.setWidget(2, 0, label);

		dtStartDate = createDateField();
		dtStartDate.setValue(new Date());
		layoutGrid.setWidget(2, 1, dtStartDate);
		
		label = new Label();
		label.setStyleName(LtlfStyle.normalText);
		label.setText("End Date:");
		layoutGrid.setWidget(3, 0, label);
		
		dtEndDate = createDateField();
		dtEndDate.setFormat(END_DATE_FIELD_FORMAT);
		dtEndDate.setValue(endDateText);
		dtEndDate.setInvalidText("it must be a four-digit year");
		layoutGrid.setWidget(3, 1, dtEndDate);
		
		// empty row/separator
		layoutGrid.getFlexCellFormatter().setColSpan(4, 0, 2);
		layoutGrid.setHTML(4, 0, "&nbsp;");

		createButton = new WaitButton();
		createButton.setStyleName(LtlfStyle.normalText);
		createButton.setText("Create");
		ClickListener clickListener = new ClickListener() {
			public void onClick(Widget sender)
			{
				createNewVersion();
			}
		};
		createButton.addClickListener(clickListener);
		layoutGrid.getFlexCellFormatter().setColSpan(5, 0, 2);
		layoutGrid.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_CENTER);
		layoutGrid.setWidget(5, 0, createButton);
		
		initWidget(layoutGrid);
	}

	private DateField createDateField()
	{
		DateField dateField = new DateField();
		dateField.setFormat(DATE_FIELD_FORMAT);
		dateField.setMinValue(DateUtil.parseDate("2000-01-01", DATE_FIELD_FORMAT));
		dateField.setValidationEvent(false);
		dateField.setFieldMsgTarget("qtip");
		return dateField;
	}
	
	private void createNewVersion()
	{
		if (tbDescription.getText()==null || tbDescription.getText().length()==0)
		{
			Window.alert("You must enter a description");
			return;
		}

		if (!this.dtStartDate.isValid())
		{
			Window.alert("Start date is NOT valid");
			return;
		}

		if (!this.dtEndDate.isValid())
		{
			Window.alert("End date is NOT valid");
			return;
		}

		if (this.dtStartDate.getValue().after(this.dtEndDate.getValue()))
		{
			Window.alert("Start date should be before end date");
			return;
		}

		AsyncCallback<Object> callback = new AsyncCallback<Object>() {
			public void onSuccess(Object result) {
				createButton.setWaiting(false);
				Long versionOid = (Long) result;
				if (versionOid == null || versionOid.longValue() < 1)
					Window.alert("Failed to create allocation version: Unknown error");
				else
					fireLtlfCompositeEvent(new LtlfCompositeEvent(_instance, LtlfCompositeEvent.EventCode.SystemAllocationVersionCreated, versionOid));
				container.close();
			}
			public void onFailure(Throwable caught) {
				createButton.setWaiting(false);
				Window.alert(caught.getMessage());
				LtlfGlobal.showDebugText("Failed to create allocation version: " + caught);
				container.close();
			}
		};
		
		Integer endYear = Integer.parseInt(DateUtil.format(dtEndDate.getValue(), "Y"));
		createButton.setWaiting(true);
		rpcProxy.getLtlfService().createAllocation(this.baseYear, dtStartDate.getValue(), endYear, tbDescription.getText(), callback);
	}
}
