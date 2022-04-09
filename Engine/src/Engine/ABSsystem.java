package Engine;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import Exceptions.*;
import Engine.LoanPlacing.LoanPlacing;
import Engine.TimeLineMoving.MoveTimeLine;
import DTO.*;
import Engine.XML_Handler.*;
import Exceptions.SystemRestrictionsException;
import Exceptions.XMLFileException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ABSsystem implements MainSystem, SystemService
{
    //need to change data structures
    private Timeline systemTimeline;
    private Map<String,Customer> name2customer;
    private Map<Loan.LoanStatus, Loan> status2loan;
    private LinkedList<Loan> loans;
    private Map<Integer, Loan> loanId2Loan;
    private InputStream loadedXMLFile = null;
    private LinkedList<Loan> activeLoans;


    public ABSsystem()
    {
        systemTimeline = new Timeline();
        name2customer = new TreeMap<>();
        loanId2Loan = new TreeMap<>();
        loans = new LinkedList<>();
        status2loan = new TreeMap<>();
    }

    @Override
    public int getCurrYaz() { return systemTimeline.getCurrentYaz();}

    @Override
    public Object getCustomersNames()
    {
        return new ArrayList<String>(name2customer.keySet());
    }


    @Override
    public void loadXML(String path) throws XMLFileException, JAXBException
    {
        try
        {
            loadedXMLFile = SchemaForLAXB.getDescriptorFromXML(path);
            takeDataFromDescriptor(SchemaForLAXB.descriptor);
            systemTimeline.resetSystemYaz();
        }
        catch (Exception ex)
        {
           throw ex;
        }
    }

    @Override
    public ArrayList<LoanDTO> showLoansInfo() {
        ArrayList<LoanDTO> loansInfo = new ArrayList<>();

        for (Loan l : loans) {
            LoanDTO curr = createLoanDTO(l);
            loansInfo.add(curr);
        }

        return loansInfo;
    }



    @Override
    public ArrayList<CustomerDTO> showCustomersInfo()
    {
        ArrayList<CustomerDTO> customersInfo = new ArrayList<>();

        try
        {
            for(Customer c : name2customer.values())
            {
                CustomerDTO curr = createCustomerDTO(c);
                customersInfo.add(curr);
            }
        }
        catch (RuntimeException ex)
        {
            System.out.println(ex.getMessage());
            System.exit(1);
        }

        return customersInfo;
    }


    @Override
    public void depositMoney(String customerName, double amount)
    {
        try {
            Customer chosenCustomer = name2customer.get(customerName);
            chosenCustomer.depositMoney(systemTimeline.getCurrentYaz(), amount);
        }

        catch (RuntimeException e1){ //user exceptions will be cached in UI
            System.out.println(e1.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void withdrawMoney(String customerName, double amount) throws Exception {
        Customer chosenCustomer = name2customer.get(customerName);
        try {
            chosenCustomer.withdrawMoney(systemTimeline.getCurrentYaz(), amount);
        }

        catch (RuntimeException e1){ //user exceptions will be catched in UI
            System.out.println(e1.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void assignLoansToLender(LoanPlacingDTO loanPlacingDTO) throws Exception
    {
        try {
            LoanPlacing.placeToLoans(loanPlacingDTO, this.activeLoans, this);

        }

        catch (RuntimeException e1){ //user exceptions will be catched in UI
            System.out.println(e1.getMessage());
            System.exit(1);
        }
    }

    @Override
    public TimelineDTO moveTimeLine()
    {
        TimelineDTO timeline;

        try {
            MoveTimeLine.moveTimeLineInOneYaz(this, systemTimeline);
        }

        catch (RuntimeException e1){ //user exceptions will be catched in UI
            System.out.println(e1.getMessage());
            System.exit(1);
        }

        timeline = new TimelineDTO(systemTimeline.getCurrentYaz());
        return timeline;
    }

    private LoanDTO createLoanDTO(Loan l)
    {
        LoanDTO loan = new LoanDTO(l.getLoanId(), l.getBorrowerName(), l.getInitialAmount(), l.getMaxYazToPay(),
                 l.getInterestPerPaymentSetByBorrowerInPercents(), l.getTotalInterestForLoan(), l.getPaymentRateInYaz(), l.getStatus(), l.getCategory());

        for(Loan.LenderDetails ld : l.getLendersDetails())
        {
            loan.addToLendersNameAndAmount(ld.lender.getName(), ld.lendersAmount);
        }

        loan.setUnpaidPayments(l.getPaymentsData().getPaymentsDataBases().get(LoanPaymentsData.PaymentType.UNPAID));
        loan.setPaidPayments(l.getPaymentsData().getPaymentsDataBases().get(LoanPaymentsData.PaymentType.PAID));

        initStatusInfo(loan, l);
        return loan;
    }

    private void initLendersInfo(LoanDTO loanToInit, Loan l)
    {
        int size = l.getLendersBelongToLoan().size();
        for(int i=0; i<size; i++) {
            loanToInit.addToLendersNameAndAmount(l.getLendersBelongToLoan().get(i).lender.getName(),
                    l.getLendersBelongToLoan().get(i).lendersAmount);
        }
    }

    //system service interface
    @Override
    public void moveMoneyBetweenAccounts(Account accountToSubtract, Account accountToAdd, double amount)
    {
        try
        {
            accountToSubtract.substructFromBalance(this.getCurrYaz(), amount);
            accountToAdd.addToBalance(this.getCurrYaz(), amount);
        }

        catch (RuntimeException e1)
        { //user exceptions will be catched in UI
            System.out.println(e1.getMessage());
            System.exit(1);
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
            Customer customer = new Customer(c.getName(), c.getAbsBalance());
            name2customer.put(customer.getName(), customer);
        }
    }

    private void takeLoansData(AbsLoans loans) throws XMLFileException
    {
        try
        {
            XMLFileChecker.checkAllLoans(loans, name2customer);
            for(AbsLoan l : loans.getAbsLoan())
            {
                Loan newLoan =  JAXBConvertor.convertLoan(l, systemTimeline.getCurrentYaz());
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

    @Override
    public Customer getCustomerByName(String name){return name2customer.get(name);}

    @Override
    public Map<String, Customer> getAllCustomers(){
        return this.name2customer;
    }

    @Override
    public Timeline getTimeLine(){
        return this.systemTimeline;
    }

    @Override
    public ArrayList<String> getSystemLoanCategories()
    {
        return LoanCategories.getCategories();
    }

}
