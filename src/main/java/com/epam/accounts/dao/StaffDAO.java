package com.epam.accounts.dao;

import com.epam.accounts.entity.Staff;
import com.epam.accounts.entityFilter.StaffFilter;
import com.epam.accounts.utils.ApplicationException;

import java.util.Map;
import java.util.Set;

public interface StaffDAO {
    Staff findStaffByLogin(String login) throws ApplicationException;
    Staff findStaffById(Long userId) throws ApplicationException;
    Map<Long, Staff> findStaffMapByIdIsIn(Set<Long> userIdSet) throws ApplicationException;
    Map<Long, Staff> findStaffMapByFilter(StaffFilter staffFilter) throws ApplicationException;
    boolean insertStaff(Staff staff) throws ApplicationException;
    boolean updateStaff(Staff staff) throws ApplicationException;
    boolean updateDepartmentAndJobTitleById(Long userId, String department, String jobTitle) throws ApplicationException;
}
