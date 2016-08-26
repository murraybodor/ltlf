package ca.aeso.ltlf.client.event;

import java.util.EventListener;

public interface LtlfCompositeEventListener extends EventListener {
	public void onEvent(LtlfCompositeEvent event);
}
