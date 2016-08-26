package ca.aeso.ltlf.server.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import ca.aeso.ltlf.model.Allocation;
import ca.aeso.ltlf.model.AllocationArea;
import ca.aeso.ltlf.model.AllocationComment;
import ca.aeso.ltlf.model.AllocationForecastYear;
import ca.aeso.ltlf.model.AllocationMp;
import ca.aeso.ltlf.model.AllocationSector;
import ca.aeso.ltlf.model.SumUnitValues;
import ca.aeso.ltlf.rpc.LtlfServiceException;
import ca.aeso.ltlf.server.util.ObjectCloner;

public class AllocationDaoImpl implements AllocationDao {

	protected static Log logger = LogFactory.getLog(AllocationDaoImpl.class);

	private SessionFactory _sessionFactory;
	private CommonDao _commonDao;
	
	public void setCommonDao(CommonDao aValue) {
		_commonDao = aValue;
	}
	
	public SessionFactory getSessionFactory() {
		return _sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		logger.debug("AllocationDaoImpl.setSessionFactory() starting");
		this._sessionFactory = sessionFactory;
	}
	
    private final String fetchAllocationQuery = "from Allocation al where al.baseYear = :byear";

	public List<Allocation> fetchAllocations(int baseYear) {
		logger.debug("AllocationDaoImpl.fetchAllocations() starting");
		List resultSet = null;
		Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();
		Query query = (Query) _sessionFactory.getCurrentSession().createQuery(fetchAllocationQuery);
		query.setParameter("byear", new Integer(baseYear));
		
		logger.debug("AllocationDaoImpl.fetchAllocations() listing");
		resultSet = query.list();
		if (resultSet == null || resultSet.size() == 0)
			return null;

		logger.debug("AllocationDaoImpl.fetchAllocations() " + resultSet.size() + " versions");
		tx.commit();
		logger.debug("AllocationDaoImpl.fetchAllocations() done");
		return resultSet;
	}

	public Allocation fetchAllocation(Long allocationVersionOid){
		logger.debug("AllocationDaoImpl.fetchAllocation() starting");
		
		Allocation fetchedAllocation = null;
		
		Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();
		
		Criteria queryCrit = _sessionFactory.getCurrentSession().createCriteria(Allocation.class);
		
		Criterion oidCrit = Restrictions.eq("oid", allocationVersionOid);
		
		queryCrit.add(oidCrit);

		fetchedAllocation = (Allocation)queryCrit.uniqueResult();

		logger.debug("AllocationDaoImpl.fetchAllocation() done, got " + (fetchedAllocation!=null?1:0));

		tx.commit();

		return fetchedAllocation;
	}

    private final String fetchAllocationMpsByAreaQuery = 
    	"select amps from Allocation al " +
    	"join al.allocationForecastYears afy " +
    	"join afy.allocationAreas aa " +
    	"join aa.allocationMps amps " +
    	"join amps.mp mp " +
    	"join mp.details mpd " +
    	"where al.oid = :aoid " +
    	"and aa.area.code = :areaCode " +
    	"and mpd.creationDate < :aDate " +
    	"and (mpd.expiryDate > :aDate " +
    	"or mpd.expiryDate = null) " +
	    "order by afy.forecastYear asc, mpd.name asc";


