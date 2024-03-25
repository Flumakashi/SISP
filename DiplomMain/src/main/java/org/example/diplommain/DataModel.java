package org.example.diplommain;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel {

    private ObservableList<DataEntry> dataList = FXCollections.observableArrayList();

    public ObservableList<DataEntry> getDataList() {
        return dataList;
    }

    public void addDataEntry(DataEntry entry) {
        dataList.add(entry);
    }
    public void clearDataList(){
        dataList.clear();
    }

    public static class DataEntry{
        private IntegerProperty id;
        private DoubleProperty num;

        public DataEntry(int id, double num) {
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
}