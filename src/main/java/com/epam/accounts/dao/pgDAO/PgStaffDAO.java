package com.epam.accounts.dao.pgDAO;

import com.epam.accounts.dao.StaffDAO;
import com.epam.accounts.entity.Staff;
import com.epam.accounts.entityFilter.StaffFilter;
import com.epam.accounts.enums.Department;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.epam.accounts.dao.pgDAO.PgLoginUserDAO.formPsFromUserFilter;
import static com.epam.accounts.dao.pgDAO.PgLoginUserDAO.formRequestOfUserFilter;
import static com.epam.accounts.utils.Constants.langCode;

public class PgStaffDAO implements StaffDAO {
    private final PgDAOFactory pgDAOFactory;
    private final PgLoginUserDAO pgLoginUserDAO;

    public PgStaffDAO(PgDAOFactory pgDAOFactory) {
        this.pgDAOFactory = pgDAOFactory;
        this.pgLoginUserDAO = new PgLoginUserDAO(pgDAOFactory);
    }

    @Override
    public Staff findDepartmentAndJobTitleById(Long userId) throws SQLException {
        Staff staff = null;
        String request = "SELECT staff.department, department_lang.name as departmen_lang, " +
                "job_title.name as job_title, job_title_lang.name as job_title_lang " +
                "FROM staff " +
                "JOIN department_lang ON department_lang.department = staff.department " +
                "JOIN language as lang1 ON lang1.code = department_lang.language_code " +
                "JOIN job_title ON job_title.id = staff.job_title_id " +
                "JOIN job_title_lang ON job_title_lang.job_title_id = staff.job_title_id " +
                "JOIN language as lang2 ON lang2.code = job_title_lang.language_code " +
                "WHERE lang1.code = " + langCode + " AND lang2.code = " + langCode + " AND staff.id = ?";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) staff = new Staff(rs);
        }
        return staff;
    }

    @Override
    public Staff findStaffById(Long userId) throws SQLException {
        Staff staff = null;
        String request = "SELECT login_user.*, staff.*, status_lang.name as status_lang, " +
                "staff.department, department_lang.name as department_lang, " +
                "job_title.name as job_title, job_title.name as job_title, job_title_lang.name as job_title_lang " +
                "FROM login_user " +
                "JOIN staff ON staff.id = login_user.id AND login_user.id = ? " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN department_lang ON department_lang.department = staff.department " +
                "JOIN job_title ON job_title.id = staff.job_title_id " +
                "JOIN job_title_lang ON job_title_lang.job_title_id = staff.job_title_id " +
                "JOIN language as lang1 ON lang1.code = status_lang.language_code " +
                "JOIN language as lang2 ON lang2.code = department_lang.language_code " +
                "JOIN language as lang3 ON lang3.code = job_title_lang.language_code " +
                "WHERE lang1.code = " + langCode + " AND lang2.code = " + langCode + " AND lang3.code = " + langCode;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) staff = new Staff(rs);
        }
        return staff;
    }

    @Override
    public Map<Long, Staff> findStaffMapByIdsAreIn(Set<Long> userIdSet) throws SQLException {
        String userIdSetStr = userIdSet.stream().map(String::valueOf).collect(Collectors.joining(","));
        Map<Long, Staff> staffs = new HashMap<>();
        Staff staff;
        String request = "SELECT login_user.*, staff.*, status_lang.name as status_lang, staff.department, " +
                "department_lang.name as department_lang, job_title.name as job_title, job_title_lang.name as job_title_lang " +
                "FROM login_user " +
                "JOIN staff ON staff.id = login_user.id AND login_user.id IN (" + userIdSetStr + ")" +
                "JOIN department_lang ON department_lang.department = staff.department " +
                "JOIN job_title ON job_title.id = staff.job_title_id " +
                "JOIN job_title_lang ON job_title_lang.job_title_id = staff.job_title_id " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language as lang1 ON lang1.code = status_lang.language_code " +
                "JOIN language as lang2 ON lang2.code = department_lang.language_code " +
                "JOIN language as lang3 ON lang3.code = job_title_lang.language_code " +
                "WHERE lang1.code = " + langCode + " AND lang2.code = " + langCode + " AND lang3.code = " + langCode;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                staff = new Staff(rs);
                staffs.put(staff.getId(), staff);
            }
        }
        return staffs;
    }

    @Override
    public Set<Staff> findStaffSetByDepartment(Department department) throws SQLException {
        Set<Staff> staffs = new HashSet<>();
        Staff staff;
        String request = "SELECT login_user.*, staff.*, status_lang.name as status_lang, staff.department, " +
                "department_lang.name as department_lang, job_title.name as job_title, job_title_lang.name as job_title_lang " +
                "FROM login_user " +
                "JOIN staff ON staff.id = login_user.id AND department = ?" +
                "JOIN department_lang ON department_lang.department = staff.department " +
                "JOIN job_title ON job_title.id = staff.job_title_id " +
                "JOIN job_title_lang ON job_title_lang.job_title_id = staff.job_title_id " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language as lang1 ON lang1.code = status_lang.language_code " +
                "JOIN language as lang2 ON lang2.code = department_lang.language_code " +
                "JOIN language as lang3 ON lang3.code = job_title_lang.language_code " +
                "WHERE lang1.code = " + langCode + " AND lang2.code = " + langCode + " AND lang3.code = " + langCode;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ps.setString(1, department.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                staff = new Staff(rs);
                staffs.add(staff);
            }
        }
        return staffs;
    }

    @Override
    public Map<Long, Staff> findStaffMapByFilter(StaffFilter staffFilter) throws SQLException {
        int i = 0;
        Map<Long, Staff> staffs = new HashMap<>();
        Staff staff;
        StringBuilder request = new StringBuilder("SELECT login_user.*, staff.*, status_lang.name as status_lang, staff.department, " +
                "department_lang.name as department_lang, job_title_lang.name as job_title_lang " +
                "FROM login_user " +
                "JOIN staff ON staff.id = login_user.id " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN department_lang ON department_lang.department = staff.department " +
                "JOIN job_title ON job_title.id = staff.job_title_id " +
                "JOIN job_title_lang ON job_title_lang.job_title_id = staff.job_title_id " +
                "JOIN language as lang1 ON lang1.code = status_lang.language_code " +
                "JOIN language as lang2 ON lang2.code = department_lang.language_code " +
                "JOIN language as lang3 ON lang3.code = job_title_lang.language_code " +
                "WHERE lang1.code = " + langCode + " AND lang2.code = " + langCode + " AND lang3.code = " + langCode);
        formRequestOfUserFilter(staffFilter, request);
        if (Objects.nonNull(staffFilter.getDepartment().toString()))
            request.append(" AND staff.department = CAST(? as department)");
        if (Objects.nonNull(staffFilter.getDepartment().toString()))
            request.append(" AND staff.job_title = ?");
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request.toString())) {
            i = formPsFromUserFilter(staffFilter, ps, i);
            if (Objects.nonNull(staffFilter.getDepartment()))
                ps.setString(++i, staffFilter.getDepartment().toString());
            if (Objects.nonNull(staffFilter.getJobTitle()))
                ps.setString(++i, staffFilter.getJobTitle());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                staff = new Staff(rs);
                staffs.put(staff.getId(), staff);
            }
        }
        return staffs;
    }

    @Override
    public boolean insertStaff(Staff staff) throws SQLException {
        int i = 0;
        String request = "INSERT INTO staff (id, department, job_title_id) VALUES (?, CAST(? as department), " +
                "(SELECT id FROM job_title WHERE department = CAST(? as department) AND name = ?))";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            pgDAOFactory.startTransaction();
            pgLoginUserDAO.insertUser(staff);

            ps.setLong(++i, staff.getId());
            ps.setString(++i, staff.getDepartment().toString());
            ps.setString(++i, staff.getDepartment().toString());
            ps.setString(++i, staff.getJobTitle());
            ps.executeUpdate();
            pgDAOFactory.commitTransaction();
            return true;
        }
    }

    @Override
    public boolean updateStaffParameters(Long userId, Department department, String jobTitle) throws SQLException {
        int i = 0;
        String request = "UPDATE staff SET department = CAST(? as department), " +
                "job_title_id = (SELECT id FROM job_title WHERE department = ? AND name = ?) " +
                "WHERE id = ?";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ps.setString(++i, department.toString());
            ps.setString(++i, department.toString());
            ps.setString(++i, jobTitle);
            ps.setLong(++i, userId);
            ps.executeUpdate();
            return true;
        }
    }
}
