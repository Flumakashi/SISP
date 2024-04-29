package org.example.diplommain;
import javafx.collections.ObservableList;

import java.util.*;

public class CalculationResult {
    private ObservableList<Double[]> data;
    private double[][] matrix;

    public CalculationResult(ObservableList<Double[]> data, double[][] matrix) {
        this.data = data;
        this.matrix = matrix;
    }

    public ObservableList<Double[]> getData() {
        return data;
    }

    public double[][] getMatrix() {
        return matrix;
    }
}
