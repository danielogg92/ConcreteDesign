package com.company;

import javax.swing.*;

import static com.company.Column.LOG_TAG;

public class Main {

    public static void main(String[] args) {
        Column C04 = new Column("C04",500,1000);
        C04.printColName();
        C04.printColDetails();
        C04.printColTension();
        C04.printColSquash();
        C04.printColMoment();
        C04.printColReo();
        C04.printColCapacity();
        C04.printTieSize();
        C04.printTieSpacing();
        C04.printEffectiveLength();
        C04.printColumnLength();
        C04.printColumnReoRate();
        System.out.println("Lap Length = " + C04.columnLapLength());

        System.out.println();

        Column C04R = new Column("C04R",1000,500);
        C04R.printColName();
        C04R.printColDetails();
        C04R.printColTension();
        C04R.printColSquash();
        C04R.printColMoment();
        C04R.printColReo();
        C04R.printColCapacity();
        C04R.printTieSize();
        C04R.printTieSpacing();
        C04R.printEffectiveLength();
        C04R.printColumnLength();
        System.out.println("Lap Length = " + C04R.columnLapLength());
        C04R.printColumnReoRate();

        //JFrame frame = new JFrame("Zendaya Coleman");
        //JLabel label = new JLabel("Becky G",JLabel.CENTER);
        //frame.add(label);
        //frame.setSize(300,300);
        //frame.setVisible(true);
    }
}
