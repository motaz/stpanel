/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd.code.stpanel.pages;

import sd.code.stpanel.common.General;
import sd.code.stpanel.common.Web;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author motaz
 */
@WebServlet(name = "EditPBX", urlPatterns = {"/EditPBX"})
public class EditPBX extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Web.setHeader(true, request, response, out, "", "");
            
        String user = Web.getCookieValue(request, "user");
        String selectedFileName = request.getParameter("pbx");
            
        if (Web.checkSession(request, user)){
                
	   doUpdate(request, out, selectedFileName, response); 
	   out.println("<h2>Edit PBX configuration</h2>");

	   displayEdit(out, selectedFileName);
	}
	else
	{
	    response.sendRedirect("Login");
	}

       Web.setFooter(out);
       out.close();
        
    }

    private void doUpdate(HttpServletRequest request, final PrintWriter out, String selectedFileName, HttpServletResponse response) throws IOException {
        if (request.getParameter("update") != null) {
            String title = request.getParameter("title");
            String url = request.getParameter("url");
            String fileName = request.getParameter("file");
	    String amiuser = request.getParameter("amiuser");
	    String amipass = request.getParameter("amipass");
            
            if ((title.trim().isEmpty()) || (url.trim().isEmpty()) || (fileName.trim().isEmpty())) {
                out.println("<p class=errormessage>Empty parameter</p>");
            }
            else {                 
                String dir = General.getPBXsDir();
                
                File theDir = new File(dir);
                
                if (! theDir.exists()) {
                    boolean added = theDir.mkdir();
                    
                    if (! added) {
                        out.println("<p class=errormessage>Unable to create " + dir + "</p>");
                    }
                    
                }
                
                File oldFile = new File(dir + selectedFileName);
                oldFile.delete();
                
                if (!fileName.contains(".")) {
                    fileName = fileName + ".stc";
                }
                fileName = dir + fileName;
                File configFile = new File(fileName);
                configFile.createNewFile();
                
                boolean success = General.setConfigurationParameter("url", url, fileName);
                if (success) {
                    success = General.setConfigurationParameter("title", title, fileName);
		    General.setConfigurationParameter("amiuser", amiuser, fileName);
		    General.setConfigurationParameter("amipass", amipass, fileName);
                }
                if (success) {
                    response.sendRedirect("Home");
                }
                else {
                    out.println("<p class=errormessage>Unable to write configuration</p>");
                    
                }
                
            }
        }
    }



    private void displayEdit(final PrintWriter out, String fileName) {
        
        String pbxFileName = General.getPBXsDir() + fileName;
        String title = General.getConfigurationParameter("title", "", pbxFileName);
        String url = General.getConfigurationParameter("url", "", pbxFileName);
       
        String amiuser = General.getConfigurationParameter("amiuser", "admin", pbxFileName);
        String amipass = General.getConfigurationParameter("amipass", "", pbxFileName);
	
        out.println("<form method=POST>");
        out.println("<table>");
        out.println("<tr><td>Title </td><td><input type=text name=title value='" + title + 
		    "' /></td></td>");
        out.println("<tr><td>Config file name </td>");
        out.println("<td><input type=text name=file value='" + fileName + "' /></td></td>");
        
        out.println("<tr><td>STAgent URL</td>");
        out.println("<td><input type=text name=url size=30 value='" + url + "' /></td></tr>");

        out.println("<tr><td>AMI User</td>");
        out.println("<td><input type=text name=amiuser size=30 value='" + amiuser + "' /></td></tr>");

	out.println("<tr><td>AMI Password</td>");
        out.println("<td><input type=text name=amipass size=30 value='" + amipass + "' /></td></tr>");

	out.println("<tr><td><input type=submit name=update value=Update /></td></tr>");
        out.println("</table>");
        out.println("</form>");
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