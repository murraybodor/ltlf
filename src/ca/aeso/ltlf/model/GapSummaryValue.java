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
import javax.persistence.Transient;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

import org.hibernate.annotations.FilterDef;

/**
 * @author dthachuk
 *
 */

@Entity
@Table(name="ANALYSIS_SUMMARY")
@FilterDef(name="FilteredResults")
@org.hibernate.annotations.Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@org.hibernate.annotations.Proxy(lazy=false)
public class GapSummaryValue extends LazyGwtPojo  implements Serializable {

	private Long oid;
	private Long measurementPointOid;
	private Integer gapCount;
	private String status;
	private Long analysisId;
	private GapAnalysis parentAnalysis;
	private String _readyValue;
	public MeasurementPoint measurementPoint;
	private Double prevYearLoadFactor;
	private Double baseYearLoadFactor;
	private Date auditDateTime;
	private String auditUserId;
	
	@Id
	@Column(name = "OID")
	public Long getOid() {
		return oid;
	}

	public void setOid(Long aValue) {
		oid = aValue;
	}
	
	@Column(name="READY")
	public String getReadyString() {
		return _readyValue;
	}
	
	public void setReadyString(String aValue){
		_readyValue = aValue;
	}
	
	@Column(name="GAP_COUNT")
	public Integer getGapCount() {
		return gapCount;
	}
	
	public void setGapCount(Integer aValue) {
		gapCount = aValue;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String aValue) {
		status = aValue;
	}
	
	@Column(name = "ANALYSIS_OID")
	public Long getAnalysisId() {
		return analysisId;
	}
	
	public void setAnalysisId(Long aValue){
		analysisId = aValue;
	}
	
	@Column(name = "MP_OID")
	public Long getMpOid() {
		return measurementPointOid;
	}
	
	public void setMpOid(Long aValue) {
		measurementPointOid = aValue;
	}
    @ManyToOne()
    @JoinColumn(name="ANALYSIS_OID", insertable=false, updatable=false)
     public GapAnalysis getGapAnalysis() {
        return parentAnalysis;
    }
    
    public void setGapAnalysis(GapAnalysis aValue) {
    	parentAnalysis = aValue;
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
	public String getDescription() {
		if (measurementPoint == null)
			return null;
		
		return measurementPoint.getDescription();
	}
	
	public void setDescription(String aValue) {
		
	}
	
	@Transient
	public String getMpName() {
		if (measurementPoint == null)
			return null;
		
		return measurementPoint.getName();
	}
	
	public void setMpName(String aValue) {
		
	}

	@OneToOne()
	@JoinColumn(name="MP_OID", insertable=false, updatable=false)
	public MeasurementPoint getMeasurementPoint() {
		return measurementPoint;
	}
	
	public void setMeasurementPoint(MeasurementPoint aValue){
		measurementPoint=aValue;
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
	
 // assert oid's type is java.lang.Long
    public boolean equals(Object other) {
    	if ( this == other) 
    		return true;
    	GapSummaryValue anEntity = (GapSummaryValue) other;
    	if (this.oid == null || anEntity.oid == null) 
    		return false;
    	return this.oid.equals(anEntity.oid);
    }
    
    public int hashCode()
    {
        int hashCode = 0;
        hashCode = 29 * hashCode + (oid == null ? 0 : oid.hashCode());

        return hashCode;
    }

	@Column(name="PREV_YEAR_LOAD_FACTOR")
	public Double getPrevYearLoadFactor() {
		return prevYearLoadFactor;
	}

	public void setPrevYearLoadFactor(Double prevYearLoadFactor) {
		this.prevYearLoadFactor = prevYearLoadFactor;
	}

	@Column(name="BASE_YEAR_LOAD_FACTOR")
	public Double getBaseYearLoadFactor() {
		return baseYearLoadFactor;
	}

	public void setBaseYearLoadFactor(Double baseYearLoadFactor) {
		this.baseYearLoadFactor = baseYearLoadFactor;
	}
}
