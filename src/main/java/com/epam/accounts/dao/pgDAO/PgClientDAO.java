package com.epam.accounts.dao.pgDAO;

import com.epam.accounts.dao.ClientDAO;
import com.epam.accounts.entity.Client;
import com.epam.accounts.utils.ApplicationException;
import org.apache.log4j.Logger;

import java.sql.*;

public class PgClientDAO implements ClientDAO {
    private static final Logger logger = Logger.getLogger(PgClientDAO.class.getName());

    private final PgDAOFactory pgDAOFactory;

    public PgClientDAO(PgDAOFactory pgDAOFactory) {
        this.pgDAOFactory = pgDAOFactory;
    }

    public Client getClientById(Integer id) throws ApplicationException {
        Client client = null;

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = pgDAOFactory.getConnection();
            ps = connection.prepareStatement("SELECT client_type_id FROM client JOIN login_user ON id WHERE id=" + id);
            rs = ps.executeQuery();
            if (rs.next()) {
                client = new Client(rs);
            }
        } catch (SQLException e) {
            logger.warn("Can't get client by id = " + id, e);
            throw new ApplicationException("Can't get client by id = " + id, e);
        } finally {
            pgDAOFactory.close(rs, ps, connection);
        }
        return client;
    }
}
