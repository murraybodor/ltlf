package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Represents an allocation comment
 * 
 * @author mbodor
 */
@Entity
@Table(name="ALLOCATION_COMMENTS")
public class AllocationComment extends LazyGwtPojo  implements Serializable  {

	private Long oid;
	private Allocation allocation;
	private Area area;
	private MeasurementPoint mp;
	private String comments;
	private Date auditDateTime;
	private String auditUserId;
	
	
	@Id @GeneratedValue( strategy=GenerationType.SEQUENCE, generator="allocation_sequence" ) 
	@SequenceGenerator( name="allocation_sequence", sequenceName="LTLF_SEQ" )
	@Column(name="OID")
	public Long getOid() {
		return oid;
	}

	public void setOid(Long id) {
		this.oid = id;
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

    @ManyToOne()
    @JoinColumn(name="ALLOCATION_OID")
	public Allocation getAllocation() {
		return allocation;
	}

	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

    @ManyToOne()
    @JoinColumn(name="AREA_CODE")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

    @ManyToOne()
    @JoinColumn(name="MP_OID")
	public MeasurementPoint getMp() {
		return mp;
	}

	public void setMp(MeasurementPoint mp) {
		this.mp = mp;
	}

	@Column(name="COMMENTS")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	
}
