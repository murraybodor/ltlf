package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * AnalysisDetail
 * Represents a load value for a measurement point at a specific date and hour ending.
 * 
 * @author mbodor
 *
 */
@Entity
@Table(name="ANALYSIS_DETAILS")
@org.hibernate.annotations.Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class AnalysisDetail extends LazyGwtPojo  implements Serializable {

	public static String ORIGINAL_TYPE = "Original";
	public static String CORRECTED_TYPE = "Corrected";
	public static String COMPARISON_TYPE = "Comparison";
	public static String FIXED_TYPE = "Fixed";
	public static String LATEST_TYPE = "Latest";
	
	private Long oid;
	private Date loadDate;
	private Integer loadHourEnd;
	private Double loadMw;
	private Double overrideMw;
	private Date auditDateTime;
	private String auditUserId;
	private MeasurementPoint measurementPoint;
	private Long mpOid;
	
	String fixId = "";
	Double fixValue = null;
	boolean changed = false;
	
	public AnalysisDetail() {
	}
	
	@Id
	@Column(name="OID")
	public Long getOid() {
		return oid;
	}
	public void setOid(Long oid) {
		this.oid = oid;
	}    
	
	@OneToOne()
	@JoinColumn(name="MP_OID", insertable=false, updatable=false)
	public MeasurementPoint getMeasurementPoint() {
		return measurementPoint;
	}
	
	public void setMeasurementPoint(MeasurementPoint aValue){
		measurementPoint=aValue;
	}

	@Column(name="CAL_DAY_DATE")
	public Date getLoadDate() {
		return loadDate;
	}

	public void setLoadDate(Date loadDate) {
		this.loadDate = loadDate;
	}

	@Column(name="CAL_HOUR_ENDING")
	public Integer getLoadHourEnd() {
		return loadHourEnd;
	}

	public void setLoadHourEnd(Integer loadHourEnd) {
		this.loadHourEnd = loadHourEnd;
	}

	@Column(name="MW")
	public Double getLoadMw() {
		return loadMw;
	}
	public void setLoadMw(Double loadMw) {
		this.loadMw = loadMw;
	}

	@Column(name="MW_OVERRIDE")
	public Double getOverrideMw() {
		return overrideMw;
	}
	public void setOverrideMw(Double overrideMw) {
		this.overrideMw = overrideMw;
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
	
	
	public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof AnalysisDetail) ) return false;

        final AnalysisDetail obj = (AnalysisDetail) other;
        
        if ( !obj.getLoadDate().equals( getLoadDate() ) ) return false;
        if ( !obj.getLoadHourEnd().equals( getLoadHourEnd() ) ) return false;
        if ( !obj.getLoadMw().equals( getLoadMw() ) ) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = getLoadDate().hashCode();
        result = 29 * result + getLoadHourEnd().hashCode();
        
        if (getLoadMw()!=null) {
            result = 29 * result * getLoadMw().hashCode();
        }
        
        return result;
    }

	@Column(name="MP_OID")
	public Long getMpOid() {
		return mpOid;
	}

	public void setMpOid(Long mpOid) {
		this.mpOid = mpOid;
	}

	public void addFix(String fixId, Double fixValue) {
		this.fixId = fixId;
		this.fixValue = fixValue;
		changed = true;
	}
	
	@Transient
	public String getFixId() {
		return fixId;
	}

	@Transient
	public boolean hasValue(String key) {
		if (key==null || key.equals(ORIGINAL_TYPE) || key.equals(COMPARISON_TYPE)) 
			return true;
		else if (key.equals(CORRECTED_TYPE))
			return true;
		else if (fixId.equals(key))
			return true;
		else
			return false;
	}
	
	@Transient
	public Double getValue(String valueKey) {
		if (valueKey==null || valueKey.equals(ORIGINAL_TYPE) || valueKey.equals(COMPARISON_TYPE)) {
			return getLoadMw();
		} else if (valueKey.equals(CORRECTED_TYPE)) {
			if (getOverrideMw()!=null) {
				return getOverrideMw();
			} else {
				return getLoadMw();
			}
		} else if (valueKey.equals(LATEST_TYPE)) {
			if (fixValue!=null)
				return fixValue;
			else {
				if (getOverrideMw()!=null) {
					return getOverrideMw();
				} else {
					return getLoadMw();
				}
			}
		} else if (valueKey.equals(FIXED_TYPE) || fixId.equals(valueKey)) {
			return fixValue;
		} else {
			return null;
		}
		
	}
	
	@Transient
	public void applyFix(String userId) {
		if (fixValue!=null) {
			setOverrideMw(fixValue);
			setAuditUserId(userId);
			setAuditDateTime(new Date());
			changed = true;
		}
	}
	
	@Transient
	public void undoFix() {
		fixId = "";
		fixValue = null;
	}
	
	@Transient
	public void removeOverride() {
		if (getOverrideMw()!=null) {
			setOverrideMw(null);
			changed = true;
		}
	}

	@Transient
	public boolean isChanged() {
		return changed;
	}
}
