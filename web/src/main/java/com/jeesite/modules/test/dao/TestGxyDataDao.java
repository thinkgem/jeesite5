/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.test.entity.TestGxyData;

/**
 * test_gxy_dataDAO接口
 * @author gxy
 * @version 2018-11-22
 */
@MyBatisDao
public interface TestGxyDataDao extends CrudDao<TestGxyData> {
	
}