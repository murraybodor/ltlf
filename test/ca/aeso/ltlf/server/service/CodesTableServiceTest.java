package ca.aeso.ltlf.server.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.aeso.ltlf.model.*;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.junit.AbstractDataAccessTest;

/** 
 * Tests CodesTableService
 * 
 * @author mbodor
 */
public class CodesTableServiceTest extends AbstractDataAccessTest {

	protected static Log logger = LogFactory.getLog(CodesTableServiceTest.class);

	public void testGetRefreshInterval() {
		logger.debug("CodesTableServiceTest.testGetRefreshInterval starting");

		CodesTableService service = (CodesTableService)this.applicationContext.getBean("codesTableService");
		
		try {
			Integer refr = service.getRefreshInterval();
			assertEquals(5, refr.intValue());
			
		} catch(LtlfServiceException le) {
			
		}
		
		logger.debug("CodesTableServiceTest.testGetRefreshInterval done");
	}

	public void testGetBaseYear() {
		logger.debug("CodesTableServiceTest.testGetBaseYear starting");

		CodesTableService service = (CodesTableService)this.applicationContext.getBean("codesTableService");
		
		try {
			Integer baseYear = service.getBaseYear();
			assertEquals(2007, baseYear.intValue());
			
		} catch(LtlfServiceException le) {
			
		}
		
		logger.debug("CodesTableServiceTest.testGetBaseYear done");
	}

	public void testGetReleaseId() {
		logger.debug("CodesTableServiceTest.testGetReleaseId starting");

		CodesTableService service = (CodesTableService)this.applicationContext.getBean("codesTableService");
		
		try {
			String releaseId = service.getReleaseId();
			assertNotNull(releaseId);
			
		} catch(LtlfServiceException le) {
			
		}
		
		logger.debug("CodesTableServiceTest.testGetReleaseId done");
	}

	public void testGetCodeValue() {
		logger.debug("CodesTableServiceTest.testGetCodeValue starting");

		CodesTableService service = (CodesTableService)this.applicationContext.getBean("codesTableService");
		
		try {
			CodesTable code = service.getCodeValue("ENVIRONMENT");
			
			assertNotNull(code);
			String str = code.getStringValue();
			assertEquals("DEV", str);
			
			
		} catch(LtlfServiceException le) {
			
		}
		
		logger.debug("CodesTableServiceTest.testGetCodeValue done");
	}
	
}
