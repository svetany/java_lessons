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

public class NewServlet1 extends HttpServlet {

    public Logger logger = LogManager.getLogger("servlet.name");

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                String req = IOUtils.toString(request.getInputStream());
                Object obj = new JSONParser().parse(req);
                JSONObject jo = (JSONObject) obj;
                String number = (String) jo.get("ubiNum");
                String languageTag = (String) jo.get("language_tag");

                RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag(languageTag),
                        RuleBasedNumberFormat.SPELLOUT);
                String resp = "{\n"
                        + "\"result\": \"string\",\n"
                        + "}";
                resp = resp.replace("string", nf.format(Long.valueOf(number)));
                out.print(resp);
                logger.info("number: " + number + " language: " + languageTag + " Spell: " + nf.format(Long.valueOf(number)));

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
