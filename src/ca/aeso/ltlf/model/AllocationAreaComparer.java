package ca.aeso.ltlf.model;

import java.util.Comparator;

/**
 * Comparator to support sorting of lists of AllocationAreas
 * @author mbodor
 */
public class AllocationAreaComparer implements Comparator {

	public AllocationAreaComparer() {
	}
	
	public int compare(Object obj1, Object obj2) {

		if (!(obj1 instanceof AllocationArea) || !(obj2 instanceof AllocationArea))
			return -1;

		AllocationArea sw1 = (AllocationArea) obj1;
		AllocationArea sw2 = (AllocationArea) obj2;

		Integer sw1I = new Integer(sw1.getArea().getCode());
		Integer sw2I = new Integer(sw2.getArea().getCode());
		
		return sw1I.compareTo(sw2I);
	}
}
