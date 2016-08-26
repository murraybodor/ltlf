package ca.aeso.ltlf.server.service;

import java.util.Date;
import java.util.List;

import ca.aeso.ltlf.model.Allocation;
import ca.aeso.ltlf.model.AllocationComment;
import ca.aeso.ltlf.model.AllocationForecastYear;
import ca.aeso.ltlf.model.AllocationMp;
import ca.aeso.ltlf.model.SumUnitValues;
import ca.aeso.ltlf.rpc.LtlfServiceException;

public interface AllocationService {
	public Allocation fetchAllocation(Long allocationVersionOid) throws LtlfServiceException;
	public List<Allocation> fetchAllocations(int baseYear) throws LtlfServiceException;
	public void saveAllocation(Allocation aValue) throws LtlfServiceException;
	public Allocation createAllocation(Integer baseYear, Date startDate, Integer endYear, String description) throws LtlfServiceException;
	public Allocation deepCopyAllocation(Allocation aValue) throws LtlfServiceException;
	public List fetchAllocationAreas(AllocationForecastYear aValue) throws LtlfServiceException;
	public Allocation fetchAllocation(int baseYear, Integer versionNumber) throws LtlfServiceException;
	public Allocation fetchAllocationAreasBySector(Long allocationVersionOid, String sectorType) throws LtlfServiceException;
	public String[][] fetchAllocationSectorsBySector(Long allocationVersionOid, String sectorType) throws LtlfServiceException;
	public Long saveAllocationAreaSectors(String[][] changes) throws LtlfServiceException;
	public Long saveAllocationComments(List<AllocationComment> comments) throws LtlfServiceException;
	public String[][] fetchAllocationMpsByArea(Long allocationVersionOid, String areaCode) throws LtlfServiceException;
    public SumUnitValues fetchSUVforMP(AllocationMp aValue) throws LtlfServiceException;
}
