package ca.aeso.ltlf.server.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate4gwt.gwt.HibernateRemoteService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.aeso.ltlf.model.Allocation;
import ca.aeso.ltlf.model.AllocationArea;
import ca.aeso.ltlf.model.AllocationComment;
import ca.aeso.ltlf.model.AllocationForecastYear;
import ca.aeso.ltlf.model.AllocationMp;
import ca.aeso.ltlf.model.AllocationSector;
import ca.aeso.ltlf.model.Area;
import ca.aeso.ltlf.model.CodesTable;
import ca.aeso.ltlf.model.MeasurementPoint;
import ca.aeso.ltlf.model.Sector;
import ca.aeso.ltlf.model.SumUnitValues;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.dao.AllocationDao;
import ca.aeso.ltlf.server.dao.CodesTableDao;

public class AllocationServiceImpl extends HibernateRemoteService implements AllocationService {
	final String versionId = "$Id: AllocationServiceImpl.java,v 1.24 2008/07/22 22:27:27 mbodor Exp $ by $Author: mbodor $, $DateTime:  $";
	protected static Log logger = LogFactory.getLog(AllocationServiceImpl.class);

	AllocationDao dao;
	CodesTableDao _codesDao;
	CommonService _commonService;
	
	public void setCodesTableDao(CodesTableDao aValue) {
		_codesDao = aValue;
	}
	public void setDao(AllocationDao lsDao) {
		dao = lsDao;
	}
	
	public void setCommonService(CommonService aValue) {
		_commonService = aValue;
	}
	public List<Allocation> fetchAllocations(int baseYear) throws LtlfServiceException {
		List returnList= dao.fetchAllocations(baseYear);
		return returnList;
	}
	
	public Allocation fetchAllocation(Long allocationVersionOid) throws LtlfServiceException{
		logger.debug("AllocationServiceImpl.fetchAllocation() starting");
		Allocation returnValue = dao.fetchAllocation(allocationVersionOid);
		logger.debug("AllocationServiceImpl.fetchAllocation() done");
		return returnValue;
	}
	
	public void saveAllocation(Allocation aValue) throws LtlfServiceException {
		logger.debug("AllocationServiceImpl.saveAllocation() starting");
		
		try {
			if ((LtlfServiceImpl.globalInstance() != null) && (LtlfServiceImpl.globalInstance().getUser() != null)) {
				String userId = LtlfServiceImpl.globalInstance().getUser();
				System.out.println("User id = " + userId);
				aValue.setAuditUserId(userId);
			}
			else {
				aValue.setAuditUserId("unittest");
				aValue.setAuditDateTime(new Date());
			}
			aValue.setAuditDateTime(new Date());
			dao.saveAllocation(aValue);
			logger.debug("AllocationServiceImpl.saveAllocation() done");
		} catch (Exception e){
			logger.error("AllocationServiceImpl.saveAllocation() error: " + e.getMessage());
			e.printStackTrace();
			throw new LtlfServiceException(e.getMessage());
		}
		
		logger.debug("AllocationServiceImpl.saveAllocation() done");
	}

