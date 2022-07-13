package Engine;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

import Engine.LoanPlacing.loanPlacingAsTask.LoanPlacingAsTask;
import Engine.PaymentsDB.PaymentsDB;
import Exceptions.*;
import Engine.LoanPlacing.regularLoanPlacing.LoanPlacing;
import Engine.TimeLineMoving.MoveTimeLine;
import DTO.*;
import Engine.XML_Handler.*;
import Exceptions.XMLFileException;
import javafx.concurrent.Task;

public class ABSsystem implements MainSystem, SystemService {
    private final Timeline systemTimeline;
    private Map<String, Customer> name2customer;
    private Map<Loan.LoanStatus, Loan> status2loan;
    private LinkedList<Loan> loans;
    private Map<Integer, Loan> loanId2Loan;
    private Map<Customer, List<Notification>> customer2Notifications;
    private ArrayList<ArrayList<String>> scrambleQueryFields;
    private UIController scrambleController;
    private Integer numberOfLoansAssignedInSinglePlacingAlgorithmRun;
    private Task<Boolean> currentRunningTask;

    public ABSsystem() {

        systemTimeline = new Timeline();
        name2customer = new TreeMap<>();
        loanId2Loan = new TreeMap<>();
        loans = new LinkedList<>();
        status2loan = new TreeMap<>();
        customer2Notifications = new TreeMap<Customer, List<Notification>>();
//        initLoanPlacingQueryFields();
        numberOfLoansAssignedInSinglePlacingAlgorithmRun = -1;
    }

    private void injectSystemServiceInterfaceToLoans() {
        for (Loan loan : loans) {
            loan.setSystemService(this);
        }
    }

    private void resetSystem() {
        resetLoans();
        systemTimeline.resetSystemYaz();
        name2customer = new TreeMap<>();
        LoanCategories.categories = new ArrayList<>();
    }

    private void resetLoans() {
        loanId2Loan = new TreeMap<>();
        loans = new LinkedList<>();
        status2loan = new TreeMap<>();
    }

    private void initStatusInfo(LoanDTO loanToInit, Loan l) {
        switch (l.getStatus()) {
            case PENDING: {
                int sum = 0;
                for (LoanDTO.LenderDetailsDTO le : loanToInit.getLenderDTOS()) {
                    sum += le.lendersInvestAmount.getValue();
                }

                loanToInit.setTotalMoneyRaised(sum);
                break;
            }
            case ACTIVE: {
                loanToInit.setActivationYaz(l.getActivationYaz());
                loanToInit.setNextPaymentYaz(loanToInit.getUnpaidPayments().firstKey());
                break;
            }
            case IN_RISK: {
                break;
            }

            case FINISHED: {
                loanToInit.setActivationYaz(l.getActivationYaz());
                loanToInit.setFinishYaz(l.getFinishYaz());
                break;
            }
        }
    }

    private CustomerDTO createCustomerDTO(Customer c) {
        CustomerDTO customerDTO = new CustomerDTO(c.getName(), c.getAccount().getBalance());
        ArrayList<Account.AccountMovement> customerMovements = c.getAccount().getMovements();
        ArrayList<AccountMovementDTO> customerDTOMovements = new ArrayList<>();
        ArrayList<LoanDTO> customerLoansAsLender = new ArrayList<>();
        ArrayList<LoanDTO> customerLoansAsBorrower = new ArrayList<>();

        for (Account.AccountMovement m : customerMovements) {
            AccountMovementDTO curr = new AccountMovementDTO(m.getYaz(), m.getAmount(), m.getMovementKind(),
                    m.getBalanceBefore(), m.getBalanceAfter());

            customerDTOMovements.add(curr);
        }
        customerDTO.setAccountMovements(customerDTOMovements);

        for (Loan loan : c.getLoansAsLender()) {
            LoanDTO newLoanDTO = createLoanDTO(loan);
            customerLoansAsLender.add(newLoanDTO);
        }
        customerDTO.setLoansAsLender(customerLoansAsLender);

        for (Loan loan : c.getLoansAsBorrower()) {
            LoanDTO newLoanDTO = createLoanDTO(loan);
            customerLoansAsBorrower.add(newLoanDTO);
        }
        customerDTO.setLoansAsBorrower(customerLoansAsBorrower);

        return customerDTO;
    }

    private void takeDataFromDescriptor(AbsDescriptor descriptor, String customer) throws XMLFileException {
        AbsCategories categories = descriptor.getAbsCategories();
        AbsLoans loans = descriptor.getAbsLoans();

        try {
            XMLFileChecker.isFileLogicallyOK(loans, categories);
        } catch (XMLFileException ex) {
            throw ex;
        }

        resetSystem();
        takeCategoriesData(categories);
        takeLoansData(loans, customer);
    }

