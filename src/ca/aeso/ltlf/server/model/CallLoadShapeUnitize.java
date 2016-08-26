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

public class CallLoadShapeUnitize extends StoredProcedure {
	public static final String versionId = "$Id: CallLoadShapeUnitize.java,v 1.5 2008/06/09 21:56:55 schen Exp $ by $Author: schen $, $DateTime:  $";
	private static final Log log = LogFactory.getLog(CallLoadShapeUnitize.class);
	private static final String STORED_FUNC_NAME = "pkg_LTLF.unitize";
	public static final String ErrorKey = "ErrorString";
	public static final String ReturnCodeKey = "ReturnCode";

	public CallLoadShapeUnitize(DataSource ds) {
		super(ds, STORED_FUNC_NAME);
		setFunction(true);
		declareParameter(new SqlOutParameter("RETURN_VALUE", Types.INTEGER));
		declareParameter(new SqlParameter("i_mp_oid", Types.VARCHAR));
		declareParameter(new SqlOutParameter("psError", Types.VARCHAR));
		compile();
	}
	
	//    function Unitize(i_mp_oid MEASUREMENT_POINT_IDENTIFIER.MEASUREMENT_POINT_OID%type default NULL, 
	//          psError out varchar2) return number is

    public HashMap<String, Object> execute(String mp_oid) {
    	Integer returnCode = null;
    	HashMap<String, Object> returnResults = new HashMap<String, Object>();
    	
        HashMap<String, Object> inParams = new HashMap<String, Object>();
        inParams.put("i_mp_oid", mp_oid);
        String errors = null;
        log.debug("Calling Unitize " );
        long start = System.currentTimeMillis();
        Map<String, Object> outParams = super.execute(inParams);
        if (outParams.size() > 0) {
        	if (outParams.get("psError") != null) {
        		errors =  outParams.get("psError").toString();
                log.debug("Received errors from server " + errors);
                returnResults.put(ErrorKey,errors);
        	}
        	returnCode =  (Integer)outParams.get("RETURN_VALUE");
        }
        returnResults.put(ReturnCodeKey, returnCode);
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        float elapsedTimeSec = elapsedTimeMillis/1000F;
        log.debug("Unitize finished in " + elapsedTimeSec + " seconds");
        return returnResults;
    }
}
