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

    public static InputStream getDescriptorFromXML(String path)
    {
        InputStream inputStream=null;
        try {
            inputStream = new FileInputStream(path);
            descriptor = deserializeFrom(inputStream);
        }
        catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return inputStream;
    }
    private static AbsDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (AbsDescriptor) u.unmarshal(in);
    }
}
