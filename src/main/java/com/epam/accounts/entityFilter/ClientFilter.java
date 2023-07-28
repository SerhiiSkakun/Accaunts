package com.epam.accounts.entityFilter;

import com.epam.accounts.entity.Account;
import com.epam.accounts.entity.Client;
import com.epam.accounts.enums.ClientType;

import java.util.List;
import java.util.Objects;

public class ClientFilter extends UserFilter {

    private ClientType clientType;
    private List<Account> accounts;

    ClientFilter() {}

    ClientFilter(Client client) {
        super(client);
        this.clientType = client.getClientType();
        this.accounts = client.getAccounts();
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientFilter that)) return false;
        if (!super.equals(o)) return false;
        return clientType == that.clientType && Objects.equals(accounts, that.accounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientType, accounts);
    }
}
