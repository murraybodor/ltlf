package ca.aeso.ltlf.client.event;

import java.util.EventObject;

public class LtlfCompositeEvent extends EventObject
{
	private static final long serialVersionUID = 8646683685274077425L;

	public enum EventCode {MenuItemSelected,
		EditLoadHistory,
		EditLoadShape,
		EditAreaAllocation,
		EditMpAllocation,
		EditMpDetailAllocation,
		SystemAllocationSectorClicked,
		SystemAllocationVersionCreated};

	private EventCode eventCode;
	private Object eventArgs;

	public LtlfCompositeEvent(Object source, EventCode eventCode, Object eventArgs)
	{
		super(source);
		this.eventCode = eventCode;
		this.eventArgs = eventArgs;
	}
	
	public EventCode getEventCode()
	{
		return this.eventCode;
	}
	
	public Object getEventArgs()
	{
		return this.eventArgs;
	}
}
