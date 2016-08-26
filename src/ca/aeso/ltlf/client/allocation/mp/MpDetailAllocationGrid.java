package ca.aeso.ltlf.client.allocation.mp;

import com.google.gwt.user.client.ui.FlexTable;
import com.gwtext.client.widgets.Panel;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfStyle;

public class MpDetailAllocationGrid extends LtlfComposite
{
	private FlexTable grid;
	
	public MpDetailAllocationGrid()
	{
		this.grid = new FlexTable();
		this.grid.setStyleName(LtlfStyle.mpDetailGrid);
		this.grid.addStyleName(LtlfStyle.normalText);
		
		// header
		this.grid.setText(0, 0, "From Date");
		this.grid.setText(0, 1, "To Date");
		this.grid.setText(0, 2, "Peak MW");
		this.grid.setText(0, 3, "%");
		this.grid.setText(0, 4, "Energy MWH");

		this.grid.setText(1, 0, "2008-01-01");
		this.grid.setText(1, 1, "2008-01-31");
		this.grid.setText(1, 2, "0");
		this.grid.setText(1, 3, "0");
		this.grid.setText(1, 4, "0");

		this.grid.setText(2, 0, "2008-02-01");
		this.grid.setText(2, 1, "2008-06-30");
		this.grid.setText(2, 2, "2");
		this.grid.setText(2, 3, "15");
		this.grid.setText(2, 4, "72.75");

		this.grid.setText(3, 0, "2008-07-01");
		this.grid.setText(3, 1, "2008-12-31");
		this.grid.setText(3, 2, "7");
		this.grid.setText(3, 3, "85");
		this.grid.setText(3, 4, "414.75");

		this.grid.setText(4, 0, "");
		this.grid.setText(4, 1, "");
		this.grid.setText(4, 2, "Total");
		this.grid.setText(4, 3, "100");
		this.grid.setText(4, 4, "487.5");
		
		for (int i = 1; i < this.grid.getRowCount(); i++)
		{
			for (int j = 0; j < this.grid.getCellCount(i) - 2; j++)
				this.grid.getFlexCellFormatter().setStyleName(i, j, LtlfStyle.mpDetailGridEditableCell);
		}

		initWidget(this.grid);
	}
}
