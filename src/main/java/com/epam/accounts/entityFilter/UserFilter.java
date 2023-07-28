package com.epam.accounts.entityFilter;

import com.epam.accounts.entity.User;
import com.epam.accounts.enums.Status;
import com.epam.accounts.enums.UserType;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFilter that = (UserFilter) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(middleName, that.middleName) && Objects.equals(lastName, that.lastName) && Objects.equals(fullName, that.fullName) && Objects.equals(login, that.login) && status == that.status && userType == that.userType && Objects.equals(createDateFrom, that.createDateFrom) && Objects.equals(createDateTo, that.createDateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, middleName, lastName, fullName, login, status, userType, createDateFrom, createDateTo);
    }
}
