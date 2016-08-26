package ca.aeso.ltlf.server.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.aeso.ltlf.model.GapSummaryValue;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.junit.AbstractDataAccessTest;

/** 
 * Tests StagingService
 * 
 * @author mbodor
 */
public class StagingServiceTest extends AbstractDataAccessTest {

	protected static Log logger = LogFactory.getLog(StagingServiceTest.class);

	public void testGetGapSummaryValue() {
		logger.debug("StagingServiceTest.testGetGapSummaryValue starting");

		StagingService service = (StagingService)this.applicationContext.getBean("stagingService");
		
		try {
			GapSummaryValue summ = service.getGapSummaryValue(new Long(717));
			assertNotNull(summ);
			assertEquals("0000087701", summ.getMpName());
		} catch(LtlfServiceException le) {
			
		}
		
		logger.debug("StagingServiceTest.testGetGapSummaryValue done");
	}
	
}
