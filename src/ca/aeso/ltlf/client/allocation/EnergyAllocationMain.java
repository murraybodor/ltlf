package ca.aeso.ltlf.client.allocation;

import ca.aeso.ltlf.client.allocation.area.AreaAllocationEditor;
import ca.aeso.ltlf.client.allocation.mp.MpAllocationEditor;
import ca.aeso.ltlf.client.allocation.mp.MpDetailAllocationEditor;
import ca.aeso.ltlf.client.allocation.system.SystemAllocationEditor;
import ca.aeso.ltlf.client.common.LtlfBorderlessPanel;
import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.event.LtlfCompositeEvent;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListener;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListenerAdapter;

import com.google.gwt.user.client.Window;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.TabPanel;

public class EnergyAllocationMain extends LtlfComposite {
	private TabPanel tabPanel = null;
	private LtlfBorderlessPanel areaAllocationPanel = null;
	private LtlfBorderlessPanel mpAllocationPanel = null;
	private LtlfBorderlessPanel mpDetailAllocationPanel = null;

	public EnergyAllocationMain() {
		tabPanel = new TabPanel();
		tabPanel.setBodyBorder(false);
		tabPanel.setBorder(false);
		tabPanel.setTabPosition(Position.TOP);
		tabPanel.setActiveTab(1);

		LtlfCompositeEventListener allocationEventListener = new LtlfCompositeEventListenerAdapter() {
			public void onEvent(LtlfCompositeEvent event)
			{
				if (event.getEventCode() == LtlfCompositeEvent.EventCode.EditMpAllocation)
				{
					Object[] eventParams = (Object[])event.getEventArgs();
					if (eventParams == null || eventParams.length < 4)
						return;
					Long allocationOid = (Long)eventParams[0];
					if (allocationOid == null)
						return;
					Integer startYear = (Integer)eventParams[1];
					if (startYear == null)
						return;
					Integer endYear = (Integer)eventParams[2];
					if (endYear == null)
						return;
					String areaCode = (String)eventParams[3];
					if (areaCode == null)
						return;
					
					String panelId = "pnlMpAllocation";
					if (mpAllocationPanel == null)
					{
						mpAllocationPanel = new LtlfBorderlessPanel("MP");
						mpAllocationPanel.setId(panelId);
						mpAllocationPanel.setClosable(true);
						mpAllocationPanel.setAutoScroll(true);
						tabPanel.add(mpAllocationPanel);
					}
					else if (!tabPanel.hasItem(panelId))
						tabPanel.add(mpAllocationPanel);
	
					MpAllocationEditor mpAllocationEditor = new MpAllocationEditor(allocationOid, startYear, endYear, areaCode);
					mpAllocationPanel.removeAll(true);	// clear any existing content
					mpAllocationPanel.add(mpAllocationEditor);
					tabPanel.setActiveTab(panelId);
				}
				else if (event.getEventCode() == LtlfCompositeEvent.EventCode.EditAreaAllocation)
				{
					Object[] eventParams = (Object[])event.getEventArgs();
					if (eventParams == null || eventParams.length < 3)
						return;
					Long allocationOid = (Long)eventParams[0];
					if (allocationOid == null)
						return;
					Integer startYear = (Integer)eventParams[1];
					if (startYear == null)
						return;
					Integer endYear = (Integer)eventParams[2];
					if (endYear == null)
						return;

					String panelId = "pnlAreaAllocation";
					if (areaAllocationPanel == null)
					{
						areaAllocationPanel = new LtlfBorderlessPanel("Area");
						areaAllocationPanel.setId(panelId);
						areaAllocationPanel.setClosable(true);
						areaAllocationPanel.setAutoScroll(true);
						tabPanel.add(areaAllocationPanel);
					}
					else if (!tabPanel.hasItem(panelId))
						tabPanel.add(areaAllocationPanel);

					
					AreaAllocationEditor areaAllocationEditor = new AreaAllocationEditor(allocationOid, startYear.intValue(), endYear.intValue());
					areaAllocationPanel.removeAll(true);	// clear any existing content
					areaAllocationPanel.add(areaAllocationEditor);
					areaAllocationEditor.addLtlfCompositeEventListener(this);
					tabPanel.setActiveTab(panelId);
					
				}
				else if (event.getEventCode() == LtlfCompositeEvent.EventCode.EditMpDetailAllocation)
				{
					String panelId = "pnlMpDetailAllocation";
					if (mpDetailAllocationPanel == null)
					{
						mpDetailAllocationPanel = new LtlfBorderlessPanel("MP By Date");
						mpDetailAllocationPanel.setId(panelId);
						mpDetailAllocationPanel.setClosable(true);
						mpDetailAllocationPanel.setAutoScroll(true);
						tabPanel.add(mpDetailAllocationPanel);
					}
					else if (!tabPanel.hasItem(panelId))
						tabPanel.add(mpDetailAllocationPanel);
	
					MpDetailAllocationEditor mpDetailAllocationEditor = new MpDetailAllocationEditor();
					mpDetailAllocationPanel.removeAll(true);	// clear any existing content
					mpDetailAllocationPanel.add(mpDetailAllocationEditor);
					tabPanel.setActiveTab(panelId);
				}
			}
		};

		LtlfBorderlessPanel systemAllocationPanel = new LtlfBorderlessPanel("Entire System");
		systemAllocationPanel.setAutoScroll(true);
		SystemAllocationEditor systemAllocationEditor = new SystemAllocationEditor();
		systemAllocationEditor.addLtlfCompositeEventListener(allocationEventListener);
		systemAllocationPanel.add(systemAllocationEditor);

		tabPanel.add(systemAllocationPanel);
		tabPanel.setActiveTab(0);

		initWidget(tabPanel);
	}
}
