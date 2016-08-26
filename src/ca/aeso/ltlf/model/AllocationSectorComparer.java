package ca.aeso.ltlf.model;

import java.util.Comparator;

/**
 * Comparator to support sorting of lists of AllocationSector for graphing
 * @author mbodor
 */
public class AllocationSectorComparer implements Comparator {

	public AllocationSectorComparer() {
	}
	
	public int compare(Object obj1, Object obj2) {

		if (!(obj1 instanceof AllocationSector) || !(obj2 instanceof AllocationSector))
			return -1;

		AllocationSector sw1 = (AllocationSector) obj1;
		AllocationSector sw2 = (AllocationSector) obj2;

		return sw1.getSectorType().compareTo(sw2.getSectorType());
	}
}
