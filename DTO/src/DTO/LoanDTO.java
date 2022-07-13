package DTO;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

public class LoanDTO {

    public class PaymentDTO
    {
        private final SimpleIntegerProperty originalYazToPay;
        private final SimpleIntegerProperty actualPaymentYaz;
        private final SimpleDoubleProperty loanPayment;
        private final SimpleDoubleProperty interestPayment;
        private final SimpleStringProperty paymentType;
        private SimpleDoubleProperty totalPayment;

        public PaymentDTO(int originalYaz, double loanPayment, double interestPayment, int actualYaz, String type) {
            this.originalYazToPay = new SimpleIntegerProperty(originalYaz);
            this.actualPaymentYaz = new SimpleIntegerProperty(actualYaz);
            this.loanPayment = new SimpleDoubleProperty(loanPayment);
            this.interestPayment = new SimpleDoubleProperty(interestPayment);
            this.paymentType = new SimpleStringProperty(type);
            this.totalPayment = new SimpleDoubleProperty();

            setTotalPayment();
        }

        public int getOriginalYazToPay() { return originalYazToPay.get(); }
        public int getActualPaymentYaz() { return actualPaymentYaz.get(); }
        public double getLoanPayment() { return loanPayment.get(); }
        public double getInterestPayment() { return interestPayment.get(); }
        public double getTotalPayment() { return totalPayment.get(); }

        public SimpleIntegerProperty getOriginalYazToPayProperty() {return originalYazToPay;}
        public SimpleIntegerProperty getActualPaymentYazProperty() {return actualPaymentYaz;}
        public SimpleDoubleProperty getLoanPaymentProperty() {return loanPayment;}
        public SimpleDoubleProperty getInterestPaymentProperty() {return interestPayment;}
        public SimpleStringProperty getPaymentTypeProperty() {return paymentType;}
        public SimpleDoubleProperty getTotalPaymentProperty() {return totalPayment;}

        private void setTotalPayment() {
            totalPayment.set(loanPayment.doubleValue() + interestPayment.doubleValue());
        }

    }

    public static class LenderDetailsDTO {

        public SimpleStringProperty lenderName;
        public SimpleDoubleProperty lendersInvestAmount;

        public LenderDetailsDTO(String lender, double amount)
        {
            lenderName = new SimpleStringProperty(lender);
            lendersInvestAmount = new SimpleDoubleProperty(amount);
        }

        public String getLenderName() {
            return lenderName.get();
        }

        public SimpleStringProperty getLenderNameProperty(){
            return lenderName;
        }

        public SimpleDoubleProperty getLendersInvestAmountProperty(){
            return this.lendersInvestAmount;
        }

        public SimpleStringProperty lenderNameProperty() {
            return lenderName;
        }

        public double getLendersInvestAmount() {
            return lendersInvestAmount.get();
        }

        public SimpleDoubleProperty lendersInvestAmountProperty() {
            return lendersInvestAmount;
        }
    }

    private final SimpleStringProperty loanName;
    private final SimpleStringProperty customerName;
    private final SimpleIntegerProperty initialAmount;
    private final SimpleIntegerProperty maxYazToPay;
    private final SimpleDoubleProperty interestPerPayment;
    private final SimpleDoubleProperty totalInterest;
    private final SimpleIntegerProperty yazPerPayment;
    private final SimpleStringProperty category;
    private final SimpleStringProperty status;
    private final SimpleDoubleProperty debt;
    private final SimpleDoubleProperty paidInterest;
    private final SimpleDoubleProperty paidLoan;
    private final SimpleDoubleProperty amountRaised;
    private final SortedMap<Integer, LoanDTO.PaymentDTO> unpaidPayments;
    private final SortedMap<Integer, LoanDTO.PaymentDTO> paidPayments;

    //pending info
    private final ArrayList<LoanDTO.LenderDetailsDTO> lendersNameAndAmount;
    private SimpleDoubleProperty totalMoneyRaised;

    //active info
    private SimpleIntegerProperty activationYaz;
    private SimpleIntegerProperty nextPaymentYaz;

    //finish info
    private SimpleIntegerProperty finishYaz;

    public LoanDTO(String loanName, String custName, int initialAmount, int totalYaz, double interestPerPayment, double totalInterest, int yazPerPayment,
                      String status, String category, double paidInterest, double paidLoan, double debt, double amountRaised)
    {
        this.totalMoneyRaised = new SimpleDoubleProperty();
        this.activationYaz = new SimpleIntegerProperty();
        this.nextPaymentYaz = new SimpleIntegerProperty();
        this.finishYaz = new SimpleIntegerProperty();
        this.loanName = new SimpleStringProperty(loanName);
        this.customerName = new SimpleStringProperty(custName);
        this.initialAmount = new SimpleIntegerProperty(initialAmount);
        this.maxYazToPay = new SimpleIntegerProperty(totalYaz);
        this.interestPerPayment = new SimpleDoubleProperty(interestPerPayment);
        this.totalInterest = new SimpleDoubleProperty(totalInterest);
        this.yazPerPayment = new SimpleIntegerProperty(yazPerPayment);
        this.category = new SimpleStringProperty(category);
        this.status = new SimpleStringProperty(status);
        this.paidInterest = new SimpleDoubleProperty(paidInterest);
        this.paidLoan = new SimpleDoubleProperty(paidLoan);
        this.debt = new SimpleDoubleProperty(debt);
        this.amountRaised = new SimpleDoubleProperty(amountRaised);

        unpaidPayments = new TreeMap<>();
        paidPayments = new TreeMap<>();
        lendersNameAndAmount = new ArrayList<>();
    }

