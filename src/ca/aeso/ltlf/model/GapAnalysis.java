package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

import org.hibernate.annotations.Filter;

@Entity
@Table(name="ANALYSIS")
public class GapAnalysis extends LazyGwtPojo  implements Serializable {
	
	public Long oid;
	private Date createdDate;
	private Date importStartDate;
	private Date importEndDate;
	private String status;
	private float progress;
	private long total_count;
	private List<GapSummaryValue> gapValues;
	
    @Id
    @Column(name="OID")
	public Long getOid() {
		return oid;
	}
    
    public void setOid(Long aValue) {
    	oid = aValue;
    }
    
    @Column(name="CREATED_DT")
    public Date getCreatedDate() {
    	return createdDate;
    }
    
    public void setCreatedDate(Date aValue) {
    	createdDate = aValue;
    }
    
    @Column(name="IMPORT_START_DT")
    public Date getImportStartDate() {
    	return importStartDate;
    }
    
    public void setImportStartDate(Date aValue) {
    	importStartDate = aValue;
    }
    
    @Column(name="IMPORT_END_DT")
    public Date getImportEndDate() {
    	return importEndDate;
    }
    
    public void setImportEndDate(Date aValue) {
    	importEndDate = aValue;
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
    
    @Column(name="TOTAL_COUNT")
    public long getTotalCount() {
    	return total_count;
    }
    
    public void setTotalCount(long aValue) {
    	total_count = aValue;
    }
    
    @OneToMany(mappedBy="gapAnalysis",cascade=CascadeType.ALL/*,fetch=FetchType.EAGER*/)
    @Filter(name="FilteredResults" ,condition="gap_Count > 0" )
    public List<GapSummaryValue> getSummaryValues() {
    	return gapValues;
    }

    public void setSummaryValues(List<GapSummaryValue> aValue) {
    	gapValues = aValue;
    }
    
    public boolean equals(Object other) {
    	if ( this == other) 
    		return true;
    	GapAnalysis anEntity = (GapAnalysis) other;
    	if (this.oid == null || anEntity.oid == null) 
    		return false;
    	return this.oid.equals(anEntity.oid);
    }
  /*  
    public int hashCode()
    {
        int hashCode = 0;
        hashCode = 29 * hashCode + (oid == null ? 0 : oid.hashCode());

        return hashCode;
    }
    */
}
