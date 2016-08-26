package ca.aeso.ltlf.client;

import ca.aeso.ltlf.client.common.AppFrame;
import ca.aeso.ltlf.client.common.LtlfBorderlessPanel;
import ca.aeso.ltlf.client.common.LtlfDialog;
import ca.aeso.ltlf.client.common.LtlfGlobal;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Ltlf implements EntryPoint {

	GWT.UncaughtExceptionHandler exHandler = new GWT.UncaughtExceptionHandler() {
		public void onUncaughtException(Throwable throwable) {
			String text = "Uncaught exception: ";
			while (throwable != null) {
				StackTraceElement[] stackTraceElements = throwable.getStackTrace();
				text += new String(throwable.toString() + "\r\n");
				for (int i = 0; i < stackTraceElements.length; i++) {
					text += "    at " + stackTraceElements[i] + "\r\n";
				}
				throwable = throwable.getCause();
				if (throwable != null) {
					text += "\r\nCaused by: ";
				}
			}
			
			final LtlfDialog dialog = new LtlfDialog("Uncaught Exception", text); // custom class based on DialogBox
			dialog.center();
		}
	};


	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		// set the uncaught exception handler 
		GWT.setUncaughtExceptionHandler(exHandler);
		
		LtlfGlobal.init();
		
		AppFrame app = new AppFrame();
		
		LtlfBorderlessPanel mainFrame = new LtlfBorderlessPanel();
		mainFrame.setLayout(new FitLayout());
		mainFrame.add(app);
		Viewport viewport = new Viewport(mainFrame);
	}
}
