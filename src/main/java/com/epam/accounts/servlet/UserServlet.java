package com.epam.accounts.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.epam.accounts.delegate.UserDelegate;
import com.epam.accounts.entity.User;
import com.epam.accounts.utils.ApplicationException;
import com.epam.accounts.utils.HttpServletUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(UserServlet.class);
    HttpServletRequest request;
    HttpServletResponse response;

    private static final String ACTION_GET_USER = "getUser";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
        String actionName = request.getParameter("actionName");

        try {
            if (actionName == null || actionName.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action name" + actionName);
            } else if (ACTION_GET_USER.equals(actionName)) {
                handleGetUser();
//            } else if (ServletConstants.ACTION_UPDATE_USER.equals(actionName)) {
//                handleUpdateUser();
//            } else if (ServletConstants.ACTION_SET_NEW_PASSWORD.equals(actionName)) {
//                handleSetNewPasswordAction();
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action name" + actionName);
            }
        } catch (Exception e) {
            logger.error("UserServlet: actionName=" + actionName, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    private void handleGetUser() {
        String userIdStr = request.getParameter("userId");
        Long userId = Long.valueOf(userIdStr);
        UserDelegate userDelegate = new UserDelegate();
        User user =  userDelegate.getUser(userId);
        HttpServletUtil<User> servletUtil = new HttpServletUtil<>(request, response, logger);
        servletUtil.sendDTO(HttpServletResponse.SC_OK,user);
    }

}
