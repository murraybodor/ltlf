package ca.aeso.ltlf.client.allocation.system;

import java.util.SortedSet;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfStyle;
import ca.aeso.ltlf.client.common.ScrollGrid;
import ca.aeso.ltlf.model.Allocation;
import ca.aeso.ltlf.model.AllocationForecastYear;
import ca.aeso.ltlf.model.AllocationForecastYearComparer;
import ca.aeso.ltlf.model.AllocationSector;
import ca.aeso.ltlf.model.AllocationSectorComparer;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;

public class SystemAllocationGrid extends LtlfComposite
{
	private int rowCount;
	private int columnCount;
	private int startYear;
	private int endYear;
	
	private Grid inputGrid;
	
	private SystemAllocationRowHeader rowHeader;
	
	public SystemAllocationGrid()
	{
		this(null);
	}

	public SystemAllocationGrid(Allocation allocation)
	{
		ScrollGrid systemAllocationGrid = new ScrollGrid();
		systemAllocationGrid.setScrollAreaSize("403px", "423px");

		if (allocation != null && !allocation.getAllocationForecastYears().isEmpty())
		{
			DateTimeFormat yearFormater = DateTimeFormat.getFormat("yyyy");
			startYear = Integer.parseInt(yearFormater.format(allocation.getStartDate()));
			endYear = allocation.getEndYear().intValue();
			
	    	SortedSet<AllocationForecastYear> AllocationForecastYearSet = allocation.getAllocationForecastYears(new AllocationForecastYearComparer());
			AllocationForecastYear[] forecastYearList = (AllocationForecastYear[])AllocationForecastYearSet.toArray(new AllocationForecastYear[AllocationForecastYearSet.size()]);

	    	SortedSet<AllocationSector> AllocationSectorSet = forecastYearList[0].getAllocationSectors(new AllocationSectorComparer());
	    	AllocationSector[] sectorList = (AllocationSector[])AllocationSectorSet.toArray(new AllocationSector[AllocationSectorSet.size()]);

			this.rowCount = endYear - startYear + 1;
			this.columnCount = sectorList.length;
			
			Grid fixedHeader = new Grid(1, 2);
			fixedHeader.setStyleName(LtlfStyle.systemAllocationGrid);
			fixedHeader.getCellFormatter().setStyleName(0, 0, LtlfStyle.forecastYearCell);
			fixedHeader.getCellFormatter().addStyleName(0, 0, LtlfStyle.normalText);
			fixedHeader.setHTML(0, 0, "<div>Forecast Year</div>");
			fixedHeader.getCellFormatter().setStyleName(0, 1, LtlfStyle.totalEnergyCell);
			fixedHeader.getCellFormatter().addStyleName(0, 1, LtlfStyle.normalText);
			fixedHeader.setHTML(0, 1, "<div>Total System MWH</div>");
			
			rowHeader = new SystemAllocationRowHeader(startYear, endYear, null);
	
			Grid columnHeader = new Grid(1, this.columnCount);
			columnHeader.setStyleName(LtlfStyle.systemAllocationGrid);
			for (int i = 0; i < sectorList.length; i++)
			{
				columnHeader.getCellFormatter().setStyleName(0, i, LtlfStyle.customerSectorCell);
				columnHeader.getCellFormatter().addStyleName(0, i, LtlfStyle.normalText);
				
				columnHeader.setHTML(0, i, "<div>" + sectorList[i].getSectorType() + "</div>");
			}
			
			inputGrid = new Grid(this.rowCount, this.columnCount);
			inputGrid.setStyleName(LtlfStyle.systemAllocationGrid);
			for (int i = 0; i < forecastYearList.length; i++)
			{
				AllocationForecastYear allocationForecastYear = forecastYearList[i];

				for (int j = 0; j < this.columnCount; j++)
				{
					inputGrid.getCellFormatter().setStyleName(i, j, LtlfStyle.inputCell);
					inputGrid.getCellFormatter().addStyleName(i, j, LtlfStyle.normalText);
					SystemAllocationGridCell inputCell = new SystemAllocationGridCell(this, i, j);
					inputCell.setEnergyValue(allocationForecastYear.getSectorEnergy(sectorList[j].getSectorType()));
					inputGrid.setWidget(i, j, inputCell);
				}
			}
			
			systemAllocationGrid.setFixedHeader(fixedHeader);
			systemAllocationGrid.setColumnHeader(columnHeader);
			systemAllocationGrid.setRowHeader(rowHeader);
			systemAllocationGrid.setScrollContent(inputGrid);
			
			updateTotalEnergy(-1);
		}
		else	// empty grid
		{
			systemAllocationGrid.setFixedHeader(new HTML("<div/>"));
			systemAllocationGrid.setColumnHeader(new HTML("<div/>"));
			systemAllocationGrid.setRowHeader(new HTML("<div/>"));
			systemAllocationGrid.setScrollContent(new HTML("<div/>"));
		}

		initWidget(systemAllocationGrid);
	}
	
	private Float getTotalEnergy(int rowIndex)
	{
		Float totalEnergy = null;
		for (int i = 0; i < this.columnCount; i++)
		{
			SystemAllocationGridCell inputCell = (SystemAllocationGridCell)this.inputGrid.getWidget(rowIndex, i);
			Float energy = inputCell.getEnergyValue();
			if (energy != null)
			{
				if (totalEnergy == null)
					totalEnergy = Float.valueOf(energy.floatValue());
				else
					totalEnergy = Float.valueOf(totalEnergy.floatValue() + energy.floatValue());
			}
		}
		return totalEnergy;
	}

	private void updateTotalEnergy(int rowIndex)
	{
		if (rowIndex < 0)	// all rows
		{
			for (int i = 0; i < this.rowCount; i++)
				this.rowHeader.setTotalEnergy(i, getTotalEnergy(i));
		}
		else
		{
			Float totalEnergy = getTotalEnergy(rowIndex);
			this.rowHeader.setTotalEnergy(rowIndex, totalEnergy);
		}
	}
	
	public SystemAllocationGridCell getCell(int rowIndex, int columnIndex)
	{
		return (SystemAllocationGridCell)this.inputGrid.getWidget(rowIndex, columnIndex);
	}
	
	public void onCellChange(SystemAllocationGridCell cell)
	{
		int rowIndex = cell.getRowIndex();
		updateTotalEnergy(rowIndex);
	}
	
	public void onCellNavigate(SystemAllocationGridCell sourceCell, char keyCode)
	{
		int rowIndex = -1;
		if (keyCode == KeyboardListenerAdapter.KEY_DOWN)
			rowIndex = (sourceCell.getRowIndex() + 1) % this.rowCount;
		else if (keyCode == KeyboardListenerAdapter.KEY_UP)
			rowIndex = (sourceCell.getRowIndex() + this.rowCount - 1) % this.rowCount;
		
		if (rowIndex != -1)
		{
			SystemAllocationGridCell targetCell = getCell(rowIndex, sourceCell.getColIndex());
			if (targetCell != null)
				targetCell.getEnergyInput().setFocus(true);
		}
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getEndYear() {
		return endYear;
	}

	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
}
