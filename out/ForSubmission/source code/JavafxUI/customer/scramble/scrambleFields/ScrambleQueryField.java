package customer.scramble.scrambleFields;

public class ScrambleQueryField {
    public String type;
    public String isMandatory;
    public String name;

    public ScrambleQueryField(String type, String isMandatory, String name){
        this.type = type;
        this.isMandatory = isMandatory;
        this.name = name;
    }
}
