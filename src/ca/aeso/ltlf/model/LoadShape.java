package ca.aeso.ltlf.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

@Entity
@Table(name="LOAD_SHAPE")
@org.hibernate.annotations.Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class LoadShape extends LazyGwtPojo {

	private Long oid;
	private Integer baseYear;
	private Date unitizeDate;
	private String status;
	private float progress;
	private List<LoadShapeSummary> summaries;
	public Collection<LoadShapeSummary> summariesByPlanningArea;
	private Date auditDateTime;
	private String auditUserId;
	
	@Id
	@Column(name="OID")
	public Long getOid() {
		return oid;
	}

	public void setOid(Long aValue) {
		oid = aValue;
	}
	
	@Column(name="BASE_YEAR")
	public Integer getBaseYear() {
		return baseYear;
	}
	
	public void setBaseYear(Integer aValue) {
		baseYear = aValue;
	}
	
	@Column(name="UNITIZE_DT")
	public Date getUnitizeDate() {
		return unitizeDate;
	}
	
	public void setUnitizeDate(Date aValue) {
		unitizeDate = aValue;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String aValue) {
		status = aValue;
	}
	
	@Column(name="PROGRESS")
	public float getProgress() {
		return progress;
	}
	
	public void setProgress(float aValue) {
		progress = aValue;
	}
	
	@OneToMany(mappedBy="loadShape",cascade=CascadeType.ALL)
	public List<LoadShapeSummary> getSummaries() {
		return summaries;
	}

	public void setSummaries(List<LoadShapeSummary> aValue) {
		summaries = aValue;
	}
	
	@Transient
	public Collection getSummariesByPlanningArea() {
		return summariesByPlanningArea;
	}
	
	public void setSummariesByPlannngArea(Collection aValue){
		summariesByPlanningArea = aValue;
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
