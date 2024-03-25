package org.example.diplommain;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DataTimeModel {

    private final DoubleProperty timenum;
    private final IntegerProperty id;

    public DataTimeModel(int id, double timenum) {
        this.id = new SimpleIntegerProperty(id);
        this.timenum = new SimpleDoubleProperty(timenum);
    }

    public int getId() {
        return id.get();
    }


    public double getTimeNum(){return timenum.get();}

    public IntegerProperty getIdProperty() {
        return id;
    }
    public DoubleProperty getTimeNumProperty(){
        return timenum;
    }



}
