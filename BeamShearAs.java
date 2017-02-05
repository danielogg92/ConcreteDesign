package com.company;

/**
 * Created by danie on 04/02/2017.
 */
public class BeamShearAs {
    double bv;
    double d;
    int fc;
    double ast;
    double nAxial;

    public BeamShearAs(double bv, double d, int fc, double ast, double nAxial){
        this.bv = bv;
        this.d = d;
        this.fc = fc;
        this.ast = ast;
        this.nAxial = nAxial;
    }

    public double shear(){
        double vumax = 0.2*fc*bv*d;
        double b1 = Math.max(0.8,1.1*(1.6-d/1000));
        double b2 = 0;
        if (nAxial < 0){
            b2=Math.max(0,1-(nAxial*1000/(3.5*bv*d)));
        } else {
            b2=Math.max(1,1+(nAxial*1000/(14.0*bv*d)));
        }
        double b3 = 1.0;
        double fcv = Math.min(4,Math.pow(fc,1/3));
        double vuc = b1*b2*b3*bv*d*fcv*Math.pow(ast/(bv*d),1.0/3);
        vuc = Math.min(vuc,vumax) / 1000;
        return vuc;
    }

    public void printShear(){
        double roundedVuc = Math.round(this.shear()*10)/10.0;
        System.out.println("Beam Shear Capacity = " + roundedVuc + "kN");
    }
}
