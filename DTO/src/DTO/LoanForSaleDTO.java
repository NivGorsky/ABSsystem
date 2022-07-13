package DTO;

public class LoanForSaleDTO {

    final private String buyerName;
    final private String sellerName;
    final private String loanName;

    LoanForSaleDTO(String buyerName, String sellerName, String loanName) {
        this.buyerName = buyerName;
        this.sellerName = sellerName;
        this.loanName = loanName;
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
}
