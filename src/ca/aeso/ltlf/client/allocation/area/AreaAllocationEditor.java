package ca.aeso.ltlf.client.allocation.area;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.aeso.ltlf.client.common.*;
import ca.aeso.ltlf.client.event.LtlfCompositeEvent;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListener;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListenerAdapter;
import ca.aeso.ltlf.model.*;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.layout.*;
import com.gwtext.client.core.ExtElement;

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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * AreaAllocationEditor
 * This UI allows editing of allocation data by areas
 * 
 * @author mbodor
 */
public class AreaAllocationEditor extends LtlfComposite {
	
	AreaAllocationEditor _instance;
	
	Allocation allocation;
	Long allocationVersionOid;
	Label versionDescriptionLabel = new Label();
	ListBox customerSectorLb = new ListBox();
	List<CodesTable> customerSectors = new ArrayList<CodesTable>();
	Button saveButton = new Button("Save");
	AreaAllocationGrid areaAllocGrid;
	Panel gridPanel = new Panel();
	private ExtElement gridPanelElement = null;

	
	ChangeListener sectorChangeListener = new ChangeListener() {
		public void onChange(Widget sender) {
			String chosenSector = ((CodesTable)customerSectors.get(customerSectorLb.getSelectedIndex())).getStringValue();
			getAllocationSectors(allocationVersionOid, chosenSector);
		}
	};
	
	ClickListener saveButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			saveAllocation();
		}
	};
	
	public AreaAllocationEditor(Long allocationVersionOid, int startYear, int endYear)
	{
		_instance = this;
		
		this.allocationVersionOid = allocationVersionOid;
		
		areaAllocGrid = new AreaAllocationGrid(startYear, endYear);
		
		LtlfCompositeEventListener areaAllocGridEventListener = new LtlfCompositeEventListenerAdapter() {
			public void onEvent(LtlfCompositeEvent event)
			{
	    		fireLtlfCompositeEvent(event);
			}
		};

		areaAllocGrid.addLtlfCompositeEventListener(areaAllocGridEventListener);
		
		gridPanel.setHeight("647px");
		gridPanel.setWidth("1126px");
		gridPanel.setBorder(false);
		gridPanel.setPaddings(3);
		gridPanelElement = new ExtElement(gridPanel.getElement());
		
		prePopulate();

		
		VerticalPanel areaEditorPanel = new VerticalPanel();
		areaEditorPanel.add(drawHeaderPanel());
		areaEditorPanel.add(drawGridPanel());
		
		
		initWidget(areaEditorPanel);
	}

	private void prePopulate() {

		getAllocation(allocationVersionOid);
		
		getAllocationSectorTypes();
		
		getAllocationSectors(allocationVersionOid, "RES");
		
	}
	
	private Widget drawHeaderPanel() {
		
		Label versionLabel = new Label("Version:");
		versionLabel.setStyleName("ltlfNormalBoldText");
		
		versionDescriptionLabel.setWordWrap(false);
		versionDescriptionLabel.setStyleName("ltlfNormalText");
		
		Label sectorLabel = new Label("Customer Sector:");
		sectorLabel.setWordWrap(false);
		sectorLabel.setStyleName("ltlfNormalBoldText");
		
		customerSectorLb.setMultipleSelect(false);
		customerSectorLb.setVisibleItemCount(1);
		customerSectorLb.setWidth("100px");
		customerSectorLb.addChangeListener(sectorChangeListener);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setStyleName(LtlfStyle.areaAllocationHeader);
		
		hp.add(versionLabel);
		hp.add(versionDescriptionLabel);
		hp.add(sectorLabel);
		hp.add(customerSectorLb);
		
		return hp;
	}
	
	private HorizontalPanel drawGridPanel() {
		
		gridPanel.add(areaAllocGrid);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(gridPanel);
		
		saveButton.setWidth("100px");
		saveButton.addClickListener(saveButtonClickListener);
		
		hp.add(saveButton);
		hp.setCellHorizontalAlignment(saveButton, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setCellVerticalAlignment(saveButton, HasVerticalAlignment.ALIGN_BOTTOM);

		return hp;
	}
	
	private void saveAllocation() {

		saveButton.setEnabled(false);
		
		// save comments first
		List<AllocationComment> comments = areaAllocGrid.getChangedComments();
		
    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			areaAllocGrid.setChangedComments(new ArrayList());
    			saveButton.setEnabled(true);
    		}

    		public void onFailure(Throwable ex) {
    			Window.alert("Failed to save allocation comments: " + ex);
    			saveButton.setEnabled(true);
    		}
    	}; 

    	rpcProxy.getLtlfService().saveAllocationComments(comments, callback);
		
		
		
		// now save changed sectors
		List<AreaAllocationGridCell> cells = areaAllocGrid.getChangedGridCells();
		
		int i = 0;
		String[][] changes = new String[cells.size()][];
		
		for (Iterator iterator = cells.iterator(); iterator.hasNext();) {
			AreaAllocationGridCell cell = (AreaAllocationGridCell) iterator.next();
			String[] rec = new String[] {
				cell.getAreaSectorId(),
				cell.getPercentageValue().toString() 
			};
			changes[i++] = rec;
		}
		
    	AsyncCallback callback2 = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			areaAllocGrid.setChangedGridCells(new ArrayList());
    			Window.alert("Allocation saved");
    			saveButton.setEnabled(true);
    		}

    		public void onFailure(Throwable ex) {
    			Window.alert("Failed to save allocation: " + ex);
    			saveButton.setEnabled(true);
    		}
    	}; 

    	rpcProxy.getLtlfService().saveAllocationAreaSectors(changes, callback2);
		
	}

    private void getAllocation(Long allocationVersionOid) {

    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			allocation = (Allocation)s;
				versionDescriptionLabel.setText(allocation.getDescription());
				areaAllocGrid.setAllocation(allocation);
				
    		}

    		public void onFailure(Throwable ex) {
    			Window.alert("Failed to get allocation: " + ex.getMessage());
    		}
    	}; 

    	rpcProxy.getLtlfService().fetchAllocation(allocationVersionOid, callback);
    	
    }

    private void getAllocationSectors(Long allocationVersionOid, String sectorType) {

    	gridPanelElement.mask("Loading...");	
    	
    	areaAllocGrid.resetGrid();
    	
    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			String[][] allocationSectors = (String[][])s;
				areaAllocGrid.populate(allocationSectors);
		    	gridPanel.doLayout();
		    	gridPanelElement.unmask();	
    		}

    		public void onFailure(Throwable ex) {
    			Window.alert("Failed to get allocation: " + ex.getMessage());
    		}
    	}; 

    	rpcProxy.getLtlfService().fetchAllocationSectorsBySector(allocationVersionOid, sectorType, callback);
    	
    }
    
    private void getAllocationSectorTypes() {

    	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
    			customerSectors = (List<CodesTable>)s;
    			for (Iterator<CodesTable> iterator = customerSectors.iterator(); iterator.hasNext();) {
    				CodesTable customerSector = (CodesTable) iterator.next();
    				customerSectorLb.addItem(customerSector.getCodeDescription());
    			}
    		}

    		public void onFailure(Throwable ex) {
    			Window.alert("Failed to get allocation sector types: " + ex.getMessage());
    		}
    	}; 

    	rpcProxy.getLtlfService().getAllocationSectorTypes(callback);
    	
    }

}
