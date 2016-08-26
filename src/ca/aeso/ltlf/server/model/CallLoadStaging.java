package ca.aeso.ltlf.server.model;

import java.util.Date;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.object.*;
import javax.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CallLoadStaging extends StoredProcedure {
	public static final String versionId = "$Id: CallLoadStaging.java,v 1.8 2008/05/09 14:39:55 schen Exp $ by $Author: schen $, $DateTime:  $";
	public static final String ErrorKey = "ErrorString";
	public static final String ReturnCodeKey = "ReturnCode";
	private static final Log log = LogFactory.getLog(CallLoadStaging.class);
	private static final String STORED_FUNC_NAME = "pkg_LTLF.LoadFull";
	
    public CallLoadStaging(DataSource ds) {
        super(ds,STORED_FUNC_NAME);

        setFunction(true);
        declareParameter(new SqlOutParameter("RETURN_VALUE", Types.INTEGER));
        declareParameter(new SqlParameter("pdStartDt", Types.DATE));
        declareParameter(new SqlParameter("pdEndDt", Types.DATE));
        declareParameter(new SqlOutParameter("psError", Types.VARCHAR));

        compile();
    }
 //   RETURN_VALUE := pkg_LTLF.loadfull(pdstartdt => :pdstartdt,
 //           pdenddt => :pdenddt,
 //           pserror => :pserror);
    
    public HashMap<String, Object> execute(Date startDate, Date endDate) {
    	Integer returnCode = null;
        HashMap<String, Object> inParams = new HashMap<String, Object>();
        inParams.put("pdStartDt", startDate);
        inParams.put("pdEndDt", endDate);
        String errors = null;
        HashMap returnResult = new HashMap();

        Map<String, Object> outParams = super.execute(inParams);
        if (outParams.size() > 0) {
        	if (outParams.get("psError") != null) {
        		errors =  outParams.get("psError").toString();
                log.error("Received errors from server: " + errors);
                returnResult.put(ErrorKey, errors);
        	}
            returnCode =  (Integer)outParams.get("RETURN_VALUE");
        } 
        returnResult.put(ReturnCodeKey, returnCode);
        
        return returnResult;
    }
    
}
