package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Represents a load forecast detail
 * 
 * @author mbodor
 */
@Entity
@Table(name="LOAD_FORECAST_DETAILS")
@org.hibernate.annotations.Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@org.hibernate.annotations.Proxy(lazy=false)
public class LoadForecastDetail extends LazyGwtPojo  implements Serializable  {

	private Long oid;
	private MeasurementPoint measurementPoint;
	private Date forecastDate;
	private Integer forecastHourEnd;
	private Double forecastValue;
	private List<LoadForecastSummary> loadForecastSummaries;
	private Date auditDateTime;
	private String auditUserId;
	
	
	@Id
	@Column(name="OID", insertable=false, updatable=false)
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
	
	@Column(name="FORECAST_DATE")
	public Date getForecastDate() {
		return forecastDate;
	}

	public void setForecastDate(Date forecastDate) {
		this.forecastDate = forecastDate;
	}

	@Column(name="FORECAST_HOUR_END")
	public Integer getForecastHourEnd() {
		return forecastHourEnd;
	}

	public void setForecastHourEnd(Integer forecastHourEnd) {
		this.forecastHourEnd = forecastHourEnd;
	}

	@Column(name="FORECAST_VALUE")
	public Double getForecastValue() {
		return forecastValue;
	}

	public void setForecastValue(Double forecastValue) {
		this.forecastValue = forecastValue;
	}

	@ManyToMany(targetEntity=LoadForecastSummary.class,
	        cascade={CascadeType.PERSIST, CascadeType.MERGE},
	        mappedBy="loadForecastDetails")
    public List<LoadForecastSummary> getLoadForecastSummaries() {
        return loadForecastSummaries;
    }

	public void setLoadForecastSummaries(List<LoadForecastSummary> loadForecastSummaries) {
		this.loadForecastSummaries = loadForecastSummaries;
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
