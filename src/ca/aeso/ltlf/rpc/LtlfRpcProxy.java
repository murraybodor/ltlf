package ca.aeso.ltlf.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class LtlfRpcProxy {
	private LtlfServiceAsync ltlfService = (LtlfServiceAsync) GWT.create(LtlfService.class);
	private ServiceDefTarget ltlfServiceEndPoint = (ServiceDefTarget)ltlfService;

	public LtlfRpcProxy()
	{
		ltlfServiceEndPoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "ltlf.service");
	}

	public LtlfServiceAsync getLtlfService()
	{
		return ltlfService;
	}
}
