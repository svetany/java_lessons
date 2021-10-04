/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;

import org.apache.hc.client5.http.impl.sync.CloseableHttpClient;
import org.apache.hc.client5.http.impl.sync.HttpClients;
import org.apache.hc.client5.http.methods.CloseableHttpResponse;
import org.apache.hc.client5.http.methods.HttpPost;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.entity.ContentType;
import org.apache.hc.core5.http.entity.EntityUtils;
import org.apache.hc.core5.http.entity.StringEntity;
import org.apache.log4j.Logger;

/**
 *
 * @author Svetlana
 */
public class NewServlet1 extends HttpServlet {

    /**
     *
     */
    public Logger logger = LogManager.getLogger("servlet.name");
    static Random rnd = new Random();

    @Override
    public void init() {

        logger.info("LOGGINGHEAR");

    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                String site = "https://www.dataaccess.com/webservicesserver/NumberConversion.wso?op=NumberToWords";
                String req = IOUtils.toString(request.getInputStream());
                String message = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                        + "  <soap:Body>\n"
                        + "    <NumberToWords xmlns=\"http://www.dataaccess.com/webservicesserver/\">\n"
                        + "      <ubiNum>unsignedLong</ubiNum>\n"
                        + "    </NumberToWords>\n"
                        + "  </soap:Body>\n"
                        + "</soap:Envelope>";
                String res;
                int rndNum = rnd.nextInt(1000);
                message = message.replace("unsignedLong", String.valueOf(rndNum));
                if (req.equals("send SOAP message")) {

                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpPost httpPost = new HttpPost(site);

                    httpPost.setHeader("Host", "www.dataaccess.com");
                    httpPost.setHeader("Content-Type", "text/xml; charset=utf-8");
                    HttpEntity stringEntity = new StringEntity(message);
                    httpPost.setEntity(stringEntity);

                    long startRequest = System.currentTimeMillis();
                    CloseableHttpResponse http_response = httpClient.execute(httpPost);
                    long endResponse = System.currentTimeMillis();
                    HttpEntity entity = http_response.getEntity();
                    String resp = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    boolean check_response = resp.contains("<m:NumberToWordsResult>");
                    long time_response = endResponse - startRequest;
                    res = "Успешный ответ: " + check_response + "\r\nВремя ответа:  " + time_response + "\r\nОтвет: " + resp;
                    
                    logger.info("try to send");
                } else {
                    res = "didn't send";
                }

                logger.info("Result: " + res);
                out.println(res);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
