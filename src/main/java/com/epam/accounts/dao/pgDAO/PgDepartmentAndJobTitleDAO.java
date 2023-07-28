package com.epam.accounts.dao.pgDAO;

import com.epam.accounts.dao.DepartmentAndJobTitleDAO;
import com.epam.accounts.enums.Department;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PgDepartmentAndJobTitleDAO implements DepartmentAndJobTitleDAO {

    private final PgDAOFactory pgDAOFactory;

    public PgDepartmentAndJobTitleDAO(PgDAOFactory pgDAOFactory) {
        this.pgDAOFactory = pgDAOFactory;
    }

    @Override
    public Set<String> findAllDepartments() throws SQLException {
        Set<String> departments = new HashSet<>();
        String request = "SELECT enum_range(CAST(null as department))";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                departments.add(rs.getString(1));
            }
            return departments;
        }
    }

    @Override
    public boolean renameDepartment(String departmentOldName, String departmentNewName) throws SQLException {
        String request = "ALTER TYPE department RENAME VALUE ? TO ?";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)){
            ps.setString(1, departmentOldName);
            ps.setString(2, departmentNewName);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean insertDepartment(Map<String, String> department) throws SQLException {
        String request1 = "ALTER TYPE department ADD VALUE IF NOT EXISTS ?";
        String request2 = "INSERT INTO department_lang (lang_code, department, name) VALUES (?, CAST(? as client_type))";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request1)){
            ps.setString(1, department.toString());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteDepartment(Department department) throws SQLException {
        String request = "DELETE FROM pg_enum WHERE enumtypid = 'department' AND enumlabel = ?";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)){
            ps.setString(1, department.toString());
            return ps.executeUpdate() > 0;
        }
    }


}
