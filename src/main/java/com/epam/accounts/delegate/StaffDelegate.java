package com.epam.accounts.delegate;

import com.epam.accounts.dao.pgDAO.PgDAOFactory;
import com.epam.accounts.dao.pgDAO.PgLoginUserDAO;
import com.epam.accounts.dao.pgDAO.PgStaffDAO;
import com.epam.accounts.entity.Staff;
import com.epam.accounts.entityFilter.StaffFilter;
import com.epam.accounts.enums.Department;
import com.epam.accounts.utils.ApplicationException;
import org.apache.log4j.Logger;

import java.util.*;

public class StaffDelegate {

    private static final Logger logger = Logger.getLogger(ClientDelegate.class.getName());

    private final PgDAOFactory pgDAOFactory;
    private final PgStaffDAO pgStaffDAO;
    private final PgLoginUserDAO pgLoginUserDAO;

    public StaffDelegate(PgDAOFactory pgDAOFactory) {
        this.pgDAOFactory = pgDAOFactory;
        this.pgStaffDAO = new PgStaffDAO(pgDAOFactory);
        this.pgLoginUserDAO = new PgLoginUserDAO(pgDAOFactory);
    }

    public Staff findDepartmentAndJobTitleById(Long userId) throws ApplicationException {
        if (userId == null) return null;
        try {
            return pgStaffDAO.findDepartmentAndJobTitleById(userId);
        } catch (Exception e) {
            logger.warn("Can't find department and jobTitle by id = " + userId, e);
            throw new ApplicationException("Can't find department and jobTitle by id = " + userId, e);
        }
    }

    public Staff findStaffById(Long userId) throws ApplicationException {
        if (userId == null) return null;
        try {
            return pgStaffDAO.findStaffById(userId);
        } catch (Exception e) {
            logger.warn("Can't find staff by id = " + userId, e);
            throw new ApplicationException("Can't find staff by id = " + userId, e);
        }
    }

    public Map<Long, Staff> findStaffMapByIdsAreIn(Set<Long> userIdSet) throws ApplicationException {
        if (Objects.isNull(userIdSet) || userIdSet.isEmpty()) return null;
        try {
            return pgStaffDAO.findStaffMapByIdsAreIn(userIdSet);
        } catch (Exception e) {
            logger.warn("Can't find staffs by userIds: " + userIdSet, e);
            throw new ApplicationException("Can't find staffs by userIds: " + userIdSet, e);
        }
    }

    public Set<Staff> findStaffSetByDepartment(Department department) throws ApplicationException {
        if (Objects.isNull(department)) return null;
        try {
            return pgStaffDAO.findStaffSetByDepartment(department);
        } catch (Exception e) {
            logger.warn("Can't find staffs by department = " + department, e);
            throw new ApplicationException("Can't find staffs by department = " + department, e);
        }
    }

    public Map<Long, Staff> findStaffMapByFilter(StaffFilter staffFilter) throws ApplicationException {
        if (Objects.isNull(staffFilter)) return null;
        try {
            return pgStaffDAO.findStaffMapByFilter(staffFilter);
        } catch (Exception e) {
            logger.warn("Can't find staffs by filter: " + staffFilter, e);
            throw new ApplicationException("Can't find staffs by filter: " + staffFilter, e);
        }
    }

    public boolean insertStaff(Staff staff) throws ApplicationException {
        try {
            pgDAOFactory.startTransaction();
            boolean result1 = pgLoginUserDAO.insertUser(staff);
            boolean result2 = pgStaffDAO.insertStaff(staff);
            pgDAOFactory.commitTransaction();
            return result1 && result2;
        } catch (Exception e) {
            try {
                pgDAOFactory.rollbackTransaction();
            } catch (Exception ex) {
                logger.warn("Can't rollback transaction while insert staff: " + staff, e);
                throw new ApplicationException("Can't rollback transaction while insert staff: " + staff, e);
            }
            logger.warn("Can't insert staff: " + staff, e);
            throw new ApplicationException("Can't insert staff: " + staff, e);
        }
    }

    public boolean updateStaffParameters(Long userId, Department department, String jobTitle) throws ApplicationException {
        if (Objects.isNull(userId) || Objects.isNull(department) || Objects.isNull(jobTitle)) return false;
        try {
            pgStaffDAO.updateStaffParameters(userId, department, jobTitle);
            return true;
        } catch (Exception e) {
            logger.warn("Can't update department = " + department + " and jobTitle = " + jobTitle + " for userId = " + userId, e);
            throw new ApplicationException("Can't update department = " + department + " and jobTitle = " + jobTitle + " for userId = " + userId, e);
        }
    }

    public boolean updateStaff(Staff staff) throws ApplicationException {
        if (Objects.isNull(staff)) return false;
        try {
            pgDAOFactory.startTransaction();
            boolean result1 = pgLoginUserDAO.updateUser(staff);
            boolean result2 = pgStaffDAO.updateStaffParameters(staff.getId(), staff.getDepartment(), staff.getJobTitle());
            pgDAOFactory.commitTransaction();
            return result1 && result2;
        } catch (Exception e) {
            try {
                pgDAOFactory.rollbackTransaction();
            } catch (Exception ex) {
                throw new ApplicationException("Can't rollback transaction of updating staff " + staff, ex);
            }
            logger.warn("Can't update staff: " + staff, e);
            throw new ApplicationException("Can't update staff: " + staff, e);
        }
    }
}
