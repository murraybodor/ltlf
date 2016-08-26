package ca.aeso.ltlf.model;

import java.util.Comparator;

/**
 * Comparator to support sorting of lists of LoadShapeDetails for graphing
 * @author mbodor
 */
public class ShapeComparer implements Comparator {

	private String valueKey;
	
	public ShapeComparer(String valueKey) {
		this.valueKey = valueKey;
	}
	
	public int compare(Object obj1, Object obj2) {

		if (!(obj1 instanceof LoadShapeDetail) || !(obj2 instanceof LoadShapeDetail))
			return -1;

		LoadShapeDetail sw1 = (LoadShapeDetail) obj1;
		LoadShapeDetail sw2 = (LoadShapeDetail) obj2;

		if (this.valueKey.equals("time")) {

			int sw2Day = sw2.getBaseDay();
			int sw1Day = sw1.getBaseDay();
			
			if (sw2Day>sw1Day)
				return -1;
			else if (sw2Day<sw1Day)
				return 1;
			else {
				// days are equal, compare hours
				int sw2Hour = sw2.getBaseHourEnding();
				int sw1Hour = sw1.getBaseHourEnding();

				if (sw2Hour>sw1Hour) {
					return -1;
				} else if (sw2Hour<sw1Hour) {
					return 1;
				} else
					return 0;
			}
		} else {
			return sw2.getValue(this.valueKey).compareTo(sw1.getValue(this.valueKey));
		}
	}
}
