package ca.aeso.ltlf.client.allocation.mp;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Window;
import java.util.*;

import ca.aeso.ltlf.client.allocation.LtlfAllocationCommentButton;
import ca.aeso.ltlf.client.allocation.area.AreaAllocationGridCell;
import ca.aeso.ltlf.client.allocation.area.AreaAllocationRowHeader;
import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfGlobal;
import ca.aeso.ltlf.client.common.LtlfStyle;
import ca.aeso.ltlf.client.common.LtlfTextAreaDialog;
import ca.aeso.ltlf.client.common.ScrollGrid;
import ca.aeso.ltlf.client.common.TooltipListener;
import ca.aeso.ltlf.client.event.LtlfCompositeEvent;
import ca.aeso.ltlf.model.*;

public class MpAllocationGrid extends LtlfComposite
{
	private int rowCount;
	private int columnCount = 0;
	private int startYear = 0;
	private int endYear = 0;

	private Allocation allocation;
	private Grid columnHeader;
	private MpAllocationRowHeader rowHeader;
	private Grid inputGrid;
	
	private ScrollGrid mpAllocationGrid = new ScrollGrid();
	private String[][] allocationMps;
	private List<MpAllocationGridCell> changedGridCells = new ArrayList<MpAllocationGridCell>(); 
	private List<AllocationComment> changedComments = new ArrayList<AllocationComment>(); 
	