    private void takeCategoriesData(AbsCategories categories) {
        for (String c : categories.getAbsCategory()) {
            String category = c.trim();
            LoanCategories.addCategory(category);
        }
    }

    private void takeLoansData(AbsLoans loans, String customer) {
        for (AbsLoan l : loans.getAbsLoan()) {
            Loan newLoan = JAXBConvertor.convertLoan(l, systemTimeline.getCurrentYaz(), customer);
            this.status2loan.put(newLoan.getStatus(), newLoan);
            this.loanId2Loan.put(newLoan.getLoanId(), newLoan);
            this.loans.add(newLoan);
        }
    }

    public void addNotificationToCustomer(Customer customer, Notification notification) {
        List<Notification> customerNotifications = customer2Notifications.getOrDefault(customer, new ArrayList<Notification>());
        customerNotifications.add(notification);
        customer2Notifications.put(customer, customerNotifications);
    }

    private Loan getLoanByName(String loanName) throws Exception {
        for (Loan loan : loans) {
            if (loan.getLoanName().equals(loanName)) {
                return loan;
            }
        }

        throw new Exception("Loan was not found in loans data base in get loan by name");
    }

    private double getLendersPartOfLoanInAmount(Loan loan, Customer lender) throws Exception {
        for (Loan.LenderDetails lenderDetails : loan.getLendersDetails()) {
            if (lenderDetails.lender.getName().equals(lender.getName())) {
                return lenderDetails.lendersAmount;
            }
        }

        throw new Exception("Lender was not found in loan while trying to find lenders part of loan");
    }

    private double getLendersPartOfLoanInPercent(Loan loan, Customer lender) throws Exception {
        for (Loan.LenderDetails lenderDetails : loan.getLendersDetails()) {
            if (lenderDetails.lender.getName().equals(lender.getName())) {
                return lenderDetails.lendersPartOfLoanInPercent;
            }
        }

        throw new Exception("Lender was not found in loan while trying to find lenders part of loan");
    }

    private boolean isCustomerIsLenderInLoan(Loan loan, String customerName) {
        boolean result = false;
        LinkedList<Loan.LenderDetails> lendersDetails = loan.getLendersDetails();

        for (Loan.LenderDetails lenderDetails : lendersDetails) {
            if (lenderDetails.lender.getName().equals(customerName)) {
                result = true;
                break;
            }
        }

        return result;
    }

    public void makeLoanPaymentFromBorrowerToLender(Loan loan, LoanPaymentsData.Payment payment, Customer borrower, Customer lender) throws Exception{
        try {
            Account borrowerAccount = borrower.getAccount();
            Account lenderAccount = lender.getAccount();
            double paymentFullAmount = payment.getBothPartsOfAmountToPay();
            double lendersPartOfLoanInPercent = getLendersPartOfLoanInPercent(loan, lender);
            double lendersPartOfPaymentInAmount = (lendersPartOfLoanInPercent / 100) * paymentFullAmount;

            moveMoneyBetweenAccounts(borrowerAccount, lenderAccount, lendersPartOfPaymentInAmount);
            payment.setPartialPaidInPercents(lendersPartOfLoanInPercent);
        }

        catch (Exception e) {
            throw new Exception("There was a problem while trying to make loan payment from borrower to lender");

        }
    }

    private void changeLoanStatusIfNeeded(Loan loan){
        if(!loan.isTherePaymentsOfSpecificType(LoanPaymentsData.PaymentType.UNPAID)){
            loan.setLoanStatus(Loan.LoanStatus.FINISHED, systemTimeline.getCurrentYaz());
        }
    }

    private void closePayment(LoanPaymentsData.Payment payment, Customer borrower, Loan loan){
        double amountToPay = payment.getBothPartsOfAmountToPay();
        Account loansAccount = loan.getLoanAccount();
        Account borrowerAccount = borrower.getAccount();

        moveMoneyBetweenAccounts(borrowerAccount, loansAccount, amountToPay);
        splitLoanMoneyToLenders(loan);
        payment.setPaymentType(LoanPaymentsData.PaymentType.PAID);
        payment.setActualPaymentYaz(this.getCurrYaz());
        loan.setAmountPaid(payment.getLoanPartOfThePayment());
        loan.setInterestPaid(payment.getInterestPartThatWasPaid());
        loan.setDebt(loan.getDebt() - amountToPay);
    }

