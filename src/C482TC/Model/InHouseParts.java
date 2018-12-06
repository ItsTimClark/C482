package C482TC.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class InHouseParts extends Parts {

    private final IntegerProperty partID;

    public InHouseParts(){
        super();
        partID = new SimpleIntegerProperty();
    }

    public int getPartID(){
        return this.partID.get();
    }

    public void setPartID(int partID){
        this.partID.set(partID);
    }
}