	ClickListener mpLinkClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			String areaCode = ((Label)sender).getText();
    		fireLtlfCompositeEvent(new LtlfCompositeEvent(this, LtlfCompositeEvent.EventCode.EditMpDetailAllocation, new Object[]{allocation.getOid(), new Integer(startYear), new Integer(endYear), areaCode}));
		}
	};
	
	ClickListener commentButtonClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			final LtlfAllocationCommentButton pb = (LtlfAllocationCommentButton)sender;

			ChangeListener commentChangeListener = new ChangeListener() {
				public void onChange(Widget sender) {
					String comments = ((TextArea)sender).getText();
					
					AllocationComment mpComment = allocation.getMpComment(pb.getMp());
					
					if (mpComment==null) {
						mpComment = new AllocationComment();
						mpComment.setMp(pb.getMp());
						allocation.addComment(mpComment);
					}

					mpComment.setComments(comments);
					changedComments.add(mpComment);
					
				}
			};
			
			String comments = new String();
			AllocationComment mpComment = allocation.getMpComment(pb.getMp());
			if (mpComment!=null)
				comments = mpComment.getComments();
		
			// get allocation comment based on area code
			LtlfTextAreaDialog commentDialog = new LtlfTextAreaDialog(commentChangeListener, "MP " + pb.getMp().getCurrentDetail().getName() + " Comments", comments);
			commentDialog.setPopupPosition(200, 200);
			commentDialog.show();
		}
	};
	
	public MpAllocationGrid(int startYear, int endYear)
	{
		this.startYear = startYear;
		this.endYear = endYear;
		
		this.rowCount = endYear - startYear + 1;
		
		mpAllocationGrid.setScrollAreaSize("880px", "555px");
//		systemAllocationGrid.setScrollAreaSize("800px", "400px");
		mpAllocationGrid.setFixedHeader(new HTML("<div/>"));
		mpAllocationGrid.setColumnHeader(new HTML("<div/>"));
		mpAllocationGrid.setRowHeader(new HTML("<div/>"));
		mpAllocationGrid.setScrollContent(new HTML("<div/>"));
		
		initWidget(mpAllocationGrid);
		
	}
	
	public void populate(String[][] allocationMps, String areaCode) {
		this.allocationMps =  allocationMps;
		
		drawGrid(areaCode);
	}

	public void resetGrid() {

		columnHeader = null;
		rowHeader = null;
		inputGrid = null;

	}
	private void drawGrid(String areaCode) {


		// fixed header (top-left corner)
		Grid fixedHeader = new Grid(1, 1);
		fixedHeader.setStyleName(LtlfStyle.mpAllocationGrid);

		fixedHeader.getCellFormatter().setStyleName(0, 0, LtlfStyle.mpAllocationFixedHeaderCell);

		FlexTable fixedHeaderLayoutGrid = new FlexTable();
		fixedHeaderLayoutGrid.setCellPadding(0);
		fixedHeaderLayoutGrid.setCellSpacing(0);
		fixedHeaderLayoutGrid.setStyleName(LtlfStyle.mpAllocationFixedHeaderLayoutGrid);
		FlexTable.FlexCellFormatter cf = fixedHeaderLayoutGrid.getFlexCellFormatter();

		cf.setStyleName(0, 0, LtlfStyle.mpAllocationYearHeaderCell);
		SimplePanel wrapper = new SimplePanel();
		Label label = new Label("Year/Area");
		label.setStyleName(LtlfStyle.normalText);
		wrapper.add(label);
		fixedHeaderLayoutGrid.setWidget(0, 0, wrapper);

		cf.setStyleName(0, 1, LtlfStyle.mpAllocationTotalCell);
		cf.setColSpan(0, 1, 2);
		wrapper = new SimplePanel();
		label = new Label("Totals");
		label.setStyleName(LtlfStyle.normalText);
		wrapper.add(label);
		fixedHeaderLayoutGrid.setWidget(0, 1, wrapper);

		cf.setRowSpan(1, 0, 2);
		cf.setStyleName(1, 0, LtlfStyle.mpAllocationYearCell);
		wrapper = new SimplePanel();
		label = new Label("");
		label.setStyleName(LtlfStyle.normalText);
		wrapper.add(label);
		fixedHeaderLayoutGrid.setWidget(1, 0, wrapper);

		cf.setRowSpan(1, 1, 2);
		cf.setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		cf.setStyleName(1, 1, LtlfStyle.mpAllocationPercentTotalCell);
		wrapper = new SimplePanel();
		label = new Label("%");
		label.setStyleName(LtlfStyle.normalText);
		wrapper.add(label);
		fixedHeaderLayoutGrid.setWidget(1, 1, wrapper);

		cf.setRowSpan(1, 2, 2);
		cf.setVerticalAlignment(1, 2, HasVerticalAlignment.ALIGN_MIDDLE);
		cf.setStyleName(1, 2, LtlfStyle.mpAllocationEnergyTotalCell);
		wrapper = new SimplePanel();
		label = new Label("MWH");
		label.setStyleName(LtlfStyle.normalText);
		wrapper.add(label);
		fixedHeaderLayoutGrid.setWidget(1, 2, wrapper);

		fixedHeader.setWidget(0, 0, fixedHeaderLayoutGrid);

		
		// row header (left)
		rowHeader = new MpAllocationRowHeader(startYear, endYear, null);

		// column headers (top)
		List mps = new ArrayList();

		// figure out how many columns to draw (varies by area)
		List<MeasurementPoint> mpList = LtlfGlobal.getAreaToMpMap().get(areaCode);

		columnCount = mpList.size();
		
		columnHeader = new Grid(1, this.columnCount);
		columnHeader.setStyleName(LtlfStyle.mpAllocationGrid);
		
		for (int h = 0; h < mpList.size(); h++) {
		
			MeasurementPoint anMp = (MeasurementPoint)mpList.get(h);
			
			columnHeader.getCellFormatter().setStyleName(0, h, LtlfStyle.mpAllocationHeaderCell);

			FlexTable layoutGrid = new FlexTable();
			FlexTable.FlexCellFormatter cf2 = layoutGrid.getFlexCellFormatter();
			layoutGrid.setCellPadding(0);
			layoutGrid.setCellSpacing(0);
			layoutGrid.setStyleName(LtlfStyle.mpAllocationHeaderLayoutGrid);

			cf2.setColSpan(0, 0, 2);
			//cf2.setStyleName(0, 0, LtlfStyle.mpAllocationEnergyCell);
			wrapper = new SimplePanel();
			

			HorizontalPanel mpColWrapper = new HorizontalPanel();
			mpColWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			
			Label areaLinkLabel = new Label(anMp.getCurrentDetail().getName());
			areaLinkLabel.setStyleName(LtlfStyle.normalBoldLink);
			areaLinkLabel.addClickListener(mpLinkClickListener);
//			areaLinkLabel.addMouseListener(new TooltipListener(" " + mpName + " ", 5000 ,"ltlfTooltip"));
			
			mpColWrapper.add(areaLinkLabel);
			
			Image commentImage = new Image();
			commentImage.setUrl("images/comment.gif");
			
			LtlfAllocationCommentButton mpCommentButton = new LtlfAllocationCommentButton(commentImage, h);
			mpCommentButton.addClickListener(commentButtonClickListener);
			mpCommentButton.setMp(anMp);
			
			mpColWrapper.add(mpCommentButton);

			
			mpColWrapper.setCellWidth(mpCommentButton, "40px");
			mpColWrapper.setCellHorizontalAlignment(mpCommentButton, HasHorizontalAlignment.ALIGN_CENTER);
			
			layoutGrid.setWidget(0, 0, mpColWrapper);
			
			cf2.setStyleName(1, 0, LtlfStyle.mpAllocationPercentageCell);
			wrapper = new SimplePanel();
			label = new Label("%");
			label.setStyleName(LtlfStyle.normalText);
			wrapper.add(label);
			layoutGrid.setWidget(1, 0, wrapper);

			cf2.setStyleName(1, 1, LtlfStyle.mpAllocationPeakCell);
			wrapper = new SimplePanel();
			label = new Label("Peak");
			label.setStyleName(LtlfStyle.normalText);
			wrapper.add(label);
			layoutGrid.setWidget(1, 1, wrapper);

			cf2.setColSpan(2, 0, 2);
			//cf2.setStyleName(2, 0, LtlfStyle.mpAllocationEnergyCell);
			wrapper = new SimplePanel();
			label = new Label("MWH");
			label.setStyleName(LtlfStyle.normalText);
			wrapper.add(label);
			layoutGrid.setWidget(2, 0, wrapper);

			columnHeader.setWidget(0, h, layoutGrid);
		}

		
		// scrollable table body
		inputGrid = new Grid(this.rowCount, this.columnCount);
		inputGrid.setStyleName(LtlfStyle.mpAllocationGrid);
		
		int curRow = -1;
		int curCol = 0;
		String forecastYearPrev = "0";
		
		for (int k = 0; k < allocationMps.length; k++) {
			String[] aMp = allocationMps[k];
			String fy = aMp[1];
			
			if (!fy.equals(forecastYearPrev)) {
				curRow++;
				curCol = 0;
				
				Float fyAllocEnergy = new Float(aMp[2]);
				rowHeader.setTotalEnergy(curRow, fyAllocEnergy);
				
//				Float fyTotalPercent = new Float(aMp[4]);
//				fyTotalPercent = roundTwoDecimals(new Float(fyTotalPercent));
//				rowHeader.setTotalPercentage(curRow, fyTotalPercent);
//				Float fyTotalPercent = new Float(aMp[4]);
//				fyTotalPercent = roundTwoDecimals(new Float(fyTotalPercent));
//				rowHeader.setTotalPercentage(curRow, fyTotalPercent);
//				
				forecastYearPrev = fy;
			}
			
			
			inputGrid.getCellFormatter().setStyleName(curRow, curCol, LtlfStyle.mpAllocationInputCell);
			MpAllocationGridCell inputCell = new MpAllocationGridCell(this, curRow, curCol);
			inputCell.setAllocMpId(aMp[0]);
			inputCell.setPercentageValue(new Float(aMp[5]));
			inputCell.setPeakValue(new Float(aMp[6]));
			inputCell.setSuv(new Float(aMp[8]));
			inputCell.recalculateEnergy();
			inputGrid.setWidget(curRow, curCol, inputCell);
			
			curCol++;
		}
		
		mpAllocationGrid.setFixedHeader(fixedHeader);
		mpAllocationGrid.setColumnHeader(columnHeader);
		mpAllocationGrid.setRowHeader(rowHeader);
		mpAllocationGrid.setScrollContent(inputGrid);


	}
	
	public void onCellChange(MpAllocationGridCell cell)
	{
		int rowIndex = cell.getRowIndex();
		updateRowTotals(rowIndex);
		
		if (!changedGridCells.contains(cell))	
			changedGridCells.add(cell);
	}
	
	public void onCellNavigate(MpAllocationGridCell sourceCell, char keyCode, MpAllocationGridCell.InputField inputField)
	{
		int rowIndex = -1;
		if (keyCode == KeyboardListenerAdapter.KEY_DOWN)
			rowIndex = (sourceCell.getRowIndex() + 1) % this.rowCount;
		else if (keyCode == KeyboardListenerAdapter.KEY_UP)
			rowIndex = (sourceCell.getRowIndex() + this.rowCount - 1) % this.rowCount;
		
		if (rowIndex != -1)
		{
			MpAllocationGridCell targetCell = null;
			targetCell = (MpAllocationGridCell)this.inputGrid.getWidget(rowIndex, sourceCell.getColIndex());
			if (inputField == MpAllocationGridCell.InputField.Percentage)
				targetCell.getPercentageInput().setFocus(true);
			else if (inputField == MpAllocationGridCell.InputField.Peak)
				targetCell.getPeakInput().setFocus(true);
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
		for (int i = 0; i < columnCount; i++)
		{
			MpAllocationGridCell inputCell = (MpAllocationGridCell)this.inputGrid.getWidget(rowIndex, i);
			Float percent = inputCell.getPercentageValue();
			
			if (percent != null)
			{
				totalPercent = Float.valueOf(totalPercent.floatValue() + percent.floatValue());
			}
		}
		return totalPercent;
	}

	public MpAllocationRowHeader getRowHeader() {
		return rowHeader;
	}
	
	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	public List<MpAllocationGridCell> getChangedGridCells() {
		return changedGridCells;
	}

	public void setChangedGridCells(List<MpAllocationGridCell> changedGridCells) {
		this.changedGridCells = changedGridCells;
	}

	public List<AllocationComment> getChangedComments() {
		return changedComments;
	}

	public void setChangedComments(List<AllocationComment> changedComments) {
		this.changedComments = changedComments;
	}
}
