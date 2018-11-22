/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.test.entity.TestGxyData;
import com.jeesite.modules.test.dao.TestGxyDataDao;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * test_gxy_dataService
 * @author gxy
 * @version 2018-11-22
 */
@Service
@Transactional(readOnly=true)
public class TestGxyDataService extends CrudService<TestGxyDataDao, TestGxyData> {
	
	/**
	 * 获取单条数据
	 * @param testGxyData
	 * @return
	 */
	@Override
	public TestGxyData get(TestGxyData testGxyData) {
		return super.get(testGxyData);
	}
	
	/**
	 * 查询分页数据
	 * @param testGxyData 查询条件
	 * @param testGxyData.page 分页对象
	 * @return
	 */
	@Override
	public Page<TestGxyData> findPage(TestGxyData testGxyData) {
		return super.findPage(testGxyData);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param testGxyData
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(TestGxyData testGxyData) {
		super.save(testGxyData);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(testGxyData.getId(), "testGxyData_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(testGxyData.getId(), "testGxyData_file");
	}
	
	/**
	 * 更新状态
	 * @param testGxyData
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(TestGxyData testGxyData) {
		super.updateStatus(testGxyData);
	}
	
	/**
	 * 删除数据
	 * @param testGxyData
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(TestGxyData testGxyData) {
		super.delete(testGxyData);
	}
	
}