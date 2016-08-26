package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Represents a load forecast
 * 
 * @author mbodor
 */
@Entity
@Table(name="LOAD_FORECAST_SUMMARY")
@org.hibernate.annotations.Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class LoadForecastSummary extends LazyGwtPojo  implements Serializable  {

	private Long oid;
	private Integer baseYear;
	private Integer versionNumber;
	private String comments;
	private Date createDate;
	private Date publishDate;
	private List<LoadForecastDetail> loadForecastDetails;
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

	@Column(name="BASE_YEAR")
	public Integer getBaseYear() {
		return baseYear;
	}

	public void setBaseYear(Integer baseYear) {
		this.baseYear = baseYear;
	}

	@Column(name="VERSION_NUM")
	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	@Column(name="COMMENTS")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name="CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name="PUBLISH_DATE")
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
	@ManyToMany(targetEntity=LoadForecastDetail.class,
	        cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="LOAD_FORECAST_SD",
	        joinColumns=@JoinColumn(name="LFS_OID"),
	        inverseJoinColumns=@JoinColumn(name="LFD_OID"))
    public List<LoadForecastDetail> getLoadForecastDetails() {
        return loadForecastDetails;
    }

	public void setLoadForecastDetails(List<LoadForecastDetail> loadForecastDetails) {
		this.loadForecastDetails = loadForecastDetails;
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
