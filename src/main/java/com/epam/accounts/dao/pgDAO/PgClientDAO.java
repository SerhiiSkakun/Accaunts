package com.epam.accounts.dao.pgDAO;

import com.epam.accounts.dao.ClientDAO;
import com.epam.accounts.entity.Client;
import com.epam.accounts.entityFilter.ClientFilter;
import com.epam.accounts.enums.ClientType;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.accounts.dao.pgDAO.PgLoginUserDAO.formPsFromUserFilter;
import static com.epam.accounts.dao.pgDAO.PgLoginUserDAO.formRequestOfUserFilter;
import static com.epam.accounts.utils.Constants.langCode;

public class PgClientDAO implements ClientDAO {

    private final PgDAOFactory pgDAOFactory;

    public PgClientDAO(PgDAOFactory pgDAOFactory) {
        this.pgDAOFactory = pgDAOFactory;
    }

    public ClientType findClientTypeById(Long userId) throws SQLException {
        ClientType clientType = null;
        String request = "SELECT client_type FROM client WHERE id = ?";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) clientType = ClientType.valueOf(rs.getString("client_type"));
        }
        return clientType;
    }

    public Client findClientById(Long userId) throws SQLException {
        Client client = null;
        String request = "SELECT login_user.*, status_lang.name as status_lang, client_type_lang.name as client_type " +
                "FROM login_user " +
                "JOIN client ON client.id = login_user.id AND login_user.id = ? " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN client_type_lang ON client_type_lang.client_type = client.client_type " +
                "JOIN language ON language.code = status_lang.language_code " +
                "WHERE language.code = '" + langCode + "'";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) client = new Client(rs);
        }
        return client;
    }

    @Override
    public Map<Long, Client> findClientMapByIdsAreIn(Set<Long> userIdSet) throws SQLException {
        String userIdSetStr = userIdSet.stream().map(String::valueOf).collect(Collectors.joining(","));
        Map<Long, Client> clients = new HashMap<>();
        Client client;
        String request = "SELECT login_user.*, status_lang.name as status_lang, client_type_lang.name as client_type " +
                "FROM login_user " +
                "JOIN client ON client.id = login_user.id AND login_user.id IN (" + userIdSetStr + ") " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN client_type_lang ON client_type_lang.client_type = client.client_type " +
                "JOIN language ON language.code = status_lang.language_code " +
                "WHERE language.code = " + langCode;
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                client = new Client(rs);
                clients.put(client.getId(), client);
            }
        }
        return clients;
    }

    public Client findClientByAccountNumber(String accountNumber) throws SQLException {
        if(Objects.isNull(accountNumber) || accountNumber.isEmpty()) return null;
        Client client = null;
        String request = "SELECT DISTINCT login_user.*, status_lang.name as status_lang, client_type_lang.name as client_type, account.number " +
                "FROM client " +
                "JOIN account on client.id = account.client_id AND account.number = ?" +
                "JOIN login_user ON client.id = login_user.id " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN client_type_lang ON client_type_lang.client_type = client.client_type " +
                "JOIN language ON language.code = status_lang.language_code AND language.code = '" + langCode + "'";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                client = new Client(rs);
            }
        }
        return client;
    }

    public Map<String, Client> findClientMapByAccountNumbersAreIn(Set<String> accountNumberSet) throws SQLException {
        if(Objects.isNull(accountNumberSet) || accountNumberSet.isEmpty()) return null;
        String accountNumberSetStr = "'" + String.join("','", accountNumberSet) + "'";
        Map<String, Client> clients = new HashMap<>();
        Client client;
        String request = "SELECT DISTINCT login_user.*, status_lang.name as status_lang, client_type_lang.name as client_type, account.number " +
                "FROM client " +
                "JOIN account on client.id = account.client_id AND account.number IN (" + accountNumberSetStr + ")" +
                "JOIN login_user ON client.id = login_user.id " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN client_type_lang ON client_type_lang.client_type = client.client_type " +
                "JOIN language ON language.code = status_lang.language_code AND language.code = '" + langCode + "'";
            try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                client = new Client(rs);
                clients.put(rs.getString("number"), client);
            }
        }
        return clients;
    }

    @Override
    public Map<Long, Client> findClientMapByFilter(ClientFilter clientFilter) throws SQLException {
        int i = 0;
        Map<Long, Client> clients = new HashMap<>();
        Client client;
        StringBuilder request = new StringBuilder("SELECT login_user.*, status_lang.name as status_lang " +
                "FROM login_user " +
                "JOIN client ON client.id = login_user.id " +
                "JOIN status_lang ON status_lang.status = login_user.status " +
                "JOIN client_type_lang ON client_type_lang.client_type = client.client_type " +
                "JOIN language ON language.id = status_lang.language_id " +
                "WHERE language.code = " + langCode);
        formRequestOfUserFilter(clientFilter, request);
        if (Objects.nonNull(clientFilter.getClientType().toString()))
            request.append(" AND client.client_type = CAST(? as client_type)");
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request.toString())) {
            i = formPsFromUserFilter(clientFilter, ps, i);
            if (Objects.nonNull(clientFilter.getClientType()))
                ps.setString(++i, clientFilter.getClientType().toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                client = new Client(rs);
                clients.put(client.getId(), client);
            }
        }
        return clients;
    }

    @Override
    public boolean insertClient(Client client) throws SQLException {
        int i = 0;
        String request = "INSERT INTO client (id, client_type) VALUES (?, CAST(? as client_type))";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)){
            ps.setLong(++i, client.getId());
            ps.setString(++i, client.getClientType().toString());
            ps.executeUpdate();
            return true;
        }
    }

    @Override
    public boolean updateClientParameters(Long userId, ClientType clientType) throws SQLException {
        int i = 0;
        String request = "UPDATE client SET client_type = CAST(? as client_type) WHERE id = ?";
        try (PreparedStatement ps = pgDAOFactory.getConnection().prepareStatement(request)){
            ps.setString(++i, clientType.toString());
            ps.setLong(++i, userId);
            ps.executeUpdate();
            return true;
        }
    }
}