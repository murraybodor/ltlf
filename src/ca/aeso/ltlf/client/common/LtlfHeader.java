package ca.aeso.ltlf.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class LtlfHeader extends LtlfComposite {
	SimplePanel panel = new SimplePanel();
	Label userIdLabel;
	Label userId;
	Label releaseLabel;
	Label releaseId;
	Hyperlink logout;
	LtlfImageBundle ltlfImageBundle = (LtlfImageBundle) GWT.create(LtlfImageBundle.class);

	ClickListener logoutClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			if (sender == logout) {
				logoutUser();
			} else {
				return;
			}
		}
	};
	
	public LtlfHeader()
	{
		getUser();
		getApplicationReleaseId();

		panel.setStylePrimaryName(LtlfStyle.headerPanel);

		FlexTable headerTable = new FlexTable();
		headerTable.setStylePrimaryName(LtlfStyle.headerTable);
		FlexCellFormatter cf = headerTable.getFlexCellFormatter();
		
		AbstractImagePrototype logoImgPrototype = ltlfImageBundle.logo();
		Image logo = logoImgPrototype.createImage();
		logo.setStyleName(LtlfStyle.headerLogo);
		
		cf.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		headerTable.setWidget(0, 0, logo);
		
		Label title = new Label();
		title.setText("Long Term Load Forecast");
		title.setStyleName(LtlfStyle.headerTitle);
		
		cf.setWidth(0, 1, "100%");
		cf.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		headerTable.setWidget(0, 1, title);

		releaseLabel = new Label("Release:");
		releaseLabel.setStyleName(LtlfStyle.normalBoldText);

		releaseId = new Label("");
		releaseId.setStyleName(LtlfStyle.normalText);
		
		userIdLabel = new Label("User:");
		userIdLabel.setStyleName(LtlfStyle.normalBoldText);

		userId = new Label("");
		userId.setStyleName(LtlfStyle.normalText);
		
		logout = new Hyperlink("Logout", "main");
		logout.setStyleName(LtlfStyle.normalText);
		logout.addClickListener(logoutClickListener);
		
		cf.setWidth(0, 2, "10px");
		headerTable.setHTML(0, 2, "&nbsp;");
		
		cf.setVerticalAlignment(0, 3, HasVerticalAlignment.ALIGN_MIDDLE);
		headerTable.setWidget(0, 3, releaseLabel);
		cf.setVerticalAlignment(0, 4, HasVerticalAlignment.ALIGN_MIDDLE);
		headerTable.setWidget(0, 4, releaseId);

		cf.setWidth(0, 5, "10px");
		headerTable.setHTML(0, 5, "&nbsp;");
		
		cf.setVerticalAlignment(0, 6, HasVerticalAlignment.ALIGN_MIDDLE);
		headerTable.setWidget(0, 6, userIdLabel);
		cf.setVerticalAlignment(0, 7, HasVerticalAlignment.ALIGN_MIDDLE);
		headerTable.setWidget(0, 7, userId);

		cf.setWidth(0, 8, "10px");
		headerTable.setHTML(0, 8, "&nbsp;");
		
		cf.setVerticalAlignment(0, 9, HasVerticalAlignment.ALIGN_MIDDLE);
		headerTable.setWidget(0, 9, logout);

		cf.setWidth(0, 10, "10px");
		headerTable.setHTML(0, 10, "&nbsp;");
		
		panel.add(headerTable);
		
		
		initWidget(panel);
	}
	
	public void setWidth(String width)
	{
		panel.setWidth(width);
	}
	
	/**
	 * Retrieve the release information
	 */
	private void getApplicationReleaseId() {

	  AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
        	  String release = (String)result;
        	  releaseId.setText(release);
        	  
          }
          public void onFailure(Throwable caught) {
          }
      };
	  
		rpcProxy.getLtlfService().getApplicationReleaseId(callback);
	}

	/**
	 * Retrieve the user information
	 */
	private void getUser() {

	  AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
        	  String user = (String)result;
        	  userId.setText(user);
        	  
          }
          public void onFailure(Throwable caught) {
        	  sendToLogin(GWT.getModuleBaseURL() + "index.html");
          }
      };
	  
		rpcProxy.getLtlfService().getUser(callback);
	}

	/**
	 * Log out the user
	 */
	private void logoutUser() {

	  AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
        	  sendToLogin(GWT.getModuleBaseURL() + "index.html");
          }
          public void onFailure(Throwable caught) {
        	  sendToLogin(GWT.getModuleBaseURL() + "index.html");
          }
      };
	  
		rpcProxy.getLtlfService().logoutUser(callback);
	}

	/**
	 * Redirect the user to the login page
	 * @param url
	 */
	public native void sendToLogin(String url) /*-{
  		$doc.location = url;
	}-*/; 
	
}
