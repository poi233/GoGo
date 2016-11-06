package com.example.cc.gogo.LinearRegression;

public class Main {

    public static void main(String[] args) throws Exception{
        Matrix N = new Matrix(new double[][]{});
        calc_mile(N);
    }

    public static void calc_mile(Matrix N) throws Exception
    {
        final Matrix X = new Matrix(new double [][]{
                {22,177,68,1,10},
                {19,172,120,2,2},
                {20,180,70,2,2},
                {20,178,52,2,1},
                {18,164,55,2,2},
                {21,175,62,2,1},
                {20,175,57,2,1},
                {20,163,63,2,1},
                {20,160,62.5,1,0.8}});
        final Matrix Y = new Matrix(new double[][]{{5},{2},{1},{0.5},{0.8},{0.5},{0.5},{0.5},{0.4}});
        final MultiLinear ml = new MultiLinear(X,Y);
        final Matrix beta = ml.calculate();

        boolean bias = true;
        if(bias)
        {
            double predictedY = beta.getValueAt(0,0);
            for(int j=1;j<beta.getNrows();j++)
            {
                predictedY += beta.getValueAt(j,0)*N.getValueAt(0,j-1);
            }
            System.out.println("predicted value is "+predictedY);
        }
        else
        {
            double predictedY = 0.0;
            for(int j=0;j<beta.getNrows();j++)
            {
                predictedY += beta.getValueAt(j,0)*N.getValueAt(0,j);
            }
            System.out.println("predicted value is "+predictedY);
        }

    }

    /*
    public static void test_mile() throws Exception
    {
        System.out.print("+++++++++++++++++++++ TEST MILE ++++++++++++++++++++");
        final Matrix X = new Matrix(new double [][]{
                {22,177,68,1,10},
                {19,172,120,2,2},
                {20,180,70,2,2},
                {20,178,52,2,1},
                {18,164,55,2,2},
                {21,175,62,2,1},
                {20,175,57,2,1},
                {20,163,63,2,1},
                {20,160,62.5,1,0.8}});
        final Matrix Y = new Matrix(new double[][]{{5},{2},{1},{0.5},{0.8},{0.5},{0.5},{0.5},{0.4}});
        final MultiLinear ml = new MultiLinear(X,Y);
        final Matrix beta = ml.calculate();

        printY(Y,X,beta,true);
    }

    public static void test_bmi() throws Exception {
        System.out.println("+++++++++++++++++++++ TEST BMI ++++++++++++++++++++");
        final Matrix X = new Matrix(new double[][]{{4,0,1},{7,1,1},{6,1,0},{2,0,0},{3,0,1}});

        final Matrix Y = new Matrix(new double[][]{{27},{29},{23},{20},{21}});
        final MultiLinear ml = new MultiLinear(X, Y);
        final Matrix beta = ml.calculate();

        printY(Y, X, beta, true);


    }

    public static void test_withbias() throws Exception {
        System.out.println("+++++++++++++++++++++ TEST 2 ++++++++++++++++++++");
        final Matrix X = new Matrix(new double[][]{{1,1},{2,8},{3,27},{4,64},{5,125}});

        final Matrix Y = new Matrix(new double[][]{{1},{2},{2},{2},{3}});
        final MultiLinear ml = new MultiLinear(X, Y);
        final Matrix beta = ml.calculate();

        printY(Y, X, beta, true);
    }

    public static void test_nobias() throws Exception {
        System.out.println("+++++++++++++++++++++ TEST 3 ++++++++++++++++++++");
        final Matrix X = new Matrix(new double[][]{{1,1},{2,8},{3,27},{4,64},{5,125}});

        final Matrix Y = new Matrix(new double[][]{{1},{2},{2},{2},{3}});
        final MultiLinear ml = new MultiLinear(X, Y, false);
        final Matrix beta = ml.calculate();

        printY(Y, X, beta, true);
    }

    public static void test3() throws Exception {
        System.out.println("+++++++++++++++++++++ TEST 4  - classification ++++++++++++++++++++");
        final Matrix X = new Matrix(new double[][]{{1,1},{1,2},{1,3},{1,4},{2,1},{2,2},{2,3},{2,4},{3,1},{3,2},{3,3},{3,4}});

        final Matrix Y = new Matrix(new double[][]{{1},{1},{1},{-1},{1},{1},{-1},{-1},{1},{-1},{-1},{-1}});
        final MultiLinear ml = new MultiLinear(X, Y);
        final Matrix beta = ml.calculate();

        printY(Y, X, beta, true);
    }

    public static void checkDiemnsion_nullX() {
        final Matrix X = null;
        final Matrix Y = new Matrix(5, 1);
        final MultiLinear ml = new MultiLinear(X, Y);
        ml.checkDiemnsion();
    }

    public static void checkDiemnsion_nullY() {
        final Matrix X = new Matrix(5, 3);
        final Matrix Y = null;
        final MultiLinear ml = new MultiLinear(X, Y);
        ml.checkDiemnsion();
    }

    public static void checkDiemnsion_bigX() {
        final Matrix X = new Matrix(5, 6);
        final Matrix Y = new Matrix(5,1);
        final MultiLinear ml = new MultiLinear(X, Y);
        ml.checkDiemnsion();
    }

    public static void checkDiemnsion_wrongY() {
        final Matrix X = new Matrix(5, 3);
        final Matrix Y = new Matrix(4,1);
        final MultiLinear ml = new MultiLinear(X, Y);
        ml.checkDiemnsion();
    }

    public static void printY(final Matrix Y, final Matrix X, final Matrix beta, final boolean bias) {
        System.out.println("observed -> predicted");
        if (bias) {
            for (int i=0;i<Y.getNrows();i++) {
                double predictedY =  beta.getValueAt(0, 0);
                for (int j=1; j< beta.getNrows(); j++) {
                    predictedY += beta.getValueAt(j, 0) * X.getValueAt(i, j-1);
                }
                System.out.println(Y.getValueAt(i, 0) + " -> " + predictedY);
            }
        } else {
            for (int i=0;i<Y.getNrows();i++) {
                double predictedY =  0.0;
                for (int j=0; j< beta.getNrows(); j++) {
                    predictedY += beta.getValueAt(j, 0) * X.getValueAt(i, j);
                }
                System.out.println(Y.getValueAt(i, 0) + " -> " + predictedY);
            }
        }

        System.out.print("\n\n");
        System.out.print("+++++++++++++++++++++++++++++++ PRINT BETA +++++++++++++++++++++++++");

        for(int m=0;m<beta.getNrows();m++)
        {
            System.out.print(beta.getValueAt(m,0));
            System.out.print("\n");
        }
    }
    */
}
