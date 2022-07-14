package DTO;

public class LoanForSaleDTO {

    final private String buyerName;
    final private String sellerName;
    final private String loanName;
    final private double price;

    public LoanForSaleDTO(String buyerName, String sellerName, String loanName, double price) {
        this.buyerName = buyerName;
        this.sellerName = sellerName;
        this.loanName = loanName;
        this.price = price;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getLoanName() {
        return loanName;
    }

    public double getPrice() {
        return price;
    }
}
