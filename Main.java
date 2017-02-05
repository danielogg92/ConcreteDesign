package com.company;

public class Main {

    public static void main(String[] args) {
        double s1bv = 1000;
        double s1d = 165;
        int s1fc = 40;
        double s1ast = 667;
        double s1nAxial = 0;

        BeamShearAs s1 = new BeamShearAs(s1bv,s1d,s1fc,s1ast,s1nAxial);
        System.out.println(s1.shear());
        s1.printShear();

        double c1xDim = 250;
        double c1yDim = 650;
        int c1bx = 2;
        int c1by = 4;
        int c1bd = 25;
        int c1fy = 500;
        int c1fc = 50;
        int c1cover = 50;

        Column c1 = new Column(c1xDim,c1yDim,c1bx,c1by,c1bd,c1fy,c1fc,c1cover);
        System.out.println(c1.columnTension());
        c1.printColTension();
        System.out.println(c1.columnSquash());
        c1.printColSquash();
        c1.printColReo();
    }
}
