package org.example.diplommain;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class DataModel {
    private final IntegerProperty id;
    private final DoubleProperty num;


    public DataModel(int id, double num) {
        this.id = new SimpleIntegerProperty(id);
        this.num = new SimpleDoubleProperty(num);
    }

    public int getId() {
        return id.get();
    }

    public double getNum() {
        return num.get();
    }


    public IntegerProperty idProperty() {
        return id;
    }

    public DoubleProperty numProperty() {
        return num;
    }


}