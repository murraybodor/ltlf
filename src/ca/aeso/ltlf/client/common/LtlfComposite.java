package ca.aeso.ltlf.client.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.aeso.ltlf.client.event.LtlfCompositeEvent;
import ca.aeso.ltlf.client.event.LtlfCompositeEventListener;
import ca.aeso.ltlf.rpc.LtlfRpcProxy;

import com.google.gwt.user.client.ui.Composite;
import com.gwtext.client.widgets.TabPanel;

public class LtlfComposite extends Composite {
	private List<LtlfCompositeEventListener> listenerList = new ArrayList<LtlfCompositeEventListener>();
	private TabPanel tabPanel;
	private String id;

	protected final static LtlfRpcProxy rpcProxy = new LtlfRpcProxy();
	
	public void addLtlfCompositeEventListener(LtlfCompositeEventListener listener)
	{
		this.listenerList.add(listener);
	}
	
	public void removeLtlfCompositeEventListener(LtlfCompositeEventListener listener) {
		Iterator<LtlfCompositeEventListener> itr = this.listenerList.iterator();
        while (itr.hasNext())
        {
        	if (itr.next().equals(listener))
        	{
        		itr.remove();
        		break;
        	}
        }
    }

	protected void fireLtlfCompositeEvent(LtlfCompositeEvent event) {
		Iterator<LtlfCompositeEventListener> itr = this.listenerList.iterator();
        while (itr.hasNext())
        	itr.next().onEvent(event);
    }
	
	public void setTabPanel(TabPanel tabPanel) {
		this.tabPanel = tabPanel;
	}

	public TabPanel getTabPanel() {
		return tabPanel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
