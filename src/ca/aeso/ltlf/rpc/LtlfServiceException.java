package ca.aeso.ltlf.rpc;

import com.google.gwt.user.client.rpc.SerializableException;

/**
 * LtlfServiceException
 * Represents an exception in ltlf services
 * 
 * @author mbodor
 */
public class LtlfServiceException extends SerializableException {

	private static final long serialVersionUID = 3713131258099607589L;
	
	String message;
	
	public LtlfServiceException () {
	}

	public LtlfServiceException (String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