    public String[][] fetchAllocationMpsByArea(Long allocationOid, String areaCode)  throws LtlfServiceException {
		logger.debug("AllocationDaoImpl.fetchAllocationMpsByArea() starting oid=" + allocationOid + " area=" + areaCode);
		
		Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();
		
		Query query = (Query) _sessionFactory.getCurrentSession().createQuery(fetchAllocationMpsByAreaQuery);
		query.setParameter("aoid", allocationOid);
		query.setParameter("areaCode", areaCode);
		query.setParameter("aDate", new Date());
		
		List<AllocationMp> fetchedAllocationMps = (List<AllocationMp>)query.list();

		String[][] allocMpArray = new String[fetchedAllocationMps.size()][];
		int i = 0;
		
		for (Iterator iterator = fetchedAllocationMps.iterator(); iterator.hasNext();) {
			AllocationMp allocMp = (AllocationMp) iterator.next();
			
			SumUnitValues suv = (SumUnitValues)fetchSUVforMP(allocMp);
			
			String[] temp = new String[] {
					allocMp.getOid().toString(),
					allocMp.getAllocationArea().getAllocationForecastYear().toString(),
					allocMp.getAllocationArea().calculateTotalAreaFyEnergy().toString(),
					allocMp.getMp().getName(),
					allocMp.getMp().getOid().toString(),
					allocMp.getAllocPercent().toString(),
					allocMp.getPeakFactor().toString(),
					allocMp.getAllocEnergy().toString(),
					suv==null?"0":suv.getSuv()==null?"0":suv.getSuv().toString()
			};
			allocMpArray[i++] = temp;
		}
		
		
		logger.debug("AllocationDaoImpl.fetchAllocationMpsByArea() done, got " + fetchedAllocationMps.size());

		tx.commit();

		return allocMpArray;
	}
	
    private final String findAllocationQuery = "from Allocation al where al.baseYear = :byear and al.versionNumber=:vnum";

	public Allocation fetchAllocation(int baseYear, Integer versionNumber){
		logger.debug("AllocationDaoImpl.fetchAllocation() starting");
		
		Allocation fetchedAllocation = null;
		
		Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();
		Query query = (Query) _sessionFactory.getCurrentSession().createQuery(findAllocationQuery);
		query.setParameter("byear", new Integer(baseYear));
		query.setParameter("vnum", versionNumber);
		List records = query.list();
		if (records == null || records.size() == 0)
			return null;
		fetchedAllocation = (Allocation)records.get(0);

		logger.debug("AllocationDaoImpl.fetchAllocation() done, got " + (fetchedAllocation!=null?1:0));

		tx.commit();

		return fetchedAllocation;
	}
	
	public Allocation deepCopyAllocation(Allocation inValue) throws LtlfServiceException {
		try {
		Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();

		Allocation returnValue = (Allocation) ObjectCloner.deepCopy(inValue);
		returnValue.setOid(null);

		Iterator itr = returnValue.getAllocationForecastYears().iterator();
		while (itr.hasNext()) {
			AllocationForecastYear fcYear = (AllocationForecastYear) itr.next();
			if (fcYear.getOid() != null)
				_sessionFactory.getCurrentSession().update(fcYear);
			fcYear.setOid(null);
			Iterator itr2 = fcYear.getAllocationAreas().iterator();
			while (itr2.hasNext()) {
				AllocationArea allocArea = (AllocationArea) itr2.next();
				allocArea.setOid(null);
				Iterator itr4 = allocArea.getAllocationSectors().iterator();
				while (itr4.hasNext()) {
					AllocationSector allocSec = (AllocationSector) itr4.next();
					allocSec.setOid(null);
				}
				Iterator itr5 = allocArea.getAllocationMps().iterator();
				while (itr5.hasNext()) {
					AllocationMp allocMp = (AllocationMp) itr5.next();
					allocMp.setOid(null);
				}
			}
			Iterator itr3 = fcYear.getAllocationSectors().iterator();
			while (itr3.hasNext()) {
				AllocationSector allocSec = (AllocationSector) itr3.next();
				allocSec.setOid(null);
			}
		}
		_sessionFactory.getCurrentSession().close();

		return returnValue;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LtlfServiceException(e.getMessage());
		}
	}
	public void saveAllocation(Allocation aValue) throws LtlfServiceException {
		logger.debug("AllocationDaoImpl.saveAllocation() starting");

		try {
			Transaction tx1 = _sessionFactory.getCurrentSession().beginTransaction();
			_sessionFactory.getCurrentSession().saveOrUpdate(aValue);
			tx1.commit();
		} catch (Exception e) {
			logger.error("AllocationServiceImpl.saveAllocation() error: " + e.getMessage());
			e.printStackTrace();
			throw new LtlfServiceException(e.getMessage());
		}

		logger.debug("AllocationDaoImpl.saveAllocation() done");
	}
	
