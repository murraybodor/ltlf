package ca.aeso.ltlf.server.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.aeso.ltlf.model.*;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.junit.AbstractDataAccessTest;

/** 
 * Tests CommonService
 * 
 * @author mbodor
 */
public class CommonServiceTest extends AbstractDataAccessTest {

	protected static Log logger = LogFactory.getLog(CommonServiceTest.class);

	public void testGetMeasurementPoints() {
		logger.debug("CommonServiceTest.testGetMeasurementPoints starting");

		CommonService service = (CommonService)this.applicationContext.getBean("commonService");
		
		try {
			List mps = service.getMeasurementPoints();
			assertTrue(mps.size()>700);
			
		} catch(LtlfServiceException le) {
			
		}
		
		logger.debug("CommonServiceTest.testGetMeasurementPoints done");
	}

	public void testGetMeasurementPoint() {
		logger.debug("CommonServiceTest.testGetMeasurementPoint starting");

		CommonService service = (CommonService)this.applicationContext.getBean("commonService");
		
		try {
			MeasurementPoint mp = service.getMeasurementPoint("S01");
			assertEquals("S01", mp.getCurrentDetail().getName());
			
		} catch(LtlfServiceException le) {
			
		}
		
		logger.debug("CommonServiceTest.testGetMeasurementPoint done");
	}

	public void testGetAreas() {
		logger.debug("CommonServiceTest.testGetAreas starting");

		CommonService service = (CommonService)this.applicationContext.getBean("commonService");
		
		try {
			List areas = service.getAreas();
			assertTrue(areas.size()>10);
			
		} catch(LtlfServiceException le) {
			
		}
		
		logger.debug("CommonServiceTest.testGetAreas done");
	}

	public void testGetArea() {
		logger.debug("CommonServiceTest.testGetArea starting");

		CommonService service = (CommonService)this.applicationContext.getBean("commonService");
		
		try {
			Area area6 = service.getArea("6");
			assertEquals("Calgary", area6.getName());
			
		} catch(LtlfServiceException le) {
			
		}
		
		logger.debug("CommonServiceTest.testGetArea done");
	}
	
}
