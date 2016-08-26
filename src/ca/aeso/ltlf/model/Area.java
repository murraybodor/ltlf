package ca.aeso.ltlf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

@Entity
@Immutable
@Table(name="AREA_V")
public class Area extends LazyGwtPojo implements Serializable {
	private String code;
	private String name;

	@Id
	@Column(name = "AREA_CODE")
	public String getCode(){
		return code;
	}
	
	public void setCode(String aValue){
		code = aValue;
	}
	

	
	@Column(name = "AREA_NAME")
	public String getName(){
		return name;
	}
	
	public void setName(String aValue){
		name = aValue;
	}
}
