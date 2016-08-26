package ca.aeso.ltlf.client.allocation;

import ca.aeso.ltlf.model.Area;
import ca.aeso.ltlf.model.MeasurementPoint;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

/**
 * LtlfAllocationCommentButton
 * Extends PushButton to remember it's column location in a grid, and Area and MP  
 * @author mbodor
 *
 */
public class LtlfAllocationCommentButton extends PushButton {

	private int colIndex;
	private Area area;
	private String areaCode;
	private MeasurementPoint mp;
//	private String mpName;
//	private Long mpOid;
	
	public LtlfAllocationCommentButton(Image image, int colIdx) {
		super(image);
		this.colIndex = colIdx;
	}
	
	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

//	public String getMpName() {
//		return mpName;
//	}
//
//	public void setMpName(String mpName) {
//		this.mpName = mpName;
//	}
//
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

//	public Long getMpOid() {
//		return mpOid;
//	}
//
//	public void setMpOid(Long mpOid) {
//		this.mpOid = mpOid;
//	}
//
	public MeasurementPoint getMp() {
		return mp;
	}

	public void setMp(MeasurementPoint mp) {
		this.mp = mp;
	}


}
