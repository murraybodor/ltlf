package ca.aeso.ltlf.client.allocation.area;

import java.util.*;

import ca.aeso.ltlf.client.allocation.LtlfAllocationCommentButton;
import ca.aeso.ltlf.client.allocation.mp.MpAllocationGridCell;
import ca.aeso.ltlf.client.allocation.mp.MpAllocationRowHeader;
import ca.aeso.ltlf.client.common.*;
import ca.aeso.ltlf.client.event.LtlfCompositeEvent;
import ca.aeso.ltlf.model.*;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.*;

import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.gwtext.client.core.ExtElement;

/**
 * AreaAllocationGrid
 * @author mbodor
 *
 */
public class AreaAllocationGrid extends LtlfComposite {

	private int startYear = 0;
	private int endYear = 0;
	private int rowCount = 0;
	
	private String[][] allocationSectors;
	private Allocation allocation;
	private ScrollGrid areaAllocationGrid = new ScrollGrid();
	private AreaAllocationRowHeader rowHeader;
	private Grid columnHeader;
	private Grid inputGrid;
	private List<AreaAllocationGridCell> changedGridCells = new ArrayList<AreaAllocationGridCell>(); 
	private List<AllocationComment> changedComments = new ArrayList<AllocationComment>(); 
	
//	private static final NumberFormat energyFormat = NumberFormat.getFormat("#,###,##0.00");
//	private static final NumberFormat percentFormat = NumberFormat.getFormat("##0.00");
	
