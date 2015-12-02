package com.mypackage;
/*
 * @Author1 : Kunal Sharma 2014054
 * @Author2 : Sahil Ruhela 2014092
 */
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = -4433102460849019660L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath()+ "/");
    }
}
