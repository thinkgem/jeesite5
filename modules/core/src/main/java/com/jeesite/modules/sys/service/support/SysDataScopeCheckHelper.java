/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.service.support;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.DataScope;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.service.ServiceException;
import com.jeesite.modules.sys.dao.PostRoleDao;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.EmployeeOffice;
import com.jeesite.modules.sys.entity.EmployeePost;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.PostRole;
import com.jeesite.modules.sys.entity.Role;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.CompanyService;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.service.RoleService;
import com.jeesite.modules.sys.utils.UserUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据权限校验辅助类
 * @author ThinkGem
 */
public final class SysDataScopeCheckHelper {

	private SysDataScopeCheckHelper() {
	}

	public static String defaultCtrlPermi(String ctrlPermi) {
		return StringUtils.defaultIfBlank(ctrlPermi,
				Global.getConfig("user.adminCtrlPermi", DataScope.CTRL_PERMI_MANAGE));
	}

	public static void checkUserDataScope(String userCode, String ctrlPermi, EmpUserService empUserService) {
		if (StringUtils.isBlank(userCode) || isSuperAdmin()) {
			return;
		}
		EmpUser where = new EmpUser();
		where.setUserCode(userCode);
		where.setUserType(EmpUser.USER_TYPE_EMPLOYEE);
		where.sqlMap().getWhere().disableAutoAddStatusWhere();
		empUserService.addDataScopeFilter(where, defaultCtrlPermi(ctrlPermi));
		if (empUserService.findCount(where) == 0) {
			throw new ServiceException(Global.getText("没有权限访问用户数据！"));
		}
	}

	public static void checkEmpUserDataScope(EmpUser empUser, String ctrlPermi,
			EmpUserService empUserService, OfficeService officeService, CompanyService companyService,
			PostRoleDao postRoleDao, RoleService roleService) {
		if (empUser == null || isSuperAdmin()) {
			return;
		}
		String dataScopeCtrlPermi = defaultCtrlPermi(ctrlPermi);
		checkUserDataScope(empUser.getUserCode(), dataScopeCtrlPermi, empUserService);
		Employee employee = empUser.getEmployee();
		Set<String> officeCodes = new HashSet<>();
		Set<String> postCodes = new HashSet<>();
		checkOfficeDataScope(employee.getOffice().getOfficeCode(), dataScopeCtrlPermi, officeService, officeCodes);
		checkCompanyDataScope(employee.getCompany().getCompanyCode(), dataScopeCtrlPermi, companyService);
		checkEmployeePostList(employee.getEmployeePostList(), dataScopeCtrlPermi, postRoleDao, roleService, postCodes);
		checkEmployeeOfficeList(employee.getEmployeeOfficeList(), dataScopeCtrlPermi,
				officeService, postRoleDao, roleService, officeCodes, postCodes);
	}

	public static void checkRoleDataScope(String roleCodes, String ctrlPermi, RoleService roleService) {
		if (StringUtils.isBlank(roleCodes)) {
			return;
		}
		Set<String> codeSet = new HashSet<>();
		for (String roleCode : StringUtils.splitComma(roleCodes)) {
			if (StringUtils.isNotBlank(roleCode)) {
				codeSet.add(roleCode);
			}
		}
		checkRoleDataScope(codeSet, ctrlPermi, roleService);
	}

	public static void checkRoleDataScope(Collection<String> roleCodes, String ctrlPermi, RoleService roleService) {
		if (roleCodes == null || roleCodes.isEmpty() || isSuperAdmin()) {
			return;
		}
		Role where = new Role();
		where.setStatus(Role.STATUS_NORMAL);
		where.setUserType(User.USER_TYPE_EMPLOYEE);
		where.sqlMap().getWhere().and("role_code", QueryType.IN, roleCodes.toArray(new String[0]));
		roleService.addDataScopeFilter(where, defaultCtrlPermi(ctrlPermi));
		if (roleService.findCount(where) != roleCodes.size()) {
			throw new ServiceException(Global.getText("没有权限使用该角色数据！"));
		}
	}

	private static void checkOfficeDataScope(String officeCode, String ctrlPermi,
			OfficeService officeService, Set<String> checkedOfficeCodes) {
		if (StringUtils.isBlank(officeCode) || !checkedOfficeCodes.add(officeCode)) {
			return;
		}
		Office office = new Office();
		office.setOfficeCode(officeCode);
		office.setStatus(Office.STATUS_NORMAL);
		officeService.addDataScopeFilter(office, ctrlPermi);
		if (officeService.findCount(office) == 0) {
			throw new ServiceException(Global.getText("没有权限使用该机构数据！"));
		}
	}

	private static void checkCompanyDataScope(String companyCode, String ctrlPermi, CompanyService companyService) {
		if (StringUtils.isBlank(companyCode)) {
			return;
		}
		Company company = new Company();
		company.setCompanyCode(companyCode);
		company.setStatus(Company.STATUS_NORMAL);
		companyService.addDataScopeFilter(company, ctrlPermi);
		if (companyService.findCount(company) == 0) {
			throw new ServiceException(Global.getText("没有权限使用该公司数据！"));
		}
	}

	private static void checkEmployeePostList(Collection<EmployeePost> employeePostList,
			String ctrlPermi, PostRoleDao postRoleDao, RoleService roleService, Set<String> checkedPostCodes) {
		if (employeePostList == null) {
			return;
		}
		for (EmployeePost employeePost : employeePostList) {
			if (employeePost != null) {
				checkPostRoleDataScope(employeePost.getPostCode(), ctrlPermi, postRoleDao, roleService, checkedPostCodes);
			}
		}
	}

	private static void checkEmployeeOfficeList(Collection<EmployeeOffice> employeeOfficeList, String ctrlPermi,
			OfficeService officeService, PostRoleDao postRoleDao, RoleService roleService,
			Set<String> checkedOfficeCodes, Set<String> checkedPostCodes) {
		if (employeeOfficeList == null) {
			return;
		}
		for (EmployeeOffice employeeOffice : employeeOfficeList) {
			if (employeeOffice == null) {
				continue;
			}
			checkOfficeDataScope(employeeOffice.getOfficeCode(), ctrlPermi, officeService, checkedOfficeCodes);
			checkPostRoleDataScope(employeeOffice.getPostCode(), ctrlPermi, postRoleDao, roleService, checkedPostCodes);
		}
	}

	private static void checkPostRoleDataScope(String postCode, String ctrlPermi,
			PostRoleDao postRoleDao, RoleService roleService, Set<String> checkedPostCodes) {
		if (StringUtils.isBlank(postCode) || !checkedPostCodes.add(postCode)) {
			return;
		}
		PostRole where = new PostRole();
		where.setPostCode(postCode);
		where.sqlMap().loadJoinTableAlias("r");
		Set<String> roleCodes = new HashSet<>();
		for (PostRole postRole : postRoleDao.findList(where)) {
			if (postRole.getRole() != null && PostRole.STATUS_NORMAL.equals(postRole.getRole().getStatus())) {
				roleCodes.add(postRole.getRoleCode());
			}
		}
		checkRoleDataScope(roleCodes, ctrlPermi, roleService);
	}

	private static boolean isSuperAdmin() {
		User user = UserUtils.getUser();
		return user != null && user.isSuperAdmin();
	}
}
