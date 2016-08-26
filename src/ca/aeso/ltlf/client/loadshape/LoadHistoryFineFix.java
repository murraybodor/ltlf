package ca.aeso.ltlf.client.loadshape;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.model.AnalysisDetail;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;

/**
 * LoadHistoryFineFix
 * Fine fix control on MP Analysis Editor UI 
 * @author mbodor
 */
public class LoadHistoryFineFix extends LtlfComposite { 

	private FlexTable fixTable = new FlexTable();
	private DateTimeFormat dateFmtSlash = DateTimeFormat.getFormat("yyyy/MM/dd");
	private DateTimeFormat dateFmtDash = DateTimeFormat.getFormat("yyyy-MM-dd");
	private DateTimeFormat dateFmtNopunc = DateTimeFormat.getFormat("yyyyMMdd");

	public LoadHistoryFineFix() {
	    fixTable.setCellSpacing(0);
	    fixTable.setCellPadding(0);
	    fixTable.setBorderWidth(1);
	    fixTable.setWidth("180px");
	    
	    initWidget(fixTable);
	    setStyleName("fineFixLoadTable");
	    initTable();
	}

	private void initTable() {
		fixTable.setHTML(0, 0, "<b>Date</b>");
		fixTable.setHTML(0, 1, "<b>HE</b>");
		fixTable.setHTML(0, 2, "<b>Corrected</b>");
		fixTable.setHTML(0, 3, "<b>Override</b>");
		fixTable.getRowFormatter().setStyleName(0, "fineFixLoadTableHeader");
	}
	
	public void loadData(List<AnalysisDetail> data) {
		  
		int rows = fixTable.getRowCount();
		
		if (rows>1) {
			for (int i = 2; i <= rows; i++) {
				fixTable.removeRow(1);
			}
		}

		try {
		    for (int i=0; i < data.size(); ++i) {
		    	AnalysisDetail aLoad = (AnalysisDetail)data.get(i);

			    fixTable.setHTML(i + 1, 0, dateFmtDash.format(aLoad.getLoadDate()));
			    fixTable.setHTML(i + 1, 1, aLoad.getLoadHourEnd().toString());
			    fixTable.setHTML(i+1, 2, aLoad.getValue(AnalysisDetail.CORRECTED_TYPE).toString());

			    TextBox fixTb = new TextBox();
			    fixTb.setWidth("50px");
			    Double fixVal = aLoad.getValue(AnalysisDetail.FIXED_TYPE);
			    
			    if (fixVal==null)
			    	fixTb.setText("");
			    else {
				    fixTb.setText(fixVal.toString());
			    }
			    
			    fixTable.setWidget(i+1, 3, fixTb);
			    
		    }
		} catch (Exception e) {}
	}
	
	public List<AnalysisDetail> getData() {

		int rows = fixTable.getRowCount();
		List<AnalysisDetail> fineFixList = new ArrayList<AnalysisDetail>();
		
		for (int i = 0; i < rows; i++) { 

			if (i==0) { // skip header row
				continue;
			}
			
			// create a load wrapper
			AnalysisDetail load = new AnalysisDetail();

			// get the value in the override column
			String date = fixTable.getHTML(i, 0);
			String he = fixTable.getHTML(i, 1);
			String fineFixStr = ((TextBox)fixTable.getWidget(i, 3)).getText();
			Double fineFix = null;

			try {
				fineFix = Double.valueOf(fineFixStr);
				long fineFixL = Math.round(fineFix * 1000000);
				fineFix = new Long(fineFixL).doubleValue() / 1000000;
			} catch (NumberFormatException ne) {
				
			}

			load.setLoadDate(parseDate(date));
			load.setLoadHourEnd(new Integer(he));
			load.setLoadMw(fineFix);
			
			fineFixList.add(load);
			
		}

		return fineFixList;
		
	}
	
	private Date parseDate(String dateStr) {
		Date aDate = null;
		
		try {
			aDate = dateFmtSlash.parse(dateStr);
		} catch (IllegalArgumentException iae) {
			try {
				aDate = dateFmtDash.parse(dateStr);
			} catch (IllegalArgumentException iae2) {
				try {
					aDate = dateFmtNopunc.parse(dateStr);
				} catch (IllegalArgumentException iae3) {}
				
			}
		}
		return aDate;
	}
	
}
