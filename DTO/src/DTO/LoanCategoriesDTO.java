package DTO;

import java.util.ArrayList;
import java.util.List;

public class LoanCategoriesDTO {
    public ArrayList<String> loanCategories;

    public LoanCategoriesDTO(List<String> categoriesList){
        loanCategories = new ArrayList<>();
        loanCategories.addAll(categoriesList);
    }
}
