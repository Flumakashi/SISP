package org.example.diplommain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DataModel {
    private final String id;
    private final String num;

    public DataModel(String id, String num) {
        this.id = id;
        this.num = num;
    }

    public String getId(){
        return id;
    }

    public String getNum(){
        return num;
    }

    public StringProperty idProperty(){
        return new SimpleStringProperty(id);
    }

    public StringProperty numProperty(){
        return new SimpleStringProperty(num);
    }
}
