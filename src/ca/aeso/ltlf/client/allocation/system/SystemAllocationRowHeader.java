package ca.aeso.ltlf.client.allocation.system;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfStyle;

public class SystemAllocationRowHeader extends LtlfComposite
{
	private Grid rowHeader;
	private Label[] totalEnergies;
	private float[] maxEnergies;
	
	public SystemAllocationRowHeader(int startYear, int endYear, float[] maxEnergies)
	{
		this.maxEnergies = maxEnergies;
		
		int rowCount = endYear - startYear + 1;
		rowHeader = new Grid(rowCount, 2);
		totalEnergies = new Label[rowCount];
		rowHeader.setStyleName(LtlfStyle.systemAllocationGrid);
		for (int i = 0; i < rowCount; i++)
		{
			rowHeader.getCellFormatter().setStyleName(i, 0, LtlfStyle.forecastYearCell);
			rowHeader.getCellFormatter().addStyleName(i, 0, LtlfStyle.normalText);
			rowHeader.setHTML(i, 0, "<div>" + String.valueOf(i + startYear) + "</div>");

			rowHeader.getCellFormatter().setStyleName(i, 1, LtlfStyle.totalEnergyCell);
			rowHeader.getCellFormatter().addStyleName(i, 1, LtlfStyle.normalText);
			Label totalEnergy = new Label("");
			totalEnergy.setStyleName(LtlfStyle.normalText);
			totalEnergies[i] = totalEnergy;
			SimplePanel layoutPanel = new SimplePanel();
			layoutPanel.add(totalEnergy);
			rowHeader.setWidget(i, 1, layoutPanel);
		}

		initWidget(rowHeader);
	}

	public void setTotalEnergy(int rowIndex, Float totalEnergy)
	{
		this.totalEnergies[rowIndex].setText(totalEnergy == null ? "" : totalEnergy.toString());
	}
}
