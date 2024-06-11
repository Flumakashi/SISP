package org.example.diplommain;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DataTimeModel {

    private final DoubleProperty passengerExchange;
    private final IntegerProperty id;

    public DataTimeModel(int id, double timenum) {

        this.id = new SimpleIntegerProperty(id);
        this.passengerExchange = new SimpleDoubleProperty(timenum);
    }

    public int getId() {
        return id.get();
    }


    public double getTimeNum(){return passengerExchange.get();}

    public IntegerProperty getIdProperty() {
        return id;
    }
    public DoubleProperty getPassengerExchangeProperty(){
        return passengerExchange;
    }

    public int getTimeId() {
        return id.get();
    }

    public double getPassengerExchange() {
        return passengerExchange.get();
    }



}
