import Engine.MainSystem;
import Exceptions.XMLFileException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Collection;

@WebServlet("/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String custName = request.getParameter("customer-name");
        Collection<Part> parts = request.getParts();
        MainSystem absSystem = ServletUtils.getAbsSystem(getServletContext());
        try {
            synchronized (this) {
                for (Part part : parts) {
                    absSystem.loadXML(part.getContentType(), part.getInputStream(), custName);
                    getServletContext().setAttribute(ServletUtils.MAIN_SYSTEM_ATTRIBUTE_NAME, absSystem);
                    response.setStatus(200);
                }
            }
        }
        catch (XMLFileException | JAXBException ex) {
          String errorMessage = ex.getMessage();
          response.getWriter().println(errorMessage);
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
          response.getWriter().close();

        }

       finally {
            response.getWriter().close();
        }

    }
}
