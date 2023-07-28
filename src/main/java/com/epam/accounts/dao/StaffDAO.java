package com.epam.accounts.dao;

import com.epam.accounts.entity.Staff;
import com.epam.accounts.entityFilter.StaffFilter;
import com.epam.accounts.enums.Department;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public interface StaffDAO {
    Staff findDepartmentAndJobTitleById(Long userId) throws SQLException;
    Staff findStaffById(Long userId) throws SQLException;
    Map<Long, Staff> findStaffMapByIdsAreIn(Set<Long> userIdSet) throws SQLException;
    Set<Staff> findStaffSetByDepartment(Department department) throws SQLException;
    Map<Long, Staff> findStaffMapByFilter(StaffFilter staffFilter) throws SQLException;
    boolean insertStaff(Staff staff) throws SQLException;
    boolean updateStaffParameters(Long userId, Department department, String jobTitle) throws SQLException;
}
