package com.epam.accounts.dao.pgDAO;

import com.epam.accounts.dao.ClientDAO;
import com.epam.accounts.dao.LoginUserDAO;
import com.epam.accounts.dao.DAOFactory;
import com.epam.accounts.dao.StaffDAO;
import com.epam.accounts.utils.ApplicationException;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class PgDAOFactory extends DAOFactory {
    private static final Logger logger = Logger.getLogger(PgDAOFactory.class.getName());
    private static volatile boolean inTransaction = false;
    private Connection connection;

    public PgDAOFactory() {
        try {
            getConnectionFromPool();
        } catch (SQLException e) {
            logger.fatal(e);
        }
    }

    private void getConnectionFromPool() throws SQLException {
//          Connection connection = (DataSource)PgDataSource.context.lookup("java:comp/env/jdbc/datasource").getConnection();
            Connection connection = PgDataSource.hikariDataSource.getConnection();
            setConnection(connection);
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void startTransaction() throws SQLException {
        if (inTransaction) {
            logger.error("Can't start transaction - already in transaction");
            throw new SQLException ("Can't start transaction - already in transaction");
        }
        connection.setAutoCommit(false);
        inTransaction = true;
    }

    @Override
    public void commitTransaction() throws SQLException {
        if (!inTransaction) {
            logger.error("Can't commit transaction - not in transaction");
            throw new SQLException ("Can't commit transaction - not in transaction");
        }
        try {
            if (connection != null) {
                connection.commit();
            } else {
                logger.error("Can't commit transaction - connection is closed");
                throw new SQLException("Can't commit transaction - connection is closed");
            }
        } catch (SQLException e) {
            logger.error("Can't commit transaction");
            throw new SQLException ("Can't commit transaction", e);
        } finally {
            inTransaction = false;
            if (connection != null) {
                connection.setAutoCommit(true);
            } else {
                logger.error("Can't set AutoCommit - connection is closed");
            }
        }
    }

    @Override
    public void rollbackTransaction() throws SQLException {
        if (!inTransaction) {
            logger.error("Can't rollback transaction - not in transaction");
            throw new SQLException("Can't rollback transaction - not in transaction");
        }
        try {
            if (connection != null) {
                connection.rollback();
            } else {
                logger.error("Can't rollback transaction - connection is closed");
                throw new SQLException("Can't rollback transaction - connection is closed");
            }
        } finally {
            inTransaction = false;
            if (connection != null) {
                connection.setAutoCommit(true);
            } else {
                logger.error("Can't set AutoCommit - connection is closed");
            }
        }
    }

    @Override
    public void close(Statement st) throws ApplicationException {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error("Can't close Statement", e);
                throw new ApplicationException("Can't close Statement", e);
            }
        }
    }
    @Override
    public void close(ResultSet rs, Statement st, Connection connection) throws ApplicationException {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Can't close ResultSet", e);
                throw new ApplicationException("Can't close ResultSet", e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error("Can't close Statement", e);
                throw new ApplicationException("Can't close Statement", e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.warn("Can't close Connection", e);
                throw new ApplicationException("Can't close Connection", e);
            }
        }
    }

    @Override
    public LoginUserDAO getLoginUserDAO() {
        return new PgLoginUserDAO(this);
    }

    @Override
    public ClientDAO getClientDAO() {
        return new PgClientDAO(this);
    }

    @Override
    public StaffDAO getStaffDAO() {
        return new PgStaffDAO(this);
    }
}