	public List fetchAllocationAreas(AllocationForecastYear aValue) throws LtlfServiceException {
		logger.debug("AllocationDaoImpl.fetchAllocationAreas() starting");

		try {
		Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();
		String queryString = "from AllocationArea where allocationForecastYear = :fcastyear";
		
		Query q = _sessionFactory.getCurrentSession().
		createQuery(queryString).
		setParameter("fcastyear", aValue, Hibernate.entity(AllocationForecastYear.class));
		
		List result = q.list();
		tx.commit();
		
		return result;
		} catch (Exception ex) {
			logger.error("AllocationServiceImpl.fetchAllocationAreas() error: " + ex.getMessage());
			ex.printStackTrace();
			throw new LtlfServiceException(ex.getMessage());
		}
	}
	
	
	public Allocation fetchAllocationAreasBySector(Long allocationVersionOid, String sectorType) throws LtlfServiceException {
		logger.debug("AllocationDaoImpl.fetchAllocationAreasBySector() starting");
		
		Allocation fetchedAllocation = null;
		
		Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();

		
		Query query = (Query) _sessionFactory.getCurrentSession().createQuery(fetchAllocationAreasBySectorQuery);
		query.setParameter("aoid", new Long(allocationVersionOid));
		query.setParameter("sector", "RES");
		
//		fetchedAllocation = (Allocation)query.uniqueResult();
		
		
		List<AllocationSector> fetchedAllocationSectors = (List<AllocationSector>)query.list();

		
		Iterator itr = fetchedAllocationSectors.iterator();
		
		while (itr.hasNext()) {
			AllocationSector sector = (AllocationSector) itr.next();
			sector.getAllocationArea();
		}
		
		logger.debug("AllocationDaoImpl.fetchAllocationAreasBySector() done, got " + (fetchedAllocation!=null?1:0));

		tx.commit();

		return fetchedAllocation;
		
	}

    private final String fetchAllocationAreasBySectorQuery = 
    	"select asect from Allocation al " +
    	"join al.allocationForecastYears afy " +
    	"join afy.allocationAreas aa " +
    	"join aa.allocationSectors asect " +
    	"where al.oid = :aoid " +
	    "and asect.sectorType = :sector " +
	    "order by afy.forecastYear asc, aa.area asc";
	
	public String[][] fetchAllocationSectorsBySector(Long allocationVersionOid, String sectorType) throws LtlfServiceException {
		logger.debug("AllocationDaoImpl.fetchAllocationSectorsBySector() starting");
		
		List<AllocationSector> fetchedAllocationSectors = null;
		
		Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();
		
		Query query = (Query) _sessionFactory.getCurrentSession().createQuery(fetchAllocationAreasBySectorQuery);
		query.setParameter("aoid", allocationVersionOid);
		query.setParameter("sector", sectorType);
		
		fetchedAllocationSectors = (List<AllocationSector>)query.list();

		String[][] tempArray = new String[fetchedAllocationSectors.size()][];
		int i = 0;
		
		for (Iterator iterator = fetchedAllocationSectors.iterator(); iterator.hasNext();) {
			AllocationSector sector = (AllocationSector) iterator.next();
			
			String[] temp = new String[] {
			sector.getOid().toString(),
			sector.getAllocationArea().getAllocationForecastYear().toString(),
			sector.getAllocationArea().getAllocationForecastYear().getSectorEnergy(sectorType).toString(),
			sector.getAllocationArea().getArea().getCode(),
			new Float(sector.getAllocationArea().getAllocationForecastYear().getAreaTotalPercent(sectorType)).toString(),
			sector.getAllocPercent().toString()
			};
			tempArray[i++] = temp;
		}
		
		tx.commit();

		logger.debug("AllocationDaoImpl.fetchAllocationSectorsBySector() done, got " + fetchedAllocationSectors.size());
		
		return tempArray;
	}

