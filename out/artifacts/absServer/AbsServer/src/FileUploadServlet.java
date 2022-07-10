import Engine.MainSystem;
import Exceptions.XMLFileException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import javax.jws.WebService;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;

@WebServlet("/load-file")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");

        String custName = request.getParameter("customer-name");
        Collection<Part> parts = request.getParts();
        MainSystem AbsSystem = ServletUtils.getAbsSystem(getServletContext());
        try {
            for (Part part : parts) {
                AbsSystem.loadXML(part.getContentType(), part.getInputStream(), custName);
                response.setStatus(200);
            }
        }
        catch (XMLFileException | JAXBException ex) {
          response.setStatus(400);
        }

    }
}
