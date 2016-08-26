package ca.aeso.ltlf.client.common;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * LtlfGridTextBox
 * Extends TextBox to remember it's location in a grid, and customize events
 * @author mbodor
 *
 */
public class LtlfGridTextBox extends TextBox {

	private int rowIndex;
	private int colIndex;
    private ChangeListener updater = null;

	FocusListener focusListener = new FocusListener() {
		public void onFocus(Widget sender) {
			
		}
		public void onLostFocus(Widget sender) {
			
		}
	};

	ChangeListener changeListener = new ChangeListener() {
		public void onChange(Widget sender) {
			updater.onChange(sender);
		}
	};

	KeyboardListener keyListener = new KeyboardListener() {
		public void onKeyPress(Widget sender, char a, int b) {
			
		}
		public void onKeyDown(Widget sender, char a, int b) {
			
		}
		public void onKeyUp(Widget sender, char a, int b) {
			
		}

	};
	
	public LtlfGridTextBox(ChangeListener onUpdate, int rowIdx, int colIdx) {
		super();
		this.rowIndex = rowIdx;
		this.colIndex = colIdx;
		this.updater = onUpdate;
		
		this.addFocusListener(focusListener);
		this.addChangeListener(changeListener);
		this.addKeyboardListener(keyListener);
	}
	
	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	public void setValue(String text) {
		this.setText(text);
	}
}
