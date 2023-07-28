package com.epam.accounts.dao;

import com.epam.accounts.entity.User;
import com.epam.accounts.entityFilter.UserFilter;
import com.epam.accounts.enums.Status;
import com.epam.accounts.enums.UserType;
import com.epam.accounts.utils.ApplicationException;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public interface LoginUserDAO {
    User findUserByLogin(String login) throws SQLException;
    UserType findUserTypeByLogin(String login) throws SQLException;
    User findUserById(Long userId) throws SQLException;
    Map<Long, User> findUserMapByIdsAreIn(Set<Long> userIdSet) throws SQLException;
    Map<Long, User> findUserMapByFilter(UserFilter userFilter) throws SQLException;
    boolean insertUser(User user) throws SQLException;
    boolean updateUser(User user) throws SQLException;
    boolean updateUserPasswordAndSaltById(Long userId, String password, String salt) throws SQLException;
    boolean updateUserStatus(Long userId, Status status) throws SQLException;
}
