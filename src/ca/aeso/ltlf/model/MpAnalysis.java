package ca.aeso.ltlf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ca.aeso.ltlf.model.util.DateUtil;

/**
 * Aggregate MP Analysis object
 * @author mbodor
 */
public class MpAnalysis {

	private Long mpOid;
	private List<AnalysisDetail> loadValues;
	private int size;
	private int numFixes;
	private Double maxValue;
	
	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public MpAnalysis(Long mpOid, Double maxValue, List<AnalysisDetail> loadValues) {
		this.mpOid = mpOid;
		this.maxValue = maxValue;
		this.loadValues = loadValues;
		if (loadValues!=null)
			this.size = loadValues.size();
		else
			this.size = 0;
	}
	
	public List<AnalysisDetail> getAllLoadValues() {
		return loadValues;
	}
	public void setLoadValues(List<AnalysisDetail> loadValues) {
		this.loadValues = loadValues;
	}

	public List<AnalysisDetail> getLoadValues(Date startDate, int startHe, Date endDate, int endHe) {

		if (loadValues!=null) {
			List<AnalysisDetail> vals = new ArrayList();
			
			for (Iterator iterator = loadValues.iterator(); iterator.hasNext();) {
				AnalysisDetail load = (AnalysisDetail) iterator.next();
				
				if (DateUtil.checkDateRange(load.getLoadDate(), load.getLoadHourEnd(), startDate, startHe, endDate, endHe)) {
					vals.add(load);
				}
			}
			return vals;
		} else {
			return null;
		}
	}

	public List<AnalysisDetail> getChangedLoadValues() {

		List<AnalysisDetail> vals = new ArrayList();
		
		for (Iterator iterator = loadValues.iterator(); iterator.hasNext();) {
			AnalysisDetail load = (AnalysisDetail) iterator.next();
			if (load.isChanged()) {
				vals.add(load);
			}
		}
		return vals;
	}
	
	public void undoFixes() {
		for (Iterator iterator = loadValues.iterator(); iterator.hasNext();) {
			AnalysisDetail load = (AnalysisDetail) iterator.next();
			load.undoFix();
		}
		this.numFixes=0;
	}

	public void applyFixes(String userId) {
		for (Iterator iterator = loadValues.iterator(); iterator.hasNext();) {
			AnalysisDetail load = (AnalysisDetail) iterator.next();
			load.applyFix(userId);
			load.undoFix(); // clear out the fix value
		}
		this.numFixes=0;
	}

	public void removeOverrides() {
		for (Iterator iterator = loadValues.iterator(); iterator.hasNext();) {
			AnalysisDetail load = (AnalysisDetail) iterator.next();
			load.removeOverride();
		}
	}

	public Long getMpOid() {
		return mpOid;
	}

//	public void setMpName(String mpName) {
//		this.mpName = mpName;
//	}
//
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
