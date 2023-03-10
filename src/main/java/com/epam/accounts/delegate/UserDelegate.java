package com.epam.accounts.delegate;

import com.epam.accounts.dao.DAOFactory;
import com.epam.accounts.dao.LoginUserDAO;
import com.epam.accounts.entity.User;
import com.epam.accounts.enums.SqlDb;
import com.epam.accounts.utils.ApplicationException;

public class UserDelegate {

    public User getUser(Long userId) {
        DAOFactory factory = DAOFactory.getDAOFactory(SqlDb.PostgreSQL);
        LoginUserDAO loginUserDAO = factory.getLoginUserDAO();
        User user = null;
        try {
            user = loginUserDAO.findUserById(userId);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        return user;
    }
}
