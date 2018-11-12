/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.test.entity.TestGxyData;
import com.jeesite.modules.test.service.TestGxyDataService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * test_gxy_dataController
 * @author gxy
 * @version 2018-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/test/testGxyData")
public class TestGxyDataController extends BaseController {

	@Autowired
	private TestGxyDataService testGxyDataService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public TestGxyData get(String id, boolean isNewRecord) {
		return testGxyDataService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("test:testGxyData:view")
	@RequestMapping(value = {"list", ""})
	public String list(TestGxyData testGxyData, Model model) {
		model.addAttribute("testGxyData", testGxyData);
		return "modules/test/testGxyDataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("test:testGxyData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<TestGxyData> listData(TestGxyData testGxyData, HttpServletRequest request, HttpServletResponse response) {
		testGxyData.setPage(new Page<>(request, response));
		Page<TestGxyData> page = testGxyDataService.findPage(testGxyData); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("test:testGxyData:view")
	@RequestMapping(value = "form")
	public String form(TestGxyData testGxyData, Model model) {
		model.addAttribute("testGxyData", testGxyData);
		return "modules/test/testGxyDataForm";
	}

	/**
	 * 保存test_gxy_data
	 */
	@RequiresPermissions("test:testGxyData:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated TestGxyData testGxyData) {
		testGxyDataService.save(testGxyData);
		return renderResult(Global.TRUE, text("保存test_gxy_data成功！"));
	}
	
	/**
	 * 删除test_gxy_data
	 */
	@RequiresPermissions("test:testGxyData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(TestGxyData testGxyData) {
		testGxyDataService.delete(testGxyData);
		return renderResult(Global.TRUE, text("删除test_gxy_data成功！"));
	}
	
}