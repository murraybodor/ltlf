package ca.aeso.ltlf.client.util;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Reader;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;

public class Utils {
	public static void addHtml(Panel panel, String html)
	{
		panel.add(new HTML(html));
	}
	
	public static void addHtml(com.gwtext.client.widgets.Panel panel, String html)
	{
		panel.add(new HTML(html));
	}

    public static void displayDataInGrid(GridPanel gridPanel, ColumnModel columnModel, Object[][] data, Reader dataReader)
    {
        MemoryProxy proxy = new MemoryProxy(data);

        Store store = new Store(proxy, dataReader);
        store.load();

        if (gridPanel.getStore() == null)
        {
        	gridPanel.setStore(store);
        	gridPanel.setColumnModel(columnModel);
        }
        else
        	gridPanel.reconfigure(store, columnModel);
    }
}
