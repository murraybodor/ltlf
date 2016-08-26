package ca.aeso.ltlf.client.common;

import java.util.List;
import java.util.Map;

import ca.aeso.ltlf.model.Area;
import ca.aeso.ltlf.model.LtlfGlobalSettings;
import ca.aeso.ltlf.model.MeasurementPoint;
import ca.aeso.ltlf.rpc.LtlfService;
import ca.aeso.ltlf.rpc.LtlfServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class LtlfGlobal {
	private static LtlfGlobalSettings globalSettings;
	
	public static void init()
	{
		try
		{
			LtlfServiceAsync ltlfService = (LtlfServiceAsync) GWT.create(LtlfService.class);
			ServiceDefTarget ltlfServiceEndPoint = (ServiceDefTarget)ltlfService;
			ltlfServiceEndPoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "ltlf.service");
			ltlfService.getGlobalSettings(new AsyncCallback<LtlfGlobalSettings>() {
				public void onSuccess(LtlfGlobalSettings result)
				{
					globalSettings = result;
					if (globalSettings.isDebug())
						openDebugWin();
				}
				public void onFailure(Throwable caught) {
				}
			});
			
			ltlfService.getAreas(new AsyncCallback<List>() {
				public void onSuccess(List result)
				{
					globalSettings.setAreaList(result);
				}
				public void onFailure(Throwable caught) {
				}
			});
			
			ltlfService.getAreaToMpMap(new AsyncCallback<Map<String,List<MeasurementPoint>>>() {
				public void onSuccess(Map<String,List<MeasurementPoint>> result)
				{
					globalSettings.setAreaToMpMap(result);
				}
				public void onFailure(Throwable caught) {
				}
			});
			
		}
		catch (Exception ignored) {}
	}
	
	public static void showDebugText(String message)
	{
		if (globalSettings.isDebug())
			showError(message);
	}
	
	private native static void showError(String message)
	/*-{
 		if (!$wnd.debugWin || $wnd.debugWin.closed)
 		{
	 		var url = "debugWin.jsp?msg=" + escape(message + "\n");
			$wnd.debugWin = window.open(url, "ltlfDebugWin", "width=600,height=440,resizable=yes,status=yes");
		}
		else
	 		$wnd.debugWin.appendMessage(message + "\n");

		$wnd.debugWin.focus();
	}-*/;
	
	private native static void openDebugWin()
	/*-{
 		var url = "debugWin.jsp";
		$wnd.debugWin = window.open(url, "ltlfDebugWin", "width=600,height=440,resizable=yes,status=yes");
	}-*/;
	
	public static int getRefreshInterval()
	{
		return (globalSettings == null ? 30 * 1000 : globalSettings.getRefreshInterval().intValue());
	}
	
	public static Integer getBaseYear()
	{
		return (globalSettings == null ? 0 : globalSettings.getBaseYear());
	}
	public static List<Area> getAreaList()
	{
		return globalSettings.getAreaList();
	}
	public static Map<String, List<MeasurementPoint>> getAreaToMpMap()
	{
		return globalSettings.getAreaToMpMap();
	}
}
