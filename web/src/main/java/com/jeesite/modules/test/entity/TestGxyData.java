/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * test_gxy_dataEntity
 * @author gxy
 * @version 2018-10-31
 */
@Table(name="test_gxy_data", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="name", attrName="name", label="name", queryType=QueryType.LIKE),
		@Column(name="creat_date", attrName="creatDate", label="creat_date"),
		@Column(name="image", attrName="image", label="图片"),
		@Column(name="file", attrName="file", label="附件"),
	}, orderBy="a.id DESC"
)
public class TestGxyData extends DataEntity<TestGxyData> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// name
	private Date creatDate;		// creat_date
	private String image;		// 图片
	private String file;		// 附件
	
	public TestGxyData() {
		this(null);
	}

	public TestGxyData(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="name长度不能超过 255 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}
	
	@Length(min=0, max=255, message="图片长度不能超过 255 个字符")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Length(min=0, max=255, message="附件长度不能超过 255 个字符")
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
}