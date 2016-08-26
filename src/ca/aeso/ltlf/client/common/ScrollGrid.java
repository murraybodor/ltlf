package ca.aeso.ltlf.client.common;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.ExtElement;

public class ScrollGrid extends LtlfComposite {
	private static final String DEFAULT_WIDTH = "400px";
	private static final String DEFAULT_HEIGHT = "300px";
	private SimplePanel fixedHeaderViewPort;
	private SimplePanel fixedHeaderContainer;
	private SimplePanel columnHeaderViewPort;
	private SimplePanel rowHeaderViewPort;
	private SimplePanel columnHeaderContainer;
	private ExtElement columnHeaderContainerElement;
	private SimplePanel rowHeaderContainer;
	private ExtElement rowHeaderContainerElement;
	private ScrollPanel scrollArea;
	private boolean isScrollColumnHeader = true;
	private boolean isScrollRowHeader = true;
	
	public ScrollGrid()
	{
		Grid layoutGrid = new Grid(2, 2);
		layoutGrid.setCellPadding(0);
		layoutGrid.setCellSpacing(0);
		layoutGrid.setBorderWidth(0);
		layoutGrid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutGrid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		layoutGrid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutGrid.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		layoutGrid.getRowFormatter().setVerticalAlign(0, HasVerticalAlignment.ALIGN_BOTTOM);
		layoutGrid.getRowFormatter().setVerticalAlign(1, HasVerticalAlignment.ALIGN_TOP);

		// fixed header
		fixedHeaderContainer = new SimplePanel();
		fixedHeaderContainer.setStyleName("scrollGridHeaderContainer");

		fixedHeaderViewPort = new SimplePanel();
		fixedHeaderViewPort.setStyleName("scrollGridHeader");
		fixedHeaderViewPort.add(fixedHeaderContainer);
		  
		layoutGrid.setWidget(0, 0, fixedHeaderViewPort);
		
		// column header
		columnHeaderContainer = new SimplePanel();
		columnHeaderContainer.setStyleName("scrollGridHeaderContainer");
		  
		columnHeaderViewPort = new SimplePanel();
		columnHeaderViewPort.setWidth(DEFAULT_WIDTH);
		columnHeaderViewPort.setStyleName("scrollGridHeader");
		columnHeaderViewPort.add(columnHeaderContainer);
		  
		layoutGrid.setWidget(0, 1, columnHeaderViewPort);

		// row header
		rowHeaderContainer = new SimplePanel();
		rowHeaderContainer.setStyleName("scrollGridHeaderContainer");

		rowHeaderViewPort = new SimplePanel();
		rowHeaderViewPort.setHeight(DEFAULT_HEIGHT);
		rowHeaderViewPort.setStyleName("scrollGridHeader");
		rowHeaderViewPort.add(rowHeaderContainer);

		layoutGrid.setWidget(1, 0, rowHeaderViewPort);

		// scroll area
		scrollArea = new ScrollPanel();
		scrollArea.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		scrollArea.addScrollListener(new ScrollListener() {
			public void onScroll(Widget widget, int scrollLeft, int scrollTop)
			{
				if (isScrollColumnHeader)
				{
					if (columnHeaderContainerElement == null)
						columnHeaderContainerElement = new ExtElement(columnHeaderContainer.getElement());
					columnHeaderContainerElement.setLeft((scrollLeft * -1) + "px");
				}

				if (isScrollRowHeader)
				{
					if (rowHeaderContainerElement == null)
						rowHeaderContainerElement = new ExtElement(rowHeaderContainer.getElement());
					rowHeaderContainerElement.setTop((scrollTop * -1) + "px");
				}
			}
		});

		layoutGrid.setWidget(1, 1, scrollArea);

		initWidget(layoutGrid);
	}

	public void setFixedHeader(Widget widget)
	{
		if (this.fixedHeaderContainer.getWidget() != null)
			this.fixedHeaderContainer.getWidget().removeFromParent();
		this.fixedHeaderContainer.add(widget);
	}

	public void setColumnHeader(Widget widget)
	{
		if (this.columnHeaderContainer.getWidget() != null)
			this.columnHeaderContainer.getWidget().removeFromParent();
		this.columnHeaderContainer.add(widget);
	}
	
	public void setRowHeader(Widget widget)
	{
		if (this.rowHeaderContainer.getWidget() != null)
			this.rowHeaderContainer.getWidget().removeFromParent();
		this.rowHeaderContainer.add(widget);
	}
	
	public void setScrollContent(Widget widget)
	{
		if (this.scrollArea.getWidget() != null)
			this.scrollArea.getWidget().removeFromParent();
		this.scrollArea.add(widget);
	}
	
	public void setScrollAreaWidth(String width)
	{
		this.scrollArea.setWidth(width);
		this.columnHeaderViewPort.setWidth(width);
	}
	
	public void setScrollAreaHeight(String height)
	{
		this.scrollArea.setHeight(height);
		this.rowHeaderViewPort.setHeight(height);
	}
	
	public void setScrollAreaSize(String width, String height)
	{
		setScrollAreaWidth(width);
		setScrollAreaHeight(height);
	}
	
	public void setScrollColumnHeader(boolean isScrollColumnHeader)
	{
		this.isScrollColumnHeader = isScrollColumnHeader;
	}
	
	public void setScrollRowHeader(boolean isScrollRowHeader)
	{
		this.isScrollRowHeader = isScrollRowHeader;
	}
}