    //getters
    public String getLoanName() {
        return loanName.get();
    }
    public SimpleStringProperty getLoanNameProperty() { return loanName; }

    public String getCustomerName() {
        return customerName.get();
    }
    public SimpleStringProperty getCustomerNameProperty() { return customerName; }

    public int getInitialAmount() {
        return initialAmount.get();
    }
    public SimpleIntegerProperty getInitialAmountProperty() {
        return initialAmount;
    }

    public int getMaxYazToPay() {
        return maxYazToPay.get();
    }
    public SimpleIntegerProperty getMaxYazToPayProperty() {
        return maxYazToPay;
    }

    public double getInterestPerPayment() {
        return interestPerPayment.get();
    }
    public SimpleDoubleProperty getInterestPerPaymentProperty() {
        return interestPerPayment;
    }

    public double getTotalInterest() {
        return totalInterest.get();
    }
    public SimpleDoubleProperty getTotalInterestProperty() {
        return totalInterest;
    }

    public int getYazPerPayment() {
        return yazPerPayment.get();
    }
    public SimpleIntegerProperty getYazPerPaymentProperty() {
        return yazPerPayment;
    }

    public String getCategory() {
        return category.get();
    }
    public SimpleStringProperty getCategoryProperty() {
        return category;
    }

    public String getStatus() { return status.get(); }
    public SimpleStringProperty getStatusProperty() { return status; }

    public double getDebt() { return debt.get(); }
    public SimpleDoubleProperty getDebtProperty() { return debt; }

    public double getPaidLoan() { return paidLoan.get(); }
    public SimpleDoubleProperty getPaidLoanProperty() { return paidLoan; }

    public double getPaidInterest() { return paidInterest.get(); }
    public SimpleDoubleProperty getPaidInterestProperty() { return paidInterest; }

    public int getActivationYAZ() { return activationYaz.get(); }
    public SimpleIntegerProperty getActivationYAZProperty() { return activationYaz; }

    public int getNextPaymentYAZ() { return nextPaymentYaz.get(); }
    public SimpleIntegerProperty getNextPaymentYAZProperty() { return nextPaymentYaz; }

    public double getAmountRaised() { return amountRaised.get(); }
    public SimpleDoubleProperty getAmountRaisedProperty() { return amountRaised; }

    public int getFinishYAZ() { return finishYaz.get(); }
    public SimpleIntegerProperty getFinishYAZProperty() { return finishYaz; }


    public ArrayList<LoanDTO.LenderDetailsDTO> getLenderDTOS() { return lendersNameAndAmount; }
    public SortedMap<Integer, LoanDTO.PaymentDTO> getUnpaidPayments() { return unpaidPayments; }
    public SortedMap<Integer, LoanDTO.PaymentDTO> getPaidPayments() { return paidPayments; }

    public void addToLendersNameAndAmount(String lender, double amount)
    {
        LoanDTO.LenderDetailsDTO ld = new LoanDTO.LenderDetailsDTO(lender, amount);
        lendersNameAndAmount.add(ld);
    }

    public void setTotalMoneyRaised(double totalMoneyRaised) {
        this.totalMoneyRaised.set(totalMoneyRaised);
    }
    public void setActivationYaz(int activationYaz) {
        this.activationYaz.set(activationYaz);
    }
    public void setNextPaymentYaz(int nextPaymentYaz) {
        this.nextPaymentYaz.set(nextPaymentYaz);
    }
    public void setFinishYaz(int finishYaz) { this.finishYaz.set(finishYaz); }
//    public void setUnpaidPayments(Engine.PaymentsDB.PaymentsDB payments)
//    {
//        for(LoanPaymentsData.Payment p : payments.getPayments().values())
//        {
//            LoanDTO.PaymentDTO payment = new LoanDTO.PaymentDTO(p.getScheduledYaz(), p.getLoanPartOfThePayment(),
//                    p.getInterestPartOfThePayment(), p.getActualPaymentYaz(), p.getPaymentType().toString());
//
//            unpaidPayments.put(payment.originalYazToPay.getValue(), payment);
//        }
//    }

//    public void setPaidPayments(Engine.PaymentsDB.PaymentsDB payments)
//    {
//        for(LoanPaymentsData.Payment p : payments.getPayments().values())
//        {
//            LoanDTO.PaymentDTO payment = new LoanDTO.PaymentDTO(p.getScheduledYaz(), p.getLoanPartOfThePayment(),
//                    p.getInterestPartOfThePayment(), p.getActualPaymentYaz(), p.getPaymentType().toString());
//
//            paidPayments.put(payment.actualPaymentYaz.getValue(), payment);
//        }
//    }

}