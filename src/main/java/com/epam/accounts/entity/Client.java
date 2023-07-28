package com.epam.accounts.entity;

import com.epam.accounts.enums.ClientType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class Client extends User {
    private ClientType clientType;
    private List<Account> accounts;

    public Client() {}

    public Client(ResultSet rs) throws SQLException {
        super(rs);
        this.clientType = ClientType.valueOf(rs.getString("clientType"));
    }

//    public Client getClientByUserAndRs(User user, ResultSet rs) throws SQLException {
//        Client client = null;
//        if(Objects.nonNull(user) && Objects.nonNull(rs)) {
//            client = (Client) user;
//            client.clientType = ClientType.valueOf(rs.getString("clientType"));
//        }
//        return client;
//    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
