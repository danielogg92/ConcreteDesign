package com.company;

public class Main {

    public static void main(String[] args) {
        double s1bv = 1000;
        double s1d = 165;
        int s1fc = 40;
        double s1ast = 667;
        double s1nAxial = 0;

        BeamShearAs s1 = new BeamShearAs(s1bv,s1d,s1fc,s1ast,s1nAxial);
//        s1.printShear();

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
        //c1.printColSectCapacity();
        //System.out.println("");
        c1.printColMoment();
        System.out.println("");
        double c1EffectiveLength = 4150.0;
        double c1BetaD = 0.85;
        double c1DeadLoad = 1.0;
        double c1LiveLoad = (c1DeadLoad-c1BetaD)/c1BetaD;
        //System.out.println(c1.concElasticCritBuckling(c1EffectiveLength,c1DeadLoad,c1LiveLoad));
        double c1Moment1 = -1.0;
        double c1Moment2 = 1.0;
        //System.out.println(c1.columnKm(c1Moment1,c1Moment2));
        double roundedNuc = Math.round(10*c1.columnCapacitySolver(c1BetaD,c1EffectiveLength))/10.0;
        System.out.println("Column capacity = " + roundedNuc + "kN");
    }
}
