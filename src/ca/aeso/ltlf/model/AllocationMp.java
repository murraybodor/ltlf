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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Represents an MP energy allocation
 * 
 * @author mbodor
 */
@Entity
@Table(name="ALLOCATION_MP")
@org.hibernate.annotations.Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class AllocationMp extends LazyGwtPojo  implements Serializable  {

	private Long oid;
	private MeasurementPoint mp;
	private Integer baseYear;
	private Date beginDate;
	private Date endDate;
	private Float peakFactor = new Float(0);
	private Float allocPercent = new Float(0);
	private Float allocEnergy = new Float(0);
	private String status;
	private Date auditDateTime;
	private String auditUserId;
	private AllocationArea allocationArea;
	transient private SumUnitValues suv;
	
	@Id @GeneratedValue( strategy=GenerationType.SEQUENCE, generator="allocation_mp_sequence" ) 
	@SequenceGenerator( name="allocation_mp_sequence", sequenceName="LTLF_SEQ" )
	@Column(name="OID")
	public Long getOid() {
		return oid;
	}

	public void setOid(Long id) {
		this.oid = id;
	}


	@Column(name="BEGIN_DATE")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@Column(name="END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name="PEAK_FACTOR")
	public Float getPeakFactor() {
		return peakFactor;
	}

	public void setPeakFactor(Float peakFactor) {
		this.peakFactor = peakFactor;
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

	@OneToOne()
	@JoinColumn(name="MP_OID")
	public MeasurementPoint getMp() {
		return mp;
	}

	public void setMp(MeasurementPoint mp) {
		this.mp = mp;
	}

    @ManyToOne()
    @JoinColumn(name="ALLOC_AREA_OID")
	public AllocationArea getAllocationArea() {
		return allocationArea;
	}

    public void setAllocationArea(AllocationArea allocationArea) {
		this.allocationArea = allocationArea;
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

	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name="BASE_YEAR")
	public Integer getBaseYear() {
		return baseYear;
	}

	public void setBaseYear(Integer baseYear) {
		this.baseYear = baseYear;
	}

    
}
