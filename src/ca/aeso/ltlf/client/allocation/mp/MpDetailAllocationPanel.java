package ca.aeso.ltlf.client.allocation.mp;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;

public class MpDetailAllocationPanel extends Panel
{
	private MpDetailAllocationGrid allocationGrid;
	
	public MpDetailAllocationPanel(int forecastYear, float allocatedEnergy)
	{
	  	this.setWidth(500);
		this.setCollapsible(true);
		this.setCollapsed(true);
	  	this.setTitle(forecastYear + " Allocated Energy = " + allocatedEnergy + " MWH");

		this.allocationGrid = new MpDetailAllocationGrid();
		this.add(this.allocationGrid);

		ToolbarButton addRowButton = new ToolbarButton("Add Row");
		addRowButton.setIconCls("add-icon");
		Toolbar bottomToolbar = new Toolbar();
		bottomToolbar.addButton(addRowButton);
		this.setTopToolbar(bottomToolbar);
	}
}
