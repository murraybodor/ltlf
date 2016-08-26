package ca.aeso.ltlf.client.common;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.MenuListenerAdapter;

import ca.aeso.ltlf.client.event.LtlfCompositeEvent;

public class LtlfMenu extends LtlfComposite {
    private Panel panel = new Panel();
    private ToolbarButton toolButtonCalendar = new ToolbarButton("Calendar");
    private ToolbarButton toolButtonLoadShapes = new ToolbarButton("Load Shapes");
    private ToolbarButton toolButtonEnergy = new ToolbarButton("Energy Factors");
//    private ToolbarButton toolButtonForecast = new ToolbarButton("Forecast");
//    private ToolbarButton toolButtonReports = new ToolbarButton("Reports");

    private boolean isShowingMenu = false;
    
	public LtlfMenu()
	{
        Toolbar toolbar = new Toolbar();

        String dummyCss = "nothing";

        ButtonListenerAdapter buttonListener = new ButtonListenerAdapter() {
        	public void onMouseOver(Button button, EventObject e)
        	{
        		if (isShowingMenu)	// simulate the button click to display the menu
        			button.showMenu();
        	}
        };

        MenuListenerAdapter menuListener = new MenuListenerAdapter() {
        	public void onClick(Menu menu, java.lang.String menuItemId, EventObject e)
        	{
        		Item item = (Item)menu.getItem(menuItemId);
        		if (item != null)
        			fireLtlfCompositeEvent(new LtlfCompositeEvent(item, LtlfCompositeEvent.EventCode.MenuItemSelected, item.getText()));
        	}
        	
        	public void onHide(Menu menu)
        	{
        		isShowingMenu = false;
        	}
        	
        	public void onShow(Menu menu)
        	{
        		isShowingMenu = true;
        	}
        };
        
        // Calendar
        this.toolButtonCalendar.setStyleName(dummyCss);
        this.toolButtonCalendar.addListener(buttonListener);
        Menu menu = createMenu(new String[] {
        		LtlfMenuItems.CALENDAR_DATE_MAPPING});
        menu.addListener(menuListener);
        this.toolButtonCalendar.setMenu(menu);
        toolbar.addButton(this.toolButtonCalendar);

	    // Load Shapes
        this.toolButtonLoadShapes.setStyleName(dummyCss);
        this.toolButtonLoadShapes.addListener(buttonListener);
        menu = createMenu(new String[] {
        		LtlfMenuItems.LOAD_HISTORY_AND_SHAPES});
        menu.addListener(menuListener);
        this.toolButtonLoadShapes.setMenu(menu);
        toolbar.addButton(this.toolButtonLoadShapes);

	    // Energy Factors
        this.toolButtonEnergy.setStyleName(dummyCss);
        this.toolButtonEnergy.addListener(buttonListener);
        menu = createMenu(new String[] {
        		LtlfMenuItems.ENERGY_ALLOCATE_ENERGY});
        menu.addListener(menuListener);
        this.toolButtonEnergy.setMenu(menu);
        toolbar.addButton(this.toolButtonEnergy);

//	    // Forecast
//        this.toolButtonForecast.setStyleName(dummyCss);
//        this.toolButtonForecast.addListener(buttonListener);
//        menu = createMenu(new String[] {
//        		LtlfMenuItems.FORECAST_PREVIEW,
//        		LtlfMenuItems.FORECAST_GENERATE,
//        		LtlfMenuItems.FORECAST_APPLY_TRANSMISSION_LOSSES,
//        		LtlfMenuItems.FORECAST_PUBLISH});
//        menu.addListener(menuListener);
//        this.toolButtonForecast.setMenu(menu);
//        toolbar.addButton(this.toolButtonForecast);
//
//	    // Reports
//        this.toolButtonReports.setStyleName(dummyCss);
//        this.toolButtonReports.addListener(buttonListener);
//        menu = createMenu(new String[] {
//        		LtlfMenuItems.REPORT_PROJECT_SUMMARY,
//        		LtlfMenuItems.REPORT_PROJECT_DETAIL,
//        		LtlfMenuItems.REPORT_GROWTH_ALLOCATIONS,
//        		LtlfMenuItems.REPORT_ENERGY_FACTORS,
//        		LtlfMenuItems.REPORT_MP_VALIDATION,
//        		LtlfMenuItems.REPORT_FORECAST,
//        		LtlfMenuItems.REPORT_MP_LOAD_HISTORY});
//        menu.addListener(menuListener);
//        this.toolButtonReports.setMenu(menu);
//        toolbar.addButton(this.toolButtonReports);

        panel.setBorder(false);
        panel.setPaddings(0);
        panel.add(toolbar);
        
        initWidget(panel);
	}
	
	private Menu createMenu(String[] menuItems)
	{
        Menu menu = new Menu();
        menu.setShadow(true);
        menu.setMinWidth(10);

        for (int i = 0; i < menuItems.length; i++)
        {
	        Item menuItem = new Item();
	        menuItem.setText(menuItems[i]);
	        menu.addItem(menuItem);
        }
        
        return menu;
	}
}
