package com.company;

import static com.company.Column.LOG_TAG;

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
        double c1BetaD = 0.85;
        double c1EffectiveLength = 4150;

        Column c1 = new Column(c1xDim,c1yDim,c1bx,c1by,c1bd,c1fs,c1fc,c1cover,c1BetaD,c1EffectiveLength);
        c1.printColTension();
        c1.printColSquash();
        c1.printColReo();
        c1.printColMoment();
//        double c1EffectiveLength = 4150.0;
//        double c1BetaD = 0.85;
        double c1DeadLoad = 1.0;
        double c1LiveLoad = (c1DeadLoad-c1BetaD)/c1BetaD;
        double c1Moment1 = -1.0;
        double c1Moment2 = 1.0;
        double roundedNc = Math.round(10*c1.columnCapacitySolver())/10.0;
//        System.out.println("Column capacity = " + roundedNuc + "kN");
//        System.out.println("");
        c1.printColCapacity();
        System.out.println("");

        double c2xDim = 650;
        double c2yDim = 250;
        int c2bx = 4;
        int c2by = 2;
        int c2bd = c1bd;
        int c2fs = c1fs;
        int c2fc = c1fc;
        int c2cover = c1cover;
        double c2BetaD = c1BetaD;
        double c2EffectiveLength = c1EffectiveLength;

        Column c2 = new Column(c2xDim,c2yDim,c2bx,c2by,c2bd,c2fs,c2fc,c2cover,c2BetaD,c2EffectiveLength);
        c2.printColTension();
        c2.printColSquash();
        c2.printColReo();
        c2.printColMoment();
        double c2DeadLoad = 1.0;
        double c2LiveLoad = (c2DeadLoad-c2BetaD)/c2BetaD;
        double c2Moment1 = -1.0;
        double c2Moment2 = 1.0;
        c2.printColCapacity();
        System.out.println("");
        c1.columnCapacitySolverWithCalcs();
        c2.columnCapacitySolverWithCalcs();
        System.out.println("");
        System.out.println(LOG_TAG);
    }
}
