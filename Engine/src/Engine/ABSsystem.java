package Engine;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import DTO.*;
import Engine.XML_Handler.*;
import Exceptions.XMLFileException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ABSsystem implements MainSystem {

    //need to change data structures
    private Timeline systemTimeline;
    private Map<String,Customer> name2customer;
    private Map<Loan.LoanStatus, Loan> status2loan;
    private LinkedList<Loan> loans;
    private Map<Integer, Loan> loanId2Loan;
    private File loadedXMLFile = null;

    public ABSsystem()
    {
        systemTimeline = new Timeline();
    }

    public int getCurrYaz() { return systemTimeline.getCurrentYaz(); }

    @Override
    public Object getCustomersNames()
    {
        return name2customer.keySet().toArray();
    }

    @Override
    public void loadXML(String path) throws XMLFileException, JAXBException
    {
        try {
            File file = new File(path);
            XMLFileChecker.isFileExists(file);
            XMLFileChecker.isXMLFile(path);

            JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AbsDescriptor descriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(file);

            takeDataFromDescriptor(descriptor);
            loadedXMLFile = file;
            systemTimeline.resetSystemYaz();
        }
        catch (Exception e) {
           throw e;
        }
    }

    @Override
    public ArrayList<LoanDTO> showLoansInfo()
    {
        ArrayList<LoanDTO> loansInfo = new ArrayList<>();

        for(Loan l : loans)
        {
           LoanDTO curr = createLoanDTO(l);
           loansInfo.add(curr);
        }

        return loansInfo;
    }

    @Override
    public ArrayList<CustomerDTO> showCustomersInfo()
    {
        ArrayList<CustomerDTO> customersInfo = new ArrayList<>();

        for(Customer c : name2customer.values())
        {
            CustomerDTO curr = createCustomerDTO(c);
            customersInfo.add(curr);
        }

        return customersInfo;
    }

    @Override
    public void depositMoney(String customerName, double amount)
    {
        Customer chosenCustomer = name2customer.get(customerName);
        chosenCustomer.depositMoney(systemTimeline.getCurrentYaz(), amount);
    }

    @Override
    public void withdrawMoney(String customerName, double amount) throws Exception {
        Customer chosenCustomer = name2customer.get(customerName);
        try {
            chosenCustomer.withdrawMoney(systemTimeline.getCurrentYaz(), amount);
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    @Override
    public void assignLoansToLender() {


    }

    @Override
    public void moveTimeline()
    {

    }

    private LoanDTO createLoanDTO(Loan l)
    {
        LoanDTO loan = new LoanDTO(l.getLoanId(), l.getBorrowerName(), l.getInitialAmount(), l.getMaxYazToPay(),
                 l.getInterestPerPaymentSetByBorrowerInPercents(), l.getPaymentRateInYaz(), l.getStatus(), l.getCategory());

        for(Loan.LenderDetails ld : l.getLendersDetails())
        {
            loan.addToLendersNameAndAmount(ld.lender.getName(), ld.lendersAmount);
        }

        loan.setUnpaidPayments(l.getPaymentsData().getPaymentsDataBase().get(LoanPaymentsData.PaymentType.UNPAID));
        loan.setPaidPayments(l.getPaymentsData().getPaymentsDataBase().get(LoanPaymentsData.PaymentType.PAID));

        initStatusInfo(loan, l);
        return loan;
    }

    private void initLendersInfo(LoanDTO loanToInit, Loan l)
    {
        int size = l.getLendersBelongToLoan().size();
        for(int i=0; i<size; i++)
        {
            loanToInit.addToLendersNameAndAmount(l.getLendersBelongToLoan().get(i).lender.getName(),
                    l.getLendersBelongToLoan().get(i).lendersAmount);
        }


    }

    private void initStatusInfo(LoanDTO loanToInit, Loan l)
    {
        switch (l.getStatus())
        {
            case PENDING:
            {
                int sum = 0;

                initLendersInfo(loanToInit, l);
                for(LoanDTO.LenderDetailsDTO le : loanToInit.getLendersNamesAndAmounts())
                {
                    sum += le.lendersInvestAmount;
                }

                loanToInit.setTotalMoneyRaised(sum);
                break;
            }
            case ACTIVE:
            {
                initLendersInfo(loanToInit, l);
                loanToInit.setActivationYaz(l.getActivationYaz());
                loanToInit.setNextPaymentYaz(loanToInit.getUnpaidPayments().firstKey());
                break;
            }
            case IN_RISK:
            {
                initLendersInfo(loanToInit, l);
                break;
            }

            case FINISHED:
            {
                initLendersInfo(loanToInit, l);
                loanToInit.setActivationYaz(l.getActivationYaz());
                loanToInit.setFinishYaz(l.getFinishYaz());
                break;
            }
        }
    }

    private CustomerDTO createCustomerDTO(Customer c)
    {
        CustomerDTO customer = new CustomerDTO(c.getName(), c.getAccount().getBalance());
        ArrayList<Account.AccountMovement> customerMovements = c.getAccount().getMovements();
        ArrayList<AccountMovementDTO> customerDTOMovements = new ArrayList<>();

        for (Account.AccountMovement m : customerMovements)
        {
            AccountMovementDTO curr = new AccountMovementDTO(m.getYaz(), m.getAmount(), m.getMovementKind(),
                    m.getBalanceBefore(), m.getBalanceAfter());

            customerDTOMovements.add(curr);
        }

        customer.setAccountMovements(customerDTOMovements);

        return customer;
    }

    private void takeDataFromDescriptor(AbsDescriptor descriptor) throws XMLFileException
    {
        AbsCategories categories = descriptor.getAbsCategories();
        AbsLoans loans = descriptor.getAbsLoans();
        AbsCustomers customers = descriptor.getAbsCustomers();

        takeCategoriesData(categories);
        takeCustomersData(customers);

        try {
            takeLoansData(loans);
        }
        catch (XMLFileException ex)
        {
            name2customer = null;
            LoanCategories.setCategories(null);
            throw ex;
        }
    }

    private void takeCategoriesData(AbsCategories categories)
    {
        for(String category : categories.getAbsCategory())
        {
            LoanCategories.addCategory(category);
        }
    }

    private void takeCustomersData(AbsCustomers customers)
    {
        for(AbsCustomer c : customers.getAbsCustomer())
        {
            Customer customer = new Customer(c.getName(), c.getAbsBalance()); //TODO: change balance to int like aviad?
        }
    }

    private void takeLoansData(AbsLoans loans) throws XMLFileException
    {
        try
        {
            XMLFileChecker.checkAllLoans(loans, name2customer);
            for(AbsLoan l : loans.getAbsLoan())
            {
                Loan newLoan =  JAXBConvertor.convertLoan(l);
                this.status2loan.put(newLoan.getStatus(), newLoan);
                this.loanId2Loan.put(newLoan.getLoanId(), newLoan);
                this.loans.add(newLoan); //TODO: delete some loan fields from system
            }
        }
        catch (XMLFileException ex)
        {
            throw ex;
        }
    }
}
