/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd.code.stpanel.pages;

import sd.code.stpanel.common.General;
import sd.code.stpanel.common.Web;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sd.code.stpanel.types.AMIResult;

/**
 *
 * @author motaz
 */
@WebServlet(name = "Status", urlPatterns = {"/Status"})
public class Status extends HttpServlet {

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
        String user = Web.getCookieValue(request, "st-user");
        String pbxfile = General.getPBXsDir() + Web.getCookieValue(request, "file");
        try {
            if (Web.checkSession(request, user)) {
                Web.setHeader(true, request, response, out, "advanced", "status");

                out.println("<h2>Status</h2>");

                String command = request.getParameter("command");

                String commandLine = "";
                String chColor =  "";
                String peerColor = "";
                String usersColor = "";
                String codecColor = "";
                String statusColor = "";
                String queueColor = "";
              
                String selectedColor = "bgcolor=#FFFFcc";

                if (command != null) {

                    if (command.equals("channels")){
                            commandLine = "core show channels verbose";
                            chColor = selectedColor;
                    }
                    else if (command.equals("peers")){
                            commandLine = "sip show peers";
                            peerColor = selectedColor;
                    }
                    else if (command.equals("users")){
                            commandLine = "sip show users";
                            usersColor = selectedColor;
                    }
                    else if (command.equals("codecs")){
                            commandLine = "core show codecs";
                            codecColor = selectedColor;
                    }
                    else if (command.equals("stats")){
                            commandLine = "sip show channelstats";
                            statusColor = selectedColor;
                    }
                    else if (command.equals("queues")){
                            commandLine = "queue show";
                            queueColor = selectedColor;

                    }
                   /* else if (command.equals("agents")){
                            commandLine = "agent show";
                            agentColor = selectedColor;

                    }*/
                }


                out.println("<table><tr bgcolor=#eeeecc>");
                out.println("<td " + chColor + "><a href='Status?command=channels'>Channels</a></td>");
                out.println("<td " + peerColor + "><a href='Status?command=peers'>Peers</a></td>");
                out.println("<td " + usersColor + "><a href='Status?command=users'>Users</a></td>");
                out.println("<td " + statusColor + "><a href='Status?command=stats'>Channel stats.</a></td>");
                out.println("<td " + queueColor + "><a href='Status?command=queues'>Queue</a></td>");
                out.println("<td " + codecColor + "><a href='Status?command=codecs'>Codecs</a></td>");
                out.println("</tr></table>");

                if (command != null){

                    out.println("<h3>" + command + "</h3>");
                    out.println("<form method=post>");
                    out.println("<input type=submit value=Refresh class=btn />");
                    out.println("</form>");

                    AMIResult result =  AMI.callAMICommand(pbxfile, commandLine);
                    if (result.success){
                        out.println("<pre>" + result.result + "</pre>");
                    }
                    else {
                       out.println("<p class=errormessage>" + result.errorMessage + "</p>");
                    }
                }

                Web.setFooter(request, response);
            }
            else {
                response.sendRedirect("Login");
            }
        }
        catch (Exception ex){
            out.println(ex.toString());
        }
        out.close();
        
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
