package com.herongwang.p2p.entity.parameters;

import java.io.Serializable;

import com.herongwang.p2p.dao.parameters.IParametersDao;
import com.sxj.mybatis.orm.annotations.Column;
import com.sxj.mybatis.orm.annotations.Entity;
import com.sxj.mybatis.orm.annotations.GeneratedValue;
import com.sxj.mybatis.orm.annotations.GenerationType;
import com.sxj.mybatis.orm.annotations.Id;
import com.sxj.mybatis.orm.annotations.Table;
import com.sxj.mybatis.pagination.Pagable;

/**
 * 参数
 * @author nishaotang
 *
 */
@Entity(mapper = IParametersDao.class)
@Table(name = "PARAMETERS")
public class ParametersEntity  extends Pagable implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8099435225892297011L;

	/**
	 * 主键
	 */
	@Id(column = "PAR_ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String parId;
	
	 /**
     * 参数名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 参数类型
     */
    @Column(name = "TYPE")
    private String type;
    /**
     * 值
     */
    @Column(name = "VALUE")
    private String value;
    /**
     * 值名称
     */
    @Column(name = "TEXT")
    private String text;
    /**
     * 状态
     * 0：未启用；1：启用。
     */
    @Column(name = "STSTUS")
    private Integer ststus;
	public String getParId() {
		return parId;
	}
	public void setParId(String parId) {
		this.parId = parId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getStstus() {
		return ststus;
	}
	public void setStstus(Integer ststus) {
		this.ststus = ststus;
	}
    
    
}
