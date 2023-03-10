package com.epam.accounts.entity;

import com.epam.accounts.enums.Status;
import com.epam.accounts.enums.UserType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class User {

    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String login;
    private String password;
    private String salt;
    private Status status;
    private String statusLang;
    private UserType userType;
    private LocalDateTime createDate;

    public User() {}

    public User(ResultSet rs) throws SQLException{
        this.id = rs.getLong("id");
        this.firstName = rs.getString("first_name");
        this.middleName = rs.getString("middle_name");
        this.lastName = rs.getString("last_name");
        this.fullName = this.getFirstName() + (this.getMiddleName() != null ? " " + this.getMiddleName() : "") + " " + this.getLastName();
        this.login = rs.getString("login");
        this.password = rs.getString("password");
        this.salt = rs.getString("salt");
        this.status = Status.valueOf(rs.getString("status"));
        this.statusLang = rs.getString("status_lang");
        this.userType = UserType.valueOf(rs.getString("user_type"));
//        this.createDate = rs.getDate("create_date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStatusLang() {
        return statusLang;
    }

    public void setStatusLang(String statusLang) {
        this.statusLang = statusLang;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

}
