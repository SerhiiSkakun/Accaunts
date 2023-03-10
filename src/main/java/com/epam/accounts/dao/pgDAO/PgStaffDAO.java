package com.epam.accounts.dao.pgDAO;

import com.epam.accounts.dao.StaffDAO;
import com.epam.accounts.entity.Staff;
import com.epam.accounts.entityFilter.StaffFilter;
import com.epam.accounts.utils.ApplicationException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.accounts.utils.Constants.langCode;

public class PgStaffDAO implements StaffDAO {
    private static final Logger logger = Logger.getLogger(PgStaffDAO.class.getName());
    private final PgDAOFactory pgDAOFactory;
    private final PgLoginUserDAO pgLoginUserDAO;

    public PgStaffDAO(PgDAOFactory pgDAOFactory) {
        this.pgDAOFactory = pgDAOFactory;
        this.pgLoginUserDAO = new PgLoginUserDAO(pgDAOFactory);
    }

    @Override
    public Staff findStaffByLogin(String login) throws ApplicationException {
        Staff staff = null;
        String request = "SELECT login_user.*, staff.*, status_lang.name as status_lang, " +
                "department_lang.name as department_lang, job_title_lang.name as job_title_lang " +
                "FROM login_user " +
                "JOIN staff ON staff.id = login_user.id " +
                "JOIN department_lang ON department_lang.department_id = staff.department_id " +
                "JOIN job_title_lang ON job_title_lang.job_title_id = staff.job_title_id " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language ON as lang1 lang1.id = status_lang.language_id " +
                "JOIN language ON as lang2 lang2.id = department_lang.language_id " +
                "JOIN language ON as lang3 lang3.id = job_title_lang.language_id " +
                "WHERE login = " + login +
                " AND lang1.code = " + langCode + " AND lang2.code = " + langCode + " AND lang3.code = " + langCode;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) staff = new Staff(rs);
        } catch (Exception e) {
            logger.warn("Can't get staff by login = " + login, e);
            throw new ApplicationException("Can't get staff by login = " + login, e);
        }
        return staff;
    }

    @Override
    public Staff findStaffById(Long userId) throws ApplicationException {
        Staff staff = null;
        String request = "SELECT login_user.*, staff.*, status_lang.name as status_lang, " +
                "department_lang.name as department_lang, job_title_lang.name as job_title_lang" +
                "FROM login_user " +
                "JOIN staff ON staff.id = login_user.id " +
                "JOIN department_lang ON department_lang.department_id = staff.department_id " +
                "JOIN job_title_lang ON job_title_lang.job_title_id = staff.job_title_id " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language ON as lang1 lang1.id = status_lang.language_id " +
                "JOIN language ON as lang2 lang2.id = department_lang.language_id " +
                "JOIN language ON as lang3 lang3.id = job_title_lang.language_id " +
                "WHERE id = " + userId +
                " AND lang1.code = " + langCode + " AND lang2.code = " + langCode + " AND lang3.code = " + langCode;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) staff = new Staff(rs);
        } catch (Exception e) {
            logger.warn("Can't get staff by id = " + userId, e);
            throw new ApplicationException("Can't get staff by id = " + userId, e);
        }
        return staff;
    }
    @Override
    public Map<Long, Staff> findStaffMapByIdIsIn(Set<Long> userIdSet) throws ApplicationException {
        String userIdSetStr = userIdSet.stream().map(String::valueOf).collect(Collectors.joining(", "));
        Map<Long, Staff> staffs = new HashMap<>();
        Staff staff;
        String request = "SELECT login_user.*, staff.*, status_lang.name as status_lang, " +
                "department_lang.name as department_lang, job_title_lang.name as job_title_lang" +
                "FROM login_user " +
                "JOIN staff ON staff.id = login_user.id " +
                "JOIN department_lang ON department_lang.department_id = staff.department_id " +
                "JOIN job_title_lang ON job_title_lang.job_title_id = staff.job_title_id " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language ON as lang1 lang1.id = status_lang.language_id " +
                "JOIN language ON as lang2 lang2.id = department_lang.language_id " +
                "JOIN language ON as lang3 lang3.id = job_title_lang.language_id " +
                "WHERE id IN (" + userIdSetStr + ") " +
                " AND lang1.code = " + langCode + " AND lang2.code = " + langCode + " AND lang3.code = " + langCode;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                staff = new Staff(rs);
                staffs.put(staff.getId(), staff);
            }
        } catch (Exception e) {
            logger.warn("Can't get staffs " + userIdSetStr, e);
            throw new ApplicationException("Can't get staffs " + userIdSetStr, e);
        }
        return staffs;
    }

    @Override
    public Map<Long, Staff> findStaffMapByFilter(StaffFilter staffFilter) throws ApplicationException {
        Map<Long, Staff> staffs = new HashMap<>();
        Staff staff;
        String request = "SELECT login_user.*, staff.*, status_lang.name as status_lang, " +
                "department_lang.name as department_lang, job_title_lang.name as job_title_lang" +
                "FROM login_user " +
                "JOIN staff ON staff.id = login_user.id " +
                "JOIN department_lang ON department_lang.department_id = staff.department_id " +
                "JOIN job_title_lang ON job_title_lang.job_title_id = staff.job_title_id " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language ON as lang1 lang1.id = status_lang.language_id " +
                "JOIN language ON as lang2 lang2.id = department_lang.language_id " +
                "JOIN language ON as lang3 lang3.id = job_title_lang.language_id " +
                "WHERE lang1.code = " + langCode + " AND lang2.code = " + langCode + " AND lang3.code = " + langCode +
                (Objects.nonNull(staffFilter.getId()) ? " AND login_user.id = ?" : "") +
                (Objects.nonNull(staffFilter.getFirstName()) ? " AND login_user.first_name = ?" : "") +
                (Objects.nonNull(staffFilter.getMiddleName()) ? " AND login_user.middle_name = ?" : "") +
                (Objects.nonNull(staffFilter.getLastName()) ? " AND login_user.last_name = ?" : "") +
                (Objects.nonNull(staffFilter.getLogin()) ? " AND login_user.login = ?" : "") +
                (Objects.nonNull(staffFilter.getStatus()) ? " AND login_user.status = CAST(? as status)" : "") +
                (Objects.nonNull(staffFilter.getUserType()) ? " AND login_user.user_type = CAST(? as user_type)" : "") +
                (Objects.nonNull(staffFilter.getDepartment()) ? " AND staff.department = ?)" : "") +
                (Objects.nonNull(staffFilter.getJobTitle()) ? " AND staff.job_title = ?)" : "");
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                staff = new Staff(rs);
                staffs.put(staff.getId(), staff);
            }
        } catch (Exception e) {
            logger.warn("Can't get staffs by filter: " + staffFilter, e);
            throw new ApplicationException("Can't get staffs by filter: " + staffFilter, e);
        }
        return staffs;
    }

    @Override
    public boolean insertStaff(Staff staff) throws ApplicationException {
        int i = 0;
        String request = "INSERT INTO staff (id, department_id, job_title_id) " +
                "VALUES (?, (SELECT id FROM department WHERE name = ?), " +
                "(SELECT id FROM job_title WHERE department_id = (SELECT id FROM department WHERE name = ?) AND name = ?))";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            pgDAOFactory.startTransaction();
            pgLoginUserDAO.insertUser(staff);

            ps.setLong(++i, staff.getId());
            ps.setString(++i, staff.getDepartment());
            ps.setString(++i, staff.getDepartment());
            ps.setString(++i, staff.getJobTitle());
            ps.executeUpdate();
            pgDAOFactory.commitTransaction();
            return true;
        } catch (Exception e) {
            try {
                pgDAOFactory.rollbackTransaction();
            } catch (Exception ex) {
                logger.warn("Can't rollback transaction while insert staff " + staff, e);
                throw new ApplicationException("Can't rollback transaction while insert staff " + staff, e);
            }
            logger.warn("Can't insert staff " + staff, e);
            throw new ApplicationException("Can't insert staff " + staff, e);
        }
    }

    @Override
    public boolean updateStaff(Staff staff) throws ApplicationException {
        try {
            pgDAOFactory.startTransaction();
            pgLoginUserDAO.updateUser(staff);
            updateDepartmentAndJobTitleById(staff.getId(), staff.getDepartment(), staff.getJobTitle());
            pgDAOFactory.commitTransaction();
            return true;
        } catch (Exception e) {
            try {
                pgDAOFactory.rollbackTransaction();
            } catch (Exception ex) {
                logger.warn("Can't rollback transaction while update staff " + staff, e);
                throw new ApplicationException("Can't rollback transaction while update staff " + staff, e);
            }
            logger.warn("Can't update staff " + staff, e);
            throw new ApplicationException("Can't update staff " + staff, e);
        }
    }

    @Override
    public boolean updateDepartmentAndJobTitleById(Long userId, String department, String jobTitle) throws ApplicationException {
        int i = 0;
        String request = "UPDATE staff SET department_id = (SELECT id FROM department WHERE name = ?), " +
                "job_title_id = (SELECT id FROM job_title WHERE department_id = (SELECT id FROM department WHERE name = ?) AND name = ?)) " +
                "WHERE id = " + userId;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ps.setString(++i, department);
            ps.setString(++i, department);
            ps.setString(++i, jobTitle);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.warn("Can't update department = " + department + " and jobTitle = " + jobTitle + "of userId = " + userId, e);
            throw new ApplicationException("Can't update department = " + department + " and jobTitle = " + jobTitle + "of userId = " + userId, e);
        }
    }
}
