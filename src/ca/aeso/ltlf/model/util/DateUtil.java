package ca.aeso.ltlf.model.util;

import java.util.Date;

public class DateUtil {

	public static boolean checkDateRange(Date inDate, int inHe, Date startDate, int startHe, Date endDate, int endHe) {

		
		if (inDate.compareTo(startDate) < 0 || inDate.compareTo(endDate) > 0) {
			return false;
		}

		// in date = start date, so compare start hours
		if (inDate.compareTo(startDate)==0) {
			if (inHe >= startHe) {
				return true;
			} else {
				return false;
			}
				
		}
		
		// in date is greater than start dat but less than end date
		if (inDate.compareTo(startDate)>0 && inDate.compareTo(endDate) < 0) {
			return true;
		}
			

		// in date = end date, so compare end hours
		if (inDate.compareTo(endDate)==0) {
			if (inHe <= endHe) {
				return true;
			} else {
				return false;
			}
		}

		return false;
		
	}
	
}
