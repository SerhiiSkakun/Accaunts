package com.epam.accounts.dao.pgDAO;

import com.epam.accounts.entity.User;
import com.epam.accounts.entityFilter.UserFilter;
import com.epam.accounts.enums.Status;
import com.epam.accounts.dao.LoginUserDAO;
import com.epam.accounts.enums.UserType;
import com.epam.accounts.utils.ApplicationException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.epam.accounts.utils.Constants.langCode;

public class PgLoginUserDAO implements LoginUserDAO {
    private static final Logger logger = Logger.getLogger(PgLoginUserDAO.class.getName());
    private final PgDAOFactory pgDAOFactory;

    public PgLoginUserDAO(PgDAOFactory pgDAOFactory) {
        this.pgDAOFactory = pgDAOFactory;
    }

    @Override
    public User findUserByLogin(String login) throws ApplicationException {
        User user = null;
        String request = "SELECT login_user.*, status_lang.name as status_lang " +
                "FROM login_user " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language ON language.id = status_lang.language_id " +
                "WHERE login = " + login + " AND language.code = " + langCode;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) user = new User(rs);
        } catch (Exception e) {
            logger.warn("Can't get user by login = " + login, e);
            throw new ApplicationException("Can't get user by login = " + login, e);
        }
        return user;
    }

    @Override
    public User findUserById(Long userId) throws ApplicationException {
        User user = null;
        String request = "SELECT login_user.*, status_lang.name as status_lang " +
                "FROM login_user " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language ON language.id = status_lang.language_id " +
                "WHERE login_user.id = " + userId + " AND language.code = '" + langCode + "'";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) user = new User(rs);
        } catch (Exception e) {
            logger.warn("Can't get user by id = " + userId, e);
            throw new ApplicationException("Can't get user by id = " + userId, e);
        }
        return user;
    }

    public UserType findUserTypeByLogin(String login) throws ApplicationException {
        String request = "SELECT login_user.user_type " +
                "FROM login_user " +
                "WHERE login = " + login;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return UserType.valueOf(rs.getString("user_type"));
        } catch (Exception e) {
            logger.warn("Can't get userType by login = " + login, e);
            throw new ApplicationException("Can't get userType by login = " + login, e);
        }
        return null;
    }

    @Override
    public Map<Long, User> findUserMapByIdIsIn(Set<Long> userIdSet) throws ApplicationException {
        String userIdSetStr = userIdSet.stream().map(String::valueOf).collect(Collectors.joining(", "));
        Map<Long, User> users = new HashMap<>();
        User user;
        String request = "SELECT login_user.*, status_lang.name as status_lang " +
                "FROM login_user " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language ON language.id = status_lang.language_id " +
                "WHERE login_user.id IN (" + userIdSetStr + ") " + "AND language.code = " + langCode;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User(rs);
                users.put(user.getId(), user);
            }
        } catch (Exception e) {
            logger.warn("Can't get users " + userIdSetStr, e);
            throw new ApplicationException("Can't get users " + userIdSetStr, e);
        }
        return users;
    }

    @Override
    public Map<Long, User> findUserMapByFilter(UserFilter userFilter) throws ApplicationException {
        Map<Long, User> users = new HashMap<>();
        User user;
        String request = "SELECT login_user.*, status_lang.name as status_lang " +
                "FROM login_user " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language ON language.id = status_lang.language_id " +
                "WHERE language.code = " + langCode +
                (Objects.nonNull(userFilter.getId()) ? " AND login_user.id = ?" : "") +
                (Objects.nonNull(userFilter.getFirstName()) ? " AND login_user.first_name = ?" : "") +
                (Objects.nonNull(userFilter.getMiddleName()) ? " AND login_user.middle_name = ?" : "") +
                (Objects.nonNull(userFilter.getLastName()) ? " AND login_user.last_name = ?" : "") +
                (Objects.nonNull(userFilter.getLogin()) ? " AND login_user.login = ?" : "") +
                (Objects.nonNull(userFilter.getStatus()) ? " AND login_user.status = CAST(? as status)" : "") +
                (Objects.nonNull(userFilter.getUserType()) ? " AND login_user.user_type = CAST(? as user_type)" : "");
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User(rs);
                users.put(user.getId(), user);
            }
        } catch (Exception e) {
            logger.warn("Can't get users by filter: " + userFilter, e);
            throw new ApplicationException("Can't get users by filter: " + userFilter, e);
        }
        return users;
    }

    @Override
    public boolean insertUser(User user) throws ApplicationException {
        int i = 0;
        PreparedStatement ps = null;
        try (Connection connection = pgDAOFactory.getConnection()){
            ps = connection.prepareStatement("INSERT INTO login_user (id, first_name, middle_name, last_name, " +
                            "login, password, salt, status, user_type, create_date) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, " +
                            "CAST(? as status), CAST(? as user_type), ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(++i, user.getFirstName());
            ps.setString(++i, user.getMiddleName());
            ps.setString(++i, user.getLastName());
            ps.setString(++i, user.getLogin());
            ps.setString(++i, user.getPassword());
            ps.setString(++i, user.getSalt());
            ps.setString(++i, user.getStatus().toString());
            ps.setString(++i, user.getUserType().toString());
            ps.setDate(++i, new Date(user.getCreateDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) user.setId(rs.getLong("id"));
            return true;
        } catch (Exception e) {
            logger.warn("Can't insert user " + user, e);
            throw new ApplicationException("Can't insert user " + user, e);
        } finally {
            pgDAOFactory.close(ps);
        }
    }

    @Override
    public boolean updateUser(User user) throws ApplicationException {
        int i = 0;
        PreparedStatement ps = null;
        try (Connection connection = pgDAOFactory.getConnection()){
            ps = connection.prepareStatement("UPDATE login_user SET first_name = ?, middle_name = ?, last_name = ?, " +
                    "login = ?, password = ?, salt = ?, status = ?, user_type = ?, create_date = ? WHERE id = ?");
            ps.setString(++i, user.getFirstName());
            ps.setString(++i, user.getMiddleName());
            ps.setString(++i, user.getLastName());
            ps.setDate(++i, new Date(user.getCreateDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            ps.setString(++i, user.getLogin());
            ps.setString(++i, user.getStatus().toString());
            ps.setString(++i, user.getUserType().toString());
            ps.setString(++i, user.getId().toString());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            logger.warn("Can't update user " + user, e);
            throw new ApplicationException("Can't update user " + user, e);
        } finally {
            pgDAOFactory.close(ps);
        }
    }

    @Override
    public boolean updateUserPasswordAndSaltById(Long userId, String password, String salt) throws ApplicationException {
        int i = 0;
        PreparedStatement ps = null;
        try (Connection connection = pgDAOFactory.getConnection()){
            ps = connection.prepareStatement("UPDATE login_user SET password = ?, salt = ? WHERE id = ?");
            ps.setString(++i, password);
            ps.setString(++i, salt);
            ps.setLong(++i, userId);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            logger.warn("Can't update user password for userId=" + userId, e);
            throw new ApplicationException("Can't update user password for userId=" + userId, e);
        } finally {
            pgDAOFactory.close(ps);
        }
    }

    @Override
    public boolean updateUserStatus(Long userId, Status status) throws ApplicationException {
        int i = 0;
        PreparedStatement ps = null;
        try (Connection connection = pgDAOFactory.getConnection()){
            ps = connection.prepareStatement("UPDATE login_user SET status = CAST(? as status) WHERE id = ?");
            ps.setString(++i, status.toString());
            ps.setLong(++i, userId);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            logger.warn("Can't update user type for userId=" + userId, e);
            throw new ApplicationException("Can't update user type for userId=" + userId, e);
        } finally {
            pgDAOFactory.close(ps);
        }
    }

}