	ClickListener areaLinkClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			String areaCode = ((Label)sender).getText();
    		fireLtlfCompositeEvent(new LtlfCompositeEvent(this, LtlfCompositeEvent.EventCode.EditMpAllocation, new Object[]{allocation.getOid(), new Integer(startYear), new Integer(endYear), areaCode}));
		}
	};
	
	ClickListener commentButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			final LtlfAllocationCommentButton pb = (LtlfAllocationCommentButton)sender;

			ChangeListener commentChangeListener = new ChangeListener() {
				public void onChange(Widget sender) {
					String comments = ((TextArea)sender).getText();
					
					AllocationComment areaComment = allocation.getAreaComment(pb.getArea());
					
					if (areaComment==null) {
						areaComment = new AllocationComment();
						areaComment.setArea(pb.getArea());
						allocation.addComment(areaComment);
					}

					areaComment.setComments(comments);
					changedComments.add(areaComment);
					
				}
			};
			
			String comments = new String();
			AllocationComment areaComment = allocation.getAreaComment(pb.getArea());
			if (areaComment!=null)
				comments = areaComment.getComments();
		
			// get allocation comment based on area code
			LtlfTextAreaDialog commentDialog = new LtlfTextAreaDialog(commentChangeListener, "Area " + pb.getAreaCode() + " Comments", comments);
			commentDialog.setPopupPosition(200, 200);
			commentDialog.show();
		}
	};
	
	
	public AreaAllocationGrid(int startYear, int endYear) {

		this.startYear = startYear;
		this.endYear = endYear;
		this.rowCount = endYear - startYear + 1;
		
		areaAllocationGrid.setScrollAreaSize("880px", "585px");
		areaAllocationGrid.setFixedHeader(new HTML("<div/>"));
		areaAllocationGrid.setColumnHeader(new HTML("<div/>"));
		areaAllocationGrid.setRowHeader(new HTML("<div/>"));
		areaAllocationGrid.setScrollContent(new HTML("<div/>"));
		
		initWidget(areaAllocationGrid);
	}
	
	public void populate(String[][] allocationSectors) {
		this.allocationSectors =  allocationSectors;
		drawGrid();
	}
	
	public void resetGrid() {
		
		columnHeader = null;
		rowHeader = null;
		inputGrid = null;
		
	}
	private void drawGrid() {


		// fixed header (top-left corner)
		Grid fixedHeader = new Grid(1, 1);
		fixedHeader.setStyleName(LtlfStyle.areaAllocationGrid);

		fixedHeader.getCellFormatter().setStyleName(0, 0, LtlfStyle.areaAllocationFixedHeaderCell);

		FlexTable fixedHeaderLayoutGrid = new FlexTable();
		fixedHeaderLayoutGrid.setCellPadding(0);
		fixedHeaderLayoutGrid.setCellSpacing(0);
		fixedHeaderLayoutGrid.setStyleName(LtlfStyle.areaAllocationFixedHeaderLayoutGrid);
		FlexTable.FlexCellFormatter cf = fixedHeaderLayoutGrid.getFlexCellFormatter();
		
		cf.setStyleName(0, 0, LtlfStyle.areaAllocationYearHeaderCell);
		SimplePanel wrapper = new SimplePanel();
		Label label = new Label("Year/Area");
		label.setStyleName(LtlfStyle.normalText);
		wrapper.add(label);
		fixedHeaderLayoutGrid.setWidget(0, 0, wrapper);
		
		cf.setStyleName(0, 1, LtlfStyle.areaAllocationTotalCell);
		cf.setColSpan(0, 1, 2);
		wrapper = new SimplePanel();
		label = new Label("Totals");
		label.setStyleName(LtlfStyle.normalText);
		wrapper.add(label);
		fixedHeaderLayoutGrid.setWidget(0, 1, wrapper);
		
		cf.setRowSpan(1, 0, 2);
		cf.setStyleName(1, 0, LtlfStyle.areaAllocationYearCell);
		wrapper = new SimplePanel();
		label = new Label("");
		label.setStyleName(LtlfStyle.normalText);
		wrapper.add(label);
		fixedHeaderLayoutGrid.setWidget(1, 0, wrapper);
		
		cf.setRowSpan(1, 1, 2);
		cf.setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		cf.setStyleName(1, 1, LtlfStyle.areaAllocationPercentTotalCell);
		wrapper = new SimplePanel();
		label = new Label("%");
		label.setStyleName(LtlfStyle.normalText);
		wrapper.add(label);
		fixedHeaderLayoutGrid.setWidget(1, 1, wrapper);
		
		cf.setRowSpan(1, 2, 2);
		cf.setVerticalAlignment(1, 2, HasVerticalAlignment.ALIGN_MIDDLE);
		cf.setStyleName(1, 2, LtlfStyle.areaAllocationEnergyTotalCell);
		wrapper = new SimplePanel();
		label = new Label("MWH");
		label.setStyleName(LtlfStyle.normalText);
		wrapper.add(label);
		fixedHeaderLayoutGrid.setWidget(1, 2, wrapper);
		
		fixedHeader.setWidget(0, 0, fixedHeaderLayoutGrid);

		// row header (left)
		rowHeader = new AreaAllocationRowHeader(startYear, endYear, null);

		// column header (top)
		columnHeader = new Grid(1, LtlfGlobal.getAreaList().size()); // a column for every area
		columnHeader.setStyleName(LtlfStyle.areaAllocationGrid);
		
		
		for (int i = 0; i < LtlfGlobal.getAreaList().size(); i++) {
			
			Area anArea = (Area)LtlfGlobal.getAreaList().get(i);
			
			columnHeader.getCellFormatter().setStyleName(0, i, LtlfStyle.areaAllocationHeaderCell);

			FlexTable layoutGrid = new FlexTable();
			FlexTable.FlexCellFormatter cf2 = layoutGrid.getFlexCellFormatter();
			layoutGrid.setCellPadding(0);
			layoutGrid.setCellSpacing(0);
			layoutGrid.setStyleName(LtlfStyle.areaAllocationHeaderLayoutGrid);

			// Area column header
			cf2.setColSpan(0, 0, 2);

			HorizontalPanel areaColWrapper = new HorizontalPanel();
			areaColWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			
			Label areaLinkLabel = new Label(anArea.getCode());
			areaLinkLabel.setStyleName(LtlfStyle.normalBoldLink);
			areaLinkLabel.addClickListener(areaLinkClickListener);
			areaLinkLabel.addMouseListener(new TooltipListener(" " + anArea.getName() + " ", 5000 ,"ltlfTooltip"));
			
			areaColWrapper.add(areaLinkLabel);
			
			Image commentImage = new Image();
			commentImage.setUrl("images/comment.gif");
			
			LtlfAllocationCommentButton areaCommentButton = new LtlfAllocationCommentButton(commentImage, i);
			areaCommentButton.addClickListener(commentButtonClickListener);
			areaCommentButton.setAreaCode(anArea.getCode());
			
			areaCommentButton.setArea(anArea);
			areaColWrapper.add(areaCommentButton);

			
			areaColWrapper.setCellWidth(areaCommentButton, "40px");
			areaColWrapper.setCellHorizontalAlignment(areaCommentButton, HasHorizontalAlignment.ALIGN_CENTER);
			
			layoutGrid.setWidget(0, 0, areaColWrapper);
			// Area column header end

			
			cf2.setStyleName(1, 0, LtlfStyle.areaAllocationPercentageCell);
			wrapper = new SimplePanel();
			label = new Label("%");
			label.setStyleName(LtlfStyle.normalText);
			wrapper.add(label);
			layoutGrid.setWidget(1, 0, wrapper);
			
			cf2.setStyleName(1, 1, LtlfStyle.areaAllocationEnergyCell);
			wrapper = new SimplePanel();
			label = new Label("MWh");
			label.setStyleName(LtlfStyle.normalText);
			wrapper.add(label);
			layoutGrid.setWidget(1, 1, wrapper);
			
			columnHeader.setWidget(0, i, layoutGrid);
		}

		// scrollable table body
		inputGrid = new Grid(rowCount, LtlfGlobal.getAreaList().size());
		inputGrid.setStyleName(LtlfStyle.mpAllocationGrid);

		int curRow = -1;
		int curCol = 0;
		String forecastYearPrev = "0";
		
		for (int k = 0; k < allocationSectors.length; k++) {
			String[] aSector = allocationSectors[k];
			String fy = aSector[1];
			
			if (!fy.equals(forecastYearPrev)) {
				curRow++;
				curCol = 0;
				
				Float fyAllocEnergy = new Float(aSector[2]);
				rowHeader.setTotalEnergy(curRow, fyAllocEnergy);
				Float fyTotalPercent = new Float(aSector[4]);
//				fyTotalPercent = roundTwoDecimals(new Float(fyTotalPercent));
				rowHeader.setTotalPercentage(curRow, fyTotalPercent);
//				
				forecastYearPrev = fy;
			}
			
			
			inputGrid.getCellFormatter().setStyleName(curRow, curCol, LtlfStyle.areaAllocationInputCell);
			AreaAllocationGridCell inputCell = new AreaAllocationGridCell(this, curRow, curCol);
			inputCell.setAreaSectorId(aSector[0]);
			inputCell.setPercentageValue(new Float(aSector[5]));
			inputCell.recalculateEnergy();
			inputGrid.setWidget(curRow, curCol, inputCell);
			
			curCol++;
		}
		
		areaAllocationGrid.setFixedHeader(fixedHeader);
		areaAllocationGrid.setColumnHeader(columnHeader);
		areaAllocationGrid.setRowHeader(rowHeader);
		areaAllocationGrid.setScrollContent(inputGrid);
	
	}
	
	public void onCellChange(AreaAllocationGridCell cell)
	{
		int rowIndex = cell.getRowIndex();
		updateRowTotals(rowIndex);
		
		if (!changedGridCells.contains(cell))	
			changedGridCells.add(cell);
		
	}
	
	public void onCellNavigate(AreaAllocationGridCell sourceCell, char keyCode, AreaAllocationGridCell.InputField inputField)
	{
		int rowIndex = -1;
		if (keyCode == KeyboardListenerAdapter.KEY_DOWN)
			rowIndex = (sourceCell.getRowIndex() + 1) % this.rowCount;
		else if (keyCode == KeyboardListenerAdapter.KEY_UP)
			rowIndex = (sourceCell.getRowIndex() + this.rowCount - 1) % this.rowCount;
		
		if (rowIndex != -1)
		{
			AreaAllocationGridCell targetCell = null;
			targetCell = (AreaAllocationGridCell)this.inputGrid.getWidget(rowIndex, sourceCell.getColIndex());
			if (inputField == AreaAllocationGridCell.InputField.Percentage)
				targetCell.getPercentageInput().setFocus(true);
		}
	}
	
	private void updateRowTotals(int rowIndex)
	{
		if (rowIndex < 0)	// all rows
		{
			for (int i = 0; i < this.rowCount; i++)
				this.rowHeader.setTotalPercentage(i, getTotalPercent(i));
		}
		else
		{
			Float totalPercent = getTotalPercent(rowIndex);
			this.rowHeader.setTotalPercentage(rowIndex, totalPercent);
		}
	}
	
	private Float getTotalPercent(int rowIndex)
	{
		Float totalPercent = new Float(0);
		for (int i = 0; i < LtlfGlobal.getAreaList().size(); i++)
		{
			AreaAllocationGridCell inputCell = (AreaAllocationGridCell)this.inputGrid.getWidget(rowIndex, i);
			Float percent = inputCell.getPercentageValue();
			
			if (percent != null)
			{
				totalPercent = Float.valueOf(totalPercent.floatValue() + percent.floatValue());
			}
		}
		return totalPercent;
	}

	private Float roundTwoDecimals(Float value) {
		
		int valueL = Math.round(value * 100);
		Float newVal = new Float(valueL);
		return new Float(newVal/100);
		
	}

	public AreaAllocationRowHeader getRowHeader() {
		return rowHeader;
	}

	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	public List<AreaAllocationGridCell> getChangedGridCells() {
		return changedGridCells;
	}

	public void setChangedGridCells(List<AreaAllocationGridCell> changedGridCells) {
		this.changedGridCells = changedGridCells;
	}

	public List<AllocationComment> getChangedComments() {
		return changedComments;
	}

	public void setChangedComments(List<AllocationComment> changedComments) {
		this.changedComments = changedComments;
	}
}
