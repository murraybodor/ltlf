package ca.aeso.ltlf.client.allocation.mp;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfStyle;

public class MpDetailAllocationEditor extends LtlfComposite
{
	public MpDetailAllocationEditor()
	{
	  	VerticalPanel layoutPanel = new VerticalPanel();

	  	MpDetailAllocationPanel panel = new MpDetailAllocationPanel(2008, 487.5f);
	  	layoutPanel.add(panel);
	  	
	  	panel = new MpDetailAllocationPanel(2009, 650);
	  	layoutPanel.add(panel);
	  	
	  	for (int i = 2010; i < 2029; i++)
	  	{
		  	panel = new MpDetailAllocationPanel(i, 300);
		  	layoutPanel.add(panel);
	  	}
	  	
	  	SimplePanel wrapperPanel = new SimplePanel();
		wrapperPanel.setStyleName(LtlfStyle.tabContentPanel);
		wrapperPanel.add(layoutPanel);

		initWidget(wrapperPanel);
	}
}
