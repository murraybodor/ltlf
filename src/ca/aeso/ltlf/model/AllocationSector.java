package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Represents an allocation sector
 * 
 * @author mbodor
 */
@Entity
@Table(name="ALLOCATION_SECTOR")
public class AllocationSector extends LazyGwtPojo  implements Serializable  {

	private Long oid;
	private AllocationForecastYear allocationForecastYear;
	private AllocationArea allocationArea;
	private String sectorType;
	private Float allocPercent = new Float(0);
	private Float allocEnergy = new Float(0);
	private Date auditDateTime;
	private String auditUserId;
	
	
	@Id @GeneratedValue( strategy=GenerationType.SEQUENCE, generator="allocation_sequence" ) 
	@SequenceGenerator( name="allocation_sequence", sequenceName="LTLF_SEQ" )
	@Column(name="OID")
	public Long getOid() {
		return oid;
	}

	public void setOid(Long id) {
		this.oid = id;
	}

    @ManyToOne()
    @JoinColumn(name="ALLOC_FY_OID")
	public AllocationForecastYear getAllocationForecastYear() {
		return allocationForecastYear;
	}

	public void setAllocationForecastYear(AllocationForecastYear allocationForecastYear) {
		this.allocationForecastYear = allocationForecastYear;
	}

	@Column(name="ALLOC_PERCENT")
	public Float getAllocPercent() {
		return allocPercent;
	}

	public void setAllocPercent(Float allocPercent) {
		this.allocPercent = allocPercent;
	}

	@Column(name="ALLOC_ENERGY")
	public Float getAllocEnergy() {
		return allocEnergy;
	}

	public void setAllocEnergy(Float allocEnergy) {
		this.allocEnergy = allocEnergy;
	}

    @ManyToOne()
    @JoinColumn(name="ALLOC_AREA_OID")
	public AllocationArea getAllocationArea() {
		return allocationArea;
	}

	public void setAllocationArea(AllocationArea allocationArea) {
		this.allocationArea = allocationArea;
	}

	@Column(name="SECTOR_TYPE")
	public String getSectorType() {
		return sectorType;
	}

	public void setSectorType(String sectorType) {
		this.sectorType = sectorType;
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

	
}
