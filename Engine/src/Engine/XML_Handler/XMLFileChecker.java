package Engine.XML_Handler;

import Engine.Customer;
import Engine.LoanCategories;
import Exceptions.XMLFileException;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

public class XMLFileChecker {

    public static void isFileExists(String fileName) throws XMLFileException
    {
        File file = new File(fileName);
        if(!file.isFile())
        {
            throw new XMLFileException("The file specified in the path could not be found");
        }
    }

    public static void isXMLFile(String fileName) throws XMLFileException
    {
        if(!fileName.endsWith(".xml") && !fileName.endsWith(".XML"))
        {
            throw new XMLFileException("The file is not an XML file!");
        }
    }


    public static void checkPath(String path) throws XMLFileException
    {
        if(path == null)
        {
            throw new XMLFileException("Non path received, please try again");
        }

        else if(path.matches("\\\\p*Hebrew"))
        {
            throw new XMLFileException("The path contains non-English characters! \nplease try again");
        }
    }

    public static void checkAllLoans(AbsLoans loans, Map<String, Customer> allCustomers) throws XMLFileException
    {
        for(AbsLoan l : loans.getAbsLoan())
        {
            try {
                XMLFileChecker.checkLoan(l, allCustomers);
            }
            catch (XMLFileException ex)
            {
                throw ex;
            }
        }
    }

    private static void checkLoan(AbsLoan loan, Map<String, Customer> allCustomers) throws XMLFileException
    {
        if(!LoanCategories.getCategories().contains(loan.absCategory))
        {
            throw new XMLFileException("Loan number " + loan.id + ": The loan's category is illegal!");
        }

        if(!allCustomers.containsKey(loan.absOwner))
        {
            throw new XMLFileException("Loan number " + loan.id + ": The loan's owner isn't a registered customer!");
        }

        if((loan.absTotalYazTime%loan.absPaysEveryYaz) != 0)
        {
            throw new XMLFileException("Loan number "+ loan.id + ": The payments rate does not divides with the total yaz of the loan");
        }
    }
}
