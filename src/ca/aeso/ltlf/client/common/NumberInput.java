package ca.aeso.ltlf.client.common;

import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class NumberInput extends TextBox
{
	public NumberInput()
	{
		KeyboardListenerAdapter keyListener = new KeyboardListenerAdapter() {
			public void onKeyPress(Widget sender, char keyCode, int modifiers)
			{
				if ((keyCode < '0' || keyCode > '9') && keyCode != '.')
				{
					TextBox textBox = (TextBox)sender;
					textBox.cancelKey();
				}
			}
		};
		
		addKeyboardListener(keyListener);
	}
}
