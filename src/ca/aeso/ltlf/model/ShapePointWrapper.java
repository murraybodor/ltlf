package ca.aeso.ltlf.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Load Shape point wrapper object
 * @author mbodor
 */
public class ShapePointWrapper {

	private Long mpOid;
	private List<LoadShapeDetail> shapeValues;
	private int size;
	private int numFixes;
	
	public ShapePointWrapper(Long mpOid, List<LoadShapeDetail> shapeValues) {
		this.mpOid = mpOid;
		this.shapeValues = shapeValues;
		this.size = shapeValues.size();
	}
	
	public List<LoadShapeDetail> getAllLoadValues() {
		return shapeValues;
	}
	public void setLoadValues(List<LoadShapeDetail> shapeValues) {
		this.shapeValues = shapeValues;
	}

	public List<LoadShapeDetail> getShapeValues(int startDay, int startHe, int endDay, int endHe) {

		List<LoadShapeDetail> vals = new ArrayList();
		
		for (Iterator iterator = shapeValues.iterator(); iterator.hasNext();) {
			LoadShapeDetail point = (LoadShapeDetail) iterator.next();

			if (startDay==endDay) {
				if (point.getBaseHourEnding() >= startHe & point.getBaseHourEnding() <= endHe) {
					vals.add(point);
				}
			} else {
				if ( (point.getBaseDay()==startDay & point.getBaseHourEnding() >= startHe) ||
					(point.getBaseDay() > startDay & point.getBaseDay() < endDay) ||
					(point.getBaseDay()==endDay & point.getBaseHourEnding()<=endHe) ) {
					vals.add(point);
				}
			}
		}
		return vals;
	}

	public List<LoadShapeDetail> getChangedLoadValues() {

		List<LoadShapeDetail> vals = new ArrayList();
		
		for (Iterator iterator = shapeValues.iterator(); iterator.hasNext();) {
			LoadShapeDetail load = (LoadShapeDetail) iterator.next();
			if (load.isChanged()) {
				vals.add(load);
			}
		}
		return vals;
	}
	
	public void undoFixes() {
		for (Iterator iterator = shapeValues.iterator(); iterator.hasNext();) {
			LoadShapeDetail load = (LoadShapeDetail) iterator.next();
			load.undoFix();
		}
		this.numFixes=0;
	}

	public void applyFixes(String userId) {
		for (Iterator iterator = shapeValues.iterator(); iterator.hasNext();) {
			LoadShapeDetail load = (LoadShapeDetail) iterator.next();
			load.applyFix(userId);
		}
		this.numFixes=0;
	}

	public void removeOverrides() {
		for (Iterator iterator = shapeValues.iterator(); iterator.hasNext();) {
			LoadShapeDetail point = (LoadShapeDetail) iterator.next();
			point.removeOverride();
		}
	}

	public Long getMpOid() {
		return mpOid;
	}

	public int getSize() {
		return size;
	}

	public int getNumFixes() {
		return numFixes;
	}

	public void setNumFixes(int numFixes) {
		this.numFixes = numFixes;
	}
}
