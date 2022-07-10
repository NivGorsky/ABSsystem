import Engine.MainSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        MainSystem AbsSystem = ServletUtils.getAbsSystem(getServletContext());
        String loginType = request.getParameter("Login-type");
        String name = request.getReader().readLine();
        name = name.trim();

        switch (loginType) {
            case "ADMIN": {
                synchronized (this) {
                    if(AbsSystem.isAdminLoggedIn(name)) {
                        String errorMessage = "Admin is already logged-in";
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                    else if (AbsSystem.isAdminExists(name)) {
                        String errorMessage = "Admin " + name + " already exists. Please enter a different admin name.";
                        response.getWriter().println(errorMessage);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                    else {
                            AbsSystem.addAdmin(name);
                        }
                    }
            }
            case "CUSTOMER": {
                synchronized (this) {
                    if (AbsSystem.isCustomerExists(name)) {
                        String errorMessage = "Username " + name + " already exists. Please enter a different username.";
                        response.getWriter().println(errorMessage);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    } else {
                        AbsSystem.addCustomer(name);
                    }
                }
            }
        }


    }

}
