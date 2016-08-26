package ca.aeso.ltlf.client.common;

import com.gwtext.client.widgets.Panel;

public class LtlfBorderlessPanel extends Panel {
	public LtlfBorderlessPanel()
	{
		this(null);
	}

	public LtlfBorderlessPanel(String title)
	{
		if (title != null)
			setTitle(title);

		setBorder(false);
		setBodyBorder(false);
	}
}
