package com.epam.accounts.dao.pgDAO;

import com.epam.accounts.entity.User;
import com.epam.accounts.entityFilter.UserFilter;
import com.epam.accounts.enums.Status;
import com.epam.accounts.dao.LoginUserDAO;
import com.epam.accounts.enums.UserType;

import java.sql.*;
import java.sql.Date;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.epam.accounts.utils.Constants.langCode;

public class PgLoginUserDAO implements LoginUserDAO {
    private final PgDAOFactory pgDAOFactory;

    public PgLoginUserDAO(PgDAOFactory pgDAOFactory) {
        this.pgDAOFactory = pgDAOFactory;
    }

    @Override
    public User findUserByLogin(String login) throws SQLException {
        User user = null;
        String request = "SELECT login_user.*, status_lang.name as status_lang " +
                "FROM login_user " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language ON language.code = status_lang.language_code " +
                "WHERE login = ? AND language.code = " + langCode;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) user = new User(rs);
        }
        return user;
    }

    public UserType findUserTypeByLogin(String login) throws SQLException {
        String request = "SELECT login_user.user_type FROM login_user WHERE login = ?";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return UserType.valueOf(rs.getString("user_type"));
        }
        return null;
    }

    @Override
    public User findUserById(Long userId) throws SQLException {
        User user = null;
        String request = "SELECT login_user.*, status_lang.name as status_lang " +
                "FROM login_user " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language ON language.code = status_lang.language_code " +
                "WHERE login_user.id = ? AND language.code = '" + langCode + "'";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) user = new User(rs);
        }
        return user;
    }

    @Override
    public Map<Long, User> findUserMapByIdsAreIn(Set<Long> userIdSet) throws SQLException {
        String userIdSetStr = userIdSet.stream().map(String::valueOf).collect(Collectors.joining(","));
        Map<Long, User> users = new HashMap<>();
        User user;
        String request = "SELECT login_user.*, status_lang.name as status_lang " +
                "FROM login_user " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language ON language.code = status_lang.language_code " +
                "WHERE login_user.id IN (" + userIdSetStr + ") " + "AND language.code = " + langCode;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User(rs);
                users.put(user.getId(), user);
            }
        }
        return users;
    }

    @Override
    public Map<Long, User> findUserMapByFilter(UserFilter userFilter) throws SQLException {
        int i = 0;
        Map<Long, User> users = new HashMap<>();
        User user;
        StringBuilder request = new StringBuilder("SELECT login_user.*, status_lang.name as status_lang " +
                "FROM login_user " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN language ON language.id = status_lang.language_id " +
                "WHERE language.code = " + langCode);
        formRequestOfUserFilter(userFilter, request);
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request.toString())) {
            formPsFromUserFilter(userFilter, ps, i);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User(rs);
                users.put(user.getId(), user);
            }
        }
        return users;
    }

    public static void formRequestOfUserFilter(UserFilter userFilter, StringBuilder request) {
        if (Objects.nonNull(userFilter.getId())) request.append(" AND login_user.id = ?");
        if (Objects.nonNull(userFilter.getFirstName())) request.append(" AND login_user.first_name = ?");
        if (Objects.nonNull(userFilter.getMiddleName())) request.append(" AND login_user.middle_name = ?");
        if (Objects.nonNull(userFilter.getLastName())) request.append(" AND login_user.last_name = ?");
        if (Objects.nonNull(userFilter.getLogin())) request.append(" AND login_user.login = ?");
        if (Objects.nonNull(userFilter.getStatus())) request.append(" AND login_user.status = CAST(? as status)");
        if (Objects.nonNull(userFilter.getUserType().toString())) request.append(" AND login_user.user_type = CAST(? as user_type)");
        if (Objects.nonNull(userFilter.getCreateDateFrom())) request.append(" AND login_user.create_date FROM ?");
        if (Objects.nonNull(userFilter.getCreateDateTo())) request.append(" AND login_user.create_date TO ?");
    }

    public static int formPsFromUserFilter(UserFilter userFilter, PreparedStatement ps, int i) throws SQLException {
        if (Objects.nonNull(userFilter.getId())) ps.setLong(++i, userFilter.getId());
        if (Objects.nonNull(userFilter.getFirstName())) ps.setString(++i, userFilter.getFirstName());
        if (Objects.nonNull(userFilter.getMiddleName())) ps.setString(++i, userFilter.getMiddleName());
        if (Objects.nonNull(userFilter.getLastName())) ps.setString(++i, userFilter.getLastName());
        if (Objects.nonNull(userFilter.getLogin())) ps.setString(++i, userFilter.getLogin());
        if (Objects.nonNull(userFilter.getStatus())) ps.setString(++i, userFilter.getStatus().toString());
        if (Objects.nonNull(userFilter.getUserType().toString())) ps.setString(++i, userFilter.getUserType().toString());
        if (Objects.nonNull(userFilter.getCreateDateFrom())) ps.setDate(++i, new Date(userFilter.getCreateDateFrom().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        if (Objects.nonNull(userFilter.getCreateDateTo())) ps.setDate(++i, new Date(userFilter.getCreateDateTo().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        return i;
    }

    @Override
    public boolean insertUser(User user) throws SQLException {
        int i = 0;
        String request = "INSERT INTO login_user (id, first_name, middle_name, last_name, " +
                "login, password, salt, status, user_type, create_date) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, " +
                "CAST(? as status), CAST(? as user_type), ?)";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request, Statement.RETURN_GENERATED_KEYS)){
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
            if (rs.next()) {
                user.setId(rs.getLong("id"));
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        int i = 0;
        String request = "UPDATE login_user SET first_name = ?, middle_name = ?, last_name = ?, " +
                "login = ?, password = ?, salt = ?, status = CAST(? as status), " +
                "user_type = CAST(? as user_type), create_date = ? WHERE id = ?";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
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
        }
    }

    @Override
    public boolean updateUserPasswordAndSaltById(Long userId, String password, String salt) throws SQLException {
        int i = 0;
        String request = "UPDATE login_user SET password = ?, salt = ? WHERE id = ?";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)){
            ps.setString(++i, password);
            ps.setString(++i, salt);
            ps.setLong(++i, userId);
            ps.executeUpdate();
            return true;
        }
    }

    @Override
    public boolean updateUserStatus(Long userId, Status status) throws SQLException {
        int i = 0;
        String request = "UPDATE login_user SET status = CAST(? as status) WHERE id = ?";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)){
            ps.setString(++i, status.toString());
            ps.setLong(++i, userId);
            ps.executeUpdate();
            return true;
        }
    }

}