    private void splitLoanMoneyToLenders(Loan loan){
        LinkedList<Loan.LenderDetails> lenders = loan.getLendersDetails();

        for (Loan.LenderDetails lenderDetails: lenders){
            Account lendersAccount = lenderDetails.lender.getAccount();
            Account loansAccount = loan.getLoanAccount();
            double amountInLoan = loansAccount.getBalance();
            double lendersPartOfLoanInPercent = lenderDetails.lendersPartOfLoanInPercent;
            double amountToTransfer = (lendersPartOfLoanInPercent / 100) * amountInLoan;

            moveMoneyBetweenAccounts(loansAccount, lendersAccount, amountToTransfer);
        }
    }

    private LoanDTO createLoanDTO(Loan l) {
        LoanDTO loanDTO = new LoanDTO(l.getLoanName(), l.getBorrowerName(), l.getInitialAmount(),
                l.getMaxYazToPay(), l.getInterestPerPaymentSetByBorrowerInPercents(), l.getTotalInterestForLoan(),
                l.getPaymentRateInYaz(), l.getStatus().toString(), l.getCategory(), l.getInterestPaid(), l.getAmountPaid(),
                l.getDebt(), l.getLoanAmountFinancedByLenders());

        for (Loan.LenderDetails ld : l.getLendersDetails()) {
            loanDTO.addToLendersNameAndAmount(ld.lender.getName(), ld.lendersAmount);
        }

        setLoanDTOUnpaidPayments(l.getPaymentsData().getPaymentsDataBases().get(LoanPaymentsData.PaymentType.UNPAID), loanDTO);
        setLoanDTOPaidPayments(l.getPaymentsData().getPaymentsDataBases().get(LoanPaymentsData.PaymentType.PAID), loanDTO);

        initStatusInfo(loanDTO, l);
        return loanDTO;
    }

    private void setLoanDTOUnpaidPayments(Engine.PaymentsDB.PaymentsDB payments, LoanDTO loanDTO){

        for(LoanPaymentsData.Payment p : payments.getPayments().values())
        {

            LoanDTO.PaymentDTO payment = loanDTO. new PaymentDTO(p.getScheduledYaz(), p.getLoanPartOfThePayment(),
                    p.getInterestPartOfThePayment(), p.getActualPaymentYaz(), p.getPaymentType().toString());

            loanDTO.getUnpaidPayments().put(payment.getOriginalYazToPayProperty().getValue(), payment);
        }
    }

    private void setLoanDTOPaidPayments(Engine.PaymentsDB.PaymentsDB payments, LoanDTO loanDTO)
    {
        for(LoanPaymentsData.Payment p : payments.getPayments().values())
        {
            LoanDTO.PaymentDTO payment = loanDTO.new PaymentDTO(p.getScheduledYaz(), p.getLoanPartOfThePayment(),
                    p.getInterestPartOfThePayment(), p.getActualPaymentYaz(), p.getPaymentType().toString());

            loanDTO.getPaidPayments().put(payment.getActualPaymentYazProperty().getValue(), payment);
        }
    }


    @Override
    public int getCurrYaz() {
        return systemTimeline.getCurrentYaz();
    }

    @Override
    public ArrayList<String> getCustomersNames() {
        return new ArrayList<>(name2customer.keySet());
    }

