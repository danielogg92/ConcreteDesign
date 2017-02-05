package com.company;

/**
 * Created by danie on 04/02/2017.
 */
public class Column {
    double xDim;
    double yDim;
    int bx;
    int by;
    int bd;
    int fy;
    int fc;
    int cover;

    public Column(double xDim, double yDim, int bx, int by, int bd, int fy, int fc, int cover){
        this.xDim = xDim;
        this.yDim = yDim;
        this.bx = bx;
        this.by = by;
        this.bd = bd;
        this.fy = fy;
        this.fc = fc;
        this.cover = cover;
    }

    public double columnTension(){
        int nosBars = 2*bx + 2*by - 4;
        double colNut = -1.0 * nosBars * Math.PI * Math.pow(bd,2) / 4 * fy / 1000;
        return colNut;
    }

    public double columnSquash(){
        int nosBars = 2*bx + 2*by - 4;
        double alpha1=Math.max(0.72,Math.min(0.85,1.0-0.003*fc));
        double aGross=xDim*yDim;
        double aSteel=nosBars*Math.PI*Math.pow(bd,2.0)/4.0;
        double aConc=aGross-aSteel;
        double colSquash=(alpha1*fc*aConc+aSteel*fy)/1000.0;
        return colSquash;
    }

    public double[][] columnReo(){
        double[][] colReo = new double[by][3];
        double firstBar;
        double lastBar;
        double barSpacing;

        if (by==1) {
            firstBar = yDim / 2.0;
            lastBar = yDim / 2.0;
            barSpacing = 0;
        } else {
            firstBar=cover+bd/2.0;
            lastBar=yDim-cover-bd/2.0;
            barSpacing=(lastBar-firstBar)/(by-1.0);
        }

        for(int i=0;i<by;i++){
            if(by==1){
                colReo[0][0] = 1;
                colReo[0][1] = firstBar+i*barSpacing;
                colReo[0][2] = bx*Math.PI*Math.pow(bd,2)/4.0;
            } else if (by!=1 && (i==0 || i==(by-1))) {
                colReo[i][0] = bx;
                colReo[i][1] = firstBar+i*barSpacing;
                colReo[i][2] = bx*Math.PI*Math.pow(bd,2)/4.0;
            } else if (i!=0 && i!=(by-1) && bx>1){
                colReo[i][0] = 2;
                colReo[i][1] = firstBar+i*barSpacing;
                colReo[i][2] = 2*Math.PI*Math.pow(bd,2)/4.0;
            }
        }
        return colReo;
    }

    public void printColTension(){
        double roundedNut = Math.round(this.columnTension()*10)/10.0;
        System.out.println("Column Tension Capacity = " + roundedNut + "kN");
    }

    public void printColSquash(){
        double roundedNuc = Math.round(this.columnSquash()*10)/10.0;
        System.out.println("Column Squash Capacity = " + roundedNuc + "kN");
    }

    public void printColReo(){
        double[][] colReoData = this.columnReo();
        int reoRow;
        int nosBars;
        double reoYDim;
        double reoArea;
        for (int i=0;i<colReoData.length;i++){
            reoRow = i + 1;
            nosBars = (int) colReoData[i][0];
            reoYDim = Math.round(10*colReoData[i][1])/10.0;
            reoArea = Math.round(10*colReoData[i][2])/10.0;
            System.out.println("Reo row " + reoRow + ": " + nosBars +
                    "/" + bd + "mm Dia bars: depth " + reoYDim + "mm: As " +
                    reoArea + "sq.mm");
        }
    }
}
