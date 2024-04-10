package org.example.diplommain;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DataModel {

    private IntegerProperty id;
    private DoubleProperty num;

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


    public IntegerProperty getIdProperty() {
        return id;
    }

    public DoubleProperty getNumProperty() {
        return num;
    }


}