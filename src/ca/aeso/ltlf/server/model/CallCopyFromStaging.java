package ca.aeso.ltlf.server.model;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CallCopyFromStaging extends StoredProcedure {
	public static final String versionId = "$Id: CallCopyFromStaging.java,v 1.4 2008/06/09 19:08:48 schen Exp $ by $Author: schen $, $DateTime:  $";
	private static final Log log = LogFactory.getLog(CallCopyFromStaging.class);
	private static final String STORED_FUNC_NAME = "pkg_LTLF.CopyFromStaging";
	public static final String ErrorKey = "ErrorString";
	public static final String RecordCountKey = "RecordCount";
	
	   public CallCopyFromStaging(DataSource ds) {
	        super(ds,STORED_FUNC_NAME);
	        setFunction(true);
	        declareParameter(new SqlOutParameter("RETURN_VALUE", Types.INTEGER));
	        declareParameter(new SqlOutParameter("pnRowCount", Types.INTEGER));
	        declareParameter(new SqlOutParameter("psError", Types.VARCHAR));
	        declareParameter(new SqlParameter("i_mp_name", Types.VARCHAR));
	        compile();
	    }
	   
	  // RETURN_VALUE := pkg_LTLF.CopyFromStaging(pnRowCount out number, psError out varchar2, i_mp_id in varchar2)
	   
	    public HashMap<String, Object> execute(String mpName) {
	    	Integer recordcount = null;
	    	HashMap<String, Object> returnResults = new HashMap<String, Object>();
	    	
	        HashMap<String, Object> inParams = new HashMap<String, Object>();
	        inParams.put("i_mp_name", mpName);
	        String errors = null;
	        log.debug("Calling CopyFromStaging");
	        Map<String, Object> outParams = super.execute(inParams);
	        if (outParams.size() > 0) {
	        	if (outParams.get("psError") != null) {
	        		errors =  outParams.get("psError").toString();
	                log.debug("Received errors from server " + errors);
	                returnResults.put(ErrorKey,errors);
	        	}
	        	recordcount =  (Integer)outParams.get("pnRowCount");
	        	returnResults.put(RecordCountKey,recordcount);
	        }

	        return returnResults;
	    }
}
