package ca.aeso.ltlf.client.common;


import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.layout.FitLayout;

public class LtlfWorkspace extends LtlfComposite {
	private Panel rootPanel = null;
	TabPanel tabPanel = new TabPanel();
	
	public LtlfWorkspace()
	{
		rootPanel = new Panel();
		rootPanel.setLayout(new FitLayout());
		
		tabPanel = new TabPanel();
		tabPanel.setBodyBorder(false);
		tabPanel.setBorder(false);
		tabPanel.setTabPosition(Position.TOP);
		tabPanel.setActiveTab(0);
		
		rootPanel.add(tabPanel);
		
		initWidget(rootPanel);
	}
	
	public void loadWidget(Widget widget)
	{
		this.rootPanel.removeAll(false);
		
		if (widget == null)
			return;
	
		this.rootPanel.add(widget);
		this.rootPanel.doLayout();
	}
	public void addTab(LtlfComposite aComposite)
	{
		if (aComposite == null)
			return;

		tabPanel.add(aComposite);
		
		aComposite.setTabPanel(tabPanel);
		
		this.rootPanel.doLayout();
	}

	public void setActiveTab(int tabNum) {
		tabPanel.setActiveTab(tabNum);
	}
	public void setActiveTab(String panelId) {
		tabPanel.setActiveTab(panelId);
	}
	
}
