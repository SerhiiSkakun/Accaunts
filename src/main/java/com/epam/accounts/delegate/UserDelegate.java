package com.epam.accounts.delegate;

import com.epam.accounts.dao.DAOFactory;
import com.epam.accounts.dao.LoginUserDAO;
import com.epam.accounts.dao.pgDAO.PgClientDAO;
import com.epam.accounts.dao.pgDAO.PgDAOFactory;
import com.epam.accounts.dao.pgDAO.PgLoginUserDAO;
import com.epam.accounts.entity.User;
import com.epam.accounts.entityFilter.UserFilter;
import com.epam.accounts.enums.SqlDb;
import com.epam.accounts.enums.Status;
import com.epam.accounts.enums.UserType;
import com.epam.accounts.utils.ApplicationException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.accounts.utils.Constants.langCode;

public class UserDelegate {

    private static final Logger logger = Logger.getLogger(ClientDelegate.class.getName());

    private final PgDAOFactory pgDAOFactory;
    private final PgLoginUserDAO pgLoginUserDAO;

    public UserDelegate(PgDAOFactory pgDAOFactory) {
        this.pgDAOFactory = pgDAOFactory;
        this.pgLoginUserDAO = new PgLoginUserDAO(pgDAOFactory);
    }

    public User findUserByLogin(String login) throws ApplicationException {
        try {
            return pgLoginUserDAO.findUserByLogin(login);
        } catch (Exception e) {
            logger.warn("Can't find user by login = " + login, e);
            throw new ApplicationException("Can't find user by login = " + login, e);
        }
    }

    public UserType findUserTypeByLogin(String login) throws ApplicationException {
        try {
            return pgLoginUserDAO.findUserTypeByLogin(login);
        } catch (Exception e) {
            logger.warn("Can't find userType by login = " + login, e);
            throw new ApplicationException("Can't find userType by login = " + login, e);
        }
    }

    public User findUserById(Long userId) throws ApplicationException {
        try {
            return pgLoginUserDAO.findUserById(userId);
        } catch (Exception e) {
            logger.warn("Can't find user by id = " + userId, e);
            throw new ApplicationException("Can't find user by id = " + userId, e);
        }
    }

    public Map<Long, User> findUserMapByIdsAreIn(Set<Long> userIdSet) throws ApplicationException {
        try {
            return pgLoginUserDAO.findUserMapByIdsAreIn(userIdSet);
        } catch (Exception e) {
            logger.warn("Can't find users by userIds: " + userIdSet, e);
            throw new ApplicationException("Can't find users by userIds: " + userIdSet, e);
        }
    }

    public Map<Long, User> findUserMapByFilter(UserFilter userFilter) throws ApplicationException {
        try {
            return pgLoginUserDAO.findUserMapByFilter(userFilter);
        } catch (Exception e) {
            logger.warn("Can't find users by filter: " + userFilter, e);
            throw new ApplicationException("Can't find users by filter: " + userFilter, e);
        }
    }

    public boolean insertUser(User user) throws ApplicationException {
        try {
            return pgLoginUserDAO.insertUser(user);
        } catch (Exception e) {
            logger.warn("Can't insert user: " + user, e);
            throw new ApplicationException("Can't insert user: " + user, e);
        }
    }

    public boolean updateUser(User user) throws ApplicationException { //Не использовать. Есть updateClient и updateStaff
        try {
            return pgLoginUserDAO.updateUser(user);
        } catch (Exception e) {
            logger.warn("Can't update user: " + user, e);
            throw new ApplicationException("Can't update user: " + user, e);
        }
    }

    public boolean updateUserPasswordAndSaltById(Long userId, String password, String salt) throws ApplicationException {
        try {
            return pgLoginUserDAO.updateUserPasswordAndSaltById(userId, password, salt);
        } catch (Exception e) {
            logger.warn("Can't update user password for userId = " + userId, e);
            throw new ApplicationException("Can't update user password for userId = " + userId, e);
        }
    }

    public boolean updateUserStatus(Long userId, Status status) throws ApplicationException {
        try {
            return pgLoginUserDAO.updateUserStatus(userId, status);
        } catch (Exception e) {
            logger.warn("Can't update user type for userId = " + userId, e);
            throw new ApplicationException("Can't update user type for userId = " + userId, e);
        }
    }
}
