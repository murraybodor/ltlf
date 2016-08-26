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

public class CallGapAnalysis extends StoredProcedure {
	public static final String versionId = "$Id: CallGapAnalysis.java,v 1.11 2008/06/09 21:56:55 schen Exp $ by $Author: schen $, $DateTime:  $";
	private static final Log log = LogFactory.getLog(StoredProcedure.class);
	private static final String STORED_FUNC_NAME = "pkg_LTLF.GapAnalysis";
	public static final String ErrorKey = "ErrorString";
	public static final String Results= "results";
	
    public CallGapAnalysis(DataSource ds) {
        super(ds,STORED_FUNC_NAME);

        setFunction(true);
        declareParameter(new SqlOutParameter("RETURN_VALUE", Types.INTEGER));
        declareParameter(new SqlOutParameter("psError", Types.VARCHAR));
        declareParameter(new SqlParameter("i_mp_oid", Types.VARCHAR));

        compile();
    }
    
  //  RETURN_VALUE := pkg_LTLF.gapanalysis(psError, i_mp_oid);
    
    public HashMap<String, Object> execute(String mp_oid) {
    	Integer status = null;
    	HashMap<String, Object> returnResults = new HashMap<String, Object>();
    	
        HashMap<String, Object> inParams = new HashMap<String, Object>();
        inParams.put("i_mp_oid", mp_oid);
        String errors = null;
        log.debug("Calling gapAnalysis");
        Map<String, Object> outParams = super.execute(inParams);
        if (outParams.size() > 0) {
        	if (outParams.get("psError") != null) {
        		errors =  outParams.get("psError").toString();
                log.debug("Received errors from server " + errors);
                returnResults.put(ErrorKey,errors);
        	}
        	status =  (Integer)outParams.get("RETURN_VALUE");
        	returnResults.put(Results,status);
        }

        return returnResults;
    }
}
