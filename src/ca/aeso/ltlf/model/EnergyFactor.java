package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Represents an energy factor - is this needed??
 * 
 * @author mbodor
 */
@Entity
@Table(name="ENERGY_FACTORS")
public class EnergyFactor extends LazyGwtPojo  implements Serializable  {

	private Long oid;
	private Integer forecastYear;
	private Area area;
	private MeasurementPoint mp;
	private Date beginDate;
	private Date endDate;
	private Float energyFactor;
	private Float peakFactor;
	private String comments;
	private Date auditDateTime;
	private String auditUserId;
	private AllocationArea allocationDetail;
	
	
	@Id
	@Column(name="OID", insertable=false, updatable=false)
	public Long getOid() {
		return oid;
	}

	public void setOid(Long id) {
		this.oid = id;
	}

	@Column(name="FORECAST_YEAR")
	public Integer getForecastYear() {
		return forecastYear;
	}

	public void setForecastYear(Integer forecastYear) {
		this.forecastYear = forecastYear;
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

	@Column(name="ENERGY_FACTOR")
	public Float getEnergyFactor() {
		return energyFactor;
	}

	public void setEnergyFactor(Float energyFactor) {
		this.energyFactor = energyFactor;
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

	@OneToOne()
	@JoinColumn(name="AREA_OID", insertable=false, updatable=false)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@OneToOne()
	@JoinColumn(name="MP_OID", insertable=false, updatable=false)
	public MeasurementPoint getMp() {
		return mp;
	}

	public void setMp(MeasurementPoint mp) {
		this.mp = mp;
	}

	@Column(name="PEAK_FACTOR")
	public Float getPeakFactor() {
		return peakFactor;
	}

	public void setPeakFactor(Float peakFactor) {
		this.peakFactor = peakFactor;
	}

	@Column(name="COMMENTS")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

    @ManyToOne()
    @JoinColumn(name="ALLOC_DETAIL_OID")
	public AllocationArea getAllocationDetail() {
		return allocationDetail;
	}

	public void setAllocationDetail(AllocationArea allocationDetail) {
		this.allocationDetail = allocationDetail;
	}

	
}
