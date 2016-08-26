package ca.aeso.ltlf.client.allocation.area;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfStyle;
import ca.aeso.ltlf.client.common.NumberInput;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * AreaAllocationGridCell
 * 
 * @author mbodor
 */
public class AreaAllocationGridCell extends LtlfComposite {

	public enum InputField {Percentage};
	
	private AreaAllocationGridCell _instance;
	private int rowIndex;
	private int colIndex;
    private AreaAllocationGrid grid = null;
    private String areaSectorId;
	private NumberInput tbPercentage;
	private Label energyLabel;
	
//	private static final NumberFormat energyFormat = NumberFormat.getFormat("#,###,##0.00");
//	private static final NumberFormat percentFormat = NumberFormat.getFormat("##0.00");
	
	ChangeListener changeListener1 = new ChangeListener() {
		public void onChange(Widget sender) {
			recalculateEnergy();
			_instance.grid.onCellChange(_instance);
		}
	};
	
	KeyboardListenerAdapter keyListener1 = new KeyboardListenerAdapter() {
		public void onKeyPress(Widget sender, char keyCode, int modifiers)
		{
			if (keyCode == KeyboardListenerAdapter.KEY_ENTER)
				_instance.grid.onCellChange(_instance);
		}
		
		public void onKeyUp(Widget sender, char keyCode, int modifiers)
		{
			if (keyCode == KeyboardListenerAdapter.KEY_DOWN
					|| keyCode == KeyboardListenerAdapter.KEY_UP) 
				_instance.grid.onCellNavigate(_instance, keyCode, InputField.Percentage);
		}
	};
	
	public AreaAllocationGridCell(AreaAllocationGrid grid, int rowIdx, int colIdx) {
		
		rowIndex = rowIdx;
		colIndex = colIdx;
		this.grid = grid;
		_instance = this;
		
		tbPercentage = new NumberInput();
		tbPercentage.setStyleName(LtlfStyle.areaAllocationPercentageInput);
		tbPercentage.addStyleName(LtlfStyle.normalText);
		tbPercentage.addChangeListener(changeListener1);
		tbPercentage.addKeyboardListener(keyListener1);
		
		energyLabel = new Label();
		energyLabel.addStyleName(LtlfStyle.normalText);
//		energyLabel.setWidth("75px");
		
		FlexTable layoutGrid = new FlexTable();
		FlexTable.FlexCellFormatter cf = layoutGrid.getFlexCellFormatter();
		layoutGrid.setCellPadding(0);
		layoutGrid.setCellSpacing(0);
		layoutGrid.setStyleName(LtlfStyle.areaAllocationInputLayoutGrid);
		
		cf.setStyleName(0, 0, LtlfStyle.areaAllocationPercentageCell);
		SimplePanel wrapper = new SimplePanel();
		wrapper.add(tbPercentage);
		layoutGrid.setWidget(0, 0, wrapper);
		
		cf.setStyleName(0, 1, LtlfStyle.areaAllocationEnergyLabel);
		wrapper = new SimplePanel();
		wrapper.add(energyLabel);
		layoutGrid.setWidget(0, 1, wrapper);
		
		initWidget(layoutGrid);
	}
	
	public void recalculateEnergy()
	{
		setEnergyValue(_instance.grid.getRowHeader().getTotalEnergy(getRowIndex()) * getPercentageValue()/100);
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

	public NumberInput getPercentageInput()
	{
		return this.tbPercentage;
	}
	
	public Float getPercentageValue()
	{
		return (tbPercentage.getText().trim().length() > 0  ? Float.valueOf(tbPercentage.getText()) : new Float(0));
	}
	
	public void setPercentageValue(Float percent) {
		tbPercentage.setText(percent == null ? "0.0" : percent.toString());
	}

	public Label getEnergyLabel()
	{
		return this.energyLabel;
	}
	
	public Float getEnergyValue()
	{
		return (energyLabel.getText().trim().length() > 0  ? Float.valueOf(energyLabel.getText()) : new Float(0));
	}
	
	public void setEnergyValue(Float energy) {
		energyLabel.setText(energy == null ? "0" : energy.toString());
	}

	public String getAreaSectorId() {
		return areaSectorId;
	}

	public void setAreaSectorId(String areaSectorId) {
		this.areaSectorId = areaSectorId;
	}
	
	
//	private Float roundTwoDecimals(Float value) {
//		
//		int valueL = Math.round(value * 100);
//		Float newVal = new Float(valueL);
//		return new Float(newVal/100);
//		
//	}
	
}
