package DTO;

import java.util.ArrayList;
import java.util.List;

public class LoanCategorisDTO {
    public ArrayList<String> loanCategories;

    public LoanCategorisDTO(List<String> categorisList){
        loanCategories = new ArrayList<>();
        loanCategories.addAll(categorisList);
    }
}
