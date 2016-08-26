package ca.aeso.ltlf.client.allocation.system;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.client.common.LtlfStyle;
import ca.aeso.ltlf.client.common.NumberInput;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class SystemAllocationGridCell extends LtlfComposite {

	private SystemAllocationGridCell _instance;
	private int rowIndex;
	private int colIndex;
	private NumberInput tbEnergy;
	
    private SystemAllocationGrid grid = null;

	public SystemAllocationGridCell(SystemAllocationGrid grid, int rowIdx, int colIdx) {
		this.rowIndex = rowIdx;
		this.colIndex = colIdx;
		this.grid = grid;

		_instance = this;

		ChangeListener changeListener = new ChangeListener() {
			public void onChange(Widget sender) {
				_instance.grid.onCellChange(_instance);
			}
		};
		KeyboardListenerAdapter keyListener = new KeyboardListenerAdapter() {
			public void onKeyPress(Widget sender, char keyCode, int modifiers)
			{
				if (keyCode == KeyboardListenerAdapter.KEY_ENTER)
					_instance.grid.onCellChange(_instance);
			}
			
			public void onKeyUp(Widget sender, char keyCode, int modifiers)
			{
				if (keyCode == KeyboardListenerAdapter.KEY_DOWN
						|| keyCode == KeyboardListenerAdapter.KEY_UP) 
					_instance.grid.onCellNavigate(_instance, keyCode);
			}
		};
		tbEnergy = new NumberInput();
		tbEnergy.setStyleName(LtlfStyle.inputField);
		tbEnergy.addStyleName(LtlfStyle.normalText);
		tbEnergy.addChangeListener(changeListener);
		tbEnergy.addKeyboardListener(keyListener);
		
		SimplePanel wrapper = new SimplePanel();
		wrapper.add(tbEnergy);
		
		initWidget(wrapper);
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

	public NumberInput getEnergyInput()
	{
		return this.tbEnergy;
	}
	
	public Float getEnergyValue()
	{
		return (tbEnergy.getText().trim().length() > 0  ? Float.valueOf(tbEnergy.getText()) : null);
	}
	
	public void setEnergyValue(Float energy) {
		tbEnergy.setText(energy == null ? "" : energy.toString());
	}
}
