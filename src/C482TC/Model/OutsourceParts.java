package C482TC.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OutsourceParts extends Parts {

    private final StringProperty outsourceCompanyName;

    public OutsourceParts(){
        super();
        outsourceCompanyName = new SimpleStringProperty();
    }

    public String getOutsourceCompanyName(){
        return this.outsourceCompanyName.get();
    }

    public void setOutsourceCompanyName(String outsourceCompanyName){
        this.outsourceCompanyName.set(outsourceCompanyName);
    }
}
