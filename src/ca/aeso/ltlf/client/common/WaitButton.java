package ca.aeso.ltlf.client.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class WaitButton extends LtlfComposite implements ClickListener {
	private Button button = null;
	private Image image = new Image();
	private List<ClickListener> listenerList = new ArrayList<ClickListener>();
	
	public WaitButton()
	{
		this(null, null);
	}
	
	public WaitButton(String html)
	{
		this(html, null);
	}
	
	public WaitButton(String html, ClickListener listener)
	{
		if (html != null && listener != null)
			this.button = new Button(html, listener);
		else if (html != null)
			this.button = new Button(html);
		else
			this.button = new Button();
		
		Grid grid = new Grid(1, 2);
		grid.getRowFormatter().setVerticalAlign(0, HasVerticalAlignment.ALIGN_MIDDLE);
		grid.setWidget(0, 0, this.button);
		grid.setWidget(0, 1, this.image);
		
		initWidget(grid);
		
		this.button.addClickListener(this);
		this.image.setUrl("images/loading.gif");
		this.image.setVisible(false);
	}

	public void onClick(Widget widget)
	{
		Iterator<ClickListener> itr = this.listenerList.iterator();
		while (itr.hasNext())
			itr.next().onClick(widget);
	}
	
	public void setImageUrl(String url)
	{
		this.image.setUrl(url);
	}
	
	public void click()
	{
		this.button.click();
	}
	
	public String getHTML()
	{
		return this.button.getHTML();
	}
	
	public void setHTML(String html)
	{
		this.button.setHTML(html);
	}
	
	public String getText()
	{
		return this.button.getText();
	}
	
	public void setText(String text)
	{
		this.button.setText(text);
	}
	
	public String getTitle()
	{
		return this.button.getTitle();
	}
	
	public void setTitle(String title)
	{
		this.button.setTitle(title);
	}
	
	public void addClickListener(ClickListener listener)
	{
		this.listenerList.add(listener);
	}
	
	public void removeClickListener(ClickListener listener)
	{
		Iterator<ClickListener> itr = this.listenerList.iterator();
		while (itr.hasNext())
		{
			if (itr.next().equals(listener))
			{
				itr.remove();
				break;
			}
		}
	}
	
	public void setEnabled(boolean enabled)
	{
		this.button.setEnabled(enabled);
		this.image.setVisible(false);
	}
	
	public boolean isEnalbed()
	{
		return this.button.isEnabled();
	}
	
	public String getStyleName()
	{
		return this.button.getStyleName();
	}
	
	public void addStyleName(String style)
	{
		this.button.addStyleName(style);
	}
	
	public void setStyleName(String style)
	{
		this.button.setStyleName(style);
	}
	
	public void addStyleDependentName(String styleSuffix)
	{
		this.button.addStyleDependentName(styleSuffix);
	}
	
	public void removeStyleDependentName(String styleSuffix)
	{
		this.button.removeStyleDependentName(styleSuffix);
	}
	
	public String getStylePrimaryName()
	{
		return this.button.getStylePrimaryName();
	}
	
	public void setStylePrimaryName(String style)
	{
		this.button.setStylePrimaryName(style);
	}
	
	public void setHeight(String height)
	{
		this.button.setHeight(height);
	}
	
	public void setWidth(String width)
	{
		this.button.setWidth(width);
	}
	
	public void setWaiting(boolean waiting)
	{
		if (waiting)
		{
			this.button.setEnabled(false);
			this.image.setVisible(true);
		}
		else
		{
			this.button.setEnabled(true);
			this.image.setVisible(false);
		}
	}
}
