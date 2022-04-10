package Engine.XML_Handler;

import Engine.Customer;
import Engine.LoanCategories;
import Exceptions.XMLFileException;
import java.io.File;
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

//    public static void checkAllLoans(AbsLoans loans, Map<String, Customer> allCustomers) throws XMLFileException
//    {
//        for(AbsLoan l : loans.getAbsLoan())
//        {
//            XMLFileChecker.checkLoan(l, allCustomers);
//        }
//    }

    private static void checkLoan(AbsLoan loan, AbsCustomers allCustomers, AbsCategories categories) throws XMLFileException
    {
//        if(!LoanCategories.getCategories().contains(loan.absCategory))
//        {
//            throw new XMLFileException("Loan name: " + loan.getId() + ": The loan's category is illegal!");
//        }

        boolean found = false;
        for (String c : categories.getAbsCategory())
        {
            if (c.equals(loan.absCategory))
            {
                found = true;
                break;
            }
        }

        if(!found)
        {
            throw new XMLFileException("Loan name: " + loan.getId() + ": The loan's category is illegal!");
        }

        if((loan.absTotalYazTime%loan.absPaysEveryYaz) != 0)
        {
            throw new XMLFileException( "Loan name: " + loan.getId() + ": The payments rate does not divides with the total yaz of the loan");
        }

        found = false;
        for (AbsCustomer c : allCustomers.getAbsCustomer())
        {
            if (c.name.equals(loan.absOwner))
            {
                found = true;
                break;
            }
        }

        if(!found)
        {
            throw new XMLFileException("Loan name: " + loan.getId() + ": The loan's owner isn't a registered customer!");
        }

    }

//        if(!allCustomers.containsKey(loan.absOwner))
//        {
//            throw new XMLFileException("Loan name: " + loan.getId() + ": The loan's owner isn't a registered customer!");
//        }

    public static void isFileLogicallyOK(AbsLoans loans, AbsCustomers customers, AbsCategories categories) throws XMLFileException
    {
        for(AbsLoan loan : loans.getAbsLoan())
        {
            try
            {
                checkLoan(loan,customers, categories);
            }
            catch (XMLFileException ex)
            {
                throw ex;
            }
        }


    }
}
