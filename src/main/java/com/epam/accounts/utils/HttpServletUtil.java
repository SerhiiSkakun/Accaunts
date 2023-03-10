package com.epam.accounts.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpServletUtil<OUT> {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Logger logger;

    public HttpServletUtil (HttpServletRequest request, HttpServletResponse response, Logger logger) {
        this.request = request;
        this.response = response;
        this.logger = logger;
    }

    public JSONObject parseJSON() throws ApplicationException {
        try (BufferedReader reader = this.request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String jsonStr = sb.toString();
            return jsonStr.length() > 0 ? new JSONObject(jsonStr) : null;
        } catch (Exception e) {
            logger.error("Exception in parseJSON: " + e.getMessage(), e);
            throw new ApplicationException("Exception in parseJSON", e);
        }
    }

    public void sendStatus(int statusCode, String error) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", error);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(statusCode);
            PrintWriter writer = response.getWriter();
            writer.write(jsonObject.toString());
        } catch (JSONException | IOException e) {
            logger.error("sendStatus(): ", e);
        }
    }

    public void sendDTO(int statusCode, OUT dto) {
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        try {
            PrintWriter writer = response.getWriter();
            response.setStatus(statusCode);
            mapper.writeValue(writer, dto);
        } catch (Exception e) {
            logger.error("sendDTO(): ", e);
        }
    }
}