    @Override
    public void loadXML(String path, String customer) throws XMLFileException {
        try {
            InputStream loadedXMLFile = SchemaForLAXB.getDescriptorFromXML(path);
            XMLFileChecker.isFileExists(path);
            XMLFileChecker.isXMLFile(path);
            takeDataFromDescriptor(SchemaForLAXB.descriptor, customer);
            injectSystemServiceInterfaceToLoans();
        } catch (XMLFileException ex) {
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
    public ArrayList<CustomerDTO> showCustomersInfo() {
        ArrayList<CustomerDTO> customersInfo = new ArrayList<>();
        for (Customer c : name2customer.values()) {
            CustomerDTO curr = createCustomerDTO(c);
            customersInfo.add(curr);
        }

        return customersInfo;
    }

    @Override
    public void depositMoney(String customerName, double amount) {
        Customer chosenCustomer = name2customer.get(customerName);
        chosenCustomer.depositMoney(systemTimeline.getCurrentYaz(), amount);
    }

    @Override
    public void withdrawMoney(String customerName, double amount) throws ValueOutOfRangeException {
        try {
            Customer chosenCustomer = name2customer.get(customerName);
            chosenCustomer.withdrawMoney(systemTimeline.getCurrentYaz(), amount);
        } catch (ValueOutOfRangeException ex) {
            throw ex;
        }

    }

    @Override
    public void assignLoansToLender(LoanPlacingDTO loanPlacingDTO) throws Exception {
        LoanPlacing.placeToLoans(loanPlacingDTO, this.loans, this, getCurrYaz());
    }

    @Override
    public TimelineDTO moveTimeLine() {
        TimelineDTO timeline;

        MoveTimeLine.moveTimeLineInOneYaz(this, systemTimeline);
        timeline = new TimelineDTO(systemTimeline.getCurrentYaz());

        return timeline;
    }

    @Override
    public void moveMoneyBetweenAccounts(Account accountToSubtract, Account accountToAdd, double amount) {
        accountToSubtract.substructFromBalance(this.getCurrYaz(), amount);
        accountToAdd.addToBalance(this.getCurrYaz(), amount);
    }

    @Override
    public Customer getCustomerByName(String name) {
        return name2customer.get(name);
    }

    @Override
    public Map<String, Customer> getAllCustomers() {
        return this.name2customer;
    }

    @Override
    public Timeline getTimeLine() {
        return this.systemTimeline;
    }

    @Override
    public LoanCategorisDTO getSystemLoanCategories() {
        return new LoanCategorisDTO(LoanCategories.getCategories());
    }

    @Override
    public CustomerDTO getCustomerDTO(String customerName) {
        Customer c = name2customer.get(customerName);

        return createCustomerDTO(c);
    }

    @Override
    public NotificationsDTO getNotificationsDTO(String customerName) {
        Customer customer = name2customer.get(customerName);

        List<Notification> customerNotifications = customer2Notifications.getOrDefault(customer, new ArrayList<Notification>());
        NotificationsDTO newNotificationsDTO = new NotificationsDTO();

        for (Notification n : customerNotifications) {
            NotificationsDTO.NotificationDTO singleNotificationDTO = newNotificationsDTO.new NotificationDTO(n.yaz, n.loanName, n.amount, n.DateTime);
            newNotificationsDTO.notifications.add(singleNotificationDTO);
        }

        return newNotificationsDTO;
    }

    @Override
    public ArrayList<LoanDTO> getLoansByCustomerNameAsBorrower(String customerName) {
        ArrayList<LoanDTO> loansByCustomerAsBorrower = new ArrayList<>();
        for (Loan l : loans) {
            if (l.getBorrowerName().equals(customerName)) {
                LoanDTO curr = createLoanDTO(l);
                loansByCustomerAsBorrower.add(curr);
            }
        }

        return loansByCustomerAsBorrower;
    }

    @Override
    public ArrayList<LoanDTO> getLoansByCustomerNameAsLender(String customerName) {
        ArrayList<LoanDTO> loansByCustomerAsBorrower = new ArrayList<>();

        for (Loan l : loans) {
            if (isCustomerIsLenderInLoan(l, customerName)) {
                LoanDTO curr = createLoanDTO(l);
                loansByCustomerAsBorrower.add(curr);
            }
        }

        return loansByCustomerAsBorrower;
    }

    @Override
    public void setScrambleController(UIController controller) {
        this.scrambleController = controller;
    }

    @Override
    public void assignLoansToLenderWithTask(LoanPlacingDTO loanPlacingDTO, Consumer<Integer> numberOfLoansAssigned) {
        //Consumers wiring
        Consumer<Integer> numberOfLoansAssignedToLenderConsumer = number -> {
            this.numberOfLoansAssignedInSinglePlacingAlgorithmRun = number;
            numberOfLoansAssigned.accept(number);
        };

        currentRunningTask = new LoanPlacingAsTask(numberOfLoansAssignedToLenderConsumer, loanPlacingDTO, this.loans, this, getCurrYaz());
        scrambleController.bindTaskToUIComponents(currentRunningTask);
        new Thread(currentRunningTask).start();
    }

    @Override
    public void payToLender(LoanDTO.LenderDetailsDTO lenderDTO, LoanDTO loanDTO, int yaz) throws Exception{
        try {
            Customer lender = name2customer.get(lenderDTO.getLenderName());
            Customer borrower = name2customer.get(loanDTO.getCustomerName());
            Loan loan = getLoanByName(loanDTO.getLoanName());
            LoanPaymentsData.Payment payment = loan.pollPaymentForSpecificYaz(yaz);

            makeLoanPaymentFromBorrowerToLender(loan, payment, borrower, lender);
            if (payment.getBothPartsOfAmountToPay() == payment.getBothPartsOfPaymentThatWasPaid()) {
                double loanInterestPaid = loan.getInterestPaid();
                loan.setInterestPaid(loanInterestPaid + payment.getInterestPartThatWasPaid());
                payment.setPaymentType(LoanPaymentsData.PaymentType.PAID);
                payment.setActualPaymentYaz(getCurrYaz());
                loan.setAmountPaid(payment.getLoanPartOfThePayment());
                loan.setInterestPaid(payment.getInterestPartOfThePayment());
                loan.setDebt(loan.getDebt() - payment.getBothPartsOfPaymentThatWasPaid());
            }
            //else, the payment was not fully paid, so its status is still UNPAID
            loan.addNewPayment(payment);
            changeLoanStatusIfNeeded(loan);
        }

        catch (Exception e) {
            throw new Exception("There was a problem while trying to pay to lender");
        }
    }

    @Override
    public void payToAllLendersForCurrentYaz(LoanDTO loanDTO, int yaz) throws Exception {
        for (LoanDTO.LenderDetailsDTO lenderDetailsDTO : loanDTO.getLenderDTOS())
        {
            try {
                payToLender(lenderDetailsDTO, loanDTO, yaz);
            }
            catch (Exception ex) {
                throw ex;
            }
        }
    }

    @Override
    public void closeLoan(LoanDTO loanDTO, int yaz) throws Exception{
        Loan loan = getLoanByName(loanDTO.getLoanName());
        closePaymentsByStatus(loan, yaz, LoanPaymentsData.PaymentType.UNPAID);
        closePaymentsByStatus(loan, yaz, LoanPaymentsData.PaymentType.EXPIRED);
        loan.setDebt(0);
        loan.setLoanStatus(Loan.LoanStatus.FINISHED, yaz);
        loan.setFinishYaz(yaz);
    }

    private void closePaymentsByStatus(Loan loan, int yaz, LoanPaymentsData.PaymentType paymentType){
        try{
            PaymentsDB paymentsDb = (PaymentsDB)loan.getPayments(paymentType);
            Customer borrower = name2customer.get(loan.getBorrowerName());
            List<Integer> paymentYazs = new ArrayList<>();

            for (LoanPaymentsData.Payment payment: paymentsDb.getPayments().values()){
                closePayment(payment,borrower, loan);
                paymentYazs.add(payment.getScheduledYaz());
            }

            //updating the payments to PAID
            for (Integer currentYaz:paymentYazs) {
                LoanPaymentsData.Payment payment = loan.pollPaymentForSpecificYaz(currentYaz);
                payment.setPaymentType(LoanPaymentsData.PaymentType.PAID);
                loan.addNewPayment(payment);
            }
        }

        catch (Exception e){

        }
    }

    @Override
    public void payDebt(Double amount, LoanDTO loanDTO, int yaz) throws Exception{
        Customer borrower = name2customer.get(loanDTO.getCustomerName());
        Loan loan = getLoanByName(loanDTO.getLoanName());

        if(amount >= borrower.getAccount().getBalance()){
            throw new Exception("The amount is greater than borrower's balance");
        }
        loan.setDebt(loan.getDebt() - amount);
        moveMoneyBetweenAccounts(borrower.getAccount(), loan.getLoanAccount(), amount);
        splitLoanMoneyToLenders(loan);

        if(loan.getDebt() == 0){
            loan.setFinishYaz(yaz);
            loan.setLoanStatus(Loan.LoanStatus.FINISHED, yaz);
            loan.setAmountPaid(loan.getInitialAmount());
            loan.setInterestPaid(loan.getTotalInterestForLoan());


        }
    }

    @Override
    public boolean hasBorrowerEnoughFundsToPayAmount(Customer customerDTO, double amount){
        Customer borrower = name2customer.get(customerDTO.getName());
        double funds = borrower.getAccount().getBalance();

        return funds >= amount;
    }

    public void addNewCustomer(String name) throws Exception {
        if (name2customer.containsKey(name)) {
            throw new Exception("Customer's name already exists!");
        }
        else {
            name2customer.values().add(new Customer(name, 0));
        }
    } //TODO: new, maybe should be a servlet?

    @Override
    public void createNewLoan(String customerName, String loanName, String category, int amount,
                              int totalYazToPay, int paymentsRate, int interestPerPayment) {
        Loan loan = new Loan(loanName, customerName, amount, totalYazToPay, paymentsRate, interestPerPayment, category, getCurrYaz());
        status2loan.put(Loan.LoanStatus.NEW, loan);
        //TODO: loanId2loan.put()??

        name2customer.get(customerName).addLoanAsLender(loan);
    }
}