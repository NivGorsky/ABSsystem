package Engine;
import java.util.ArrayList;

public class LoanCategories {

    static ArrayList<String> categories = new ArrayList<>();

    public static void addCategory(java.lang.String category)
    {
        categories.add(category);
    }

    public static ArrayList<String> getCategories() {
        return categories;
    }

    public static void setCategories(ArrayList<String> categories) {
        LoanCategories.categories = categories;
    }

}
