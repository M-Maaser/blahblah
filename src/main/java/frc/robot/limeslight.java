package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import java.lang.Math;

public class limeslight {
    NetworkTable limelightBack; // table for the limelight

    NetworkTableEntry tx; // Table for the x-coordinate
    NetworkTableEntry ty; // Table for the y-coordinate
    NetworkTableEntry ta; // Table for the area the target takes up
    NetworkTableEntry ts; // Table for the skew/rotation of target
    NetworkTableEntry tv; // Table to see if there are valid targets
    NetworkTableEntry tl; /// Table for latency/delay before data transfer

    NetworkTableEntry tshort; // Table for short side length
    NetworkTableEntry tlong; // Table for long side length
    NetworkTableEntry thoriz; // Table for width
    NetworkTableEntry tvert; // Table for height
    NetworkTableEntry ledMode; // Table to set blinking LEDS
    NetworkTableEntry camMode; // Table to set camera mode
    NetworkTableEntry pipeline; // Table to switch pipelines
    NetworkTableEntry solvePNP; // Table to give position in 3D space based on camera

    boolean checkArea;
    boolean checkSkew;

    public void DebugMethodSingle() {
        var tab = Shuffleboard.getTab("Driver Diagnostics");
        tab.addBoolean("Shooter Running", () -> true);
    }

    public limeslight() {
        // instantiate the tables (basically make the tables real)
        limelightBack = NetworkTableInstance.getDefault().getTable("limelight");
        // creates each table
        tx = limelightBack.getEntry("tx");
        ty = limelightBack.getEntry("ty");
        ta = limelightBack.getEntry("ta");
        ts = limelightBack.getEntry("ts");
        tv = limelightBack.getEntry("tv");
        tl = limelightBack.getEntry("tl");

        tshort = limelightBack.getEntry("tshort");
        tlong = limelightBack.getEntry("tlong");
        thoriz = limelightBack.getEntry("thoriz");
        tvert = limelightBack.getEntry("tvert");
        ledMode = limelightBack.getEntry("ledMode");
        camMode = limelightBack.getEntry("camMode");
        pipeline = limelightBack.getEntry("pipeline");
        solvePNP = limelightBack.getEntry("solvePNP");

    }

    // vison constants
    double CAMERA_HEIGHT = 0.0; // placeholder value, fix once have robot
    double APRIL_TAG_HEIGHT = 0.0; // placeholder, fix once have robot
    double MIN_AREA = 0.3; // place holder value, fix once have robot

    // double HEIGHT_ANGLE =
    // double DISTANCE = (APRIL_TAG_HEIGHT - CAMERA_HEIGHT)/Math.tan(getArea());
    // (-37.057495484737 * getArea()) + 110.42331392686; //calculated in inches
    // ESTIMATED
    // variable does not factor in skew
    // to move, first look straight then move distance to go straight to april tag
    // need to modify later once given robot size
    public double getArea() {

        return ta.getDouble(0.0);

    }

    public double getSkew() {
        return ts.getDouble(0.0);
    }

    public boolean checkArea() {
        if (getArea() >= MIN_AREA) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkTarget() {
        var t = tv.getDouble(0.0);
        if (t == 1.0) {
            return true;
        } else {
            return false;
        }
    }

    public void checkTargget() {
        if (checkTarget()) {
            System.out.println("EYES!EYES!EYES!EYES!EYES!EYES!EYES!EYES!EYES!EYES!EYES!EYES!EYES!EYES");
        } else {
            System.out.println(
                    "FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!FOOL!");
        }
    }

    // checks how "off" the robot is to the target in degrees
    // basically how many degrees it needs to turn to be facing the target
    public int checkSkew() { // change to range instead of one int once able to test + determine range
        if (checkTarget()) {
            if ((getSkew() < 1 && getSkew() > -1) || getSkew() < 88) {
                return 0; // skew is on target
            } else if (getSkew() > 45) {
                return 1; // turn right skew amount of angles (?)
            } else {
                return -1; // turn left skew-45 angles
            }
        } else {
            return 2;
        }
    }

}

// end of class
