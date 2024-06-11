package org.example.diplommain;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel {

    private final IntegerProperty id;
    private final DoubleProperty distance;
    private final SimpleStringProperty stationName;
    private final IntegerProperty traffic;
    private final IntegerProperty intervalDistance;

    public DataModel(int i) {
        this(i, 0.0, "", 0, 0);
    }

    public DataModel(int id, double distance, String stationName, int traffic, int intervalDistance) {
        this.id = new SimpleIntegerProperty(id);
        this.distance = new SimpleDoubleProperty(distance);
        this.stationName = new SimpleStringProperty(stationName);
        this.traffic = new SimpleIntegerProperty(traffic);
        this.intervalDistance = new SimpleIntegerProperty(intervalDistance);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty getIdProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public double getDistance() {
        return distance.get();
    }

    public DoubleProperty getDistanceProperty() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance.set(distance);
    }

    public String getStationName() {
        return stationName.get();
    }

    public StringProperty stationNameProperty() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName.set(stationName);
    }

    public int getTraffic() {
        return traffic.get();
    }

    public IntegerProperty getTrafficProperty() {
        return traffic;
    }

    public void setTraffic(int traffic) {
        this.traffic.set(traffic);
    }

    public int getIntervalDistance() {
        return intervalDistance.get();
    }

    public IntegerProperty getIntervalDistanceProperty() {
        return intervalDistance;
    }

    public void setIntervalDistance(int intervalDistance) {
        this.intervalDistance.set(intervalDistance);
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "id=" + id.get() +
                ", distance=" + distance.get() +
                ", stationName='" + stationName.get() + '\'' +
                ", traffic=" + traffic.get() +
                ", intervalDistance=" + intervalDistance.get() +
                '}';
    }
}