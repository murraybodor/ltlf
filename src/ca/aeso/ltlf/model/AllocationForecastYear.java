package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Represents an energy allocation by forecast year
 * 
 * @author mbodor
 */
@Entity
@Table(name="ALLOCATION_FCAST_YR")
@org.hibernate.annotations.Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@org.hibernate.annotations.Proxy(lazy=false)
public class AllocationForecastYear extends LazyGwtPojo  implements Serializable  {

	private Long oid;
	private Allocation allocation;
	private Integer forecastYear;
	private String status;
	private Date auditDateTime;
	private String auditUserId;
	private Set<AllocationSector> allocationSectors = new LinkedHashSet();
	private Set<AllocationArea> allocationAreas = new LinkedHashSet();
	
	@Id @GeneratedValue( strategy=GenerationType.SEQUENCE, generator="allocation_sequence" ) 
	@SequenceGenerator( name="allocation_sequence", sequenceName="LTLF_SEQ" )
	@Column(name="OID")
	public Long getOid() {
		return oid;
	}

	public void setOid(Long id) {
		this.oid = id;
	}

	@Column(name="AUDIT_DATETIME")
	public Date getAuditDateTime() {
		return auditDateTime;
	}

	public void setAuditDateTime(Date auditDateTime) {
		this.auditDateTime = auditDateTime;
	}

	@Column(name="AUDIT_USERID")
	public String getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}

    @ManyToOne()
    @JoinColumn(name="ALLOCATION_OID")
	public Allocation getAllocation() {
		return allocation;
	}

	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	@Column(name="FORECAST_YEAR")
	public Integer getForecastYear() {
		return forecastYear;
	}

	public void setForecastYear(Integer forecastYear) {
		this.forecastYear = forecastYear;
	}

    @OneToMany(mappedBy="allocationForecastYear",cascade=CascadeType.ALL)
    @org.hibernate.annotations.BatchSize(size=100)
	public Set<AllocationArea> getAllocationAreas() {
		return allocationAreas;
	}

	public void setAllocationAreas(Set<AllocationArea> allocationAreas) {
		this.allocationAreas = allocationAreas;
	}

    @OneToMany(mappedBy="allocationForecastYear",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @org.hibernate.annotations.BatchSize(size=50)
	public Set<AllocationSector> getAllocationSectors() {
		return allocationSectors;
	}

	public void setAllocationSectors(Set<AllocationSector> allocationSectors) {
		this.allocationSectors = allocationSectors;
	}
	
	public void addAllocationSector(AllocationSector newSector) {
		newSector.setAllocationForecastYear(this);
		
		allocationSectors.add(newSector);
	}

	public void addAllocationArea(AllocationArea newArea) {
		newArea.setAllocationForecastYear(this);
		
		allocationAreas.add(newArea);
	}
	
	@Transient
	public AllocationSector getSector(String sectorType) {
		for (Iterator iterator = allocationSectors.iterator(); iterator.hasNext();) {
			AllocationSector sector = (AllocationSector) iterator.next();
			if (sector.getSectorType().equals(sectorType)) {
				return sector;
			}
		}
		return null;
	}

	@Transient
	public Float getSectorEnergy(String sectorType) {
		AllocationSector sector = getSector(sectorType);
		
		if (sector!=null)
			return sector.getAllocEnergy();
		
		return new Float(0);
	}

	@Transient
	public float getAreaTotalPercent(String sectorType) {
		
		float total = 0;

		if (allocationAreas!=null) {
			for (Iterator iterator = allocationAreas.iterator(); iterator.hasNext();) {
				AllocationArea area = (AllocationArea) iterator.next();
				AllocationSector sector = area.getSector(sectorType);
				if (sector!=null) {
					if (sector.getAllocPercent()==null)
						sector.setAllocPercent(new Float(0));
					
					total = total + sector.getAllocPercent().floatValue();
				}
			}
		}
		
		return total;
	}

	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Transient
    public SortedSet<AllocationArea> getAllocationAreas(Comparator c) {
    	TreeSet areaSet = new TreeSet(c);
    	areaSet.addAll(allocationAreas);
    	return areaSet;
    }
	
	@Transient
    public SortedSet<AllocationSector> getAllocationSectors(Comparator c) {
    	TreeSet sectorSet = new TreeSet(c);
    	sectorSet.addAll(allocationSectors);
    	return sectorSet;
    }
}
