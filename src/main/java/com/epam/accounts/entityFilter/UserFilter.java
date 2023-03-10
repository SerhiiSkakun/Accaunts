package com.epam.accounts.entityFilter;

import com.epam.accounts.entity.User;
import com.epam.accounts.enums.Status;
import com.epam.accounts.enums.UserType;

import java.time.LocalDateTime;

public class UserFilter {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String login;
    private Status status;
    private UserType userType;
    private LocalDateTime createDateFrom;
    private LocalDateTime createDateTo;

    public UserFilter() {}

    public UserFilter(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.middleName = user.getMiddleName();
        this.lastName = user.getLastName();
        this.login = user.getLogin();
        this.status = user.getStatus();
        this.userType = user.getUserType();
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public LocalDateTime getCreateDateFrom() {
        return createDateFrom;
    }

    public void setCreateDateFrom(LocalDateTime createDateFrom) {
        this.createDateFrom = createDateFrom;
    }

    public LocalDateTime getCreateDateTo() {
        return createDateTo;
    }

    public void setCreateDateTo(LocalDateTime createDateTo) {
        this.createDateTo = createDateTo;
    }
}
