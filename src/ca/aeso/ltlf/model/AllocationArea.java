package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Represents an area energy allocation
 * 
 * @author mbodor
 */
@Entity
@Table(name="ALLOCATION_AREA")
@org.hibernate.annotations.BatchSize(size=50)
public class AllocationArea extends LazyGwtPojo  implements Serializable  {

	private Long oid;
	private Area area;
	private String status;
	private Date auditDateTime;
	private String auditUserId;
	private AllocationForecastYear allocationForecastYear;
	private Set<AllocationSector> allocationSectors = new LinkedHashSet();
	private Set<AllocationMp> allocationMps = new LinkedHashSet();
	
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
    @JoinColumn(name="ALLOC_FY_OID")
	public AllocationForecastYear getAllocationForecastYear() {
		return allocationForecastYear;
	}

	public void setAllocationForecastYear(AllocationForecastYear allocationForecastYear) {
		this.allocationForecastYear = allocationForecastYear;
	}

    @OneToMany(mappedBy="allocationArea",cascade=CascadeType.ALL)
    public Set<AllocationMp> getAllocationMps() {
    	return allocationMps;
    }
    
    public void setAllocationMps(Set anAllocSet) {
    	allocationMps = anAllocSet;
    }

	@OneToOne()
	@JoinColumn(name="AREA_CODE")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

    @OneToMany(mappedBy="allocationArea",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @org.hibernate.annotations.BatchSize(size=250)
	public Set<AllocationSector> getAllocationSectors() {
		return allocationSectors;
	}

	public void setAllocationSectors(Set<AllocationSector> allocationSectors) {
		this.allocationSectors = allocationSectors;
	}

	public void addAllocationSector(AllocationSector newSector) {
		newSector.setAllocationArea(this);
		//newSector.setAllocationForecastYear(allocationForecastYear);
		
		allocationSectors.add(newSector);
	}
	
	public void addAllocationMp(AllocationMp newMp) {
		newMp.setAllocationArea(this);
		
		allocationMps.add(newMp);
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
	public Float getSectorPercent(String sectorType) {
		AllocationSector sector = getSector(sectorType);
		
		if (sector!=null)
			return sector.getAllocPercent();
		
		return new Float(0);
	}

	@Transient
	public Float getSectorEnergy(String sectorType) {
		AllocationSector sector = getSector(sectorType);
		
		if (sector!=null)
			return sector.getAllocEnergy();
		
		return new Float(0);
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Transient
	public Float getAllocationMpTotalPercent() {
		Float totalPct = new Float(0);
		
		for (Iterator iterator = allocationMps.iterator(); iterator.hasNext();) {
			AllocationMp allocMp = (AllocationMp) iterator.next();
			totalPct = totalPct + allocMp.getAllocPercent();
		}
		return totalPct;
	}

	/**
	 * Calculate all the energy allocated to this area for its' forecast year based on percents, regardless of sector
	 * @return
	 */
	@Transient
	public Float calculateTotalAreaFyEnergy() {
		
		Float totalAreaEnergy = new Float(0);
		
		for (Iterator iterator = allocationSectors.iterator(); iterator.hasNext();) {
			AllocationSector areaSector = (AllocationSector) iterator.next();
			Float areaSectorPercent = areaSector.getAllocPercent();
			Float fyEnergy = allocationForecastYear.getSectorEnergy(areaSector.getSectorType());

			Float areaSectorEnergy = fyEnergy * areaSectorPercent/100;
			totalAreaEnergy = totalAreaEnergy + areaSectorEnergy;
		}
		
		return totalAreaEnergy;
	}
	
}
