package org.example.diplommain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class DataModel {
    private final IntegerProperty id;
    private final IntegerProperty num;

    public DataModel(int id, int num) {
        this.id = new SimpleIntegerProperty(id);
        this.num = new SimpleIntegerProperty(num);
    }

    public int getId() {
        return id.get();
    }

    public int getNum() {
        return num.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty numProperty() {
        return num;
    }

}