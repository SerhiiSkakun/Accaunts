package com.epam.accounts.entityFilter;

import com.epam.accounts.entity.Staff;
import com.epam.accounts.enums.Department;

import java.util.Objects;

public class StaffFilter extends UserFilter{
    private Department department;
    private String jobTitle;

    StaffFilter() {}

    StaffFilter(Staff staff) {
        super(staff);
        this.department = staff.getDepartment();
        this.jobTitle = staff.getJobTitle();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StaffFilter that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(department, that.department) && Objects.equals(jobTitle, that.jobTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), department, jobTitle);
    }
}
