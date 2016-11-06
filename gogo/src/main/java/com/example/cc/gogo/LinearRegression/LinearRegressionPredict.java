package com.example.cc.gogo.LinearRegression;

import android.renderscript.ScriptGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.cc.gogo.util.Constant.dir;


public class LinearRegressionPredict {
    public static void main(String[] arg) {
        try {
            double target[] = new double[]{1,2,3,4};
            calc_mile(target);
            //System.out.println(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double calc_mile(double[] elements) throws Exception {
        Matrix N = new Matrix(new double[][]{elements});
        double data[][] = new double[125][4];
        double result[][] = new double[125][1];
        File file = new File(dir + File.separator + "linearRegression");
        if (file.exists()) {
            System.out.println("文件存在");
        }
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String str;
        int count = 0;
        while ((str = br.readLine()) != null) {
            String[] s = str.split(",");
            for (int i = 0; i < 4; i++) {
                data[count][i] = Double.parseDouble(s[i]);
                // System.out.print(data[count][i]+" ");
            }
            result[count][0] = Double.parseDouble(s[4]);
/*
            System.out.print(result[count][0]);
            System.out.println();
*/
            count++;
        }
        final Matrix X = new Matrix(data);
        final Matrix Y = new Matrix(result);
        final MultiLinear ml = new MultiLinear(X, Y);
        final Matrix beta = ml.calculate();

        boolean bias = true;
        if (bias) {
            double predictedY = beta.getValueAt(0, 0);
            for (int j = 1; j < beta.getNrows(); j++) {
                predictedY += beta.getValueAt(j, 0) * N.getValueAt(0, j - 1);
            }
            //System.out.println("predicted value is " + predictedY);
            return predictedY;
        } else {
            double predictedY = 0.0;
            for (int j = 0; j < beta.getNrows(); j++) {
                predictedY += beta.getValueAt(j, 0) * N.getValueAt(0, j);
            }
           // System.out.println("predicted value is " + predictedY);
            return predictedY;
        }

    }
}
