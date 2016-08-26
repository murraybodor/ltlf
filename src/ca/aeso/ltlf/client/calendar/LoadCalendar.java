package ca.aeso.ltlf.client.calendar;

import java.util.Date;

import ca.aeso.ltlf.client.common.LtlfComposite;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Form;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormListener;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;

public class LoadCalendar extends LtlfComposite {

	Button loadButton = null;
	Label lastLoadDate = null;
	
	public LoadCalendar()
	{
		String normalTextStyle = "ltlfNormalText";
		String normalBoldTextStyle = "ltlfNormalBoldText";
		String warningTextStyle = "ltlfWarningText";
		
		VerticalPanel layoutPanel = new VerticalPanel();
		Panel importPanel = new Panel();
		importPanel.setWidth(600);
		importPanel.setPaddings(5);
		importPanel.setFrame(true);
		
		Label label = new Label();
		label.setStyleName(normalBoldTextStyle);
		label.setText("Import Calendar Entries:");
		importPanel.add(label);		
		layoutPanel.add(importPanel);
		
		final String url = "fileupload.smvc";
		
	     final XmlReader reader = new XmlReader("data", new RecordDef(new 
	    		 FieldDef[] { new StringFieldDef("responseMsg") })); 
	    		         reader.setSuccess("@success"); 
	    		         final XmlReader errorReader = new XmlReader("data", new 
	    		 RecordDef(new FieldDef[] { new StringFieldDef("responseMsg") })); 
	    		         errorReader.setSuccess("@success"); 
	    		         
		final FormPanel formPanel = new FormPanel();
		// setFileUpload(true) is important because it sets the
		// <form>'s content type to multipart
		// so that your server knows what to do
		formPanel.setFileUpload(true);
		formPanel.setUrl("fileupload.smvc");
		formPanel.setReader(reader); 
		formPanel.setErrorReader(errorReader);
		formPanel.setFrame(true);
		formPanel.setWidth(600);
		formPanel.setLabelAlign(Position.LEFT);
        
		final TextField file = new TextField("File","file");
		file.setInputType("file");
		formPanel.add(file);
		HorizontalPanel hPanel = new HorizontalPanel();
		Label updateLabel = new Label("Last Imported:  ");
		updateLabel.setStyleName(normalTextStyle);
		lastLoadDate = new Label();
		lastLoadDate.setStyleName(normalTextStyle);
		hPanel.setStyleName("ltlfTabContentPanel");
		hPanel.add(updateLabel);
		hPanel.add(lastLoadDate);
		
		formPanel.add(hPanel);
		
		formPanel.addButton(new Button("Upload", new ButtonListenerAdapter() {
		    public void onClick(Button button, EventObject e) {
		        formPanel.getForm().submit(url, null, Connection.POST, "loading...", false);
		    }
		}));

        formPanel.addFormListener(new FormListenerAdapter() { 
            public void onActionComplete(Form form, int httpStatus, String responseText) {
            	if (responseText.indexOf("Error") > 0) {
            		responseText = "Unable to load this file. Are you sure this is an Excel document?";
            	}
            	if (responseText.indexOf("Success") > 0) {
            		responseText = "The Excel document has been successfully loaded";
            		fetchLastLoadDate();
            	}
                MessageBox.alert("Done", responseText); 
            } 
          public void onActionFailed(Form form, int httpStatus, java.lang.String responseText) { 
        	  System.out.println("action failed");
            } 
        }); 
		layoutPanel.add(formPanel);
	    
//		layoutPanel.add(new Button("Test", new ButtonListenerAdapter() {
//		    public void onClick(Button button, EventObject e) {
//		        callAllocTest();
//		    }
//		}));
		SimplePanel wrapperPanel = new SimplePanel();
		wrapperPanel.setStyleName("ltlfTabContentPanel");
		wrapperPanel.add(layoutPanel);
		fetchLastLoadDate();
		initWidget(wrapperPanel);
	}
	
	void fetchLastLoadDate() {
	   	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
				DateTimeFormat dateFormater = DateTimeFormat.getFormat("yyyy-MM-dd");
				lastLoadDate.setText(dateFormater.format((Date)s));
    		}

    		public void onFailure(Throwable ex) {
    			// do nothing for now
    			Window.alert("Failed to fetch: " + ex.getMessage());
    		}
    	}; 
		rpcProxy.getLtlfService().fetchCalendarLoadDate(callback);
	}

	void callAllocTest() {
	   	AsyncCallback callback = new AsyncCallback() {
    		public void onSuccess(Object s) {
				System.out.println("success");
    		}

    		public void onFailure(Throwable ex) {
    			// do nothing for now
    			Window.alert("Failed to commit: " + ex.getMessage());
    		}
    	}; 
		rpcProxy.getLtlfService().saveAllocation(null, callback);
	}
}
