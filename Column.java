package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danie on 04/02/2017.
 */
public class Column {

    String colName = "";
    double xDim;
    double yDim;
    int bx;
    int by;
    int mNosBars;
    int bd;
    int mTieSize;
    int mTieSpacing;
    int fs;
    int fc;
    int cover;
    double eC;
    double eS;
    double ES;
    double betaD;
    double effectiveLength;
    double mColumnLength;
    String sectionType = "";
    String bracingType = "";
    String mBarCode = "AU";
    ArrayList<Integer> mBarArray;

    public static final String LOG_TAG = Column.class.getSimpleName();

    public Column(String colName, double xDim, double yDim, int bx, int by, int bd, int fs, int fc, int cover, double betaD,
                  double effectiveLength, double columnLength){
        this.colName = colName;
        this.xDim = xDim;
        this.yDim = yDim;
        this.bx = bx;
        this.by = by;
        mNosBars = 2*(bx+by)-4;
        this.bd = bd;
        this.fs = fs;
        this.fc = fc;
        this.cover = cover;
        this.eC = 0.003;
        this.eS = 0.0025;
        this.ES = 200000;
        this.betaD = betaD;
        this.effectiveLength = effectiveLength;
        mColumnLength = columnLength;
        this.sectionType = "rectangular";
        this.bracingType = "braced";
        mBarArray = this.barDesignation(mBarCode);
        mTieSize = this.findTieSize();
        mTieSpacing = this.tieSpacing();
    }

    public Column(String colName, double xDim, double yDim){
        this.colName = colName;
        this.xDim = xDim;
        this.yDim = yDim;
        this.bx = this.columnBarNumber(xDim);
        this.by = this.columnBarNumber(yDim);
        mNosBars = 2*(bx+by)-4;

        this.fs = 500;
        this.fc = 50;
        this.cover = 50;
        this.eC = 0.003;
        this.eS = 0.0025;
        this.ES = 200000;
        this.betaD = 0.85;
        this.effectiveLength = 0.85 * 3750;
        mColumnLength = effectiveLength / 0.85;
        this.sectionType = "rectangular";
        this.bracingType = "braced";
        mBarArray = this.barDesignation(mBarCode);
        this.bd = this.findBarSize(0.01);
        mTieSize = this.findTieSize();
        mTieSpacing = this.tieSpacing();
    }

    public String setColName(String colName){
        this.colName = colName;
        return this.colName;
    }

    public double setColXDim(double xDim){
        this.xDim = xDim;
        return this.xDim;
    }

    public double setColYDim(double yDim){
        this.yDim = yDim;
        return this.yDim;
    }

    public int setBarsX(int bx){
        this.bx = bx;
        return this.bx;
    }

    public int setBarsY(int by){
        this.by = by;
        return this.by;
    }

    public int setBarsBd(int bd){
        this.bd = bd;
        return this.bd;
    }

    public int setBarFs(int fs){
        this.fs = fs;
        return fs;
    }

    public int setConcFc(int fc){
        this.fc = fc;
        return fc;
    }

    public int setBarCover(int cover){
        this.cover = cover;
        return cover;
    }

    public double setConcEx(double eC){
        this.eC = eC;
        return eC;
    }

    public double setSteelEs(double eS){
        this.eS = eS;
        return eS;
    }

    public double setSteelES(double ES){
        //This ES refers to the Youngs modulus of the steel
        this.ES = ES;
        return ES;
    }

    public double setBetaD(double betaD){
        this.betaD = betaD;
        return betaD;
    }

    public double setEffectiveLength(double effectiveLength){
        this.effectiveLength = effectiveLength;
        return effectiveLength;
    }

    public void setColumnLength(double colLength){
        mColumnLength = colLength;
    }

    public String setSectionType(String sectionType){
        this.sectionType = sectionType;
        return this.sectionType;
    }

    public String setBracingType(String bracingType){
        this.bracingType = bracingType;
        return this.bracingType;
    }

    public String setBarCode(String barCode){
        mBarCode = barCode;
        return mBarCode;
    }

