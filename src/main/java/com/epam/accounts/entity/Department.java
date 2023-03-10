package com.epam.accounts.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Department {
    private Long id;
    private String name;

    public Department() {}

    public Department(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.name = rs.getString("first_name");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
