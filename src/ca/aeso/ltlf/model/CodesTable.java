package ca.aeso.ltlf.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.DiscriminatorFormula;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

@Entity
@Table(name="CODES_T")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("case when CODE_TYPE in ('ALLOC_SECTOR') then '1' else '0' end")
@DiscriminatorValue("0")
public class CodesTable extends LazyGwtPojo implements Serializable
{
	private static final long serialVersionUID = 4014636966326023978L;
	private Long oid;
	private String code;
	private String valueStr;
	private Integer valueNum;
	private Date valueDate;
	private String codeDescription;
	private Integer orderInfo;
	private String suppInfo;
	
    @Id
    @Column(name="OID")
	public Long getId() {
		return oid;
	}
    
    public void setId(Long id) {
    	oid = id;
    }
    
    @Column(name="CODE_TYPE",insertable=false, updatable=false)
	public String getCode() {
		return code;
	}
    
    public void setCode(String code) {
    	this.code = code;
    }

    @Column(name="CODE_VALUE_STR")
	public String getStringValue() {
		return valueStr;
	}
    
    public void setStringValue(String value_str) {
    	valueStr = value_str;
    }

	@Column(name="CODE_VALUE_NUM")
	public Integer getNumericValue() {
		return valueNum;
	}
    
    public void setNumericValue(Integer value_num) {
    	valueNum = value_num;
    }

	@Column(name="CODE_VALUE_DATE")
	public Date getDateValue() {
		return valueDate;
	}
    
    public void setDateValue(Date value_date) {
    	valueDate = value_date;
    }

	@Column(name="CODE_DESC")
	public String getCodeDescription() {
		return codeDescription;
	}
    
    public void setCodeDescription(String code_description) {
    	codeDescription = code_description;
    }

	@Column(name="ORDER_INFO")
	public Integer getOrderInfo() {
		return orderInfo;
	}
    
    public void setOrderInfo(Integer order_info) {
    	orderInfo = order_info;
    }

	@Column(name="SUPP_INFO")
	public String getSuppInfo() {
		return suppInfo;
	}
    
    public void setSuppInfo(String supp_info) {
    	suppInfo = supp_info;
    }
}
