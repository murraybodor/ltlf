package ca.aeso.ltlf.client.loadshape;

import ca.aeso.ltlf.client.common.AppFrame;
import ca.aeso.ltlf.client.common.LtlfBorderlessPanel;
import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.event.LtlfCompositeEvent;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListener;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListenerAdapter;

import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.TabPanel;

public class UnitizeEditLoadShapeMain extends LtlfComposite {
	private LtlfBorderlessPanel editPanel = null;
	private UnitizeLoadShape unitizeLoadShape;

	public UnitizeEditLoadShapeMain(final AppFrame frame)
	{
		LtlfCompositeEventListener editLoadShapeEventListener = new LtlfCompositeEventListenerAdapter() {
			public void onEvent(LtlfCompositeEvent event)
			{
				if (event.getEventCode() == LtlfCompositeEvent.EventCode.EditLoadShape) {

					Object[] eventParams = (Object[])event.getEventArgs();
					if (eventParams == null || eventParams.length < 2)
						return;
					String mpOid = (String)eventParams[0];
					if (mpOid == null)
						return;
					String versionId = (String)eventParams[1];

					UnitizeLoadShape unitizeLoadShape = null;
					if ((eventParams.length==3) & (eventParams[2]!=null)) {
						unitizeLoadShape = (UnitizeLoadShape)eventParams[2];
					}

					String panelId = "pnlEditLoadShape";
					if (editPanel == null)
					{
						editPanel = new LtlfBorderlessPanel("Edit Load Shape");
						editPanel.setId(panelId);
						editPanel.setClosable(true);
						editPanel.setAutoScroll(true);
					}

					LoadShapeEditor loadShapeEditor = new LoadShapeEditor(mpOid, versionId, unitizeLoadShape); 
					editPanel.removeAll(true);	// clear any existing content
					editPanel.add(loadShapeEditor);
					getTabPanel().add(editPanel);
					getTabPanel().activate(panelId);

				} else if (event.getEventCode() == LtlfCompositeEvent.EventCode.EditLoadHistory) {

					Object[] eventParams = (Object[])event.getEventArgs();
					if (eventParams == null || eventParams.length < 1)
						return;
					String mpOid = (String)eventParams[0];
					if (mpOid == null || mpOid.trim().length() == 0)
						return;

					frame.getImportAnalyzeLoadHistory().getImportLoadHistory().editLoadHistory(mpOid);
				}
			}
		};

		this.setId("pnlUnitize");
		LtlfBorderlessPanel unitizePanel = new LtlfBorderlessPanel("Unitize");
		unitizePanel.setAutoScroll(true);
		UnitizeLoadShape unitizeLoadShape = new UnitizeLoadShape();
		unitizeLoadShape.addLtlfCompositeEventListener(editLoadShapeEventListener);
		unitizePanel.add(unitizeLoadShape);

		initWidget(unitizePanel);
	}

	public UnitizeLoadShape getUnitizeLoadShape() {
		return unitizeLoadShape;
	}

	public void setUnitizeLoadShape(UnitizeLoadShape unitizeLoadShape) {
		this.unitizeLoadShape = unitizeLoadShape;
	}
}
