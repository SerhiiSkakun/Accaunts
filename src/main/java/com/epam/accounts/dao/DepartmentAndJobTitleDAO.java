package com.epam.accounts.dao;

import com.epam.accounts.entity.Client;
import com.epam.accounts.entityFilter.ClientFilter;
import com.epam.accounts.enums.ClientType;
import com.epam.accounts.enums.Department;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public interface DepartmentAndJobTitleDAO {
    Set<String> findAllDepartments() throws SQLException;
    boolean renameDepartment(String departmentOldName, String departmentNewName) throws SQLException;
    boolean insertDepartment(Map<String, String> department) throws SQLException;
    boolean deleteDepartment(Department department) throws SQLException;
}
