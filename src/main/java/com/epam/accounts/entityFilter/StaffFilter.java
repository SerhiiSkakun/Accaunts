package com.epam.accounts.entityFilter;

import com.epam.accounts.entity.Staff;

public class StaffFilter extends UserFilter{
    private String department;
    private String jobTitle;

    StaffFilter() {}

    StaffFilter(Staff staff) {
        super(staff);
        this.department = staff.getDepartment();
        this.jobTitle = staff.getJobTitle();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
