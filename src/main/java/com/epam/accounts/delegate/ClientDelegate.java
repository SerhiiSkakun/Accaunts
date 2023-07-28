package com.epam.accounts.delegate;

import com.epam.accounts.dao.pgDAO.PgClientDAO;
import com.epam.accounts.dao.pgDAO.PgDAOFactory;
import com.epam.accounts.dao.pgDAO.PgLoginUserDAO;
import com.epam.accounts.entity.Client;
import com.epam.accounts.entityFilter.ClientFilter;
import com.epam.accounts.enums.ClientType;
import com.epam.accounts.utils.ApplicationException;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ClientDelegate {

    private static final Logger logger = Logger.getLogger(ClientDelegate.class.getName());

    private final PgDAOFactory pgDAOFactory;
    private final PgClientDAO pgClientDAO;
    private final PgLoginUserDAO pgLoginUserDAO;

    public ClientDelegate(PgDAOFactory pgDAOFactory) {
        this.pgDAOFactory = pgDAOFactory;
        this.pgClientDAO = new PgClientDAO(pgDAOFactory);
        this.pgLoginUserDAO = new PgLoginUserDAO(pgDAOFactory);
    }

    public ClientType findClientTypeById(Long userId) throws ApplicationException {
        if (Objects.isNull(userId)) return null;
        try {
            return pgClientDAO.findClientTypeById(userId);
        } catch (Exception e) {
            logger.warn("Can't find clientType by id = " + userId, e);
            throw new ApplicationException("Can't find clientType by id = " + userId, e);
        }
    }

    public Client findClientById(Long userId) throws ApplicationException {
        if (Objects.isNull(userId)) return null;
        try {
            return pgClientDAO.findClientById(userId);
        } catch (Exception e) {
            logger.warn("Can't get client by id = " + userId, e);
            throw new ApplicationException("Can't get client by id = " + userId, e);
        }
    }

    public Map<Long, Client> findClientMapByIdsAreIn(Set<Long> userIdSet) throws ApplicationException {
        if (Objects.isNull(userIdSet) || userIdSet.isEmpty()) return null;
        try {
            return pgClientDAO.findClientMapByIdsAreIn(userIdSet);
        } catch (Exception e) {
            logger.warn("Can't find clients: " + userIdSet, e);
            throw new ApplicationException("Can't find clients: " + userIdSet, e);
        }
    }

    public Map<Long, Client> findClientMapByFilter(ClientFilter clientFilter) throws ApplicationException {
        if (Objects.isNull(clientFilter)) return null;
        try {
            return pgClientDAO.findClientMapByFilter(clientFilter);
        } catch (Exception e) {
            logger.warn("Can't find clients by filter: " + clientFilter, e);
            throw new ApplicationException("Can't find clients by filter: " + clientFilter, e);
        }
    }

    public Client findClientByAccountNumber(String accountNumber) throws ApplicationException {
        if (Objects.isNull(accountNumber) || accountNumber.isEmpty()) return null;
        try {
            return pgClientDAO.findClientByAccountNumber(accountNumber);
        } catch (Exception e) {
            logger.warn("Can't find client by accountNumber: " + accountNumber, e);
            throw new ApplicationException("Can't find client by accountNumber: " + accountNumber, e);
        }
    }

    public Map<String, Client> findClientMapByAccountNumbersAreIn(Set<String> accountNumberSet) throws ApplicationException {
        if (Objects.isNull(accountNumberSet) || accountNumberSet.isEmpty()) return null;
        try {
            return pgClientDAO.findClientMapByAccountNumbersAreIn(accountNumberSet);
        } catch (Exception e) {
            logger.warn("Can't find clients by accounts: " + accountNumberSet, e);
            throw new ApplicationException("Can't find clients by accounts: " + accountNumberSet, e);
        }
    }

    public boolean insertClient(Client client) throws ApplicationException {
        if (Objects.isNull(client)) return false;
        try {
            pgDAOFactory.startTransaction();
            boolean result1 = pgLoginUserDAO.insertUser(client);
            boolean result2 = pgClientDAO.insertClient(client);
            pgDAOFactory.commitTransaction();
            return result1 && result2;
        } catch (Exception e) {
            try {
                pgDAOFactory.rollbackTransaction();
            } catch (Exception ex) {
                throw new ApplicationException("Can't rollback transaction of inserting client: " + client, ex);
            }
            logger.warn("Can't insert client: " + client, e);
            throw new ApplicationException("Can't insert client: " + client, e);
        }
    }

    public boolean updateClientParameters(Long userId, ClientType clientType) throws ApplicationException {
        if (Objects.isNull(userId) || Objects.isNull(clientType)) return false;
        try {
            pgClientDAO.updateClientParameters(userId, clientType);
            return true;
        } catch (Exception e) {
            logger.warn("Can't update clientType = " + clientType + " for userId = " + userId, e);
            throw new ApplicationException("Can't update clientType = " + clientType + " for userId = " + userId, e);
        }
    }

    public boolean updateClient(Client client) throws ApplicationException {
        if (Objects.isNull(client)) return false;
        try {
            pgDAOFactory.startTransaction();
            boolean result1 = pgLoginUserDAO.updateUser(client);
            boolean result2 = pgClientDAO.updateClientParameters(client.getId(), client.getClientType());
            pgDAOFactory.commitTransaction();
            return result1 && result2;
        } catch (Exception e) {
            try {
                pgDAOFactory.rollbackTransaction();
            } catch (Exception ex) {
                throw new ApplicationException("Can't rollback transaction of updating client " + client, ex);
            }
            logger.warn("Can't update client: " + client, e);
            throw new ApplicationException("Can't update client: " + client, e);
        }
    }
}
