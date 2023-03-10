package com.epam.accounts.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Staff extends User {
    private String department;
    private String departmentLang;
    private String jobTitle;
    private String jobTitleLang;

    public Staff() {}

    public Staff(ResultSet rs) throws SQLException {
        super(rs);
        this.department = rs.getString("department");
        this.departmentLang = rs.getString("department_lang");
        this.jobTitle = rs.getString("job_title");
        this.jobTitleLang = rs.getString("job_title_lang");
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentLang() {
        return departmentLang;
    }

    public void setDepartmentLang(String departmentLang) {
        this.departmentLang = departmentLang;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobTitleLang() {
        return jobTitleLang;
    }

    public void setJobTitleLang(String jobTitleLang) {
        this.jobTitleLang = jobTitleLang;
    }
}
