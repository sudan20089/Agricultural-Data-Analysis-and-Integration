/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.neuroph.core.NeuralNetwork;

/**
 *
 * @author kishan
 */
public class network {
    // create new perceptron network 
//8990	2030.03337	183.63286	209.083333	16.3583333

    NeuralNetwork network = NeuralNetwork.load("/home/kishan/NetBeansProjects/major/Neural Networks/paddy.nnet");

    private double normalize_area(double area) {
        return (area - 2750) / (38500 - 2750);
    }

    private double normalize_fertilizer(double f) {
        return (f - 2.9) / (151.25 - 2.9);
    }

    private double normalize_rain(double f) {
        return (f - 25.40231) / (289.05 - 25.40231);
    }

    private double normalize_temp(double f) {
        return (f - 8.95) / (229.4475 - 8.95);
    }

    private double normalize_yield(double f) {
        return ((f- 1173.33) / (6420 - 1173.33) );
    }

    private double final_value(double f) {
        return ((f * (6420 - 1173.33)) + 1173.33);
    }

    private double calculate(double... input) {
        network.setInput(input);
        network.calculate();
//        network.learn('paddy.tset');
        double[] output = network.getOutput();
        Double answer = final_value(output[0]);
        return answer;
//        System.out.println("The required yield is "+answer+" mton per hectare");
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        String csvFile = "paddy.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        double area;
        double fertilizer;
        double rainfall;
        double temp;
        double yield;
        double rmse;
        double mape;
        double mae;
        network nnet = new network();
        List<Double> original = new ArrayList<Double>();
        List<Double> predicted = new ArrayList<Double>();

        try {
            FileOutputStream fos = new FileOutputStream("output.csv");
            DataOutputStream dos = new DataOutputStream(fos);
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                area = nnet.normalize_area(Double.parseDouble(data[3]));
                fertilizer = nnet.normalize_fertilizer(Double.parseDouble(data[5]));
                rainfall = nnet.normalize_rain(Double.parseDouble(data[6]));
                temp = nnet.normalize_temp(Double.parseDouble(data[7]));
                yield = nnet.calculate(area, fertilizer, rainfall, temp);
//                double yield_new = nnet.final_value(yield);
//                System.out.println(area);
                original.add(Double.parseDouble(data[4]));
                predicted.add(yield);
                String writeToFile = data[4] + "," + yield + "\n";
                dos.write(writeToFile.getBytes());

            }
            dos.flush();
            dos.close();
            fos.close();
            rmse = nnet.RMSE(original, predicted);
            mae = nnet.MAE(original, predicted);
            mape = nnet.MAPE(original, predicted);
            System.out.println("The root mean sqaure error is "+rmse);
            System.out.println("The mean absolute error is "+mae);
            System.out.println("The mean absolute percentage error is "+mape);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Done loading");
    }

    private double[] convert_to_float(List<Double> list) {
        double[] floatArray = new double[list.size()];
        int j = 0;

        for (Double f : list) {
            floatArray[j++] = (double) (f != null ? f : Double.NaN); // Or whatever default you want.
//                System.out.println(f);

        }
        return floatArray;

    }

    public double RMSE(List<Double> original, List<Double> predicted) {
        int length = original.size();
        double[] original_array = convert_to_float(original);
        double[] predicted_array = convert_to_float(predicted);
        double sum = 0;
        for (int i = 0; i < length; i++) {
            sum = sum + Math.pow((original_array[i] - predicted_array[i]), 2);
        }
        double rmse = Math.sqrt(sum) / length;
        return rmse;
    }

    public double MAE(List<Double> original, List<Double> predicted) {
        int length = original.size();
        double[] original_array = convert_to_float(original);
        double[] predicted_array = convert_to_float(predicted);
        double sum = 0;
        for (int i = 0; i < length; i++) {
            sum = sum + Math.abs(original_array[i] - predicted_array[i]);
        }
        double mae = sum / length;
        return mae;
    }

    public double MAPE(List<Double> original, List<Double> predicted) {
        int length = original.size();
        double[] original_array = convert_to_float(original);
        double[] predicted_array = convert_to_float(predicted);
        double sum = 0;
        for (int i = 0; i < length; i++) {
            sum = sum + (Math.abs(original_array[i] - predicted_array[i])) / original_array[i];
        }
        double mape = sum / length;
        return mape;
    }
}
