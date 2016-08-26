package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

@Entity
@Table(name="MEASUREMENT_POINT_DETAILS")
@org.hibernate.annotations.Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@org.hibernate.annotations.Proxy(lazy=false)
@org.hibernate.annotations.BatchSize(size=10)
public class MeasurementPointDetail extends LazyGwtPojo implements Serializable {

	private Long oid;
	private MeasurementPoint measurementPoint;
	private Date creationDate;
	private Date expiryDate;
	private String name;
	private String zoneCode;
	private String measurementPointTypeCode;
	private String measurementPointDesc;
	private Date measurementPointEffDate;
	private Date measurementPointExpireDate;
	private Long programIn;
	private Long projectIn;
	private String inclInPodLsb;

	@Id
	@Column(name="OID")
	public Long getOid() {
		return oid;
	}
	public void setOid(Long aValue) {
		this.oid = aValue;
	}

	@Column(name="CREATE_DATE")
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date aValue) {
		creationDate = aValue;
	}
	
	@Column(name="EXPIRY_DATE")
	public Date getExpiryDate() {
		return expiryDate;
	}
	
	public void setExpiryDate(Date aValue) {
		expiryDate = aValue;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SELECT)
	@JoinColumn(name="MP_OID")
	public MeasurementPoint getMeasurementPoint() {
		return measurementPoint;
	}
	
	public void setMeasurementPoint(MeasurementPoint aValue) {
		measurementPoint = aValue;
	}
	
	@Column(name="MP_NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String aValue) {
		name = aValue;
	}
	
	@Column(name="AREA_CODE")
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	@Column(name="MEASUREMENT_POINT_TYPE_CODE")
	public String getMeasurementPointTypeCode() {
		return measurementPointTypeCode;
	}
	public void setMeasurementPointTypeCode(String measurementPointTypeCode) {
		this.measurementPointTypeCode = measurementPointTypeCode;
	}

	@Column(name="MEASUREMENT_POINT_DESCR")
	public String getMeasurementPointDesc() {
		return measurementPointDesc;
	}
	public void setMeasurementPointDesc(String measurementPointDesc) {
		this.measurementPointDesc = measurementPointDesc;
	}

	@Column(name="MEASUREMENT_POINT_EFF")
	public Date getMPEffectiveDate() {
		return measurementPointEffDate;
	}
	public void setMPEffectiveDate(Date measurementPointEffDate) {
		this.measurementPointEffDate = measurementPointEffDate;
	}

	@Column(name="MEASUREMENT_POINT_EXP")
	public Date getMPExpireDate() {
		return measurementPointExpireDate;
	}
	public void setMPExpireDate(Date measurementPointExp) {
		this.measurementPointExpireDate = measurementPointExp;
	}

	@Column(name="PROGRAM_IN")
	public Long getProgramIn() {
		return programIn;
	}
	public void setProgramIn(Long programIn) {
		this.programIn = programIn;
	}

	@Column(name="PROJECT_IN")
	public Long getProjectIn() {
		return projectIn;
	}
	public void setProjectIn(Long aValue) {
		this.projectIn = aValue;
	}
	
	@Column(name="INCL_IN_POD_LSB")
	public String getInclInPodLsb() {
		return inclInPodLsb;
	}
	public void setInclInPodLsb(String inclInPodLsb) {
		this.inclInPodLsb = inclInPodLsb;
	}

	
}
