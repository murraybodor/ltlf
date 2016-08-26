package ca.aeso.ltlf.client.allocation.mp;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfStyle;

public class MpAllocationRowHeader extends LtlfComposite
{
	private Label[] totalPercentage;
	private Label[] totalEnergies;
	private float[] maxEnergies;
	Grid layoutGrid;
	
	public MpAllocationRowHeader(int startYear, int endYear, float[] maxEnergies)
	{
		this.maxEnergies = maxEnergies;
		
		int rowCount = endYear - startYear + 1;
		layoutGrid = new Grid(rowCount, 3);
		totalPercentage = new Label[rowCount];
		totalEnergies = new Label[rowCount];
		layoutGrid.setStyleName(LtlfStyle.mpAllocationRowHeaderLayoutGrid);
		
		for (int i = 0; i < rowCount; i++)
		{
			layoutGrid.getCellFormatter().setStyleName(i, 0, LtlfStyle.mpAllocationYearCell);
			layoutGrid.getCellFormatter().addStyleName(i, 0, LtlfStyle.normalText);
			
			layoutGrid.setHTML(i, 0, "<div>" + String.valueOf(i + startYear) + "</div>");

			layoutGrid.getCellFormatter().setStyleName(i, 1, LtlfStyle.mpAllocationPercentTotalCell);
			layoutGrid.getCellFormatter().addStyleName(i, 1, LtlfStyle.normalText);
			Label totalPercent = new Label("");
			totalPercent.setStyleName(LtlfStyle.normalText);
			totalPercentage[i] = totalPercent;
			
			SimplePanel layoutPanel = new SimplePanel();
			layoutPanel.add(totalPercent);
			layoutGrid.setWidget(i, 1, layoutPanel);

			layoutGrid.getCellFormatter().setStyleName(i, 2, LtlfStyle.mpAllocationEnergyTotalCell);
			layoutGrid.getCellFormatter().addStyleName(i, 2, LtlfStyle.normalText);
			Label totalEnergy = new Label("");
			totalEnergy.setStyleName(LtlfStyle.normalText);
			totalEnergies[i] = totalEnergy;
			
			layoutPanel = new SimplePanel();
			layoutPanel.add(totalEnergy);
			layoutGrid.setWidget(i, 2, layoutPanel);
		}

		Grid rowHeader = new Grid(1, 1);
		rowHeader.setStyleName(LtlfStyle.mpAllocationGrid);
		rowHeader.setWidget(0, 0, layoutGrid);
		initWidget(rowHeader);
	}

	public void setTotalPercentage(int rowIndex, Float totalPercentage)
	{
		this.totalPercentage[rowIndex].setText(totalPercentage == null ? "0" : totalPercentage.toString());
		
		if (totalPercentage==100.0f) {
			layoutGrid.getCellFormatter().removeStyleName(rowIndex, 1, LtlfStyle.mpAllocationPercentTotalWarningCell);
			layoutGrid.getCellFormatter().addStyleName(rowIndex, 1, LtlfStyle.mpAllocationPercentTotalCell);
		} else {
			layoutGrid.getCellFormatter().removeStyleName(rowIndex, 1, LtlfStyle.mpAllocationPercentTotalCell);
			layoutGrid.getCellFormatter().addStyleName(rowIndex, 1, LtlfStyle.mpAllocationPercentTotalWarningCell);
		}
	}

	public void setTotalEnergy(int rowIndex, Float totalEnergy)
	{
		this.totalEnergies[rowIndex].setText(totalEnergy == null ? "" : totalEnergy.toString());
	}
	public Float getTotalEnergy(int rowIndex)
	{
		String totalEnergyStr = this.totalEnergies[rowIndex].getText();
		return new Float(totalEnergyStr);
	}
}