	public Allocation createAllocation(Integer baseYear, Date startDate, Integer endYear, String description) throws LtlfServiceException {
		logger.debug("AllocationServiceImpl.createAllocation() starting");
		if (baseYear == null) {
			CodesTable aCode = _codesDao.getCodeValue("BASE_YEAR");
			
			if ((aCode != null) && (aCode.getNumericValue() != null))
				baseYear = aCode.getNumericValue();
			else {
				throw new LtlfServiceException("Unable to find base year");
			}
		}
		Integer versionNumber = new Integer(1);
		Integer currentVersion = dao.fetchHighestVersionNumber(baseYear, startDate, endYear);
		
		logger.debug("AllocationServiceImpl.createAllocation() got version " + currentVersion);
		if (currentVersion != null) {
			versionNumber = new Integer(currentVersion.intValue() + 1);
		}
		
		List sectorList = _commonService.getSectors();
		List areaList = _commonService.getAreas();
		Map areaToMpMap = _commonService.getAreaToMpMap();

			Allocation anAllocation = null;

			anAllocation = new Allocation();
			anAllocation.setDescription(description);
			anAllocation.setBaseYear(baseYear);
			anAllocation.setStartDate(startDate);
			anAllocation.setEndYear(endYear);
			anAllocation.setVersionNumber(versionNumber);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(startDate);
			int startYear = cal.get(Calendar.YEAR);
			
			GregorianCalendar yearCalBegin = new GregorianCalendar();
			GregorianCalendar yearCalEnd = new GregorianCalendar();
			
			for (int x = startYear; x <= endYear; x++){
				AllocationForecastYear afYear = new AllocationForecastYear();
				
				anAllocation.addAllocationForecastYear(afYear);
				afYear.setAllocation(anAllocation);
				
				Integer year = new Integer(x);
				yearCalBegin.set(Calendar.YEAR, year.intValue());
				yearCalBegin.set(Calendar.MONTH, 1);
				yearCalBegin.set(Calendar.DATE, 1);
				yearCalBegin.set(Calendar.HOUR, 0);
				yearCalBegin.set(Calendar.MINUTE, 0);
				yearCalBegin.set(Calendar.SECOND, 0);

				yearCalEnd.set(Calendar.YEAR, year.intValue());
				yearCalEnd.set(Calendar.MONTH, 12);
				yearCalEnd.set(Calendar.DATE, 31);
				yearCalEnd.set(Calendar.HOUR, 0);
				yearCalEnd.set(Calendar.MINUTE, 0);
				yearCalEnd.set(Calendar.SECOND, 0);
				
				afYear.setForecastYear(year);
				Iterator sectorItr = sectorList.iterator();
				
				int x1 = 0;
				while (sectorItr.hasNext()){
					Sector aSector = (Sector)sectorItr.next();
					AllocationSector anAllocSector = new AllocationSector();
					anAllocSector.setSectorType(aSector.getStringValue());
					afYear.addAllocationSector(anAllocSector);
					anAllocSector.setAllocationForecastYear(afYear);
					x1++;
				}
				logger.debug("AllocationServiceImpl.createAllocation() sectors created = " + x1);
				Iterator areaItr = areaList.iterator();
				
				while (areaItr.hasNext()){
					Area anArea = (Area)areaItr.next();
					AllocationArea anAllocArea = new AllocationArea();
					anAllocArea.setArea(anArea);
					afYear.addAllocationArea(anAllocArea);
					anAllocArea.setAllocationForecastYear(afYear);
					
					Iterator sectorItr2 = sectorList.iterator();
					
					while (sectorItr2.hasNext()){
						Sector aSector = (Sector)sectorItr2.next();
						AllocationSector anAllocSector = new AllocationSector();
						anAllocSector.setSectorType(aSector.getStringValue());
						anAllocArea.addAllocationSector(anAllocSector);
						anAllocSector.setAllocationArea(anAllocArea);
					}
					
					List<MeasurementPoint> mpList = (List<MeasurementPoint>)areaToMpMap.get(anArea.getCode());
					
					if (mpList != null) {
						Iterator mpItr = mpList.iterator();
						while (mpItr.hasNext()){
							MeasurementPoint anMP = (MeasurementPoint)mpItr.next();
							AllocationMp anAllocMp = new AllocationMp();
							anAllocMp.setMp(anMP);
							anAllocMp.setAllocationArea(anAllocArea);
							anAllocMp.setBaseYear(baseYear);
							
							anAllocMp.setBeginDate(yearCalBegin.getTime());
							anAllocMp.setEndDate(yearCalEnd.getTime());
							
							anAllocArea.addAllocationMp(anAllocMp);
						}
					}
				}
			}
			
			logger.debug("AllocationServiceImpl.createAllocation() done");
		
		return anAllocation;
	}
	
	public Allocation fetchAllocation(int baseYear, Integer versionNumber) throws LtlfServiceException {
		return dao.fetchAllocation(baseYear, versionNumber);
	}

	public List fetchAllocationAreas(AllocationForecastYear aValue) throws LtlfServiceException {
		return dao.fetchAllocationAreas(aValue);
	}
	public Allocation deepCopyAllocation(Allocation aValue) throws LtlfServiceException{
		return dao.deepCopyAllocation(aValue);
	}

	public Allocation fetchAllocationAreasBySector(Long allocationVersionOid, String sectorType) throws LtlfServiceException{
		logger.debug("AllocationServiceImpl.fetchAllocationAreasBySector() starting");
		Allocation returnValue = dao.fetchAllocationAreasBySector(allocationVersionOid, sectorType);
		logger.debug("AllocationServiceImpl.fetchAllocationAreasBySector() done");
		return returnValue;
	}

	public String[][] fetchAllocationSectorsBySector(Long allocationVersionOid, String sectorType) throws LtlfServiceException{
		logger.debug("AllocationServiceImpl.fetchAllocationSectorsBySector() starting");
		return dao.fetchAllocationSectorsBySector(allocationVersionOid, sectorType);
	}

	public Long saveAllocationAreaSectors(String[][] changes) throws LtlfServiceException{
		logger.debug("AllocationServiceImpl.saveAllocationAreaSectors() starting");
		return dao.saveAllocationAreaSectors(changes);
	}

	public Long saveAllocationComments(List<AllocationComment> comments) throws LtlfServiceException{
		logger.debug("AllocationServiceImpl.saveAllocationComments() starting");
		return dao.saveAllocationComments(comments);
	}
	
	public String[][] fetchAllocationMpsByArea(Long allocationVersionOid, String areaCode)  throws LtlfServiceException {
		return dao.fetchAllocationMpsByArea(allocationVersionOid, areaCode);
	}
	
    public SumUnitValues fetchSUVforMP(AllocationMp aValue) throws LtlfServiceException {
    	return dao.fetchSUVforMP(aValue);
    }

}
