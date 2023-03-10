package com.epam.accounts.dao;

import com.epam.accounts.dao.pgDAO.PgDAOFactory;
import com.epam.accounts.enums.SqlDb;
import com.epam.accounts.utils.ApplicationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DAOFactory {
    public static DAOFactory getDAOFactory(SqlDb daoFactory) {
        switch (daoFactory) {
            case PostgreSQL:
                return new PgDAOFactory();
            default:
                return null;
        }
    }

    public abstract void startTransaction() throws SQLException;

    public abstract void commitTransaction() throws SQLException;

    public abstract void rollbackTransaction() throws SQLException;

    public abstract void close(Statement st) throws ApplicationException;

    public abstract void close(ResultSet rs, Statement st, Connection connection) throws ApplicationException;

    public abstract LoginUserDAO getLoginUserDAO();

    public abstract ClientDAO getClientDAO();

    public abstract StaffDAO getStaffDAO();
}
