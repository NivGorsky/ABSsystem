import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/isRewindMode")
public class IsRewindModeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("application/json");
        boolean res = ServletUtils.getIsRewind();
        response.getWriter().println(ServletUtils.GSON.toJson(res));
        response.getWriter().close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String rewindQuery = request.getParameter("move-direction");

        response.setContentType("application/json");
        boolean res = ServletUtils.getIsRewind();
        response.getWriter().println(ServletUtils.GSON.toJson(res));
        response.getWriter().close();
    }


}
