package ca.aeso.ltlf.server.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import ca.aeso.ltlf.model.*;
import ca.aeso.ltlf.model.AllocationArea;
import ca.aeso.ltlf.model.AllocationForecastYear;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.junit.AbstractDataAccessTest;
import ca.aeso.ltlf.server.util.ObjectCloner;

public class AllocationServiceTest extends AbstractDataAccessTest {

	protected static Log logger = LogFactory.getLog(AllocationServiceTest.class);
	private SessionFactory sessionFactory = null;
	public AllocationService service;
	public CommonService commonService;
	
	public void setAllocationService(AllocationService aValue) {
		service = aValue;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
	      this.sessionFactory = sessionFactory;
	}
    
	public void testFetchAllocation() {
		logger.debug("AllocationServiceTest.fetchAllocation starting");

		//AllocationService service = (AllocationService)this.applicationContext.getBean("allocationService");
		
		try {
			Allocation alloc = service.fetchAllocation(2007, new Integer(1));
//			sessionFactory.openSession().beginTransaction();
//			Iterator itr = alloc.getAllocationForecastYears().iterator();
//			while (itr.hasNext()){
//				System.out.println("loop");
//				AllocationForecastYear fyear = (AllocationForecastYear)itr.next();
//				sessionFactory.getCurrentSession().update((AllocationForecastYear)itr.next());
//				Hibernate.initialize(fyear.getAllocationAreas());
//			}
			System.out.println(alloc);
			
/*			sessionFactory.getCurrentSession().beginTransaction();
			Iterator aitr =  alloc.getAllocationForecastYears().iterator();
			while (aitr.hasNext()) {
				AllocationForecastYear fy = (AllocationForecastYear)aitr.next();
				sessionFactory.getCurrentSession().update(fy);
				Iterator farea = fy.getAllocationAreas().iterator();
				while (farea.hasNext()){
					AllocationArea ar = (AllocationArea) farea.next();
					if (ar.getAllocationMps().size() > 0) {
						Iterator mipt = ar.getAllocationMps().iterator();
						while (mipt.hasNext()) {
							AllocationMp amp = (AllocationMp) mipt.next();
							if (amp.getSuv() != null) {
								System.out.println(amp.getSuv());
								break;
							}
						}
					}
				}

			}*/
		//	System.out.println(alloc.getAllocationForecastYears().size());
		//	assertEquals(2007, alloc.getBaseYear().intValue());
		//	System.out.println(alloc.getAllocationForecastYears().size());
			
		//	List results = service.fetchAllocations(2007);
		//	System.out.println(results.size());
		//	assertNotNull(results);

		} catch(LtlfServiceException le) {
			
		}
		
		//sessionFactory.getCurrentSession().

		logger.debug("AllocationServiceTest.fetchAllocation done");
	}
	
//	public void testAllocationAreaFetch() {
//		try {
//			Allocation alloc = service.fetchAllocation(2007, new Integer(1));
//			
//			AllocationForecastYear fcast = (AllocationForecastYear) alloc.getAllocationForecastYears().toArray()[0];
//			
//			List result = service.fetchAllocationAreas(fcast);
//			
//			System.out.println(result);
//		} catch (Exception ex) {
//			System.out.println(ex);
//		}
//	}

