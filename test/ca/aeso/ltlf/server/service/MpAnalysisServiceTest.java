package ca.aeso.ltlf.server.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.junit.AbstractDataAccessTest;
import javax.servlet.http.*;

/** 
 * Tests MpAnalysisService
 * 
 * @author mbodor
 */
public class MpAnalysisServiceTest extends AbstractDataAccessTest {

	protected static Log logger = LogFactory.getLog(MpAnalysisServiceTest.class);

//	public void testGetDualCount() {
//		logger.debug("MpAnalysisServiceTest.testGetDualCount starting");
//
//		MpAnalysisService service = (MpAnalysisService)this.applicationContext.getBean("mpAnalysisService");
//		try {
//			long test = service.getDualCount();
//			assertEquals(1, test);
//		} catch(LtlfServiceException le) {
//			
//		}
//
//		logger.debug("MpAnalysisServiceTest.testGetDualCount done");
//		
//	}
	
	public void testSmoothOutliers() {
		logger.debug("MpAnalysisServiceTest.testSmoothOutliers starting");

//		MpAnalysisService service = (MpAnalysisService)this.applicationContext.getBean("mpAnalysisService");
//		HttpSession session = this.
//		
//		try {
//			service.smoothOutliers("0000087701", 50, 6, null);
////			assertEquals(1, test);
//		} catch(LtlfServiceException le) {
//			
//		}
		
		logger.debug("MpAnalysisServiceTest.testSmoothOutliers done");
	}

	
}
