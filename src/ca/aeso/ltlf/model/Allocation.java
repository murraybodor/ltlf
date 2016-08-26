package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Represents an energy allocation
 * 
 * @author mbodor
 */
@Entity
@Table(name="ALLOCATION")
@org.hibernate.annotations.Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@org.hibernate.annotations.Proxy(lazy=false)
public class Allocation extends LazyGwtPojo  implements Serializable  {

	private Long oid;
	private Integer baseYear;
	private Integer versionNumber;
	private String description;
	private Date startDate;
	private Integer endYear;
	private String status;
	private Date auditDateTime;
	private String auditUserId;
	private Set<AllocationForecastYear> allocationForecastYears = new LinkedHashSet();
	private Set<AllocationComment> allAllocationComments = new LinkedHashSet();
	
	
	@Id @GeneratedValue( strategy=GenerationType.SEQUENCE, generator="allocation_sequence" ) 
	@SequenceGenerator( name="allocation_sequence", sequenceName="LTLF_SEQ" )
	@Column(name="OID")
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

	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	@Column(name="START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name="END_YEAR")
	public Integer getEndYear() {
		return endYear;
	}

	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
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
	
    @OneToMany(mappedBy="allocation",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @OrderBy("forecastYear")    
    public Set<AllocationForecastYear> getAllocationForecastYears() {
    	return allocationForecastYears;
    }

    public void setAllocationForecastYears(Set<AllocationForecastYear> allocationForecastYears) {
    	this.allocationForecastYears = allocationForecastYears;
    }

    @Transient 
    public void addAllocationForecastYear(AllocationForecastYear aValue) {
    	if (allocationForecastYears == null) {
    		setAllocationForecastYears(new HashSet());
    	}
    	allocationForecastYears.add(aValue);
    }
    
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Transient
    public SortedSet<AllocationForecastYear> getAllocationForecastYears(Comparator c) {
    	TreeSet fySet = new TreeSet(c);
    	fySet.addAll(allocationForecastYears);
    	return fySet;
    }

    @OneToMany(mappedBy="allocation",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	public Set<AllocationComment> getAllAllocationComments() {
		return allAllocationComments;
	}

	public void setAllAllocationComments(Set<AllocationComment> allAllocationComments) {
		this.allAllocationComments = allAllocationComments;
	}

	@Transient
	public AllocationComment getAreaComment(Area area) {
		for (Iterator iterator = allAllocationComments.iterator(); iterator.hasNext();) {
			AllocationComment comment = (AllocationComment) iterator.next();
			if (comment.getArea()!=null && comment.getArea().equals(area))
				return comment;
			
		}
		return null;
	}

	@Transient
	public AllocationComment getMpComment(MeasurementPoint mp) {
		for (Iterator iterator = allAllocationComments.iterator(); iterator.hasNext();) {
			AllocationComment comment = (AllocationComment) iterator.next();
			if (comment.getMp()!=null && comment.getMp().equals(mp))
				return comment;
			
		}
		return null;
	}
	
	public void addComment(AllocationComment comment) {
		comment.setAllocation(this);
		allAllocationComments.add(comment);
	}
}
