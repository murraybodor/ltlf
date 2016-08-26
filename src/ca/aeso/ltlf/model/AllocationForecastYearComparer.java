package ca.aeso.ltlf.model;

import java.util.Comparator;

/**
 * Comparator to support sorting of lists of AllocationForecastYears for graphing
 * @author mbodor
 */
public class AllocationForecastYearComparer implements Comparator {

	public AllocationForecastYearComparer() {
	}
	
	public int compare(Object obj1, Object obj2) {

		if (!(obj1 instanceof AllocationForecastYear) || !(obj2 instanceof AllocationForecastYear))
			return -1;

		AllocationForecastYear sw1 = (AllocationForecastYear) obj1;
		AllocationForecastYear sw2 = (AllocationForecastYear) obj2;

		return sw1.getForecastYear().compareTo(sw2.getForecastYear());
	}
}
