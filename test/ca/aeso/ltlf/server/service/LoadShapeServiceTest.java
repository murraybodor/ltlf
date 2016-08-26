package ca.aeso.ltlf.server.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.aeso.ltlf.model.LoadShape;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.junit.AbstractDataAccessTest;

/** 
 * Tests LoadShapeService
 * 
 * @author mbodor
 */
public class LoadShapeServiceTest extends AbstractDataAccessTest {

	protected static Log logger = LogFactory.getLog(LoadShapeServiceTest.class);

	
	public void testFetchLoadShapeUnitize() {
		logger.debug("LoadShapeServiceTest.testFetchLoadShapeUnitize starting");

		LoadShapeService service = (LoadShapeService)this.applicationContext.getBean("loadShapeService");
		
		try {
			LoadShape shape = service.fetchLoadShapeUnitize("6");
			assertEquals(2007, shape.getBaseYear().intValue());
			
		} catch(LtlfServiceException le) {
			
		}
		
		logger.debug("LoadShapeServiceTest.testFetchLoadShapeUnitize done");
	}

	
}
