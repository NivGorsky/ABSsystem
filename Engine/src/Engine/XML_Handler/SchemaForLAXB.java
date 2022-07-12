package Engine.XML_Handler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SchemaForLAXB {

    public static AbsDescriptor descriptor;
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "Engine.XML_Handler";

    public static AbsDescriptor getDescriptorFromXML(InputStream inputStream) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AbsDescriptor descriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(inputStream);

        return descriptor;
    }

}