	public Long saveAllocationAreaSectors(String[][] changes) throws LtlfServiceException {
		logger.debug("AllocationDaoImpl.saveAllocationAreaSectors() starting");
		
		Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();
		String updateHql = "update AllocationSector aSect set aSect.allocPercent = :newPct where aSect.oid = :oid";
		
		for (int i = 0; i < changes.length; i++) {
			String[] change = changes[i];
			logger.debug("AllocationDaoImpl.saveAllocationAreaSectors() change: id=" + change[0] + " pct=" + change[1]);
			
			int updatedEntities = _sessionFactory.getCurrentSession().createQuery( updateHql )
	        .setFloat("newPct", new Float(change[1]))
	        .setLong( "oid", new Long(change[0]))
	        .executeUpdate();
		}

		tx.commit();
		
		logger.debug("AllocationDaoImpl.saveAllocationAreaSectors() done ");
		
		return new Long(1);
	}
	
	public Long saveAllocationComments(List<AllocationComment> comments) throws LtlfServiceException {
		logger.debug("AllocationDaoImpl.saveAllocationComments() starting");

		try {
			Transaction tx1 = _sessionFactory.getCurrentSession().beginTransaction();
			
			for (Iterator iterator = comments.iterator(); iterator.hasNext();) {
				AllocationComment comment = (AllocationComment) iterator.next();
				_sessionFactory.getCurrentSession().saveOrUpdate(comment);
			}
			
			tx1.commit();
		} catch (Exception e) {
			logger.error("AllocationServiceImpl.saveAllocationComments() error: " + e.getMessage());
			e.printStackTrace();
			throw new LtlfServiceException(e.getMessage());
		}

		logger.debug("AllocationDaoImpl.saveAllocationComments() done");
		
		return new Long(1);
		
	}
	
	public Integer fetchHighestVersionNumber(Integer baseYear, Date startDate, Integer endYear) {
		Integer versionNumber = null;
		Transaction tx = _sessionFactory.getCurrentSession().beginTransaction();
		String queryString = "select sum(versionNumber) from Allocation where baseYear = :pBaseYear " +
		"and startDate = :pStartDate and endYear = :pEndYear";
		
		Query q =_sessionFactory.getCurrentSession().createQuery(queryString).
		setInteger("pBaseYear",baseYear.intValue()).setDate("pStartDate", startDate).
		setInteger("pEndYear", endYear);
		
		Integer result = (Integer)q.uniqueResult();
		
		if (result != null) {
			versionNumber = result;
			logger.debug("AllocationDaoImpl.fetchHighestVersionNumber() Version number now " + versionNumber);
		}
		
		return versionNumber;
	}

	public SumUnitValues fetchSUVforMP(AllocationMp aValue) {
		SumUnitValues result = null;
		logger.debug("AllocationDaoImpl.fetchSUVforMP() starting");

	//	Transaction tx1 = _sessionFactory.getCurrentSession().beginTransaction();
		System.out.println("baseyear = " +  aValue.getBaseYear() + "beginDate = " + aValue.getBeginDate() + 
				" endDate = " + aValue.getEndDate() + " mpId = " + aValue.getMp().getOid());
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Query q = _sessionFactory.getCurrentSession().getNamedQuery("suv").
		setParameter("cbaseYear", aValue.getBaseYear()).
		setParameter("beginDate", form.format(aValue.getBeginDate())).
		setParameter("endDate", form.format(aValue.getEndDate())).setParameter("mpId", aValue.getMp().getOid());
	
		List resultList = q.list();
		if (resultList.size()> 0) {
			result = (SumUnitValues)resultList.get(0);
		}
		logger.debug("AllocationDaoImpl.fetchSUVforMP() finishing");

//		tx1.commit();
		return result;
	}
}
