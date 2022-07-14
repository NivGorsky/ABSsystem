import Engine.MainSystem;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.security.auth.login.Configuration;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet
{


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        MainSystem absSystem = ServletUtils.getAbsSystem(getServletContext());
        String loginType = request.getParameter("Login-type");
        String name = ServletUtils.GSON.fromJson(request.getReader().readLine(), String.class);
        name = name.trim();

        switch (loginType) {
            case "ADMIN": {
                synchronized (this) {
                    if(absSystem.isAdminLoggedIn(name)) {
                        String errorMessage = "Admin is already logged-in";
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                    else if (absSystem.isAdminExists(name)) {
                        String errorMessage = "Admin " + name + " already exists. Please enter a different admin name.";
                        response.getWriter().println(errorMessage);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                    else {
                            absSystem.addAdmin(name);
                            getServletContext().setAttribute(ServletUtils.MAIN_SYSTEM_ATTRIBUTE_NAME, absSystem);
                        }
                    }

                    break;
            }
            case "CUSTOMER": {
                synchronized (this) {
                    if (absSystem.isCustomerExists(name)) {
                        String errorMessage = "Username " + name + " already exists. Please enter a different username.";
                        response.getWriter().println(ServletUtils.GSON.toJson(errorMessage));
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                    else{
                        synchronized (this){
                            absSystem.addCustomer(name);
                            getServletContext().setAttribute(ServletUtils.MAIN_SYSTEM_ATTRIBUTE_NAME, absSystem);
                        }
                    }
                }
            }
        }


    }

}
