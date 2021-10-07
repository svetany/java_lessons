package com.mycompany.example;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.ibm.icu.text.RuleBasedNumberFormat;
import java.util.Locale;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class NumbersToWords extends HttpServlet {

    public Logger logger = LogManager.getLogger("NumbersToWords_log");

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            long time_request = System.currentTimeMillis();
            String req = IOUtils.toString(request.getInputStream());
            Object obj_req = new JSONParser().parse(req);
            JSONObject jo_req = (JSONObject) obj_req;
            String number = (String) jo_req.get("ubiNum");
            String languageTag = (String) jo_req.get("language_tag");

            RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag(languageTag),
                    RuleBasedNumberFormat.SPELLOUT);
            JSONObject jo_response = new JSONObject();
            jo_response.put("result", nf.format(Long.valueOf(number)));

            out.print(jo_response);
            long time_response = System.currentTimeMillis();
            logger.info("number: " + number + "; language: " + languageTag + "; spell: " + nf.format(Long.valueOf(number))+"; time: "+(time_response-time_request)+" ms");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
