package ca.aeso.ltlf.client.allocation.mp;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfStyle;
import ca.aeso.ltlf.client.common.NumberInput;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class MpAllocationGridCell extends LtlfComposite
{
	public enum InputField {Percentage, Peak};
	
	private MpAllocationGridCell _instance;
	private int rowIndex;
	private int colIndex;
    private String allocMpId;
	private NumberInput tbPercentage;
	private NumberInput tbPeak;
	private Float suv;
	private Label energyLabel;
	
    private MpAllocationGrid grid = null;

	public MpAllocationGridCell(MpAllocationGrid grid, int rowIdx, int colIdx) {
		this.rowIndex = rowIdx;
		this.colIndex = colIdx;
		this.grid = grid;

		_instance = this;

		ChangeListener changeListener1 = new ChangeListener() {
			public void onChange(Widget sender) {
				_instance.grid.onCellChange(_instance);
				recalculateEnergy();
			}
		};
		KeyboardListenerAdapter keyListener1 = new KeyboardListenerAdapter() {
			public void onKeyPress(Widget sender, char keyCode, int modifiers)
			{
				if (keyCode == KeyboardListenerAdapter.KEY_ENTER)
					recalculateEnergy();
			}
			
			public void onKeyUp(Widget sender, char keyCode, int modifiers)
			{
				if (keyCode == KeyboardListenerAdapter.KEY_DOWN
						|| keyCode == KeyboardListenerAdapter.KEY_UP) 
					_instance.grid.onCellNavigate(_instance, keyCode, InputField.Percentage);
			}
		};
		this.tbPercentage = new NumberInput();
		this.tbPercentage.setStyleName(LtlfStyle.mpAllocationPercentageInput);
		this.tbPercentage.addStyleName(LtlfStyle.normalText);
		this.tbPercentage.addChangeListener(changeListener1);
		this.tbPercentage.addKeyboardListener(keyListener1);

		ChangeListener changeListener2 = new ChangeListener() {
			public void onChange(Widget sender) {
				recalculateEnergy();
			}
		};
		KeyboardListenerAdapter keyListener2 = new KeyboardListenerAdapter() {
			public void onKeyPress(Widget sender, char keyCode, int modifiers)
			{
				if (keyCode == KeyboardListenerAdapter.KEY_ENTER)
					recalculateEnergy();
			}
			
			public void onKeyUp(Widget sender, char keyCode, int modifiers)
			{
				if (keyCode == KeyboardListenerAdapter.KEY_DOWN
						|| keyCode == KeyboardListenerAdapter.KEY_UP) 
					_instance.grid.onCellNavigate(_instance, keyCode, InputField.Peak);
			}
		};
		this.tbPeak = new NumberInput();
		this.tbPeak.setStyleName(LtlfStyle.mpAllocationPeakInput);
		this.tbPeak.addStyleName(LtlfStyle.normalText);
		this.tbPeak.addChangeListener(changeListener2);
		this.tbPeak.addKeyboardListener(keyListener2);
		
		this.energyLabel = new Label();
		
		FlexTable layoutGrid = new FlexTable();
		FlexTable.FlexCellFormatter cf = layoutGrid.getFlexCellFormatter();
		layoutGrid.setCellPadding(0);
		layoutGrid.setCellSpacing(0);
		layoutGrid.setStyleName(LtlfStyle.mpAllocationInputLayoutGrid);
		
		cf.setStyleName(0, 0, LtlfStyle.mpAllocationPercentageCell);
		SimplePanel wrapper = new SimplePanel();
		wrapper.add(this.tbPercentage);
		layoutGrid.setWidget(0, 0, wrapper);
		
		cf.setStyleName(0, 1, LtlfStyle.mpAllocationPeakCell);
		wrapper = new SimplePanel();
		wrapper.add(this.tbPeak);
		layoutGrid.setWidget(0, 1, wrapper);
		
		cf.setColSpan(1, 0, 2);
		cf.setStyleName(1, 0, LtlfStyle.mpAllocationEnergyCell);
		cf.addStyleName(1, 0, LtlfStyle.normalText);
		wrapper = new SimplePanel();
		wrapper.add(this.energyLabel);
		layoutGrid.setWidget(1, 0, wrapper);

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
		return (tbPercentage.getText().trim().length() > 0  ? Float.valueOf(tbPercentage.getText()) : null);
	}
	
	public void setPercentageValue(Float percent) {
		tbPercentage.setText(percent == null ? "" : percent.toString());
	}

	public NumberInput getPeakInput()
	{
		return this.tbPeak;
	}
	
	public Float getPeakValue()
	{
		return (tbPeak.getText().trim().length() > 0  ? Float.valueOf(tbPeak.getText()) : null);
	}
	
	public void setPeakValue(Float peak) {
		tbPeak.setText(peak == null ? "" : peak.toString());
	}

	public Label getEnergyLabel()
	{
		return this.energyLabel;
	}
	
	public Float getEnergyValue()
	{
		return (energyLabel.getText().trim().length() > 0  ? Float.valueOf(energyLabel.getText()) : null);
	}
	
	public void setEnergyValue(Float energy) {
		energyLabel.setText(energy == null ? "" : energy.toString());
	}

	public String getAllocMpId() {
		return allocMpId;
	}

	public void setAllocMpId(String allocMpId) {
		this.allocMpId = allocMpId;
	}

	public Float getSuv() {
		return suv;
	}

	public void setSuv(Float suv) {
		this.suv = suv;
	}
}