	public void testCopyAllocation() {
		logger.debug("AllocationServiceTest.copyAllocation starting");
		
//		try {
//			Allocation alloc = service.fetchAllocation(new Long(6618150));
//			System.out.println(alloc);
//			Allocation newAlloc = service.deepCopyAllocation(alloc);
//			newAlloc.setVersionNumber( new Integer(alloc.getVersionNumber().intValue() + 1));
//			
//			service.saveAllocation(newAlloc);
//
//		} catch (LtlfServiceException le) {
//			System.out.println(le);
//		}
		logger.debug("AllocationServiceTest.copyAllocation end");
	}
/*	public void testSaveAllocation() {
		
		AllocationService service = (AllocationService)this.applicationContext.getBean("allocationService");
		
		try {
			Allocation alloc = service.fetchAllocation(2007, new Integer(1));

			service.saveAllocation(alloc);
			
		} catch(LtlfServiceException le) {
			
		}
	}*/
	
//	public void testCreateAllocation () {
//		//AllocationService service = (AllocationService)this.applicationContext.getBean("allocationService");
//
//		this.setDefaultRollback(false);
//		
//		try {
//			Allocation alloc = service.createAllocation(null, new Date(), new Integer(2020), "This is a description");
//			System.out.println(alloc);
//			assertEquals(2007, alloc.getBaseYear().intValue());
//			System.out.println(alloc.getAllocationForecastYears().size());
//			//assertEquals(12, alloc.getAllocationForecastYears().size());
//			System.out.println(((AllocationForecastYear) alloc.getAllocationForecastYears().toArray()[0]).getAllocationAreas().size());
//
//			AllocationArea ar = (AllocationArea) ((AllocationForecastYear) alloc.getAllocationForecastYears().toArray()[1]).getAllocationAreas().toArray()[2];
//			System.out.println("Mps" + ar.getAllocationMps().size());
//			
//		//	System.out.println(((AllocationMp)ar.getAllocationMps().toArray()[1]));
//			
//			Allocation newAlloc = service.deepCopyAllocation(alloc);
//			newAlloc.setVersionNumber( new Integer(1));
//			System.out.println(newAlloc);
//			assertEquals(2007, newAlloc.getBaseYear().intValue());
//			System.out.println(newAlloc.getAllocationForecastYears().size());
//		//	assertEquals(12, newAlloc.getAllocationForecastYears().size());
//			System.out.println(((AllocationForecastYear) newAlloc.getAllocationForecastYears().toArray()[0]).getAllocationAreas().size());
//		//	AllocationArea ar2 = (AllocationArea) ((AllocationForecastYear) newAlloc.getAllocationForecastYears().toArray()[0]).getAllocationAreas().toArray()[0];
//		//	System.out.println(ar2.getAllocationMps().size());
//			
//			service.saveAllocation(newAlloc);
//			
//		} catch (LtlfServiceException ex) {
//			ex.printStackTrace();
//		}
//		catch (Exception e){
//			e.printStackTrace();
//		}
//
//	}
	
//	public void testFetchAllocationMpsByArea() {
//		logger.debug("AllocationServiceTest.testFetchAllocationMpsByArea starting");
//		
//		try {
//			
//			List<AllocationMp> allocMps = service.fetchAllocationMpsByArea(new Long(6358500), "6");
//			
//			Iterator itr3 = allocMps.iterator();
//					
//			while (itr3.hasNext()){
//				AllocationMp mp = (AllocationMp)itr3.next();
//				logger.debug("AllocationServiceTest.testFetchAllocationMpsByArea mp=" + mp.getOid() + ", suv=" + mp.getSuv().getSuv());
//				
//			}
//
//		} catch (LtlfServiceException le) {
//			System.out.println(le);
//		}
//		logger.debug("AllocationServiceTest.testFetchAllocationMpsByArea end");
//	}

//	public void testFetchAllocationAreasBySector() {
//		logger.debug("AllocationServiceTest.testFetchAllocationAreasBySector starting");
//		
//		try {
//			
//			Allocation alloc = service.fetchAllocationAreasBySector(new Long(6542153), "RES");
//			
//			for (Iterator iterator = alloc.getAllocationForecastYears().iterator(); iterator.hasNext();) {
//				AllocationForecastYear fy = (AllocationForecastYear) iterator.next();
//				for (Iterator iterator2 = fy.getAllocationAreas().iterator(); iterator2.hasNext();) {
//					AllocationArea area = (AllocationArea) iterator2.next();
//					for (Iterator iterator3 = area.getAllocationSectors().iterator(); iterator3.hasNext();) {
//						AllocationSector sector = (AllocationSector) iterator3.next();
//						if (!sector.getSectorType().equals("RES")) {
//							logger.debug("AllocationServiceTest.testFetchAllocationAreasBySector nonres: " + sector.getSectorType());
//						}
//					}
//				}
//			}
//			
//		} catch (LtlfServiceException le) {
//			System.out.println(le);
//		}
//		logger.debug("AllocationServiceTest.testFetchAllocationMpsByArea end");
//	}

//	public void testFetchAllocationAreasBySectorList() {
//		logger.debug("AllocationServiceTest.testFetchAllocationAreasBySectorList starting");
//		
//		try {
//			
//			Object[][] sectors = service.fetchAllocationSectorsBySector(new Long(6542153), "RES");
//			
//			logger.debug("AllocationServiceTest.testFetchAllocationAreasBySectorList got: " + sectors.length);
//			
//		} catch (LtlfServiceException le) {
//			System.out.println(le);
//		}
//		logger.debug("AllocationServiceTest.testFetchAllocationMpsByArea end");
//	}

	public void testFetchAllocationMpsByArea() {
		logger.debug("AllocationServiceTest.testFetchAllocationMpsByArea starting");
		
		try {
			
			Object[][] mps = service.fetchAllocationMpsByArea(new Long(6542153), "4");
			
			for (int i = 0; i < mps.length; i++) {
				if (i<20) {
					Object[] mp = mps[i];
					logger.debug("AllocationServiceTest.testFetchAllocationMpsByArea " + i + ": " + mp[0] + " " + mp[1] + " " + mp[2] + " " + mp[3] + " " + mp[4] + " " + mp[5] + " " + mp[6]);
				}
				
			}
			logger.debug("AllocationServiceTest.testFetchAllocationMpsByArea got: " + mps.length);
			
		} catch (LtlfServiceException le) {
			System.out.println(le);
		}
		logger.debug("AllocationServiceTest.testFetchAllocationMpsByArea end");
	}
	
}
