package ca.aeso.ltlf.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

@Entity
@Table(name="LOAD_SHAPE_SUMMARY")
@org.hibernate.annotations.Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@org.hibernate.annotations.Proxy(lazy=false)
public class LoadShapeSummary extends LazyGwtPojo {
	
	private Long oid;
	private Long mpOid;
	private String status;
	private Integer versionNumber;
	private LoadShape loadShape;
	private MeasurementPoint measurementPoint;
	private Date auditDateTime;
	private String auditUserId;
	private List<LoadShapeDetail> details;
	private String comments;
	
	@Id
	@Column(name = "OID")
	public Long getOid() {
		return oid;
	}

	public void setOid(Long aValue) {
		oid = aValue;
	}
	
	@Column(name = "MP_OID")
	public Long getMpOid() {
		return mpOid;
	}
	
	public void setMpOid(Long aValue) {
		mpOid = aValue;
	}
	
	@Transient
	public String getMpName() {
		if (measurementPoint == null)
			return null;
		
		return measurementPoint.getName();
	}
	
	public void setMpName(String aValue) {
		
	}

	@Transient
	public String getDescription() {
		if (measurementPoint == null)
			return null;
		
		return measurementPoint.getDescription();
	}
	
	public void setDescription(String aValue) {
		
	}

	@Transient
	public Date getEffectiveDate() {
		if (measurementPoint == null)
			return null;
		
		return measurementPoint.getMPEffectiveDate();
	}
	
	public void setEffectiveDate(Date aValue) {
		
	}
	
	@Transient
	public Date getExpiryDate() {
		if (measurementPoint == null)
			return null;
		
		return measurementPoint.getMPExpireDate();
	}
	
	public void setExpiryDate(Date aValue) {
		
	}
	
	@Transient
	public String getZoneCode() {
		if (measurementPoint == null)
			return null;
		
		return measurementPoint.getZoneCode();
	}
	
	public void setZoneCode(String aValue) {
		
	}
	
		
	public void setMeasurementPoint(MeasurementPoint aValue){
		measurementPoint=aValue;
	}
	
	@OneToOne()
	@JoinColumn(name="MP_OID", insertable=false, updatable=false)
	public MeasurementPoint getMeasurementPoint() {
		return measurementPoint;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String aValue) {
		status = aValue;
	}
	
    @ManyToOne()
    @JoinColumn(name="LOAD_SHAPE_OID", insertable=false, updatable=false)
     public LoadShape getLoadShape() {
        return loadShape;
    }
    
    public void setLoadShape(LoadShape aValue) {
    	loadShape = aValue;
    }

	@Column(name="VERSION_NUM")
	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
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

	@OneToMany(mappedBy="loadShapeSummary",cascade=CascadeType.ALL)
	public List<LoadShapeDetail> getDetails() {
		return details;
	}

	public void setDetails(List<LoadShapeDetail> details) {
		this.details = details;
	}
	@Column(name = "COMMENTS")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
