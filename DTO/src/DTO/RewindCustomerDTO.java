package DTO;

public class RewindCustomerDTO {
    CustomerDTO customerDTO;
    int yaz;

    public RewindCustomerDTO(CustomerDTO i_customerDto, int i_yaz){
        this.customerDTO = i_customerDto;
        this.yaz = i_yaz;
    }
}
