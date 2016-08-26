package ca.aeso.ltlf.server.model;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CallSuggestedGapAnalysis extends StoredProcedure {
	private static final Log log = LogFactory.getLog(StoredProcedure.class);
	private static final String STORED_PROC_NAME = "load_staging.gapanalysissetsuggested";
	
	   public CallSuggestedGapAnalysis(DataSource ds) {
	        super(ds,STORED_PROC_NAME);

	       // declareParameter(new SqlParameter("pdStartDt", Types.DATE));
	      //  declareParameter(new SqlParameter("pdEndDt", Types.DATE));
	      //  declareParameter(new SqlOutParameter("psError", Types.VARCHAR));
	      //  declareParameter(new SqlOutParameter("RESULT", Types.INTEGER));
	      //  declareParameter(new SqlOutParameter("pnrowcount", Types.INTEGER));
	        setFunction(false);
	        declareParameter(new SqlParameter("psName", Types.VARCHAR));
	        declareParameter(new SqlParameter("psDesc", Types.VARCHAR));
	        declareParameter(new SqlOutParameter("psError", Types.VARCHAR));
	        declareParameter(new SqlOutParameter("result", Types.INTEGER));

	        compile();
	    }
	   /*
   function GapAnalysisSetSuggested(psName in string,
            psTargetMP_ID in varchar2,
			pdTargetDtStart in date,pdTargetDtEnd in date,
			psTargetCalHourEndingStart in varchar2,psTargetCalHourEndingEnd in varchar2,
            psSourceTable in varchar2,
			piSourceVersionID in number,
            psSourceMP_ID in varchar2,
            pdSourceDtStart in date,
			psSourceCalHourEndingStart in varchar2,
			psError out varchar2) return number
			
       */        
	   public Integer execute(String analysisName, String targetMPID,
			   Date targetStartDate, Date targetEndDate, 
			   String targetStartDateHourEnding, 
			   String targetEndDateHourEnding,
			   String sourceMPID,
			   Date sourceStartDate,
			   String sourceStartDateHourEnding,
			   String errorString){
	    	Integer status = null;
	    	String sourceTable = "STAGING";
	    	String errors = null;
	    	
	        HashMap inParams = new HashMap(5);
	        inParams.put("psName", analysisName);
	        inParams.put("psTargetMP_ID", targetMPID);
	        inParams.put("pdTargetDtStart", targetStartDate);
	        inParams.put("pdTargetDtEnd", targetEndDate);
	        inParams.put("psTargetCalHourEndingStart", targetStartDateHourEnding);
	        inParams.put("psTargetCalHourEndingEnd", targetEndDateHourEnding);
	        inParams.put("psSourceTable", sourceTable);
	        
	        Map outParams = super.execute(inParams);
	        if (outParams.size() > 0) {
	        	if (outParams.get("psError") != null) {
	        		errors =  outParams.get("psError").toString();
	        	}
	        	status =  (Integer)outParams.get("result");
	        } 
	        return status;
	    }
}
