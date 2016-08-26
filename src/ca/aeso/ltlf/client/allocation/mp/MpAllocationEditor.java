package ca.aeso.ltlf.client.allocation.mp;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.widgets.Panel;

import ca.aeso.ltlf.client.common.*;
import ca.aeso.ltlf.model.*;

public class MpAllocationEditor extends LtlfComposite
{
	private Allocation allocation;
	private MpAllocationGrid mpAllocationGrid;
	private Label versionDescriptionLabel = new Label();
	private ListBox areaLb = new ListBox();
	private Panel gridPanel = new Panel();
	private ExtElement gridPanelElement = null;
	private Button saveButton = new Button("Save");
	private Long allocationOid;
	private String areaCode;
	
	private ChangeListener areaChangeListener = new ChangeListener() {
		public void onChange(Widget sender) {
			String chosenAreaCode = LtlfGlobal.getAreaList().get(areaLb.getSelectedIndex()).getCode();
			getAllocationMps(allocationOid, chosenAreaCode);
		}
	};
	
	private ClickListener saveButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
//			saveAllocation();
		}
	};
	
	
	public MpAllocationEditor(Long allocationOid, int startYear, int endYear, String areaCode)
	{
		this.allocationOid = allocationOid;
		this.areaCode = areaCode;
		
		this.mpAllocationGrid = new MpAllocationGrid(startYear, endYear);

		gridPanel.setHeight("640px");
		gridPanel.setWidth("1130px");
		gridPanel.setBorder(false);
		gridPanel.setPaddings(3);
		gridPanelElement = new ExtElement(gridPanel.getElement());
		
		prePopulate();
		
		VerticalPanel mpEditorPanel = new VerticalPanel();
		mpEditorPanel.add(drawHeaderPanel());
		mpEditorPanel.add(drawGridPanel());
		
		initWidget(mpEditorPanel);
	}
	
	private Widget drawHeaderPanel() {
		
		Label versionLabel = new Label("Version:");
		versionLabel.setStyleName("ltlfNormalBoldText");
		
		versionDescriptionLabel.setWordWrap(false);
		versionDescriptionLabel.setStyleName("ltlfNormalText");
		
		Label areaLabel = new Label("Area:");
		areaLabel.setWordWrap(false);
		areaLabel.setStyleName("ltlfNormalBoldText");
		
		areaLb.setMultipleSelect(false);
		areaLb.setVisibleItemCount(1);
		areaLb.setWidth("350px");
		areaLb.addChangeListener(areaChangeListener);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setStyleName(LtlfStyle.mpAllocationHeader);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		hp.add(versionLabel);
		hp.add(versionDescriptionLabel);
		hp.add(areaLabel);
		hp.add(areaLb);
		
		return hp;
	}
	
	private HorizontalPanel drawGridPanel() {
		
		gridPanel.add(mpAllocationGrid);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(gridPanel);
		
		saveButton.setWidth("100px");
		saveButton.addClickListener(saveButtonClickListener);
		
		hp.add(saveButton);
		hp.setCellHorizontalAlignment(saveButton, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setCellVerticalAlignment(saveButton, HasVerticalAlignment.ALIGN_BOTTOM);

		return hp;
	}

	private void prePopulate() {

		loadAreaListBox();
		getAllocation(allocationOid);
		getAllocationMps(allocationOid, areaCode);
		
	}
	
    private void getAllocation(Long allocationOid) {

    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			allocation = (Allocation)s;
				versionDescriptionLabel.setText(allocation.getDescription());
				mpAllocationGrid.setAllocation(allocation);
				
    		}

    		public void onFailure(Throwable ex) {
    			Window.alert("Failed to get allocation: " + ex.getMessage());
    		}
    	}; 

    	rpcProxy.getLtlfService().fetchAllocation(allocationOid, callback);
    	
    }
	
    private void getAllocationMps(Long allocationVersionOid, final String areaCode) {

    	gridPanelElement.mask("Loading...");	
    	
    	mpAllocationGrid.resetGrid();
    	
    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			String[][] allocationMps = (String[][])s;
    			mpAllocationGrid.populate(allocationMps, areaCode);
		    	gridPanel.doLayout();
		    	gridPanelElement.unmask();	
    		}

    		public void onFailure(Throwable ex) {
    			Window.alert("Failed to get allocation: " + ex.getMessage());
    		}
    	}; 

    	rpcProxy.getLtlfService().fetchAllocationMpsByArea(allocationVersionOid, areaCode, callback);
    	
    }
	
    private void loadAreaListBox() {
    	
    	int areaCodeIndex = 0;
    	int i = 0;
    	
    	for (Iterator iterator = LtlfGlobal.getAreaList().iterator(); iterator.hasNext();) {
			Area area = (Area) iterator.next();
			
			if (area.getCode().equals(this.areaCode))
				areaCodeIndex = i;
			
	    	areaLb.addItem(area.getCode() + " - " + area.getName());
			i++;
		}

    	areaLb.setItemSelected(areaCodeIndex, true);
    }
}
