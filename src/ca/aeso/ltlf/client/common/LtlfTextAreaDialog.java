package ca.aeso.ltlf.client.common;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * LtlfTextAreaDialog
 * A multi-use dialog box with a title, text, OK and Cancel buttons
 * 
 * @author mbodor
 */
public class LtlfTextAreaDialog extends DialogBox {

	TextArea textArea = new TextArea();
	
    private ChangeListener caller = null;
	
	ClickListener okClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			caller.onChange(textArea);
			LtlfTextAreaDialog.this.hide();
			
		}
	};

	ClickListener cancelClickListener = new ClickListener() {
		public void onClick(Widget sender) {
			LtlfTextAreaDialog.this.hide();
		}
	};
	
	public LtlfTextAreaDialog(ChangeListener caller, String title, String text) {

		this.caller = caller;
		this.setStyleName("dialog");
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setStyleName("dialogMainPanel");

		HorizontalPanel titleBar = new HorizontalPanel();
		titleBar.setStyleName("dialogTitleBar");

		Label titleLabel = new Label(title);
		titleLabel.setStyleName("dialogTitleLabel");

		titleBar.add(titleLabel);
		mainPanel.add(titleBar);

		textArea.setStyleName("commentTextArea");
		textArea.setText(text);
		
		mainPanel.add(textArea);

		Label blankLabel = new Label("");
		mainPanel.add(blankLabel);

		Button ok = new Button("OK");
		ok.setStyleName("dialogButton");
		ok.addClickListener(okClickListener);

		Button cancel = new Button("Cancel");
		cancel.setStyleName("dialogButton");
		cancel.addClickListener(cancelClickListener);

		HorizontalPanel buttonBar = new HorizontalPanel();
		
		buttonBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonBar.add(ok);
		buttonBar.add(cancel);
		buttonBar.setCellHorizontalAlignment(ok, HasHorizontalAlignment.ALIGN_CENTER);
		buttonBar.setCellHorizontalAlignment(cancel, HasHorizontalAlignment.ALIGN_CENTER);
		
		mainPanel.add(buttonBar);
		mainPanel.setCellHorizontalAlignment(buttonBar, HasHorizontalAlignment.ALIGN_CENTER);
		
		this.setWidget(mainPanel);
	}
	
}	
