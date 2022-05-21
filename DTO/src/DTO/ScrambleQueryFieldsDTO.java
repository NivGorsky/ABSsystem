package DTO;

import java.util.ArrayList;

public class ScrambleQueryFieldsDTO {
    public ArrayList<ArrayList<String>> scrambleQueryFields;
    public ArrayList<String> loansCategories;

    public ScrambleQueryFieldsDTO(){
        scrambleQueryFields = new ArrayList<ArrayList<String>>();
        loansCategories = new ArrayList<String>();
    }
}
