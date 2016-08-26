package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Represents a version of a detailed load shape for a measurement point
 *
 * @author mbodor
 */
@Entity
@Table(name="LOAD_SHAPE_DETAILS")
@org.hibernate.annotations.Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@org.hibernate.annotations.Proxy(lazy=false)
public class LoadShapeDetail extends LazyGwtPojo  implements Serializable  {

	public static String ORIGINAL_TYPE = "Original";
	public static String CORRECTED_TYPE = "Corrected";
	public static String COMPARISON_TYPE = "Comparison";

	private Long oid;
	private int baseYear;
	private int baseDay;
	private int baseHourEnding;
	private Double unitizedValue;
	private Double unitizedValueOverride;
	private Date auditDateTime;
	private String auditUserId;
	private MeasurementPoint measurementPoint;
	private Long mpOid;
	private LoadShapeSummary loadShapeSummary;

	private String fixId = "";
	private Double fixValue = null;
	private boolean changed = false;

	@Id @GeneratedValue( strategy=GenerationType.SEQUENCE, generator="loadshape_dtl_sequence" )
	@SequenceGenerator( name="loadshape_dtl_sequence", sequenceName="LTLF_SEQ" )
	@Column(name="OID")
	public Long getOid() {
		return oid;
	}

	public void setOid(Long id) {
		this.oid = id;
	}

	@OneToOne()
	@JoinColumn(name="MP_OID", insertable=false, updatable=false)
	public MeasurementPoint getMeasurementPoint() {
		return measurementPoint;
	}

	public void setMeasurementPoint(MeasurementPoint aValue){
		measurementPoint=aValue;
	}

	@Column(name="BASE_YEAR")
	public int getBaseYear() {
		return baseYear;
	}
	public void setBaseYear(int baseYear) {
		this.baseYear = baseYear;
	}

	@Column(name="BASE_DAY")
	public int getBaseDay() {
		return baseDay;
	}
	public void setBaseDay(int baseDay) {
		this.baseDay = baseDay;
	}

	@Column(name="BASE_HOUR_END")
	public int getBaseHourEnding() {
		return baseHourEnding;
	}
	public void setBaseHourEnding(int baseHourEnding) {
		this.baseHourEnding = baseHourEnding;
	}

	@Column(name="UNIT_VALUE")
	public Double getUnitizedValue() {
		return unitizedValue;
	}
	public void setUnitizedValue(Double unitizedValue) {
		this.unitizedValue = unitizedValue;
	}

	@Column(name="UNIT_VALUE_OVERRIDE")
	public Double getUnitizedValueOverride() {
		return unitizedValueOverride;
	}
	public void setUnitizedValueOverride(Double unitizedValueOverride) {
		this.unitizedValueOverride = unitizedValueOverride;
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

	@Column(name="MP_OID")
	public Long getMpOid() {
		return mpOid;
	}

	public void setMpOid(Long mpOid) {
		this.mpOid = mpOid;
	}

	@Transient
	public void addFix(String fixId, Double fixValue) {
		this.fixId = fixId;
		this.fixValue = fixValue;
		changed = true;
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
			return getUnitizedValue();
		} else if (valueKey.equals(CORRECTED_TYPE)) {
			return getCorrectedValue();
		} else if (fixId.equals(valueKey)) {
			return fixValue;
		} else {
			// no value for given key, so just return the corrected value
			return getCorrectedValue();
		}

	}

	@Transient
	private Double getCorrectedValue() {
		if (getUnitizedValueOverride()!=null) {
			return getUnitizedValueOverride();
		} else {
			return getUnitizedValue();
		}
	}

	@Transient
	public void applyFix(String userId) {
		if (fixValue!=null) {
			setUnitizedValueOverride(fixValue);
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
		if (getUnitizedValueOverride()!=null) {
			setUnitizedValueOverride(null);
			changed = true;
		}
	}

	@Transient
	public boolean isChanged() {
		return changed;
	}

    @ManyToOne()
    @JoinColumn(name="LOAD_SHAPE_SUMM_OID", insertable=false, updatable=false)
	public LoadShapeSummary getLoadShapeSummary() {
		return loadShapeSummary;
	}

	public void setLoadShapeSummary(LoadShapeSummary loadShapeSummary) {
		this.loadShapeSummary = loadShapeSummary;
	}

	@Transient
	public LoadShapeDetail clone() {
		LoadShapeDetail clone = new LoadShapeDetail();

		clone.setLoadShapeSummary(this.loadShapeSummary);
		clone.setMeasurementPoint(this.measurementPoint);
		clone.setBaseYear(this.baseYear);
		clone.setBaseDay(this.baseDay);
		clone.setBaseHourEnding(this.baseHourEnding);
		clone.setUnitizedValue(this.unitizedValue);

		return clone;
	}

}
