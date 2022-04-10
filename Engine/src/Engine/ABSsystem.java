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
import Exceptions.XMLFileException;
import javax.xml.bind.JAXBException;

public class ABSsystem implements MainSystem, SystemService
{
    private Timeline systemTimeline;
    private Map<String,Customer> name2customer;
    private Map<Loan.LoanStatus, Loan> status2loan;
    private LinkedList<Loan> loans;
    private Map<Integer, Loan> loanId2Loan;
    private InputStream loadedXMLFile = null;

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
    public ArrayList<String> getCustomersNames()
    {
        return new ArrayList<String>(name2customer.keySet());
    }

    @Override
    public void loadXML(String path) throws XMLFileException, JAXBException
    {
        loadedXMLFile = SchemaForLAXB.getDescriptorFromXML(path);
        XMLFileChecker.isFileExists(path);
        XMLFileChecker.isXMLFile(path);
        takeDataFromDescriptor(SchemaForLAXB.descriptor);
        systemTimeline.resetSystemYaz();
    }

    @Override
    public ArrayList<LoanDTO> showLoansInfo()
    {
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
    public void withdrawMoney(String customerName, double amount) throws ValueOutOfRangeException
    {
        Customer chosenCustomer = name2customer.get(customerName);
        chosenCustomer.withdrawMoney(systemTimeline.getCurrentYaz(), amount);
    }

    @Override
    public void assignLoansToLender(LoanPlacingDTO loanPlacingDTO) throws Exception
    {
        LoanPlacing.placeToLoans(loanPlacingDTO, this.loans, this, getCurrYaz());
    }

    @Override
    public TimelineDTO moveTimeLine()
    {
        TimelineDTO timeline;

        MoveTimeLine.moveTimeLineInOneYaz(this, systemTimeline);
        timeline = new TimelineDTO(systemTimeline.getCurrentYaz());

        return timeline;
    }

    private LoanDTO createLoanDTO(Loan l)
    {
        LoanDTO loan = new LoanDTO(l.getLoanId(), l.getLoanName(),  l.getBorrowerName(), l.getInitialAmount(),
                l.getMaxYazToPay(), l.getInterestPerPaymentSetByBorrowerInPercents(), l.getTotalInterestForLoan(),
                l.getPaymentRateInYaz(), l.getStatus(), l.getCategory(), l.getInterestPaid(), l.getAmountPaid(), l.getDebt());

        for(Loan.LenderDetails ld : l.getLendersDetails())
        {
            loan.addToLendersNameAndAmount(ld.lender.getName(), ld.lendersAmount);
        }

        loan.setUnpaidPayments(l.getPaymentsData().getPaymentsDataBases().get(LoanPaymentsData.PaymentType.UNPAID));
        loan.setPaidPayments(l.getPaymentsData().getPaymentsDataBases().get(LoanPaymentsData.PaymentType.PAID));

        initStatusInfo(loan, l);
        return loan;
    }

    //system service interface
    @Override
    public void moveMoneyBetweenAccounts(Account accountToSubtract, Account accountToAdd, double amount)
    {
        accountToSubtract.substructLoanPayment(this.getCurrYaz(), amount);
        accountToAdd.addToBalance(this.getCurrYaz(), amount);
    }

    private void initStatusInfo(LoanDTO loanToInit, Loan l)
    {
        switch (l.getStatus())
        {
            case PENDING:
            {
                int sum = 0;

//                initLendersInfo(loanToInit, l);
                for(LoanDTO.LenderDetailsDTO le : loanToInit.getLendersNamesAndAmounts())
                {
                    sum += le.lendersInvestAmount;
                }

                loanToInit.setTotalMoneyRaised(sum);
                break;
            }
            case ACTIVE:
            {
//                initLendersInfo(loanToInit, l);
                loanToInit.setActivationYaz(l.getActivationYaz());
                loanToInit.setNextPaymentYaz(loanToInit.getUnpaidPayments().firstKey());
                break;
            }
            case IN_RISK:
            {
//                initLendersInfo(loanToInit, l);
                break;
            }

            case FINISHED:
            {
//                initLendersInfo(loanToInit, l);
                loanToInit.setActivationYaz(l.getActivationYaz());
                loanToInit.setFinishYaz(l.getFinishYaz());
                break;
            }
        }
    }

    private CustomerDTO createCustomerDTO(Customer c)
    {
        CustomerDTO customerDTO = new CustomerDTO(c.getName(), c.getAccount().getBalance());
        ArrayList<Account.AccountMovement> customerMovements = c.getAccount().getMovements();
        ArrayList<AccountMovementDTO> customerDTOMovements = new ArrayList<>();
        ArrayList<LoanDTO> customerLoansAsLender = new ArrayList<LoanDTO>();
        ArrayList<LoanDTO> customerLoansAsBorrower = new ArrayList<LoanDTO>();

        for (Account.AccountMovement m : customerMovements)
        {
            AccountMovementDTO curr = new AccountMovementDTO(m.getYaz(), m.getAmount(), m.getMovementKind(),
                    m.getBalanceBefore(), m.getBalanceAfter());

            customerDTOMovements.add(curr);
        }
        customerDTO.setAccountMovements(customerDTOMovements);

        for (Loan loan:c.getLoansAsLender()){
            LoanDTO newLoanDTO = createLoanDTO(loan);
            customerLoansAsLender.add(newLoanDTO);
        }
        customerDTO.setLoansAsLender(customerLoansAsLender);

        for (Loan loan:c.getLoansAsBorrower())
        {
            LoanDTO newLoanDTO = createLoanDTO(loan);
            customerLoansAsBorrower.add(newLoanDTO);
        }
        customerDTO.setLoansAsBorrower(customerLoansAsBorrower);

        return customerDTO;
    }

    private void takeDataFromDescriptor(AbsDescriptor descriptor) throws XMLFileException
    {
        //resetSystem();
        AbsCategories categories = descriptor.getAbsCategories();
        AbsLoans loans = descriptor.getAbsLoans();
        AbsCustomers customers = descriptor.getAbsCustomers();

        takeCategoriesData(categories);
        takeCustomersData(customers);


//        try {
//            takeLoansData(loans);
//        }
//        catch (XMLFileException ex) {
//            resetSystem();
//            throw ex;
//        }

    }

    private void takeCategoriesData(AbsCategories categories)
    {
        for(String c : categories.getAbsCategory())
        {
            String category =  c.trim();
            LoanCategories.addCategory(category);
        }
    }

    private void takeCustomersData(AbsCustomers customers)
    {
        for(AbsCustomer c : customers.getAbsCustomer())
        {
            Customer customer = JAXBConvertor.convertCustomer(c);
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

                Customer customer =  name2customer.get(l.getAbsOwner());
                customer.getLoansAsBorrower().add(newLoan);
            }
        }
        catch (XMLFileException ex)
        {
            throw ex;
        }
    }

    @Override
    public Customer getCustomerByName(String name) { return name2customer.get(name); }

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

    private void resetSystem()
    {
        systemTimeline.resetSystemYaz();
    }
}
