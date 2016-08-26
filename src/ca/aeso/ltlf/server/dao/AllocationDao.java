package ca.aeso.ltlf.server.dao;

import java.util.Date;
import java.util.List;

import ca.aeso.ltlf.model.Allocation;
import ca.aeso.ltlf.model.AllocationComment;
import ca.aeso.ltlf.model.AllocationForecastYear;
import ca.aeso.ltlf.model.AllocationMp;
import ca.aeso.ltlf.model.SumUnitValues;
import ca.aeso.ltlf.rpc.LtlfServiceException;

public interface AllocationDao {

	public Allocation fetchAllocation(Long allocationVersionOid);
	public List<Allocation> fetchAllocations(int baseYear);
	public void saveAllocation(Allocation aValue) throws LtlfServiceException;
	public Allocation deepCopyAllocation(Allocation inValue) throws LtlfServiceException;
	public List fetchAllocationAreas(AllocationForecastYear aValue) throws LtlfServiceException;
	public Integer fetchHighestVersionNumber(Integer baseYear, Date startDate, Integer endYear);
	public Allocation fetchAllocation(int baseYear, Integer versionNumber) throws LtlfServiceException;
	public Allocation fetchAllocationAreasBySector(Long allocationVersionOid, String sectorType) throws LtlfServiceException;
    public String[][] fetchAllocationMpsByArea(Long allocationVersionOid, String areaCode)  throws LtlfServiceException;
	public String[][] fetchAllocationSectorsBySector(Long allocationVersionOid, String sectorType) throws LtlfServiceException;
	public Long saveAllocationAreaSectors(String[][] changes) throws LtlfServiceException;
	public Long saveAllocationComments(List<AllocationComment> comments) throws LtlfServiceException;
    public SumUnitValues fetchSUVforMP(AllocationMp aValue) throws LtlfServiceException;
}
