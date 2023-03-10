package com.epam.accounts.dao;

import com.epam.accounts.entity.User;
import com.epam.accounts.entityFilter.UserFilter;
import com.epam.accounts.enums.Status;
import com.epam.accounts.enums.UserType;
import com.epam.accounts.utils.ApplicationException;

import java.util.Map;
import java.util.Set;

public interface LoginUserDAO {
    User findUserByLogin(String login) throws ApplicationException;
    User findUserById(Long userId) throws ApplicationException;
    UserType findUserTypeByLogin(String login) throws ApplicationException;
    Map<Long, User> findUserMapByIdIsIn(Set<Long> userIdSet) throws ApplicationException;
    Map<Long, User> findUserMapByFilter(UserFilter userFilter) throws ApplicationException;
    boolean insertUser(User user) throws ApplicationException;
    boolean updateUser(User user) throws ApplicationException;
    boolean updateUserPasswordAndSaltById(Long userId, String password, String salt) throws ApplicationException;
    boolean updateUserStatus(Long userId, Status status) throws ApplicationException;
}
