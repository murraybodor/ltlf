package ca.aeso.ltlf.model;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Loader;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Represents sum of unitized values for a measurement point over a period of time
 * 
 * @author mbodor
 */
@Entity
@NamedNativeQuery(name="suv",query="select mp.mp_oid as oid,  sum(lsd.unit_value) as suv " +
		"from allocation_mp mp, calendar c, load_shape_details lsd " + 
		"where c.base_year = :cbaseYear " +
		"and c.base_year = mp.base_year " +
		"and c.forecast_date BETWEEN mp.begin_date and mp.end_date " + 
		"and c.forecast_date BETWEEN TO_DATE(:beginDate,'YYYY-MM-DD HH24:MI:SS') and TO_DATE(:endDate,'YYYY-MM-DD HH24:MI:SS') " +
		"and lsd.mp_oid = mp.mp_oid and mp.mp_oid = :mpId and " + 
		"lsd.base_year = c.base_year and lsd.base_day = c.base_day " +
		"group by mp.mp_oid", resultSetMapping="suvMapping")
@SqlResultSetMapping(
		name = "suvMapping",
		entities = {
		@EntityResult(
				entityClass = ca.aeso.ltlf.model.SumUnitValues.class,
				fields = {
				@FieldResult(name = "oid", column = "oid"),
				@FieldResult(name = "suv", column = "suv")})
				})
		
public class SumUnitValues extends LazyGwtPojo  implements Serializable  {

	private Long oid;
	private Float suv = new Float(0);
	private AllocationMp allocationMp;
	
	@Id
	@Column(name="oid")
	public Long getOid() {
		return oid;
	}
	
	public void setOid(Long aValue) {
		oid = aValue;
	}
	
	@OneToOne()
	@JoinColumn(name="oid", insertable=false, updatable=false)
	public AllocationMp getAllocationMp() {
		return allocationMp;
	}
	public void setAllocationMp(AllocationMp allocationMp) {
		this.allocationMp = allocationMp;
	}
	
	@Column(name="suv")
	public Float getSuv() {
		return suv;
	}
	public void setSuv(Float suv) {
		this.suv = suv;
	}
	
	
}
