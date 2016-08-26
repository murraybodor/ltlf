package ca.aeso.ltlf.client.common;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * LtlfDialog
 * A multi-use dialog box with a title, text and OK button
 * 
 * @author mbodor
 */
public class LtlfDialog extends DialogBox {

	public LtlfDialog(String title, String text) {
		
		this.setStyleName("dialog");
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setStyleName("dialogMainPanel");

		HorizontalPanel titleBar = new HorizontalPanel();
		titleBar.setStyleName("dialogTitleBar");

		Label titleLabel = new Label(title);
		titleLabel.setStyleName("dialogTitleLabel");

		titleBar.add(titleLabel);
		mainPanel.add(titleBar);

		Label contentLabel = new Label(text);
		contentLabel.setStyleName("dialogContentLabel");
		
		mainPanel.add(contentLabel);

		Label blankLabel = new Label("");
		mainPanel.add(blankLabel);

		Button ok = new Button("OK");
		ok.setStyleName("dialogButton");
		ok.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				LtlfDialog.this.hide();
			}
		});

		mainPanel.add(ok);
		mainPanel.setCellHorizontalAlignment(ok, HasHorizontalAlignment.ALIGN_CENTER);
		
		this.setWidget(mainPanel);
	}
}	
