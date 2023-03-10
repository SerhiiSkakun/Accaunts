package com.epam.accounts.dao;

import com.epam.accounts.entity.Client;
import com.epam.accounts.utils.ApplicationException;


public interface ClientDAO {
    Client getClientById(Integer id) throws ApplicationException;
}
