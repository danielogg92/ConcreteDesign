package com.company;

public class Main {

    public static void main(String[] args) {
        double s1bv = 1000;
        double s1d = 165;
        int s1fc = 40;
        double s1ast = 667;
        double s1nAxial = 0;

        BeamShearAs s1 = new BeamShearAs(s1bv,s1d,s1fc,s1ast,s1nAxial);

        double c1xDim = 250;
        double c1yDim = 650;
        int c1bx = 2;
        int c1by = 4;
        int c1bd = 25;
        int c1fs = 500;
        int c1fc = 50;
        int c1cover = 50;

        Column c1 = new Column(c1xDim,c1yDim,c1bx,c1by,c1bd,c1fs,c1fc,c1cover);
        c1.printColTension();
        c1.printColSquash();
        System.out.println("");
        c1.printColReo();
        System.out.println("");
        c1.printColMoment();
        System.out.println("");
        double c1EffectiveLength = 4150.0;
        double c1BetaD = 0.85;
        double c1DeadLoad = 1.0;
        double c1LiveLoad = (c1DeadLoad-c1BetaD)/c1BetaD;
        double c1Moment1 = -1.0;
        double c1Moment2 = 1.0;
        double roundedNuc = Math.round(10*c1.columnCapacitySolver(c1BetaD,c1EffectiveLength))/10.0;
        System.out.println("Column capacity = " + roundedNuc + "kN");
        System.out.println("");

        double c2xDim = 650;
        double c2yDim = 250;
        int c2bx = 4;
        int c2by = 2;
        int c2bd = 25;
        int c2fs = 500;
        int c2fc = 50;
        int c2cover = 50;

        Column c2 = new Column(c2xDim,c2yDim,c2bx,c2by,c2bd,c2fs,c2fc,c2cover);
        c2.printColTension();
        c2.printColSquash();
        System.out.println("");
        c2.printColReo();
        System.out.println("");
        c2.printColMoment();
        System.out.println("");
        double c2EffectiveLength = 4150.0;
        double c2BetaD = 0.85;
        double c2DeadLoad = 1.0;
        double c2LiveLoad = (c2DeadLoad-c2BetaD)/c2BetaD;
        double c2Moment1 = -1.0;
        double c2Moment2 = 1.0;
        double c2RoundedNuc = Math.round(10*c2.columnCapacitySolver(c2BetaD,c2EffectiveLength))/10.0;
        System.out.println("Column capacity = " + c2RoundedNuc + "kN");
        System.out.println("");
    }
}
