package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Measurement Point 
 * @author dthachuk
 */
@Entity
@Table(name="MEASUREMENT_POINT")
@org.hibernate.annotations.Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@org.hibernate.annotations.Proxy(lazy=false)
@org.hibernate.annotations.BatchSize(size=10)
public class MeasurementPoint extends LazyGwtPojo  implements Serializable {

	private Long oid;
	private Long tasMeasurePointId;
	private Set<MeasurementPointDetail> details;
	private MeasurementPointDetail currentDetail;
	private Date createdDate;

	@Id
	@Column(name="OID")
	public Long getOid() {
		return oid;
	}
	public void setOid(Long aValue) {
		this.oid = aValue;
	}

	@Transient
	public MeasurementPointDetail getCurrentDetail() {
		if (currentDetail == null){
			Date today = new Date();
			currentDetail = getDetail(today);
		}
		
		return currentDetail;
	}

	@Transient
	public MeasurementPointDetail getDetail(Date requestedDate) {
		MeasurementPointDetail selectedIdentifier = null;
		if (getDetails() == null)
			return null;
		
		Iterator itr = getDetails().iterator();
		
		while (itr.hasNext()){
			MeasurementPointDetail aDetail = (MeasurementPointDetail) itr.next();
			if (aDetail.getCreationDate().before(requestedDate) && (aDetail.getExpiryDate() == null || aDetail.getExpiryDate().after(requestedDate))) {
				return aDetail;
			}
		}

		return null;
	}

	@OneToMany(mappedBy="measurementPoint",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@org.hibernate.annotations.BatchSize(size=50)
	public Set<MeasurementPointDetail> getDetails() {
		return details;
	}
	
	public void setDetails(Set aCollection) {
		details = aCollection;
	}
	
	@Column(name="CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date aValue) {
		createdDate=aValue;
	}
	
	@Transient
	public String getName() {
		if (getCurrentDetail() == null)
			return null;
		
		return getCurrentDetail().getName();
	}
	
	@Transient
	public String getDescription() {
		if (getCurrentDetail() == null)
			return null;
		
		return getCurrentDetail().getMeasurementPointDesc();
	}
	
	public void setDescription(String aValue) {
		if (getCurrentDetail() == null)
			return;
		
		getCurrentDetail().setMeasurementPointDesc(aValue);
	}
	
	@Transient
	public Date getMPEffectiveDate() {
		if (getCurrentDetail() == null)
			return null;
		
		return getCurrentDetail().getMPEffectiveDate();
	}
	
	public void setMPEffectiveDate(Date aValue) {
		if (getCurrentDetail() == null)
			return;
		
		getCurrentDetail().setMPEffectiveDate(aValue);
	}
	
	@Transient
	public Date getMPExpireDate() {
		if (getCurrentDetail() == null)
			return null;
		
		return getCurrentDetail().getMPExpireDate();
	}
	
	public void setMPExpireDate(Date aValue) {
		if (getCurrentDetail() == null)
			return;
		
		getCurrentDetail().setMPExpireDate(aValue);
	}
	
	@Transient
	public String getZoneCode() {
		if (getCurrentDetail() == null)
			return null;
		
		return getCurrentDetail().getZoneCode();
	}
	
	public void setZoneCode(String aValue) {
		if (getCurrentDetail() == null)
			return;
		
		getCurrentDetail().setZoneCode(aValue);
	}

	@Column(name="TAS_MEASURE_POINT_ID")
	public Long getTasMeasurePointId() {
		return tasMeasurePointId;
	}
	public void setTasMeasurePointId(Long tasMeasurePointId) {
		this.tasMeasurePointId = tasMeasurePointId;
	}
}