    public int setTieSize(int tieSize){
        mTieSize = tieSize;
        return mTieSize;
    }

    public void setTieSpacing(int tieSpacing){
        mTieSpacing = tieSpacing;
    }

    public double columnTension(){
        int nosBars = 2*bx + 2*by - 4;
        double colNut = -1.0 * nosBars * Math.PI * Math.pow(bd,2) / 4 * fs / 1000;
        return colNut;
    }

    public double columnSquash(){
        int nosBars = 2*bx + 2*by - 4;
        double alpha1=Math.max(0.72,Math.min(0.85,1.0-0.003*fc));
        double aGross=xDim*yDim;
        double aSteel=nosBars*Math.PI*Math.pow(bd,2.0)/4.0;
        double aConc=aGross-aSteel;
        double colSquash=(alpha1*fc*aConc+aSteel*fs)/1000.0;
        return colSquash;
    }

    public double[][] columnReo(){
        double[][] colReo = new double[by][3];
        double firstBar;
        double lastBar;
        double barSpacing;

        if (by==1) {
            firstBar = yDim / 2.0;
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

    public double[][] columnReoRot(){
        double[][] colReoRot = new double[bx][3];
        double firstBar;
        double lastBar;
        double barSpacing;

        if (bx==1) {
            firstBar = xDim / 2.0;
            barSpacing = 0;
        } else {
            firstBar=cover+bd/2.0;
            lastBar=xDim-cover-bd/2.0;
            barSpacing=(lastBar-firstBar)/(bx-1.0);
        }

        for(int i=0;i<bx;i++){
            if(bx==1){
                colReoRot[0][0] = 1;
                colReoRot[0][1] = firstBar+i*barSpacing;
                colReoRot[0][2] = by*Math.PI*Math.pow(bd,2)/4.0;
            } else if (bx!=1 && (i==0 || i==(bx-1))) {
                colReoRot[i][0] = by;
                colReoRot[i][1] = firstBar+i*barSpacing;
                colReoRot[i][2] = by*Math.PI*Math.pow(bd,2)/4.0;
            } else if (i!=0 && i!=(bx-1) && by>1){
                colReoRot[i][0] = 2;
                colReoRot[i][1] = firstBar+i*barSpacing;
                colReoRot[i][2] = 2*Math.PI*Math.pow(bd,2)/4.0;
            }
        }
        return colReoRot;
    }

    public double[] columnSolverKu(double ku){
        double[][] colReo = this.columnReo();
        double d = colReo[colReo.length-1][1];
        double kuD = ku * d;
        double alpha2 = Math.max(0.67,Math.min(0.85,1.0-0.003*fc));
        double gamma = Math.max(0.67,Math.min(0.85,1.05-0.007*fc));

        double sectAxialForce = 0.0;
        double sectMoment = 0.0;
        int barRows = colReo.length;

        double barZ;
        double barZp;
        double barAs;
        double barStrain;
        double barStress;
        double barForce;
        double barMoment;

        double concForceSub;
        double concMomentSub;

        double gammaKuD = gamma*kuD;
        double concZp = yDim/2.0-gammaKuD/2.0;
        double concAg = gammaKuD*xDim;
        double alpha2Fc = alpha2*fc;
        double concForce = concAg*alpha2Fc;
        double concMoment = concForce * concZp;
        sectAxialForce += concForce;
        sectMoment += concMoment;

        for (int i=0 ; i<barRows ; i++){
            barZ = colReo[i][1];
            barZp = yDim/2.0-colReo[i][1];
            barAs = colReo[i][2];
            barStrain = eC/kuD*(kuD-barZ);
            barStress = Math.min(fs,Math.max(-fs,ES*barStrain));
            barForce = barAs*barStress;
            barMoment = barForce*barZp;
            if(barStrain>=0){
                concForceSub=barAs*-alpha2Fc;
                concMomentSub=concForceSub*barZp;
                sectAxialForce+=concForceSub;
                sectMoment+=concMomentSub;
            }
            sectAxialForce+=barForce;
            sectMoment+=barMoment;
        }

        sectAxialForce = sectAxialForce / 1000.0;
        sectMoment = sectMoment / 1000000.0;

        double[] sectionCap = {sectAxialForce,sectMoment};

        return sectionCap;
    }

    public double[][] colInteractionPoints(){
        double[][] interactionPoints = new double[202][3];
        double colSquash = this.columnSquash();
        double colTension = this.columnTension();
        double[][] colReo = this.columnReo();
        interactionPoints[0][0] = -1.0;
        interactionPoints[0][1] = colTension;
        interactionPoints[0][2] = 0.0;
        interactionPoints[1][0] = 0.0001;
        interactionPoints[1][1] = this.columnSolverKu(0.0001)[0];
        interactionPoints[1][2] = this.columnSolverKu(0.0001)[1];
        double ku;
        for(int i=1;i<=100;i++){
            ku = (double) i/100.0;
            interactionPoints[i+1][0] = (double) i/100.0;
            interactionPoints[i+1][1] = this.columnSolverKu(i/100.0)[0];
            interactionPoints[i+1][2] = this.columnSolverKu(i/100.0)[1];
        }

        double axialIncrement = colSquash - interactionPoints[101][1];
        double momentDecrease = -interactionPoints[101][2];
        double axialForceK1 = interactionPoints[101][1];
        double momentK1 = interactionPoints[101][2];

        for(int i=1;i<100;i++){
            ku = Math.round((1 + (double) i/100.0)*100)/100.0;
            interactionPoints[101+i][0]=ku;
            interactionPoints[101+i][1]= axialForceK1 + (double) i/100.0 * axialIncrement;
            interactionPoints[101+i][2]= momentK1 + (double) i/100.0 * momentDecrease;
        }
        interactionPoints[201][0] = 2.0;
        interactionPoints[201][1] = colSquash;
        interactionPoints[201][2] = 0.0;
        return interactionPoints;
    }

    public double[] kuSolverPureMoment(){
        double ku = 0.0001;
        double axialForce = this.columnTension();
        int i=1;
        while(axialForce<0 && i<10000){
            ku = (double) i/10000.0;
            axialForce = this.columnSolverKu(ku)[0];
            i++;
        }
        double kuPureMoment = (double) i/10000.0;
        double momentCap = this.columnSolverKu(kuPureMoment)[1];
        double[] capacity = new double[2];
        capacity[0] = kuPureMoment;
        capacity[1] = momentCap;

        return capacity;
    }

    public void printLineBreak(){
        System.out.print("\n");
    }

    public void printColName(){
        System.out.println("COLUMN " + this.colName);
    }

    public void printColTension(){
        double roundedNut = Math.round(this.columnTension()*10)/10.0;
        System.out.println("Column Tension Capacity = " + roundedNut + "kN");
    }

    public void printColSquash(){
        double roundedNuc = Math.round(this.columnSquash()*10)/10.0;
        System.out.println("Column Squash Capacity = " + roundedNuc + "kN");
    }

    public void printColMoment(){
        double roundedKu = Math.round(this.kuSolverPureMoment()[0]*100)/100.0;
        double roundedMuc = Math.round(this.kuSolverPureMoment()[1]*10)/10.0;
        System.out.println("Column Moment Capacity = " + roundedMuc + "kN-m; ku = " + roundedKu);
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

    public void printColSectCapacity(){
        double[][] tempCapHolder = this.colInteractionPoints();
        double roundedAxialCap;
        double roundedMomentCap;
        double tempKu;
        for(int i=0;i<tempCapHolder.length;i++){
            tempKu = tempCapHolder[i][0];
            roundedAxialCap = Math.round(10*tempCapHolder[i][1])/10.0;
            roundedMomentCap = Math.round(10*tempCapHolder[i][2])/10.0;
            System.out.println("Point " + (i+1) +": ku = " + tempKu + ": Axial = " + roundedAxialCap + "kN: Moment = " +
                    roundedMomentCap + "kN-m");
        }
    }

    /**
     *
     * @return radius of gyration of the section
     */
    public double concreteR(){
        if(sectionType.equals("circular")){
            return 0.25*yDim;
        }else if(sectionType.equals("rectangular")){
            return 0.3*yDim;
        }else{
            return 0.01*yDim;
        }
    }

    public double concElasticCritBuckling(){
        double colDeadLoad = 1.0;
        double colLiveLoad = (1.0-betaD)/betaD;
        double[][] colReo = this.columnReo();
        double effectiveD = colReo[colReo.length-1][1];
        double[] sectionBalanceCap = columnSolverKu(0.545);
        double balanceMomentCap = sectionBalanceCap[1] * 1000000.0;
        double criticalBucklingLoad = (Math.pow(Math.PI,2.0)/Math.pow(effectiveLength,2.0))*
                (182.0*effectiveD*0.6*balanceMomentCap/(1.0+betaD))/1000.0;
        return criticalBucklingLoad;
    }

    public double columnKm(double moment1, double moment2){
        return Math.max(0.4,Math.min(1.0,0.6-0.4*moment1/moment2));
    }

    public double columnDeltaB(double colKm, double axialN, double colCriticalLoad){
        return Math.max(1.0,colKm/(1-axialN/colCriticalLoad));
    }

    /**
     * Calculates the column slender limit
     * @param axialN
     * @return
     */
    public double columnSlender(double axialN, double moment1, double moment2){
        double colSquash = this.columnSquash();
        double alphaC=0;
        double stockyLimit = 22.0;
        if(axialN<=0.0){
            axialN = 0.1;
        }
        if(bracingType.equals("braced")){
            if((axialN/(0.6*colSquash))>=0.15 && (axialN/(0.6*colSquash))<0.9){
                alphaC=Math.sqrt(2.25-2.5*axialN/(0.6*colSquash));
            }else if((axialN/(0.6*colSquash))<0.15){
                alphaC=Math.sqrt(1/(3.5*axialN/(0.6*colSquash)));
            }else if(axialN/(0.6*colSquash)>=0.9){
                alphaC=0;
            }
            stockyLimit=Math.max(25.0,alphaC*(38.0-15.0/fc)*(1.0+moment1/moment2));
        }
        return stockyLimit;
    }

    public double columnCapacitySolver(){
        double colAxialCapacity = 0.1;
        double colDeadLoad = 1.0;
        double colLiveLoad = (1.0-betaD)/betaD;
        double colEffectiveLength = effectiveLength;
        double colRadius = this.concreteR();
        double colSlenderness = colEffectiveLength/colRadius;
        double[][] colInteraction = colInteractionPoints();
        double colElasticBuckCap = this.concElasticCritBuckling();
        double mColKm = columnKm(-1.0,1.0);
        double colDeltaB = 1.0;
        double colMagnifiedMinMoment;
        int compressionPoints = 0;
        int minKuPoint = 202;
        int maxKuPoint = -1;
        for(int i=0;i<colInteraction.length;i++){
            if(colInteraction[i][1]>=0.0 && colInteraction[i][1]<=colElasticBuckCap){
                compressionPoints++;
                if(i<minKuPoint){
                    minKuPoint=i;
                }
                if(i>maxKuPoint){
                    maxKuPoint=i;
                }
            }
        }
        for(int i=minKuPoint;i<=maxKuPoint;i++){
            double slendernessLimit = this.columnSlender(colInteraction[i][1],-1.0,1.0);
            if(colSlenderness<=slendernessLimit){
                colDeltaB = 1.0;
            }else{
                colDeltaB = columnDeltaB(mColKm,colInteraction[i][1],colElasticBuckCap);
            }
            colMagnifiedMinMoment = colInteraction[i][1]*0.05*xDim/1000.0*colDeltaB;
            if(colMagnifiedMinMoment<=colInteraction[i][2]){
                colAxialCapacity = colInteraction[i][1];
            }
        }
        return colAxialCapacity;
    }

    public void printColCapacity() {
        double roundedNc = Math.round(this.columnCapacitySolver() * 10) / 10.0;
        System.out.println("Column Capacity = " + roundedNc + "kN");
    }

    public void printColDetails(){
        System.out.println((int)this.xDim + "mm x " + (int)this.yDim + "mm");
        System.out.println(this.mNosBars + "/" + this.bd + "mm Dia Bars");
        System.out.println(this.mTieSize + "mm Dia Ties at " + this.mTieSpacing + "mm Crs");
    }

    public void printTieSize(){
        System.out.println("Tie Size = " + mTieSize);
    }

    public void printTieSpacing(){
        System.out.println("Tie Spacing = " + mTieSpacing);
    }

    public void printEffectiveLength(){
        System.out.println("Effective Length = " + effectiveLength);
    }

    public void printColumnLength(){
        System.out.println("Column Length = " + mColumnLength);
    }

    public String colCapacityToString(){
        double roundedNc = Math.round(this.columnCapacitySolver() * 10) / 10.0;
        String colCapacityString = Double.toString(roundedNc);
        colCapacityString = colCapacityString + " kN";
        return colCapacityString;
    }

    public void columnCapacitySolverWithCalcs(){
        System.out.println("");
        System.out.println("DISPLAYING CALCULATIONS FOR COLUMN CAPACITY");
        System.out.println("xDim = " + xDim);
        System.out.println("yDim = " + yDim);
        System.out.println("bx = " + bx);
        System.out.println("by = " + by);
        System.out.println("bd = " + bd);
        System.out.println("fc = " + fc);
        System.out.println("betaD = " + betaD);
        System.out.println("effective length = " + effectiveLength);
        System.out.println("section type = " + sectionType);
        System.out.println("bracing type = " + bracingType);
        double colAxialCapacity = 0.1;
        double colDeadLoad = 1.0;
        double colLiveLoad = (1.0-betaD)/betaD;
        double colEffectiveLength = effectiveLength;
        double colRadius = this.concreteR();
        System.out.println("radius = " + Math.round(colRadius*10)/10.0);
        double colSlenderness = colEffectiveLength/colRadius;
        System.out.println("Le/r = " + Math.round(colSlenderness*10)/10.0);
        double[][] colInteraction = colInteractionPoints();
        double colElasticBuckCap = this.concElasticCritBuckling();
        System.out.println("Elastic Buckling Cap = " + Math.round(colElasticBuckCap*10)/10.0);
        double mColKm = columnKm(-1.0,1.0);
        System.out.println("km = " + mColKm);
        double colDeltaB = 1.0;
        double colDeltaBFinal = 1.0;
        double colMagnifiedMinMoment;
        double colMinMimomentFinal = 0.1;
        double colMagnifiedMinMomentFinal = 0.1;
        double colMomentCapciatyFinal = 0.1;
        int compressionPoints = 0;
        int minKuPoint = 202;
        int maxKuPoint = -1;
        for(int i=0;i<colInteraction.length;i++){
            if(colInteraction[i][1]>=0.0 && colInteraction[i][1]<=colElasticBuckCap){
                compressionPoints++;
                if(i<minKuPoint){
                    minKuPoint=i;
                }
                if(i>maxKuPoint){
                    maxKuPoint=i;
                }
            }
        }
        for(int i=minKuPoint;i<=maxKuPoint;i++){
            double slendernessLimit = this.columnSlender(colInteraction[i][1],-1.0,1.0);
            if(colSlenderness<=slendernessLimit){
                colDeltaB = 1.0;
            }else{
                colDeltaB = columnDeltaB(mColKm,colInteraction[i][1],colElasticBuckCap);
            }
            colMagnifiedMinMoment = colInteraction[i][1]*0.05*xDim/1000.0*colDeltaB;
            if(colMagnifiedMinMoment<=colInteraction[i][2]){
                colAxialCapacity = colInteraction[i][1];
                colDeltaBFinal = colDeltaB;
                colMinMimomentFinal = colInteraction[i][1]*0.05*xDim/1000.0;
                colMagnifiedMinMomentFinal = colMagnifiedMinMoment;
                colMomentCapciatyFinal = colInteraction[i][2];
            }
        }
        System.out.println("column deltaB = " + Math.round(colDeltaBFinal*10)/10.0);
        System.out.println("column min moment = " + Math.round(colMinMimomentFinal*10)/10.0);
        System.out.println("column magnified min moment = " + Math.round(colMagnifiedMinMomentFinal*10)/10.0);
        System.out.println("column moment capacity = " + Math.round(colMomentCapciatyFinal*10)/10.0);
        System.out.println("column axial capacity = " + Math.round(colAxialCapacity*10)/10.0);
    }

    public int columnBarNumber(double dim) {
        int barNumber = (int) Math.max(2, ((dim - 100) / 180) + 1);
        return barNumber;
    }

    public ArrayList<Integer> barDesignation(String barCode){
        ArrayList<Integer> barArray = new ArrayList<Integer>();

        if(mBarCode.equals("AU")){
            barArray.add(10);
            barArray.add(12);
            barArray.add(16);
            barArray.add(20);
            barArray.add(24);
            barArray.add(28);
            barArray.add(32);
            barArray.add(36);
            barArray.add(40);
        }
        return barArray;
    }

    public int findBarSize(double reoPercent){
        double areaGross = xDim * yDim;
        double areaSteelReq = reoPercent * areaGross;
        double areaSteel = 0;
        int tempBarSize = 0;
        int barSize = mBarArray.get(mBarArray.size()-1);
        for(int i=0;i<mBarArray.size();i++){
            tempBarSize = mBarArray.get(i);
            areaSteel = mNosBars * Math.PI * tempBarSize * tempBarSize / 4.0;
            if(areaSteel>areaSteelReq && tempBarSize<barSize){
                barSize = tempBarSize;
            }
        }
        return barSize;
    }

    public int findTieSize(){
        int tieSize = 0;
        if(bd<=28){
            tieSize = 10;
        }else if(bd>28 && bd<=36){
            tieSize = 12;
        }else if(bd>36){
            tieSize = 16;
        }
        return tieSize;
    }

    public int tieSpacing(){
        int tieSpac = 300;
        if(15*bd<tieSpac){
            tieSpac = 15*bd;
        }
        if((int) Math.min(xDim,yDim) < tieSpac){
            tieSpac = (int) Math.min(xDim,yDim);
        }
        return tieSpac;
    }

    public double columnVolume(){
        double colVolumne = 0;
        colVolumne = xDim * yDim * mColumnLength / 1000000000;
        return colVolumne;
    }

    public double columnReoMass(){
        double reoMass = 0;
        double tieMass = 0;
        double[][] tempBarTiesDetails = this.barAndTieDetails();
        int nosTies = 0;
        for(int i=0;i<tempBarTiesDetails.length;i++){
            if(tempBarTiesDetails[i][6]>nosTies){
                nosTies = (int) tempBarTiesDetails[i][6];
            }
        }
        double[][] tieMassData = new double[nosTies][7];
        for(int i=0;i<tieMassData.length;i++){
            tieMassData[i][0]=i+1;
        }
        int crossRefTie = 0;
        int placeHolderNodeNo = 0;
        for(int i=0;i<tempBarTiesDetails.length;i++){
            crossRefTie = (int) tempBarTiesDetails[i][6];
            tieMassData[crossRefTie - 1][1] = tieMassData[crossRefTie - 1][1] + 1;
            placeHolderNodeNo = (int) tieMassData[crossRefTie - 1][1] + 1;
            tieMassData[crossRefTie - 1][placeHolderNodeNo] = i + 1;
        }
        double nodeLength = 0;
        int node1 = 0;
        int node2 = 0;
        int nosIterations = 0;
        int lineNumbers = 0;
        double totalTieLength = 0;
        int nosTieSets = 0;
        double x1 = 0;
        double x2 = 0;
        double y1 = 0;
        double y2 = 0;
        for(int i=0;i<nosTies;i++){
            nosIterations = (int) tieMassData[i][1];
            lineNumbers = (int) tieMassData[i][1];
            if(lineNumbers==2){
                lineNumbers=lineNumbers-1;
            }
            for(int j=0;j<lineNumbers;j++){
                if(j==2 && lineNumbers==4){
                    node1 = (int) tieMassData[i][j+3];
                }else if(j==3 && lineNumbers == 4){
                    node1 = (int) tieMassData[i][j+1];
                }else{
                    node1 = (int)tieMassData[i][j+2];
                }
                x1 = tempBarTiesDetails[node1-1][3];
                y1 = tempBarTiesDetails[node1-1][4];

                if(j==(lineNumbers-1) && lineNumbers>1){
                    node2 = (int) tieMassData[i][2];
                    x2 = tempBarTiesDetails[node2-1][3];
                    y2 = tempBarTiesDetails[node2-1][4];
                }else if(j==1 && lineNumbers==4 ){
                    node2 = (int) tieMassData[i][j+4];
                    x2 = tempBarTiesDetails[node2-1][3];
                    y2 = tempBarTiesDetails[node2-1][4];
                }else if(j==2 && lineNumbers==4 ){
                    node2 = (int) tieMassData[i][j+2];
                    x2 = tempBarTiesDetails[node2-1][3];
                    y2 = tempBarTiesDetails[node2-1][4];
                }else{
                    node2 = (int) tieMassData[i][j+3];
                    x2 = tempBarTiesDetails[node2-1][3];
                    y2 = tempBarTiesDetails[node2-1][4];
                }
                nodeLength = this.length2Points(x1,x2,y1,y2);
                tieMassData[i][6] += nodeLength;
            }
            tieMassData[i][6] += Math.PI * (bd + mTieSize) + 2 * Math.max(100,10*mTieSize);
            totalTieLength += tieMassData[i][6];
        }

        nosTieSets = (int) (this.columnLapLength() / (mTieSpacing/2) + (mColumnLength - this.columnLapLength())
                / mTieSpacing) + 2;

        tieMass = nosTieSets * totalTieLength * Math.PI * Math.pow(mTieSize,2) / 4 / 1000000000 * 8000;
        reoMass = mNosBars * Math.PI * bd * bd / 4 * (mColumnLength + this.columnLapLength()) / 1000000000 * 8000;
        return reoMass + tieMass;
    }

    public double columnReoRate(){
        double reoRate = this.columnReoMass() / this.columnVolume();
        return reoRate;
    }

    public double columnLapLength(){
        double colLapLen = 0;
        if(this.bd<=12){
            colLapLen = 500;
        }else if(this.bd>12 && this.bd<=16){
            colLapLen = 600;
        }else if(this.bd>16 && this.bd<=20){
            colLapLen = 800;
        }else if(this.bd>20 && this.bd<=24){
            colLapLen = 1000;
        }else if(this.bd>24 && this.bd<=28){
            colLapLen = 1200;
        }else if(this.bd>28 && this.bd<=32){
            colLapLen = 1350;
        }else if(this.bd>32 && this.bd<=36){
            colLapLen = 1500;
        }else{
            colLapLen = 2000;
        }
        return colLapLen;
    }

    public void printColumnReoRate(){
        double roundedRate = Math.round(this.columnReoRate()*10)/10.0;
        System.out.println("Reo Rate = " + roundedRate + " kg/cu.m");
    }

    public double[][] barAndTieDetails(){
        double[][] barTieDetails = new double[mNosBars][7];
        double[][] barDataX = this.columnReo();
        double[][] barDataY = this.columnReoRot();
        int barCount = 0;
        int k=0;
        int tieCount = 2;
        boolean isTriType = false;
        if((bx==3 || by==3) && bx!=2 && by!=2){
            isTriType = true;
        }

        for(int i=0;i<bx;i++){
            barTieDetails[barCount][0] = barCount + 1;
            barTieDetails[barCount][1] = 1;
            barTieDetails[barCount][2] = i + 1;
            barTieDetails[barCount][3] = barDataY[i][1];
            barTieDetails[barCount][4] = barDataX[0][1];
            if(barTieDetails[barCount][2]==1||barTieDetails[barCount][2]==bx){
                barTieDetails[barCount][5] = 0;
                barTieDetails[barCount][6] = 1;
            }else if((isTriType) && ((barTieDetails[barCount][2]==2) || (barTieDetails[barCount][2]==bx-1))){
                barTieDetails[barCount][5] = 1;
                barTieDetails[barCount][6] = tieCount;
                tieCount++;
            }else{
                barTieDetails[barCount][5] = 2;
                barTieDetails[barCount][6] = tieCount;
                tieCount++;
            }
            barCount++;
        }
        for(int i=0;i<(by-2);i++){
            for(int j=0;j<2;j++){
                barTieDetails[barCount][0] = barCount + 1;
                barTieDetails[barCount][1] = i + 2;
                barTieDetails[barCount][4] = barDataX[i+1][1];
                if(j==0){
                    barTieDetails[barCount][2] = 1;
                    barTieDetails[barCount][3] = barDataY[0][1];
                }else{
                    barTieDetails[barCount][2] = bx;
                    barTieDetails[barCount][3] = barDataY[barDataY.length-1][1];
                }
                if(isTriType && (bx == 3) && i==0 ) {
                    barTieDetails[barCount][5] = 1;
                    barTieDetails[barCount][6] = barTieDetails[1][6];
                }else if(isTriType && (bx == 3) && i==(by-3)){
                    barTieDetails[barCount][5] = 1;
                    barTieDetails[barCount][6] = tieCount;
                    if(j==1){
                        tieCount++;
                    }
                }else if(isTriType && (by == 3)){
                    barTieDetails[barCount][5] = 1;
                    if(barTieDetails[barCount][2]==1){
                        barTieDetails[barCount][6] = barTieDetails[1][6];
                    }else{
                        barTieDetails[barCount][6] = barTieDetails[bx-2][6];
                    }
                }else{
                    barTieDetails[barCount][5] = 2;
                    barTieDetails[barCount][6] = tieCount;
                    if(j!=0){
                        tieCount++;
                    }
                }
                barCount++;
           }
        }
        for(int i=0;i<bx;i++){
            barTieDetails[barCount][0] = barCount + 1;
            barTieDetails[barCount][1] = by;
            barTieDetails[barCount][2] = i + 1;
            barTieDetails[barCount][3] = barDataY[i][1];
            barTieDetails[barCount][4] = barDataX[barDataX.length-1][1];
            if(barTieDetails[barCount][2]==1||barTieDetails[barCount][2]==bx){
                barTieDetails[barCount][5] = 0;
                barTieDetails[barCount][6] = 1;
            }else if(isTriType && bx==3 && (barTieDetails[barCount][2]!=1) && (barTieDetails[barCount][2]!=bx)){
                barTieDetails[barCount][5] = 1;
                barTieDetails[barCount][6] = barTieDetails[barCount-2][6];
            }else if(isTriType && by==3 && ((barTieDetails[barCount][2]==2) || (barTieDetails[barCount][2]==(bx-1)))){
                barTieDetails[barCount][5] = 1;
                barTieDetails[barCount][6] = barTieDetails[i][6];
            }else{
                barTieDetails[barCount][5] = 2;
                barTieDetails[barCount][6] = barTieDetails[i][6];
                tieCount++;
            }
            barCount++;
        }
        return barTieDetails;
    }

    public double length2Points(double x1, double x2, double y1, double y2){
        double len = Math.sqrt(Math.pow((y2 - y1),2) + Math.pow((x2 - x1),2));
        return len;
    }

}

class Reinforcement {
    public int barDia;
    public int barFs;
    public String barGradeDes;
    public String barCountryCode;

    public Reinforcement(int barDia, int barFs, String barGradeDes, String barCountryCode){
        this.barDia = barDia;
        this.barDia = barFs;
        this.barGradeDes = barGradeDes;
        this.barCountryCode = barCountryCode;
    }
}