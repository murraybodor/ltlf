package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Represents a forecast calendar entry
 * 
 * @author mbodor
 */
@Entity 
@Table(name="CALENDAR")
public class Calendar extends LazyGwtPojo  implements Serializable  {

	private Long oid;
	private Long timeDwhKey;
	private Date forecastDate;
	private Integer baseYear;
	private Integer baseDay;
	private Date auditDateTime;
	private String auditUserId;
	
	@Id @GeneratedValue( strategy=GenerationType.SEQUENCE, generator="calendar_sequence" ) 
	@SequenceGenerator( name="calendar_sequence", sequenceName="LTLF_SEQ" )
	@Column(name="OID")
	public Long getOid() {
		return oid;
	}

	public void setOid(Long id) {
		this.oid = id;
	}

	@Column(name="TIME_WHS_KEY")
	public Long getTimeDwhKey() {
		return timeDwhKey;
	}

	public void setTimeDwhKey(Long timeDwhKey) {
		this.timeDwhKey = timeDwhKey;
	}

	@Column(name="FORECAST_DATE")
	public Date getForecastDate() {
		return forecastDate;
	}

	public void setForecastDate(Date forecastDate) {
		this.forecastDate = forecastDate;
	}

	@Column(name="BASE_YEAR")
	public Integer getBaseYear() {
		return baseYear;
	}

	public void setBaseYear(Integer baseYear) {
		this.baseYear = baseYear;
	}

	@Column(name="BASE_DAY")
	public Integer getBaseDay() {
		return baseDay;
	}

	public void setBaseDay(Integer baseDay) {
		this.baseDay = baseDay;
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
